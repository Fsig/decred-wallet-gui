package com.hosvir.decredwallet;

import com.hosvir.decredwallet.gui.Main;
import com.hosvir.decredwallet.websockets.DecredEndpoint;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class DecredWallet {

    public static void main(String[] args) {
        new DecredWallet();
    }

    public DecredWallet() {
        //Setup constants
        Constants.initialise();

        //Create new end points
        Constants.setDcrdEndpoint(new DecredEndpoint(""));
        Constants.setDcrwalletEndpoint(new DecredEndpoint(""));

        //Default to localhost
        Constants.setDcrdEnpointURI("127.0.0.1");
        Constants.setDcrwalletEnpointURI("127.0.0.1");

        //Launch GUI
        Constants.setMainGui(new Main());
    }
}
