package com.hosvir.decredwallet;

import com.hosvir.decredwallet.gui.interfaces.Login;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class StartProcesses extends Thread {

    public StartProcesses() {
        this.setName("Decred Wallet - Start Processes");
        this.setPriority(MAX_PRIORITY);
    }

    public void run() {
        //No local DCRD
        if (Processes.getClosestProcess("dcrd") == -1) {
            sleep(1000);
            Login.addLoadingMessage("Starting local DCRD process, this may take a minute.");

            //Start DCRD
            Constants.log("Starting Daemon.");
            Constants.setDaemonProcess(new LocalProcess(Constants.getDaemonCommand()));

            //Check to see if the daemon is ready
            while (!Constants.isDaemonProcessReady()) {
                for (String s : Constants.getDaemonProcess().log)
                    if (s.contains("RPC server listening"))
                        Constants.setDaemonProcessReady(true);

                //Sleep
                sleep(100);
            }
        }

        //No local DCRWALLET
        if (Processes.getClosestProcess("dcrwallet") == -1) {
            sleep(1000);
            Login.addLoadingMessage("Starting local DCRWALLET process, this may take a minute.");

            //Start DCRWALLET
            Constants.log("Starting Wallet.");
            Constants.setWalletProcess(new LocalProcess(Constants.getWalletCommand()));

            //Check to see if the daemon is ready
            while (!Constants.isWalletProcessReady()) {
                for (String s : Constants.getWalletProcess().log)
                    if (s.contains("Opened wallet"))
                        Constants.setWalletProcessReady(true);
                    else if (s.contains("invalid passphrase for master public key"))
                        Constants.log("Need public key, edit settings.conf");

                //Sleep
                sleep(100);
            }

            sleep(2000);
        }

        Login.startLocal = false;
    }

    /**
     * Sleep the thread
     *
     * @param time
     */
    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
