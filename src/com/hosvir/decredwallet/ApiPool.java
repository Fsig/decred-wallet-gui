package com.hosvir.decredwallet;

import com.hosvir.decredwallet.gui.interfaces.StakingPurchase;
import com.hosvir.decredwallet.utils.URLConnector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author captain-redbeard
 * @version 1.00
 * @since 3/04/17
 */
public class ApiPool {
    private static JSONParser parser;
    private static final String API_VERSION = "v1";
    public static final String POOL_LIST_URL = "https://decred.org/api/?c=gsd";

    public synchronized static JSONObject getPoolList() {
        parser = new JSONParser();
        String test = URLConnector.getPage(POOL_LIST_URL);

        if (test.trim().length() > 1) {
            try {
                JSONObject result = (JSONObject) parser.parse(test);
                return result;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public synchronized static String callApi(String method, String[] fields, String[] values) {
        return URLConnector.getPage(
                Constants.getPoolUrl() + "/api/" + API_VERSION + "/" + method,
                fields,
                values,
                new String[]{"Authorization"},
                new String[]{"Bearer " + Constants.getPoolApiKey()}
        );
    }

    public synchronized static String joinPool(String name, String apiKey) {
        Pool pool = Constants.getPoolByName(name);


        //If we have the selected pool
        if (pool != null) {
            Constants.setPoolApiKey(apiKey);
            Constants.setPoolUrl(pool.getUrl());

            String page = callApi("getpurchaseinfo", null, null);

            //Check if we got a response
            if (page.trim().length() > 1) {
                try {
                    JSONObject result = (JSONObject) parser.parse(page);

                    if (result.get("message").toString().contains("invalid api token")) {
                        return "Invalid API Token for POS Pool " + name;
                    } else if (!result.get("message").toString().contains("error")) {
                        JSONObject data = (JSONObject) result.get("data");
                        System.out.println("POS Pool Data");
                        System.out.println(" - pool address: " + data.get("PoolAddress"));
                        System.out.println(" - ticket address: " + data.get("TicketAddress"));
                        System.out.println(" - pool fees: " + data.get("PoolFees"));
                        System.out.println(" - script: " + data.get("Script"));

                        //Unlock wallet and import script
                        String importResult = Api.importScript(data.get("Script").toString());
                        Constants.log("Import script result: " + importResult);

                        //Set settings
                        Constants.setPoolAccountName("pos-" + name.toLowerCase());
                        Constants.setPoolName(name);
                        Constants.setPoolFeePercent(data.get("PoolFees").toString());
                        Constants.setPoolAddress(data.get("PoolAddress").toString());
                        Constants.setPoolTicketAddress(data.get("TicketAddress").toString());
                        Constants.saveSettings();
                        StakingPurchase.class.cast(Constants.guiInterfaces.get(4)).updateDefaults();

                        return "Successfully configured pool.";
                    } else {
                        String newAddress = Api.getNewAddress(Constants.getAccountByName("pos-" + name.toLowerCase()).name).trim();
                        String pubkeyaddr = Api.validateAddress(newAddress);
                        Constants.log("New address: " + newAddress);
                        Constants.log("Public key address: " + pubkeyaddr);

                        System.out.println("Attempting to submit address - " + pubkeyaddr);

                        //Add public key to pool
                        page = callApi(
                                "address",
                                new String[]{"UserPubKeyAddr"},
                                new String[]{pubkeyaddr}
                        );

                        JSONObject message = (JSONObject) parser.parse(page);

                        if (message.get("message").toString().contains("success")) {
                            joinPool(name, apiKey);
                        } else {
                            System.out.println("Failed to add address.");
                            System.out.println(message.get("message").toString());
                        }
                    }
                } catch(ParseException e){
                    System.out.println("Failed to parse response.");
                }
            } else {
                return "Failed to connect to pool.";
            }
        }

        return null;
    }
}
