package com.hosvir.decredwallet.gui.interfaces.popups;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.*;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class CreateAccount extends Interface {
    public void init() {
        selectedId = -1;
        InputBox passphrase = new InputBox("passphrase", (Engine.getWidth() / 2) - 250, (Engine.getHeight() / 2) + 40, 500, 30);
        passphrase.textHidden = true;

        this.components.add(new InputBox("account", (Engine.getWidth() / 2) - 250, Engine.getHeight() / 2, 500, 30));
        this.components.add(passphrase);
        this.components.add(new Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 30, (Engine.getHeight() / 2) + 50, 100, 35, ColorConstants.flatRed, ColorConstants.flatRedHover));
        this.components.add(new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 130, (Engine.getHeight() / 2) + 50, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));

        this.components.add(new Dialog("errordiag", ""));
        getComponentByName("errordiag").width = 800;
    }

    @Override
    public void update(long delta) {
        //Update
        super.update(delta);

        //For each component
        for (Component c : components) {
            if (c.containsMouse && c.enabled) Main.containsMouse = true;

            //Buttons
            if (c instanceof Button) {
                if (c.selectedId == 0) {
                    switch (c.name) {
                        case "cancel":
                            Constants.blockInterfaces(false, this);
                            getComponentByName("account").text = "";
                            getComponentByName("account").selectedId = -1;
                            getComponentByName("passphrase").text = "";
                            getComponentByName("passphrase").selectedId = -1;
                            this.selectedId = -1;
                            break;
                        case "confirm":
                            Constants.setPrivatePassPhrase(getComponentByName("passphrase").text);

                            //Unlock wallet
                            String unlock = Api.unlockWallet("30");

                            if (unlock == null || unlock.contains("-14")) {
                                Constants.log("Error: " + unlock);
                                getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + unlock;

                                //Show dialog
                                this.blockInput = true;
                                Constants.navbar.blockInput = true;
                                getComponentByName("errordiag").selectedId = 0;
                                return;
                            } else {
                                Api.createNewAccount(getComponentByName("account").text);
                                Constants.reloadAccounts();
                                Constants.blockInterfaces(false, this);
                                getComponentByName("account").text = "";
                                getComponentByName("account").selectedId = -1;
                                getComponentByName("passphrase").text = "";
                                getComponentByName("passphrase").selectedId = -1;
                                this.selectedId = -1;
                            }
                            break;
                    }
                }

                c.selectedId = -1;
            }

            //Input boxes
            if (c instanceof InputBox) {
                if (c.clickCount > 0) Constants.unselectOtherInputs(components, c);
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(ColorConstants.transparentBlack);
        g.fillRect(0, 0, Engine.getWidth(), Engine.getHeight());

        g.setColor(Color.WHITE);
        g.fillRect(0, (Engine.getHeight() / 2) - 100, Engine.getWidth(), 200);

        //Label
        g.setFont(FontConstants.labelFont);
        g.setColor(ColorConstants.labelColor);

        g.drawString(Constants.getLangValue("Add-Account-Message"), (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Add-Account-Message")) / 2), Engine.getHeight() / 2 - 50);


        //Render
        super.render(g);
    }

    @Override
    public boolean isActive() {
        return selectedId == 0;
    }

    @Override
    public void resize() {
        getComponentByName("account").x = (Engine.getWidth() / 2) - 250;
        getComponentByName("account").y = Engine.getHeight() / 2 - 20;
        getComponentByName("account").resize();

        getComponentByName("passphrase").x = (Engine.getWidth() / 2) - 250;
        getComponentByName("passphrase").y = (Engine.getHeight() / 2) + 20;
        getComponentByName("passphrase").resize();

        getComponentByName("cancel").y = (Engine.getHeight() / 2) + 50;
        getComponentByName("confirm").y = (Engine.getHeight() / 2) + 50;
        getComponentByName("confirm").x = Engine.getWidth() - 130;

        getComponentByName("cancel").resize();
        getComponentByName("confirm").resize();
    }
}
