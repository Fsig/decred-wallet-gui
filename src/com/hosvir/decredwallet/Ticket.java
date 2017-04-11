package com.hosvir.decredwallet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
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
        JSONObject transaction = Api.getTransaction(ticketHash);
        JSONObject detail = null;

        try {
            JSONArray details = (JSONArray) transaction.get("details");
            detail = (JSONObject) details.get(0);
        } catch (NullPointerException e) {
            this.amount = "0";
            this.fee = "0";
            this.confirmations = 0;
            this.blockHash = "";
            this.time = "00000000";
            this.voteBits = 0;
            this.address = "";
            this.blockHeight = 0;
            this.live = false;
            return;
        }

        this.amount = transaction.get("amount").toString();
        this.fee = transaction.get("fee").toString();

        try {
            this.confirmations = Integer.valueOf(transaction.get("confirmations").toString());
        } catch (Exception e) {
            this.confirmations = -1;
        }

        this.blockHash = transaction.get("blockhash").toString();
        this.time = transaction.get("time").toString();

        if (detail != null) {
            this.voteBits = Integer.valueOf(detail.get("vout").toString());
        }

        this.address = String.valueOf(transaction.get("address"));

        JSONObject block = Api.getBlock(blockHash);

        try {
            this.blockHeight = Integer.valueOf(block.get("height").toString());
        } catch (Exception e) {
            this.blockHeight = -1;
        }

        this.live = Api.existsLiveTicket(ticketHash);
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
