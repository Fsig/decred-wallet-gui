package com.hosvir.decredwallet.gui.interfaces;

import com.deadendgine.Engine;
import com.deadendgine.input.Mouse;
import com.hosvir.decredwallet.Account;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.Contact;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.*;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class Send extends Interface {
    private int headerThird;
    private boolean readyToSend, sendMany;
    private ArrayList<String> suggestions;
    private int hoverSuggestionId;
    private String to;

    @Override
    public void init() {
        suggestions = new ArrayList<String>();
        headerThird = (Engine.getWidth() - 200) / 4;

        Button cancel = new Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 350, 500, 100, 35, ColorConstants.flatRed, ColorConstants.flatRedHover);
        Button send = new Button("send", Constants.getLangValue("Send-Button-Text"), Engine.getWidth() - 150, 500, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover);
        send.enabled = false;


        InputBox from = new InputBox("from", 500, 200, Engine.getWidth() - 545, 30);
        InputBox to = new InputBox("to", 500, 250, Engine.getWidth() - 545, 30);
        InputBox comment = new InputBox("comment", 500, 300, Engine.getWidth() - 545, 80);
        InputBox fee = new InputBox("fee", 500, 400, Engine.getWidth() - 545, 30);
        InputBox amount = new InputBox("amount", 500, 450, Engine.getWidth() - 545, 30);

        from.enabled = false;
        comment.enabled = false;
        fee.text = Constants.globalCache.walletFee;


        this.components.add(from);
        this.components.add(to);
        this.components.add(comment);
        this.components.add(fee);
        this.components.add(amount);
        this.components.add(cancel);
        this.components.add(send);
        this.components.add(new Dialog("errordiag", ""));
        this.getComponentByName("errordiag").width = 800;
    }

    @Override
    public void update(long delta) {
        //Allow diag closing
        if (getComponentByName("errordiag").isActive()) getComponentByName("errordiag").update(delta);

        if (!blockInput) {
            if (rectangles == null && Constants.accounts.size() > 0) {
                rectangles = new Rectangle[Constants.accounts.size()];

                for (int i = 0; i < rectangles.length; i++) {
                    rectangles[i] = new Rectangle(0,
                            60 + i * 60,
                            295,
                            60);
                }
            }

            //Update
            super.update(delta);

            //Assign name to input box
            if (getComponentByName("from").text != Constants.accounts.get(selectedId).name) {
                getComponentByName("from").text = Constants.accounts.get(selectedId).name;
            }

            //Set fee
            if (getComponentByName("fee").text == "0.00") {
                getComponentByName("fee").text = "" + Constants.globalCache.walletFee;
            }

            //Enable send
            if (getComponentByName("to").text != "" && getComponentByName("fee").text != "" && getComponentByName("amount").text != "") {
                getComponentByName("send").enabled = true;
            } else {
                getComponentByName("send").enabled = false;
            }


            //Check for sending
            if (Constants.isPrivatePassphraseSet() && getComponentByName("to").text != "") {
                if (getComponentByName("fee").text != Api.getWalletFee()) {
                    if (Api.setTxFee(getComponentByName("fee").text)) {
                        readyToSend = true;
                    } else {
                        readyToSend = false;
                    }
                } else {
                    readyToSend = true;
                }

                if (readyToSend) {
                    //Check if sending to contact
                    if (Constants.isContact(getComponentByName("to").text)) {
                        to = Constants.getContact(getComponentByName("to").text).getAddress();
                    } else if (getComponentByName("to").text.contains(",")) {
                        sendMany = true;
                        to = getComponentByName("to").text;
                    } else {
                        to = getComponentByName("to").text;
                    }

                    //Unlock wallet
                    String unlock = Api.unlockWallet("30");

                    if (unlock != null && !unlock.contains("-14")) {
                        String txId = null;

                        if (sendMany) {
                            txId = Api.sendMany(getComponentByName("from").text,
                                    to,
                                    getComponentByName("comment").text,
                                    getComponentByName("amount").text);
                        } else {
                            txId = Api.sendFrom(getComponentByName("from").text,
                                    to,
                                    getComponentByName("comment").text,
                                    getComponentByName("amount").text);
                        }

                        if (txId == "" || txId.contains("Address count must equal")) {
                            Constants.log("Unable to send DCR. " + txId);
                        } else if (txId.contains("-32603")) {
                            Constants.log("Insufficient funds: " + txId);
                            getComponentByName("errordiag").text = Constants.getLangValue("Insufficient-Funds-Error");

                            //Show dialog
                            this.blockInput = true;
                            Constants.navbar.blockInput = true;
                            getComponentByName("errordiag").selectedId = 0;
                        } else {
                            Constants.log("Sucess, transaction id: " + txId);
                            Constants.setClipboardString(txId);
                            getComponentByName("errordiag").text = Constants.getLangValue("Clipboard-Message") + ": " + txId;

                            //Show dialog
                            this.blockInput = true;
                            Constants.navbar.blockInput = true;
                            getComponentByName("errordiag").selectedId = 0;

                            Constants.forceUpdateAllAccounts();
                            Constants.globalCache.forceUpdate = true;
                        }
                    } else {
                        Constants.log("Error: " + unlock);
                        getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + unlock;

                        //Show dialog
                        this.blockInput = true;
                        Constants.navbar.blockInput = true;
                        getComponentByName("errordiag").selectedId = 0;
                    }
                } else {
                    Constants.log("Unable to set Wallet fee, sending cancelled.");
                }

                //Clear password
                Constants.setPrivatePassPhrase(null);

                //Force update accounts
                for (Account a : Constants.accounts) a.forceUpdate = true;
                Constants.globalCache.forceUpdate = true;

                //Reset form
                resetForm();
                sendMany = false;
            }

            //For each component
            for (Component c : components) {
                if (c.containsMouse) Main.containsMouse = true;

                //Buttons
                if (c instanceof Button) {
                    if (c.selectedId == 0 && c.enabled) {
                        switch (c.name) {
                            case "cancel":
                                resetForm();
                                break;
                            case "send":
                                blockInput = true;
                                Constants.navbar.blockInput = true;
                                Constants.unselectAllInputs(components);
                                Constants.guiInterfaces.get(Constants.guiInterfaces.size() - 1).selectedId = 0;
                                break;
                        }

                        //Release button
                        c.selectedId = -1;
                    }
                }

                //Input boxes
                if (c instanceof InputBox) {

                    //Check for similar contact
                    if (c.getName() == "to") {
                        if (c.text.trim() != "" && c.selectedId == 0) {
                            for (Contact cc : Constants.contacts) {
                                if (cc.getName().toLowerCase().contains(getComponentByName("to").text.toLowerCase())) {
                                    if (!suggestions.contains(cc.getName())) suggestions.add(cc.getName());
                                } else {
                                    if (suggestions.contains(cc.getName())) suggestions.remove(cc.getName());
                                }

                                //Loop over each suggestion
                                for (int i = 0; i < suggestions.size(); i++) {
                                    if (new Rectangle(500, 281 + i * 35, Engine.getWidth() - 545, 35).contains(Mouse.point)) {
                                        containsMouse = true;
                                        hoverSuggestionId = i;

                                        if (Mouse.isMouseDown(MouseEvent.BUTTON1)) {
                                            c.text = suggestions.get(i);
                                            c.selectedId = -1;
                                            Mouse.release(MouseEvent.BUTTON1);
                                        }
                                    }
                                }

                                if (!containsMouse) hoverSuggestionId = -1;
                            }
                        }
                    }

                    if (c.clickCount > 0) Constants.unselectOtherInputs(components, c);
                }
            }


            //Rename account
            if (doubleClicked && !Constants.accounts.get(selectedId).name.startsWith("default") && !Constants.accounts.get(selectedId).name.startsWith("imported")) {
                Constants.accountToRename = Constants.accounts.get(selectedId).name;
                blockInput = true;
                Constants.navbar.blockInput = true;
                Constants.guiInterfaces.get(Constants.guiInterfaces.size() - 2).selectedId = 0;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        //Header
        g.setColor(Color.WHITE);
        g.fillRect(0,
                60,
                Engine.getWidth(),
                95);

        g.drawImage(Images.getInterfaces()[19],
                0,
                150,
                Engine.getWidth(),
                60,
                null);

        //Sidebar
        g.fillRect(0,
                60,
                290,
                Engine.getHeight());

        g.drawImage(Images.getInterfaces()[5],
                290,
                60,
                10,
                Engine.getHeight(),
                null);


        if (rectangles != null) {
            for (int i = 0; i < rectangles.length; i++) {
                //Selected wallet
                g.setColor(ColorConstants.settingsSelectedColor);
                if (i == selectedId || i == hoverId) {
                    g.fillRect(rectangles[i].x,
                            rectangles[i].y,
                            rectangles[i].width,
                            rectangles[i].height);
                }

                g.fillRect(rectangles[i].x,
                        rectangles[i].y + 60,
                        rectangles[i].width,
                        1);

                //Wallet Labels
                g.setColor(ColorConstants.walletNameColor);
                g.setFont(FontConstants.walletNameFont);
                g.drawString(Constants.accounts.get(i).name, 6, 98 + i * 60);


                //Wallet Balance
                g.setColor(ColorConstants.walletBalanceColor);
                g.setFont(FontConstants.walletBalanceFont);
                g.drawString("" + Constants.accounts.get(i).totalBalance, 285 - g.getFontMetrics().stringWidth("" + Constants.accounts.get(i).totalBalance), 98 + i * 60);
            }
        }

        if (Constants.accounts.size() > 0) {
            //DCR and Balance
            g.setColor(ColorConstants.walletBalanceColor);
            g.setFont(FontConstants.dcrFont);
            g.drawString(Constants.getLangValue("DCR-Label"), Engine.getWidth() / 2, 100);

            g.setColor(ColorConstants.walletNameColor);
            g.setFont(FontConstants.totalBalanceFont);
            g.drawString("" + Constants.accounts.get(selectedId).totalBalance, (Engine.getWidth() + 150) / 2, 100);


            //Available, Pending and Locked
            g.setColor(ColorConstants.labelColor);
            g.setFont(FontConstants.labelFont);
            g.drawString(Constants.getLangValue("Available-Label"), Engine.getWidth() - (headerThird * 3), 125);
            g.drawString(Constants.getLangValue("Pending-Label"), Engine.getWidth() - (headerThird * 2), 125);
            g.drawString(Constants.getLangValue("Locked-Label"), Engine.getWidth() - (headerThird * 1), 125);

            g.setColor(ColorConstants.walletBalanceColor);
            g.setFont(FontConstants.walletBalanceFont);
            g.drawString("" + Constants.accounts.get(selectedId).spendableBalance, Engine.getWidth() - (headerThird * 3) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).spendableBalance) / 2), 145);
            g.drawString("" + Constants.accounts.get(selectedId).pendingBalance, Engine.getWidth() - (headerThird * 2) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).pendingBalance) / 2), 145);
            g.drawString("" + Constants.accounts.get(selectedId).lockedBalance, Engine.getWidth() - (headerThird * 1) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).lockedBalance) / 2), 145);


            //Content box
            g.drawImage(Images.getInterfaces()[6],
                    320,
                    180,
                    10,
                    370,
                    null);

            g.setColor(Color.WHITE);
            g.fillRect(330,
                    180,
                    Engine.getWidth() - 360,
                    370);

            g.drawImage(Images.getInterfaces()[7],
                    Engine.getWidth() - 30,
                    180,
                    10,
                    370,
                    null);

            g.drawImage(Images.getInterfaces()[19],
                    324,
                    540,
                    Engine.getWidth() - 348,
                    60,
                    null);


            //Draw log data
            g.setFont(FontConstants.labelFont);
            g.setColor(ColorConstants.labelColor);

            g.drawString(Constants.getLangValue("From-Label"), 350, 220);
            g.drawString(Constants.getLangValue("To-Label"), 350, 270);
            g.drawString(Constants.getLangValue("Comment-Label"), 350, 345);
            g.drawString(Constants.getLangValue("Fee-Label"), 350, 425);
            g.drawString(Constants.getLangValue("Amount-Label"), 350, 475);

            //Render
            super.render(g);

            //Suggestions
            if (getComponentByName("to").selectedId == 0 && getComponentByName("to").text.trim() != "") {
                g.setColor(Color.WHITE);
                g.fillRect(500,
                        281,
                        Engine.getWidth() - 545,
                        35 * suggestions.size());

                g.setColor(ColorConstants.settingsSelectedColor);
                g.drawRect(500,
                        281,
                        Engine.getWidth() - 545,
                        35 * suggestions.size());

                g.setFont(FontConstants.settingsFont);

                for (int i = 0; i < suggestions.size(); i++) {
                    if (hoverSuggestionId == i) g.setColor(ColorConstants.flatBlue);
                    else g.setColor(ColorConstants.labelColor);
                    g.drawString(suggestions.get(i), 500 + 10, 271 + ((i + 1) * 35));
                }
            }
        }


    }

    @Override
    public void resize() {
        headerThird = (Engine.getWidth() - 200) / 4;

        //Resize send button
        getComponentByName("send").x = Engine.getWidth() - 150;
        getComponentByName("send").resize();

        //Resize input boxes
        for (Component c : components) {
            if (c instanceof InputBox) {
                c.width = Engine.getWidth() - 545;
                c.resize();
            }
        }

    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 5;
    }

    /**
     * Reset all the fields on the form.
     */
    public void resetForm() {
        for (Component c : components)
            if (c instanceof InputBox)
                c.text = "";

        getComponentByName("fee").text = "" + Constants.globalCache.walletFee;
    }
}
