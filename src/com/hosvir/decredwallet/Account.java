package com.hosvir.decredwallet;

import com.deadendgine.Updatable;
import com.deadendgine.utils.Timer;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class Account extends Thread implements Updatable {
    private boolean running;
    public boolean forceUpdate;
    public boolean reloadComplete;
    private Timer updateTimer = new Timer(1000);
    public String name;
    public String balance = "0";
    public String spendableBalance = "0";
    public String pendingBalance = "0";
    public String lockedBalance = "0";
    public String totalBalance = "0";
    public String[] addresses;
    public ArrayList<JSONObject> transactions = new ArrayList<JSONObject>();

    /**
     * Construct a new account.
     *
     * @param name
     */
    public Account(String name){
        this.name = name;
        this.setName("Decred Wallet - Account Thread");
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
                reloadComplete = false;
                balance = Api.getBalance(name);
                spendableBalance = Api.getBalanceSpendable(name);
                pendingBalance = String.valueOf(Double.valueOf(spendableBalance) - Double.valueOf(balance));
                lockedBalance = Api.getLockedBalance(name);
                totalBalance = Api.getBalanceAll(name);
                addresses = Api.getAddressesByAccount(name).replaceAll("\\[|\\]|\"", "").split(",");
                transactions = Constants.getTransactionsByAccount(name);

                if(updateTimer.timeLimit <= 10000) updateTimer.timeLimit = Constants.getRandomNumber(10000, 45000);
                updateTimer.reset();
                forceUpdate = false;
                reloadComplete = true;
            }
        }catch(Exception e) {
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
