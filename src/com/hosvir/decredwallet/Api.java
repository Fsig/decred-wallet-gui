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
		try{
			return Json.parseJson(Constants.getDcrdEndpoint().callMethod("getinfo", null));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static ArrayList<JsonObject> getPeerInfo() {
		try {
			return Json.parseJson(Constants.getDcrdEndpoint().callMethod("getpeerinfo", null));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String disconnectPeer(String id) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrdEndpoint().callMethod("node", new Param[]{
					new Param(0,"disconnect"), 
					new Param(0,id), 
					new Param(0,"temp")})));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static void ping() {
		Constants.getDcrdEndpoint().callMethod("ping", null);
	}
	
	public synchronized static String existsLiveTicket(String ticketHash) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrdEndpoint().callMethod("existsliveticket", new Param[]{
					new Param(0,ticketHash)})));
		}catch(Exception e){
			return null;
		}
	}
	
	
	
	public synchronized static String getBalance(String name) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
					new Param(0,name)})).get(0).getValueByName("result");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getBalanceSpendable(String name) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
					new Param(0,name), 
					new Param(1,"0"),
					new Param(0,"spendable")})).get(0).getValueByName("result");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getLockedBalance(String name) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
					new Param(0,name), 
					new Param(1,"0"), 
					new Param(0,"locked")})).get(0).getValueByName("result");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getBalanceAll(String name) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getbalance", new Param[]{
					new Param(0,name), 
					new Param(1,"0"), 
					new Param(0,"all")})).get(0).getValueByName("result");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions() {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("listtransactions", new Param[]{
					new Param(0,"*"), new Param(1,"9999")
			}));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static ArrayList<JsonObject> getTransactions(String name) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("listtransactions", new Param[]{
					new Param(0,name)}));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static ArrayList<JsonObject> getAccounts() {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("listaccounts", null));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getWalletFee() {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getwalletfee", null)).get(0).getValueByName("result");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getStakeDifficulty() {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getstakedifficulty", null)).get(0).getValueByName("current");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getAddressesByAccount(String name) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getaddressesbyaccount", new Param[]{
					new Param(0,name)})));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getNewAddress(String name) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getnewaddress", new Param[]{
					new Param(0,name)})).get(0).getValueByName("result");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String unlockWallet(String timeout) {
		try {
			String result = processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("walletpassphrase", new Param[]{
					new Param(0,Constants.getPrivatePassPhrase()), 
					new Param(1,timeout)})));
			
			Constants.setPrivatePassPhrase(null);
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String renameAccount(String old, String name) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("renameaccount", new Param[]{
					new Param(0,old), 
					new Param(0,name)})));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String createNewAccount(String name) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("createnewaccount", new Param[]{
					new Param(0,name)})));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static ArrayList<JsonObject> getStakeInfo() {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getstakeinfo",null));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static boolean setTxFee(String fee) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("settxfee",new Param[]{
					new Param(1,fee)})).get(0).getValueByName("result").trim().equals("true");
		}catch(Exception e){
			return false;
		}
	}

	public synchronized static String sendFrom(String name, String toAddress, String comment, String amount) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("sendfrom", new Param[]{
					new Param(0,name), 
					new Param(0,toAddress), 
					new Param(1,amount)})));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String sendMany(String name, String toAddresses, String comment, String amounts){
		try {
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
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String purchaseTicket(String name, String spendLimit, String address, String numberOfTickets, String poolAddress, String poolFees) {
		try {
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
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String dumpPrivKey(String address) {
		try {
			String result = Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("dumpprivkey", new Param[]{
					new Param(0,address)})).get(0).getValueByName("result");
			
			Constants.setPrivatePassPhrase(null);
			
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String wallPassphraseChange(String oldpassphrase, String newpassphrase) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("purchaseticket", new Param[]{
					new Param(0,oldpassphrase),
					new Param(0,newpassphrase)})));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getMasterPubkey() {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getmasterpubkey", null)));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String dumpWallet(String filename) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("dumpwallet", new Param[]{
					new Param(0,filename)})));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String validateAddress(String address) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("validateaddress", new Param[]{
					new Param(0,address)})).get(0).getValueByName("pubkeyaddr");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String importScript(String script) {		
		try {
			String result = processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("importscript", new Param[]{
					new Param(0,script)})));
			
			Constants.setPrivatePassPhrase(null);
			
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getTickets(boolean includeImmature) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("gettickets", new Param[]{
					new Param(1,String.valueOf(includeImmature))})).get(0).getValueByName("hashes");
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static ArrayList<JsonObject> getTransaction(String txHash) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("gettransaction", new Param[]{
					new Param(0,txHash)}));
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static JsonObject getBlock(String blockHash) {
		try {
			return Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getblock", new Param[]{
					new Param(0,blockHash)})).get(0);
		}catch(Exception e){
			return null;
		}
	}
	
	public synchronized static String getBlockHash(String blockIndex) {
		try {
			return processJsonResult(Json.parseJson(Constants.getDcrwalletEndpoint().callMethod("getblockhash", new Param[]{
					new Param(1,blockIndex)})));
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Check if the result is null, if so return the error JSON Object.
	 * 
	 * @param jsonObjects
	 * @return String
	 */
	public synchronized static String processJsonResult(ArrayList<JsonObject> jsonObjects) {
		if(jsonObjects == null || jsonObjects.size() == 0) return "";
		
		if(jsonObjects.get(0).getValueByName("result").trim().equals("null")){
			return jsonObjects.get(1).getValueByName("code") + ": " + jsonObjects.get(1).getValueByName("message");
		}else{
			return jsonObjects.get(0).getValueByName("result");
		}
	}

}
