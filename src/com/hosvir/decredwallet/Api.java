package com.hosvir.decredwallet;

import java.util.ArrayList;

import com.deadendgine.utils.StringUtils;
import com.hosvir.decredwallet.utils.Json;
import com.hosvir.decredwallet.utils.JsonObject;
import com.hosvir.decredwallet.utils.Param;

/**
 * 
 * @author Troy
 *
 */
public class Api {
	
	public synchronized static ArrayList<JsonObject> getInfo() {
		return Json.parseJson(Constants.getDcrdEndpoint().callMethod("getinfo", null));
	}
	
	public synchronized static ArrayList<JsonObject> getPeerInfo() {
		return Json.parseJson(Constants.getDcrdEndpoint().callMethod("getpeerinfo", null));
	}
	
	public synchronized static String disconnectPeer(String id) {
		return processJsonResult(Json.parseJson(Constants.getDcrdEndpoint().callMethod("node", new Param[]{
				new Param(0,"disconnect"), 
				new Param(0,id), 
				new Param(0,"temp")})));
	}
	
	public synchronized static void ping() {
		Constants.getDcrdEndpoint().callMethod("ping", null);
	}
	
	public synchronized static String existsLiveTicket(String ticketHash) {
		return processJsonResult(Json.parseJson(Constants.getDcrdEndpoint().callMethod("existsliveticket", new Param[]{
				new Param(0,ticketHash)})));
	}
	
	
	
	public synchronized static String getBalance(String name) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
				new Param(0,name)})).get(0).getValueByName("result");
	}
	
	public synchronized static String getBalanceSpendable(String name) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
				new Param(0,name), 
				new Param(1,"0"),
				new Param(0,"spendable")})).get(0).getValueByName("result");
	}
	
	public synchronized static String getLockedBalance(String name) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
				new Param(0,name), 
				new Param(1,"0"), 
				new Param(0,"locked")})).get(0).getValueByName("result");
	}
	
	public synchronized static String getBalanceAll(String name) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
				new Param(0,name), 
				new Param(1,"0"), 
				new Param(0,"all")})).get(0).getValueByName("result");
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions() {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("listtransactions", new Param[]{
				new Param(0,"*"), new Param(1,"9999")
		}));
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions(String name) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("listtransactions", new Param[]{
				new Param(0,name)}));
	}
	
	public synchronized static ArrayList<JsonObject> getAccounts() {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("listaccounts", null));
	}
	
	public synchronized static String getWalletFee() {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getwalletfee", null)).get(0).getValueByName("result");
	}
	
	public synchronized static String getStakeDifficulty() {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getstakedifficulty", null)).get(0).getValueByName("current");
	}
	
	public synchronized static String getAddressesByAccount(String name) {
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getaddressesbyaccount", new Param[]{
				new Param(0,name)})));
	}
	
	public synchronized static String getNewAddress(String name) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getnewaddress", new Param[]{
				new Param(0,name)})).get(0).getValueByName("result");
	}
	
	public synchronized static String unlockWallet(String timeout) {
		String result = processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("walletpassphrase", new Param[]{
				new Param(0,Constants.getPrivatePassPhrase()), 
				new Param(1,timeout)})));
		
		Constants.setPrivatePassPhrase(null);
		return result;
	}
	
	public synchronized static String renameAccount(String old, String name) {
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("renameaccount", new Param[]{
				new Param(0,old), 
				new Param(0,name)})));
	}
	
	public synchronized static String createNewAccount(String name) {
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("createnewaccount", new Param[]{
				new Param(0,name)})));
	}
	
	public synchronized static ArrayList<JsonObject> getStakeInfo() {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getstakeinfo",null));
	}
	
	public synchronized static boolean setTxFee(String fee) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("settxfee",new Param[]{
				new Param(1,fee)})).get(0).getValueByName("result").trim().equals("true");
	}

	public synchronized static String sendFrom(String name, String toAddress, String comment, String amount) {
		System.out.println("Send from");
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("sendfrom", new Param[]{
				new Param(0,name), 
				new Param(0,toAddress), 
				new Param(1,amount)})));
	}
	
	public synchronized static String sendMany(String name, String toAddresses, String comment, String amounts){
		String jsonparam = "{";
		String[] naddresses = toAddresses.split(",");
		String[] namounts = amounts.split(",");
		
		//Check count
		if(naddresses.length != namounts.length) return "Address count must equal amount count.";
		
		//Append values into json string
		for(int i = 0; i < naddresses.length; i++){
			jsonparam += "'" + naddresses[i] + "':" + namounts[i] + ",";
		}
		
		//Remove final comma
		jsonparam = StringUtils.backspace(jsonparam);
		
		//Finally call method
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("sendmany", new Param[]{
				new Param(0,name), 
				new Param(0,jsonparam)})));
	}
	
	public synchronized static String purchaseTicket(String name, String spendLimit, String address, String numberOfTickets, String poolAddress, String poolFees) {
		if(poolAddress == null || poolAddress == ""){
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("purchaseticket", new Param[]{
					new Param(0,name), 
					new Param(1,spendLimit), 
					new Param(1,"1"),
					new Param(0,address),
					new Param(1,numberOfTickets)})));
		}else{
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("purchaseticket", new Param[]{
					new Param(0,name), 
					new Param(1,spendLimit), 
					new Param(1,"1"),
					new Param(0,address),
					new Param(1,numberOfTickets),
					new Param(0,poolAddress),
					new Param(1,poolFees)})));
		}
	}
	
	public synchronized static String dumpPrivKey(String address) {
		String result = Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("dumpprivkey", new Param[]{
				new Param(0,address)})).get(0).getValueByName("result");
		
		Constants.setPrivatePassPhrase(null);
		
		return result;
	}
	
	public synchronized static String wallPassphraseChange(String oldpassphrase, String newpassphrase) {
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("purchaseticket", new Param[]{
				new Param(0,oldpassphrase),
				new Param(0,newpassphrase)})));
	}
	
	public synchronized static String getMasterPubkey() {
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getmasterpubkey", null)));
	}
	
	public synchronized static String dumpWallet(String filename) {
		return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("dumpwallet", new Param[]{
				new Param(0,filename)})));
	}
	
	public synchronized static String validateAddress(String address) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("validateaddress", new Param[]{
				new Param(0,address)})).get(0).getValueByName("pubkeyaddr");
	}
	
	public synchronized static String importScript(String script) {		
		String result = processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("importscript", new Param[]{
				new Param(0,script)})));
		
		Constants.setPrivatePassPhrase(null);
		
		return result;
	}
	
	public synchronized static String getTickets(boolean includeImmature) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("gettickets", new Param[]{
				new Param(1,String.valueOf(includeImmature))})).get(0).getValueByName("hashes");
	}
	
	public synchronized static ArrayList<JsonObject> getTransaction(String txHash) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("gettransaction", new Param[]{
				new Param(0,txHash)}));
	}
	
	public synchronized static JsonObject getBlock(String blockHash) {
		return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getblock", new Param[]{
				new Param(0,blockHash)})).get(0);
	}
	
	/**
	 * Check if the result is null, if so return the error JSON Object.
	 * 
	 * @param jsonObjects
	 * @return String
	 */
	public synchronized static String processJsonResult(ArrayList<JsonObject> jsonObjects) {
		if(jsonObjects.get(0).getValueByName("result").trim().equals("null")){
			return jsonObjects.get(1).getValueByName("code") + ": " + jsonObjects.get(1).getValueByName("message");
		}else{
			return jsonObjects.get(0).getValueByName("result");
		}
	}

}
