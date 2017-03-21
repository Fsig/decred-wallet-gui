package com.hosvir.decredwallet.gui.interfaces.popups;

import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.deadendgine.Engine;
import com.hosvir.decredwallet.gui.*;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class RenameAccount extends Interface {
    public void init() {
        selectedId = -1;

        this.components.add(new InputBox("account", (Engine.getWidth() / 2) - 250,Engine.getHeight() / 2,500,30));
        this.components.add(new com.hosvir.decredwallet.gui.Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 30, (Engine.getHeight() / 2) + 50, 100, 35, ColorConstants.flatRed, ColorConstants.flatRedHover));
        this.components.add(new com.hosvir.decredwallet.gui.Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 130, (Engine.getHeight() / 2) + 50, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
    }

    @Override
    public void update(long delta) {
        //Update
        super.update(delta);

        //For each component
        for(com.hosvir.decredwallet.gui.Component c : components) {
            if(c.containsMouse && c.enabled) Main.containsMouse = true;

            if(c instanceof com.hosvir.decredwallet.gui.Button) {
                if(c.selectedId == 0){
                    switch(c.name){
                        case "cancel":
                            Constants.navbar.blockInput = false;
                            Constants.blockInterfaces(false, this);
                            getComponentByName("account").text = "";
                            getComponentByName("account").selectedId = -1;
                            this.selectedId = -1;
                            break;
                        case "confirm":
                            Api.renameAccount(Constants.accountToRename, getComponentByName("account").text);
                            Constants.reloadAccounts();
                            Constants.blockInterfaces(false, this);
                            getComponentByName("account").text = "";
                            getComponentByName("account").selectedId = -1;
                            Constants.accountToRename = "";
                            this.selectedId = -1;
                            break;
                    }
                }

                c.selectedId = -1;
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

        g.drawString(Constants.getLangValue("Rename-Account-Message") + " " + Constants.accountToRename, (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Rename-Account-Message") + " " + Constants.accountToRename) / 2), Engine.getHeight() / 2 - 30);


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
        getComponentByName("account").y = Engine.getHeight() / 2;
        getComponentByName("account").resize();

        getComponentByName("cancel").y = (Engine.getHeight() / 2) + 50;
        getComponentByName("confirm").y = (Engine.getHeight() / 2) + 50;
        getComponentByName("confirm").x = Engine.getWidth() - 130;

        getComponentByName("cancel").resize();
        getComponentByName("confirm").resize();
    }
}
