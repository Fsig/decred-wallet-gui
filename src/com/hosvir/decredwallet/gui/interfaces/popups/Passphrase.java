package com.hosvir.decredwallet.gui.interfaces.popups;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.*;
import com.hosvir.decredwallet.gui.Component;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class Passphrase extends Interface {
    public void init() {
        selectedId = -1;
        InputBox passphrase = new InputBox("passphrase", (Engine.getWidth() / 2) - 250, Engine.getHeight() / 2, 500, 30);
        passphrase.textHidden = true;

        this.components.add(passphrase);
        this.components.add(new Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 30, (Engine.getHeight() / 2) + 50, 100, 35, ColorConstants.flatRed, ColorConstants.flatRedHover));
        this.components.add(new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 130, (Engine.getHeight() / 2) + 50, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
    }

    public void update(long delta) {
        //Update
        super.update(delta);

        //For each component
        for (Component c : components) {
            if (c.containsMouse && c.enabled) Main.containsMouse = true;

            if (c instanceof Button) {
                if (c.selectedId == 0) {
                    switch (c.name) {
                        case "cancel":
                            Constants.blockInterfaces(false, this);
                            getComponentByName("passphrase").text = "";
                            getComponentByName("passphrase").selectedId = -1;
                            this.selectedId = -1;
                            break;
                        case "confirm":
                            Constants.setPrivatePassPhrase(getComponentByName("passphrase").text);
                            Constants.blockInterfaces(false, this);
                            getComponentByName("passphrase").text = "";
                            getComponentByName("passphrase").selectedId = -1;
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

        g.drawString(Constants.getLangValue("Enter-Passphrase-Message"), (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Enter-Passphrase-Message")) / 2), Engine.getHeight() / 2 - 30);


        //Render
        super.render(g);
    }

    @Override
    public boolean isActive() {
        return selectedId == 0;
    }

    @Override
    public void resize() {
        getComponentByName("passphrase").x = (Engine.getWidth() / 2) - 250;
        getComponentByName("passphrase").y = Engine.getHeight() / 2;
        getComponentByName("passphrase").resize();

        getComponentByName("cancel").y = (Engine.getHeight() / 2) + 50;
        getComponentByName("confirm").y = (Engine.getHeight() / 2) + 50;
        getComponentByName("confirm").x = (Engine.getWidth() - 130);

        getComponentByName("cancel").resize();
        getComponentByName("confirm").resize();
    }
}
