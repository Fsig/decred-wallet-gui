package com.hosvir.decredwallet.gui.interfaces;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.ApiPool;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.*;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.Label;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class StakingPool extends Interface {
    private boolean ready;
    private String newAddress;
    private String pubkeyaddr;

    @Override
    public void init() {
        this.components.add(new Label("join", Constants.getLangValue("Stakepool-Label"), 40, 190));
        this.components.add(new Label("apikeylabel", Constants.getLangValue("Api-Key-Label"), 40, 240));

        this.components.add(new DropdownBox("poolInput", 250, 170, Engine.getWidth() - 295, 30, Constants.stakePools.toArray(new String[Constants.stakePools.size()])));
        this.components.add(new InputBox("apiKeyInput", 250, 220, Engine.getWidth() - 295, 30));

        this.components.add(new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 140, 270, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));

        this.components.add(new Dialog("errordiag", ""));

        getComponentByName("poolInput").text = Constants.stakePools.get(0);
        getComponentByName("errordiag").width = 800;
    }

    @Override
    public void update(long delta) {
        super.update(delta);

        //Ready to get the public key
        if (ready && Constants.isPrivatePassphraseSet()) {
            //Unlock wallet
            String unlock = Api.unlockWallet("30");

            if (unlock == null || unlock.contains("-14")) {
                Constants.log("Error: " + unlock);
                getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + unlock;

                //Show dialog
                this.blockInput = true;
                Constants.navbar.blockInput = true;
                getComponentByName("errordiag").selectedId = 0;
                ready = false;
                return;
            }

            //Create new account if it doesn't already exist
            if (!Constants.accountNames.contains("pos-" + getComponentByName("poolInput").text.toLowerCase())) {
                Api.createNewAccount("pos-" + getComponentByName("poolInput").text.toLowerCase());
                Constants.reloadAccounts();
            }

            //Attempt to join pool
            String result = ApiPool.joinPool(getComponentByName("poolInput").text, getComponentByName("apiKeyInput").text);
            getComponentByName("errordiag").text = result;

            //Clear API key
            if (result.contains("Successfully configured pool")) {
                getComponentByName("apiKeyInput").text = "";
            }

            //Show dialog
            this.blockInput = true;
            Constants.navbar.blockInput = true;
            getComponentByName("errordiag").selectedId = 0;

            //Reset
            ready = false;
        }

        //For each component
        for (Component c : components) {
            if (c.containsMouse && c.enabled) Main.containsMouse = true;

            //Drop down
            if (c instanceof DropdownBox) {

            }

            //Buttons
            if (c instanceof Button) {
                if (c.selectedId == 0 && c.enabled) {
                    switch (c.name) {
                        case "confirm":
                            ready = true;
                            blockInput = true;
                            Constants.navbar.blockInput = true;
                            Constants.unselectAllInputs(components);
                            Constants.guiInterfaces.get(Constants.guiInterfaces.size() -1).selectedId = 0;
                            break;
                    }
                }

                //Release button
                c.selectedId = -1;
            }

            //Input boxes
            if (c instanceof InputBox) {
                if (c.clickCount > 0) {
                    Constants.unselectOtherInputs(components, c);
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        //Content box
        g.setColor(Color.WHITE);
        g.fillRect(30,
                150,
                Engine.getWidth() - 60,
                170);

        g.drawImage(Images.getInterfaces()[7],
                Engine.getWidth() - 30,
                150,
                10,
                170,
                null);

        g.drawImage(Images.getInterfaces()[6],
                20,
                150,
                10,
                170,
                null);

        g.drawImage(Images.getInterfaces()[19],
                24,
                310,
                Engine.getWidth() - 48,
                60,
                null);


        //Render
        super.render(g);

        //Dropdown
        getComponentByName("poolInput").render(g);
    }

    @Override
    public void resize() {
        getComponentByName("poolInput").width = Engine.getWidth() - 295;
        getComponentByName("apiKeyInput").width = Engine.getWidth() - 295;
        getComponentByName("confirm").x = Engine.getWidth() - 140;

        getComponentByName("poolInput").resize();
        getComponentByName("apiKeyInput").resize();
        getComponentByName("confirm").resize();
    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 3 && Constants.guiInterfaces.get(3).selectedId == 2;
    }

}
