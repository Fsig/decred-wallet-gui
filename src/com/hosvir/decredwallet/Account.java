package com.hosvir.decredwallet;

import java.util.ArrayList;

import com.deadendgine.Updatable;
import com.deadendgine.utils.Timer;
import com.hosvir.decredwallet.utils.JsonObject;

/**
 * 
 * @author Troy
 *
 */
public class Account extends Thread implements Updatable {
	private boolean running;
	public boolean forceUpdate;
	private Timer updateTimer = new Timer(1000);
	public String name;
	public String balance = "0";
	public String spendableBalance = "0";
	public String pendingBalance = "0";
	public String lockedBalance = "0";
	public String totalBalance = "0";
	public String[] addresses;
	public ArrayList<JsonObject> transactions = new ArrayList<JsonObject>();
	
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
		if(updateTimer.isUp() || forceUpdate){
			balance = Api.getBalance(name);
			spendableBalance = Api.getBalanceSpendable(name);
			pendingBalance = String.valueOf(Double.valueOf(spendableBalance) - Double.valueOf(balance));
			lockedBalance = Api.getLockedBalance(name);
			totalBalance = Api.getBalanceAll(name);
			addresses = Api.getAddressesByAccount(name).split(",");
			transactions = Constants.getTransactionsByAccount(name);
			
			if(updateTimer.timeLimit <= 180000) updateTimer.timeLimit = Constants.getRandomNumber(100000, 180000);
			updateTimer.reset();
			forceUpdate = false;
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
