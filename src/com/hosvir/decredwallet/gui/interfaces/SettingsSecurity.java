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
public class SettingsSecurity extends Interface {
    @Override
    public void init() {
        this.components.add(new Label("addressLabel", Constants.getLangValue("Address-Label"), 40, 190));
        this.components.add(new Label("oldPassphraseLabel", Constants.getLangValue("Old-Passphrase-Label"), 40, 280));
        this.components.add(new Label("newPassphraseLabel", Constants.getLangValue("New-Passphrase-Label"), 40, 330));
        this.components.add(new Label("confirmPassphraseLabel", Constants.getLangValue("Confirm-Passphrase-Label"), 40, 380));

        InputBox oldPassphraseInput = new InputBox("oldPassphraseInput", 250, 260, Engine.getWidth() - 295, 30);
        InputBox newPassphraseInput = new InputBox("newPassphraseInput", 250, 310, Engine.getWidth() - 295, 30);
        InputBox confirmPassphraseInput = new InputBox("confirmPassphraseInput", 250, 360, Engine.getWidth() - 295, 30);
        oldPassphraseInput.textHidden = true;
        newPassphraseInput.textHidden = true;
        confirmPassphraseInput.textHidden = true;

        Button dumpButton = new Button("dumpWallet", "Dump wallet", Engine.getWidth() - 190, Engine.getHeight() - 130, 150, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover);
        dumpButton.enabled = false;

        this.components.add(oldPassphraseInput);
        this.components.add(newPassphraseInput);
        this.components.add(confirmPassphraseInput);
        this.components.add(new InputBox("addressInput", 250, 170, Engine.getWidth() - 520, 30));
        this.components.add(new Button("dumpPrivateKey", Constants.getLangValue("Dump-Private-Key-Button-Text"), Engine.getWidth() - 240, 170, 200, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
        this.components.add(new Button("changePassphrase", Constants.getLangValue("Change-Passphrase-Button-Text"), Engine.getWidth() - 240, 410, 200, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
        this.components.add(new Button("getMasterPublicKey", Constants.getLangValue("Get-Master-Public-Key-Button-Text"), 40, Engine.getHeight() - 130, 220, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
        this.components.add(dumpButton);
        this.components.add(new Dialog("errordiag", ""));

        selectedId = -1;
    }

    @Override
    public synchronized void update(long delta) {
        //Allow diag closing
        if (getComponentByName("errordiag").isActive()) getComponentByName("errordiag").update(delta);

        if (!blockInput) {
            super.update(delta);

            //Check for dump private key
            if (Constants.isPrivatePassphraseSet() && getComponentByName("addressInput").text != "") {
                //Unlock wallet
                String unlock = Api.unlockWallet("30");

                if (unlock == null | unlock.trim().length() < 1) {
                    String address = Api.dumpPrivateKey(getComponentByName("addressInput").text);

                    if (address.toLowerCase().contains("invalid")) {
                        getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + address;
                    } else {
                        Constants.setClipboardString(address);

                        //Set dialog
                        getComponentByName("errordiag").text = Constants.getLangValue("Clipboard-Message");
                    }

                    //Show dialog
                    this.blockInput = true;
                    Constants.navbar.blockInput = true;
                    getComponentByName("errordiag").selectedId = 0;
                } else {
                    Constants.log("Error: " + unlock);
                    getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + unlock;

                    //Show dialog
                    this.blockInput = true;
                    Constants.navbar.blockInput = true;
                    getComponentByName("errordiag").selectedId = 0;
                }

                getComponentByName("addressInput").text = "";
            }

            //For each component
            for (com.hosvir.decredwallet.gui.Component c : components) {
                if (c.containsMouse) Main.containsMouse = true;

                //Input
                if (c instanceof InputBox) {
                    if (c.clickCount > 0) Constants.unselectOtherInputs(components, c);
                }

                //Button
                if (c instanceof com.hosvir.decredwallet.gui.Button) {
                    if (c.selectedId == 0 && c.enabled) {
                        System.out.println("Pressed button: " + c.name);

                        switch (c.name) {
                            case "dumpPrivateKey":
                                blockInput = true;
                                Constants.navbar.blockInput = true;
                                Constants.unselectAllInputs(components);
                                Constants.guiInterfaces.get(Constants.guiInterfaces.size() - 1).selectedId = 0;
                                break;
                            case "changePassphrase":
                                if (!getComponentByName("newPassphraseInput").text.contains(getComponentByName("confirmPassphraseInput").text)) {
                                    getComponentByName("errordiag").text = Constants.getLangValue("Passphrase-Mismatch-Error");
                                } else {
                                    String changed = Api.walletPassphraseChange(getComponentByName("oldPassphraseInput").text, getComponentByName("newPassphraseInput").text);

                                    if (changed == null | changed.trim().length() < 1) {
                                        getComponentByName("errordiag").text = Constants.getLangValue("Passphrase-Changed-Message");
                                        getComponentByName("oldPassphraseInput").text = "";
                                        getComponentByName("newPassphraseInput").text = "";
                                        getComponentByName("confirmPassphraseInput").text = "";

                                        Constants.unselectAllInputs(components);
                                    } else {
                                        Constants.log("Error: " + changed);
                                        getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + changed;
                                    }
                                }

                                //Show dialog
                                this.blockInput = true;
                                Constants.navbar.blockInput = true;
                                getComponentByName("errordiag").selectedId = 0;
                                break;
                            case "getMasterPublicKey":
                                Constants.setClipboardString(Api.getMasterPubkey());

                                //Set dialog
                                getComponentByName("errordiag").text = Constants.getLangValue("Clipboard-Message");

                                //Show dialog
                                this.blockInput = true;
                                Constants.navbar.blockInput = true;
                                getComponentByName("errordiag").selectedId = 0;
                                break;
                            case "dumpWallet":
                                // TODO pending method being implemented in Decred
                                break;
                        }

                        //Release button
                        c.selectedId = -1;
                    }

                }
            }
        }
    }

    @Override
    public synchronized void render(Graphics2D g) {
        //Content box
        g.drawImage(Images.getInterfaces()[6],
                20,
                150,
                10,
                Engine.getHeight() - 230,
                null);

        g.setColor(Color.WHITE);
        g.fillRect(30,
                150,
                Engine.getWidth() - 60,
                Engine.getHeight() - 230);

        g.drawImage(Images.getInterfaces()[7],
                Engine.getWidth() - 30,
                150,
                10,
                Engine.getHeight() - 230,
                null);

        g.drawImage(Images.getInterfaces()[19],
                24,
                Engine.getHeight() - 90,
                Engine.getWidth() - 48,
                60,
                null);


        //Draw separate lines
        g.setColor(ColorConstants.settingsSelectedColor);
        g.drawLine(30, 230, Engine.getWidth() - 30, 230);
        g.drawLine(30, 470, Engine.getWidth() - 30, 470);

        //Render
        super.render(g);
    }

    @Override
    public void resize() {
        getComponentByName("oldPassphraseInput").width = Engine.getWidth() - 295;
        getComponentByName("newPassphraseInput").width = Engine.getWidth() - 295;
        getComponentByName("confirmPassphraseInput").width = Engine.getWidth() - 295;
        getComponentByName("addressInput").width = Engine.getWidth() - 520;

        getComponentByName("dumpPrivateKey").x = Engine.getWidth() - 240;
        getComponentByName("changePassphrase").x = Engine.getWidth() - 240;
        getComponentByName("dumpWallet").x = Engine.getWidth() - 190;
        getComponentByName("getMasterPublicKey").y = Engine.getHeight() - 130;
        getComponentByName("dumpWallet").y = Engine.getHeight() - 130;

        super.resize();
    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 0 && Constants.guiInterfaces.get(9).selectedId == 1;
    }
}
