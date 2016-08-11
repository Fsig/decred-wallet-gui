package com.hosvir.decredwallet;

import java.util.ArrayList;

import com.hosvir.decredwallet.utils.JsonObject;

/**
 * 
 * @author Troy
 *
 */
public class Ticket {
	private String ticketHash;
	private String address;
	private int voteBits;
	private String amount;
	private String fee;
	private int confirmations;
	private int block;
	private String time;
	private String blockHash;
	private int blockHeight;
	private boolean live;
	
	public Ticket(String ticketHash) {
		this.ticketHash = ticketHash;
		
		//Get the transaction details
		ArrayList<JsonObject> transaction = Api.getTransaction(ticketHash);
		
		this.amount = transaction.get(0).getValueByName("amount");
		this.fee = transaction.get(0).getValueByName("fee");
		this.confirmations = Integer.valueOf(transaction.get(0).getValueByName("confirmations"));
		this.blockHash = transaction.get(0).getValueByName("blockhash");
		this.time = transaction.get(0).getValueByName("time");
		this.voteBits = Integer.valueOf(transaction.get(0).getValueByName("vout"));
		this.address = transaction.get(1).getValueByName("address");
		
		JsonObject block = Api.getBlock(blockHash);
		
		try{
			this.blockHeight = Integer.valueOf(block.getValueByName("height"));
		}catch(Exception e){
			this.blockHeight = -1;
		}
		
		this.live = Boolean.valueOf(Api.existsLiveTicket(ticketHash));
	}

	public String getTicketHash() {
		return ticketHash;
	}

	public String getAddress() {
		return address;
	}

	public int getVoteBits() {
		return voteBits;
	}

	public String getAmount() {
		return amount;
	}

	public String getFee() {
		return fee;
	}

	public int getConfirmations() {
		return confirmations;
	}

	public int getBlock() {
		return block;
	}

	public String getTime() {
		return time;
	}

	public String getBlockHash() {
		return blockHash;
	}

	public int getBlockHeight() {
		return blockHeight;
	}
	
	public boolean isLive() {
		return live;
	}

}
