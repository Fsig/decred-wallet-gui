package com.hosvir.decredwallet;

import com.deadendgine.Updatable;
import com.deadendgine.utils.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class GlobalCache extends Thread implements Updatable {
    private boolean running;
    public boolean forceUpdate;
    public boolean forceUpdateInfo;
    public boolean forceUpdatePeers;
    public boolean forceUpdateTickets;
    private Timer updateTimer = new Timer(1000);
    private Timer infoTimer = new Timer(1000);
    private Timer peerTimer = new Timer(1000);
    private Timer ticketTimer = new Timer(1000);
    public String walletFee = "0.00";
    public JSONArray transactions;
    public JSONObject info;
    public JSONObject currentBlock;
    public JSONObject stakeInfo;
    public JSONArray peers;
    public ArrayList<Ticket> tickets;
    private TicketComparator tc = new TicketComparator();

    public GlobalCache() {
        tickets = new ArrayList<Ticket>();

        this.setName("Decred Wallet - Cache Thread");
        this.setPriority(NORM_PRIORITY);
        this.start();
    }

    public void run() {
        running = true;

        while(running){
            if(Constants.isWalletReady())
                update(0);

            try {
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void update(long delta) {
        try {
            if(updateTimer.isUp() || forceUpdate){
                walletFee = Api.getWalletFee();
                transactions = Api.getTransactions();

                if(updateTimer.timeLimit <= 10000) updateTimer.timeLimit = Constants.getRandomNumber(10000, 45000);
                updateTimer.reset();
                forceUpdate = false;
            }

            //Info update
            if(infoTimer.isUp() || forceUpdateInfo){
                info = Api.getInfo();
                currentBlock = Api.getBlock(Api.getBlockHash(String.valueOf(info.get("blocks"))));
                stakeInfo = Api.getStakeInfo();

                if(infoTimer.timeLimit <= 10000) infoTimer.timeLimit = Constants.getRandomNumber(10000, 45000);
                infoTimer.reset();
                forceUpdateInfo = false;
            }

            //Peer update
            if(peerTimer.isUp() || forceUpdatePeers){
                Api.ping();
                peers = Api.getPeerInfo();
                Constants.guiInterfaces.get(6).resize();

                if(peerTimer.timeLimit <= 10000) peerTimer.timeLimit = Constants.getRandomNumber(10000, 45000);
                peerTimer.reset();
                forceUpdatePeers = false;
            }

            //Ticket update
            if (ticketTimer.isUp() || forceUpdateTickets) {
                tickets.clear();
                String tiks = Api.getTickets(true);

                if (tiks != null) {
                    for (String s : tiks.replaceAll("\\[|\\]|\"", "").split(",")) {
                        if (s.length() > 10) {
                            tickets.add(new Ticket(s));
                        }
                    }

                    //Sort ticket order
                    Collections.sort(tickets, tc);
                }

                if(ticketTimer.timeLimit <= 10000) ticketTimer.timeLimit = Constants.getRandomNumber(30000, 85000);
                ticketTimer.reset();
                forceUpdateTickets = false;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
