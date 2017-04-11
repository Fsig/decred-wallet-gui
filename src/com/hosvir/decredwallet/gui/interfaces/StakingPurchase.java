package com.hosvir.decredwallet.gui.interfaces;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.*;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.Label;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class StakingPurchase extends Interface {
    private int headerThird;
    private boolean ready;

    @Override
    public void init() {
        this.components.add(new Label("from", Constants.getLangValue("From-Label"), 40, 265));
        this.components.add(new Label("limit", Constants.getLangValue("Limit-Label"), 40, 315));
        this.components.add(new Label("fee", Constants.getLangValue("Transaction-Fee-Label"), 40, 365));
        this.components.add(new Label("address", Constants.getLangValue("Pool-Ticket-Address-Label"), 40, 415));
        this.components.add(new Label("pooladdr", Constants.getLangValue("Pool-Address-Label"), 40, 465));
        this.components.add(new Label("poolfee", Constants.getLangValue("Pool-Fee-Label"), 40, 515));
        this.components.add(new Label("tickets", Constants.getLangValue("Ticket-Count-Label"), 40, 565));

        this.components.add(new DropdownBox("fromInput", 250, 245, Engine.getWidth() - 295, 30, Constants.accountNames.toArray(new String[Constants.accountNames.size()])));
        this.components.add(new InputBox("limitInput", 250, 295, Engine.getWidth() - 295, 30));
        this.components.add(new InputBox("feeInput", 250, 345, Engine.getWidth() - 295, 30));
        this.components.add(new InputBox("addressInput", 250, 395, Engine.getWidth() - 295, 30));
        this.components.add(new InputBox("pooladdrInput", 250, 445, Engine.getWidth() - 295, 30));
        this.components.add(new InputBox("poolfeeInput", 250, 495, Engine.getWidth() - 295, 30));
        this.components.add(new InputBox("ticketInput", 250, 545, Engine.getWidth() - 295, 30));

        this.components.add(new Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 40, 595, 100, 35, ColorConstants.flatRed, ColorConstants.flatRedHover));

        Button confirmButton = new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 140, 595, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover);
        this.components.add(confirmButton);

        this.components.add(new Dialog("errordiag", ""));

        //getComponentByName("limitInput").enabled = false;
        getComponentByName("errordiag").width = 800;
        updateDefaults();

        headerThird = Engine.getWidth() / 9;
    }

    @Override
    public void update(long delta) {
        //Allow diag closing
        if (getComponentByName("errordiag").isActive()) {
            getComponentByName("errordiag").update(delta);
        }

        //Select current pool
        if (getComponentByName("fromInput").text == "" ||
                Constants.getPoolAccountName() != "" && !Constants.getPoolAccountName().toLowerCase().equals("pos-" + getComponentByName("fromInput").text.toLowerCase())) {
            getComponentByName("fromInput").text = Constants.getSelectedPoolAccountName();
        }

        if (!blockInput) {
            super.update(delta);

            //Update spend limit
            if (getComponentByName("limitInput").text == "0.00") {
                getComponentByName("limitInput").text = String.valueOf(Constants.globalCache.stakeInfo.get("difficulty"));
            }

            //Set fee
            if (getComponentByName("feeInput").text == "0.00") {
                getComponentByName("feeInput").text = "" + Constants.globalCache.walletFee;
            }

            //Check for ticket purchase
            if (Constants.isPrivatePassphraseSet() && getComponentByName("address").text != "") {
                if (Double.valueOf(getComponentByName("limitInput").text) >= Double.valueOf(String.valueOf(Constants.globalCache.stakeInfo.get("difficulty")))) {
                    ready = true;
                } else {
                    ready = false;

                    Constants.log("Spend limit is less than current difficulty.");
                    getComponentByName("errordiag").text = Constants.getLangValue("Spend-Limit-Message");

                    //Show dialog
                    this.blockInput = true;
                    Constants.navbar.blockInput = true;
                    getComponentByName("errordiag").selectedId = 0;
                }

                //Begin
                if (ready) {
                    //Unlock wallet
                    String unlock = getComponentByName("pooladdrInput").text != "" ? Api.unlockWallet("0") : Api.unlockWallet("30");

                    if (unlock != null && !unlock.contains("-14")) {
                        String result = Api.purchaseTicket(
                                getComponentByName("feeInput").text,
                                getComponentByName("fromInput").text,
                                getComponentByName("limitInput").text,
                                getComponentByName("addressInput").text,
                                getComponentByName("ticketInput").text,
                                getComponentByName("pooladdrInput").text,
                                getComponentByName("poolfeeInput").text
                        );

                        if (result.contains("not enough to purchase sstx")) {
                            Constants.log("Insufficient funds: " + result);
                            getComponentByName("errordiag").text = Constants.getLangValue("Insufficient-Funds-Error");

                            //Show dialog
                            this.blockInput = true;
                            Constants.navbar.blockInput = true;
                            getComponentByName("errordiag").selectedId = 0;
                        } else {
                            Constants.log("Stake result: " + result);
                            Constants.setClipboardString(result.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", ""));
                            getComponentByName("errordiag").text = Constants.getLangValue("Ticket-Purchase-Message") + ": " + Constants.getClipboardString();

                            Constants.globalCache.forceUpdate = true;
                            Constants.globalCache.forceUpdateInfo = true;
                            Constants.globalCache.forceUpdateTickets = true;
                            Constants.forceUpdateAllAccounts();

                            //Show dialog
                            this.blockInput = true;
                            Constants.navbar.blockInput = true;
                            getComponentByName("errordiag").selectedId = 0;
                        }

                        getComponentByName("limitInput").selectedId = -1;
                        getComponentByName("addressInput").selectedId = -1;
                    } else {
                        Constants.log("Error: " + unlock);
                        getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + unlock;

                        //Show dialog
                        this.blockInput = true;
                        Constants.navbar.blockInput = true;
                        getComponentByName("errordiag").selectedId = 0;
                    }
                } else {
                    Constants.log("Error: Not ready to purchase?");
                }

                //Clear password
                Constants.setPrivatePassPhrase(null);
            }


            //For each component
            for (com.hosvir.decredwallet.gui.Component c : components) {
                if (c.containsMouse && c.enabled) Main.containsMouse = true;

                //Drop down
                if (c instanceof DropdownBox) {
                    //Check if accounts are populated
                    if (DropdownBox.class.cast(c).lineItems == null || DropdownBox.class.cast(c).lineItems.length == 0) {
                        DropdownBox.class.cast(c).setLineItems(Constants.accountNames.toArray(new String[Constants.accountNames.size()]));
                        DropdownBox.class.cast(c).text = Constants.accountNames.get(0);
                    }
                }

                //Buttons
                if (c instanceof com.hosvir.decredwallet.gui.Button) {
                    if (c.selectedId == 0 && c.enabled) {
                        switch (c.name) {
                            case "cancel":
                                updateDefaults();
                                getComponentByName("limitInput").selectedId = -1;
                                getComponentByName("addressInput").selectedId = -1;
                                getComponentByName("feeInput").selectedId = -1;
                                getComponentByName("ticketInput").text = "";
                                getComponentByName("ticketInput").selectedId = -1;
                                getComponentByName("pooladdrInput").selectedId = -1;
                                getComponentByName("poolfeeInput").selectedId = -1;
                                break;
                            case "confirm":
                                if (getComponentByName("addressInput").text == "") {
                                    getComponentByName("errordiag").text = Constants.getLangValue("Address-Null-Error");

                                    //Show dialog
                                    this.blockInput = true;
                                    Constants.navbar.blockInput = true;
                                    getComponentByName("errordiag").selectedId = 0;
                                } else if (getComponentByName("ticketInput").text == "") {
                                    getComponentByName("errordiag").text = Constants.getLangValue("Ticket-Null-Error");

                                    //Show dialog
                                    this.blockInput = true;
                                    Constants.navbar.blockInput = true;
                                    getComponentByName("errordiag").selectedId = 0;
                                } else {
                                    blockInput = true;
                                    Constants.navbar.blockInput = true;
                                    Constants.unselectAllInputs(components);
                                    Constants.guiInterfaces.get(Constants.guiInterfaces.size() - 1).selectedId = 0;
                                }
                                break;
                        }
                    }

                    //Release button
                    c.selectedId = -1;
                }

                //Input boxes
                if (c instanceof InputBox) {
                    if (c.clickCount > 0) Constants.unselectOtherInputs(components, c);
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        //Content box
        g.drawImage(Images.getInterfaces()[6],
                10,
                140,
                10,
                65,
                null);

        g.setColor(Color.WHITE);
        g.fillRect(20,
                140,
                Engine.getWidth() - 40,
                65);

        g.drawImage(Images.getInterfaces()[7],
                Engine.getWidth() - 20,
                140,
                10,
                65,
                null);

        g.drawImage(Images.getInterfaces()[19],
                14,
                195,
                Engine.getWidth() - 28,
                60,
                null);


        //Draw info
        g.setColor(ColorConstants.labelColor);
        g.setFont(FontConstants.labelFont);
        g.drawString(Constants.getLangValue("Pool-Label"), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Pool-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Price-Label"), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Price-Label")) / 2), 170);

        g.drawString(Constants.getLangValue("Mempool-Label"), (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Mempool-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Ownmempool-Label"), (headerThird * 4) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Ownmempool-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Immature-Label"), (headerThird * 5) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Immature-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Live-Label"), (headerThird * 6) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Live-Label")) / 2), 170);

        g.drawString(Constants.getLangValue("Voted-Label"), (headerThird * 7) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Voted-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Missed-Label"), (headerThird * 8) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Missed-Label")) / 2), 170);
        g.drawString(Constants.getLangValue("Revoked-Label"), (headerThird * 9) - (headerThird / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Revoked-Label")) / 2), 170);

        if (Constants.globalCache.stakeInfo != null && Constants.globalCache.stakeInfo.size() > 0 && Constants.globalCache.stakeInfo.get("poolsize") != null) {
            g.setColor(ColorConstants.walletBalanceColor);
            g.setFont(FontConstants.walletBalanceFont);
            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("poolsize")), headerThird - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("poolsize"))) / 2), 190);
            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("difficulty")), (headerThird * 2) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("difficulty"))) / 2), 190);

            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("allmempooltix")), (headerThird * 3) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("allmempool"))) / 2), 190);
            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("ownmempooltix")), (headerThird * 4) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("ownmempool"))) / 2), 190);
            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("immature")), (headerThird * 5) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("immature"))) / 2), 190);

            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("live")), (headerThird * 6) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("live"))) / 2), 190);
            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("voted")), (headerThird * 7) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("voted"))) / 2), 190);
            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("missed")), (headerThird * 8) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("missed"))) / 2), 190);
            g.drawString(String.valueOf(Constants.globalCache.stakeInfo.get("revoked")), (headerThird * 9) - (headerThird / 2) - (g.getFontMetrics().stringWidth(String.valueOf(Constants.globalCache.stakeInfo.get("revoked"))) / 2), 190);
        }

        //Content box
        g.setColor(Color.WHITE);
        g.fillRect(30,
                225,
                Engine.getWidth() - 60,
                420);

        g.drawImage(Images.getInterfaces()[7],
                Engine.getWidth() - 30,
                225,
                10,
                420,
                null);

        g.drawImage(Images.getInterfaces()[6],
                20,
                225,
                10,
                420,
                null);

        g.drawImage(Images.getInterfaces()[19],
                24,
                635,
                Engine.getWidth() - 48,
                60,
                null);


        //Render
        super.render(g);

        //Dropdown
        getComponentByName("fromInput").render(g);
    }

    @Override
    public void resize() {
        getComponentByName("fromInput").width = Engine.getWidth() - 295;
        getComponentByName("limitInput").width = Engine.getWidth() - 295;
        getComponentByName("feeInput").width = Engine.getWidth() - 295;
        getComponentByName("addressInput").width = Engine.getWidth() - 295;
        getComponentByName("ticketInput").width = Engine.getWidth() - 295;
        getComponentByName("pooladdrInput").width = Engine.getWidth() - 295;
        getComponentByName("poolfeeInput").width = Engine.getWidth() - 295;
        getComponentByName("confirm").x = Engine.getWidth() - 140;

        getComponentByName("fromInput").resize();
        getComponentByName("limitInput").resize();
        getComponentByName("feeInput").resize();
        getComponentByName("addressInput").resize();
        getComponentByName("ticketInput").resize();
        getComponentByName("pooladdrInput").resize();
        getComponentByName("poolfeeInput").resize();
        getComponentByName("confirm").resize();

        headerThird = Engine.getWidth() / 9;
    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 3 && Constants.guiInterfaces.get(3).selectedId == 1;
    }

    public void updateDefaults() {
        getComponentByName("fromInput").text = "";
        getComponentByName("limitInput").text = "0.00";
        getComponentByName("feeInput").text = "0.00";
        getComponentByName("addressInput").text = Constants.getPoolTicketAddress();
        getComponentByName("pooladdrInput").text = Constants.getPoolAddress();
        getComponentByName("poolfeeInput").text = Constants.getPoolFeePercent();
    }
}
