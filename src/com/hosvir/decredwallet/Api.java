package com.hosvir.decredwallet;

import com.deadendgine.utils.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class Api {
    public synchronized static JSONObject getInfo() {
        try {
            return (JSONObject) Constants.getDcrdEndpoint().callMethod(
                    "getinfo",
                    null
            ).get("result");
        } catch (ClassCastException e) {
            return null;
        }
    }

    public synchronized static JSONArray getPeerInfo() {
        return (JSONArray) Constants.getDcrdEndpoint().callMethod(
                "getpeerinfo",
                null
        ).get("result");
    }

    public synchronized static void disconnectPeer(String id) {
        Constants.getDcrdEndpoint().callMethod(
                "node",
                new Param[]{
                        new Param(0, "disconnect"),
                        new Param(0, id),
                        new Param(0, "temp")
                }
        );
    }

    public synchronized static void ping() {
        Constants.getDcrdEndpoint().callMethod(
                "ping",
                null
        );
    }

    public synchronized static boolean existsLiveTicket(String ticketHash) {
        try {
            return Boolean.valueOf(Constants.getDcrdEndpoint().callMethod(
                    "existsliveticket",
                    new Param[]{
                            new Param(0, ticketHash)
                    }
            ).get("result").toString());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public synchronized static JSONObject getBalances(String name) {
        try {
            JSONObject result = (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                    "getbalance",
                    new Param[]{
                            new Param(0, name)
                    }
            ).get("result");

            JSONArray results = (JSONArray) result.get("balances");

            return (JSONObject) results.get(0);
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

    public synchronized static String getBalance(String name) {
        try {
            return Api.getBalances(name).get("total").toString();
        } catch (NullPointerException e) {
            return "0";
        }
    }

    public synchronized static String getBalanceSpendable(String name) {
        try {
            return Api.getBalances(name).get("spendable").toString();
        } catch (NullPointerException e) {
            return "0";
        }
    }

    public synchronized static String getLockedBalance(String name) {
        try {
            JSONObject result = (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                    "getbalance",
                    new Param[]{
                            new Param(0, name),
                            new Param(1, "0")
                    }
            ).get("result");

            JSONArray results = (JSONArray) result.get("balances");
            result = (JSONObject) results.get(0);

            return result.get("lockedbytickets").toString();
        } catch (ClassCastException | NullPointerException e) {
            return "0";
        }
    }

    public synchronized static String getBalanceAll(String name) {
        try {
            return Api.getBalances(name).get("total").toString();
        } catch (NullPointerException e) {
            return "0";
        }
    }

    public synchronized static JSONArray getTransactions() {
        try {
            return (JSONArray) Constants.getDcrwalletEndpoint().callMethod(
                    "listtransactions",
                    new Param[]{
                            new Param(0, "*"),
                            new Param(1, "9999")
                    }
            ).get("result");
        } catch (ClassCastException e) {
            return null;
        }
    }

    public synchronized static JSONObject getTransactions(String name) {
        return Constants.getDcrwalletEndpoint().callMethod(
                "listtransactions",
                new Param[]{
                        new Param(0, name)
                }
        );
    }

    public synchronized static JSONObject getAccounts() {
        return (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                "listaccounts",
                null
        ).get("result");
    }

    public synchronized static String getWalletFee() {
        try {
            return Constants.getDcrwalletEndpoint().callMethod(
                    "getwalletfee",
                    null
            ).get("result").toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public synchronized static String getTicketFee() {
        return Constants.getDcrwalletEndpoint().callMethod(
                "getticketfee",
                null
        ).get("result").toString();
    }

    public synchronized static String getStakeDifficulty() {
        return Constants.getDcrwalletEndpoint().callMethod(
                "getstakedifficulty",
                null
        ).get("current").toString();
    }

    public synchronized static String getAddressesByAccount(String name) {
        try {
            return Constants.getDcrwalletEndpoint().callMethod(
                    "getaddressesbyaccount",
                    new Param[]{
                            new Param(0, name)
                    }
            ).get("result").toString();
        } catch (NullPointerException e) {
            return "";
        }
    }

    public synchronized static String getNewAddress(String name) {
        return Constants.getDcrwalletEndpoint().callMethod(
                "getnewaddress",
                new Param[]{
                        new Param(0, name)
                }
        ).get("result").toString();
    }

    public synchronized static String unlockWallet(String timeout) {
        JSONObject result = (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                "walletpassphrase",
                new Param[]{
                        new Param(0, Constants.getPrivatePassPhrase()),
                        new Param(1, timeout)
                }
        );

        Constants.setPrivatePassPhrase(null);

        return result.get("error") != null ? result.get("error").toString() : "";
    }

    public synchronized static void renameAccount(String old, String name) {
        Constants.getDcrwalletEndpoint().callMethod(
                "renameaccount",
                new Param[]{
                        new Param(0, old),
                        new Param(0, name)
                }
        );
    }

    public synchronized static void createNewAccount(String name) {
        Constants.getDcrwalletEndpoint().callMethod(
                "createnewaccount",
                new Param[]{
                        new Param(0, name)
                }
        );
    }

    public synchronized static JSONObject getStakeInfo() {
        return (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                "getstakeinfo",
                null
        ).get("result");
    }

    public synchronized static boolean setTxFee(String fee) {
        return Boolean.valueOf(Constants.getDcrwalletEndpoint().callMethod(
                "settxfee",
                new Param[]{
                        new Param(1, fee)
                }
        ).get("result").toString());
    }

    public synchronized static boolean setTicketFee(String fee) {
        return Boolean.valueOf(Constants.getDcrwalletEndpoint().callMethod(
                "setticketfee",
                new Param[]{
                        new Param(1, fee)
                }
        ).get("result").toString());
    }

    public synchronized static String sendFrom(String name, String toAddress, String comment, String amount) {
        return Constants.getDcrwalletEndpoint().callMethod(
                "sendfrom",
                new Param[]{
                        new Param(0, name),
                        new Param(0, toAddress),
                        new Param(1, amount)
                }
        ).get("result").toString();
    }

    public synchronized static String sendMany(String name, String toAddresses, String comment, String amounts) {
        String jsonparam = "{";
        String[] naddresses = toAddresses.split(",");
        String[] namounts = amounts.split(",");

        //Check count
        if (naddresses.length != namounts.length) {
            return "Address count must equal amount count.";
        }

        //Append values into json string
        for (int i = 0; i < naddresses.length; i++) {
            jsonparam += "'" + naddresses[i] + "':" + namounts[i] + ",";
        }

        //Remove final comma
        jsonparam = StringUtils.backspace(jsonparam);

        //Finally call method
        return Constants.getDcrwalletEndpoint().callMethod(
                "sendmany",
                new Param[]{
                        new Param(0, name),
                        new Param(0, jsonparam)
                }
        ).get("result").toString();
    }

    public synchronized static String purchaseTicket(String ticketFee, String name, String spendLimit, String address, String numberOfTickets, String poolAddress, String poolFees) {
        //Attempt to set ticket fee
        Api.setTicketFee(ticketFee);

        if (poolAddress == null || poolAddress == "") {
            return Constants.getDcrwalletEndpoint().callMethod(
                    "purchaseticket",
                    new Param[]{
                            new Param(0, name),
                            new Param(1, spendLimit),
                            new Param(1, "1"),
                            new Param(0, address),
                            new Param(1, numberOfTickets)
                    }
            ).get("message").toString();
        } else {
            return Constants.getDcrwalletEndpoint().callMethod(
                    "purchaseticket",
                    new Param[]{
                            new Param(0, name),
                            new Param(1, spendLimit),
                            new Param(1, "1"),
                            new Param(0, address),
                            new Param(1, numberOfTickets),
                            new Param(0, poolAddress),
                            new Param(1, poolFees)
                    }
            ).toString();
        }
    }

    public synchronized static String dumpPrivateKey(String address) {
        String result = Constants.getDcrwalletEndpoint().callMethod(
                "dumpprivkey",
                new Param[]{
                        new Param(0, address)
                }
        ).get("result").toString();

        Constants.setPrivatePassPhrase(null);

        return result;
    }

    public synchronized static String walletPassphraseChange(String oldpassphrase, String newpassphrase) {
        return Constants.getDcrwalletEndpoint().callMethod(
                "purchaseticket",
                new Param[]{
                        new Param(0, oldpassphrase),
                        new Param(0, newpassphrase)
                }
        ).get("result").toString();
    }

    public synchronized static String getMasterPubkey() {
        return Constants.getDcrwalletEndpoint().callMethod(
                "getmasterpubkey",
                null
        ).get("result").toString();
    }

    public synchronized static String dumpWallet(String filename) {
        return Constants.getDcrwalletEndpoint().callMethod(
                "dumpwallet",
                new Param[]{
                        new Param(0, filename)
                }
        ).get("result").toString();
    }

    public synchronized static String validateAddress(String address) {
        JSONObject result = (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                "validateaddress",
                new Param[]{
                        new Param(0, address)
                }
        ).get("result");

        return result.get("pubkeyaddr").toString();
    }

    public synchronized static String importScript(String script) {
        JSONObject result = (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                "importscript",
                new Param[]{
                        new Param(0, script)
                }
        ).get("result");

        Constants.setPrivatePassPhrase(null);

        return result != null ? result.toString() : "";
    }

    public synchronized static String getTickets(boolean includeImmature) {
        try {
            JSONObject result = (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                    "gettickets",
                    new Param[]{
                            new Param(1, String.valueOf(includeImmature))
                    }
            ).get("result");

            return result.get("hashes") != null ? result.get("hashes").toString() : null;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public synchronized static JSONObject getTransaction(String txHash) {
        try {
            return (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                    "gettransaction",
                    new Param[]{
                            new Param(0, txHash)
                    }
            ).get("result");
        } catch (ClassCastException e) {
            return null;
        }
    }

    public synchronized static JSONObject getBlock(String blockHash) {
        try {
            return (JSONObject) Constants.getDcrwalletEndpoint().callMethod(
                    "getblock",
                    new Param[]{
                            new Param(0, blockHash)
                    }
            ).get("result");
        } catch (ClassCastException | NullPointerException e) {
            return null;
        }
    }

    public synchronized static String getBlockHash(String blockIndex) {
        try {
            return Constants.getDcrwalletEndpoint().callMethod(
                    "getblockhash",
                    new Param[]{
                            new Param(1, blockIndex)
                    }
            ).get("result").toString();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
