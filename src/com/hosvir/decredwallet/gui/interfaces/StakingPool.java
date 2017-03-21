package com.hosvir.decredwallet.gui.interfaces;

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
public class StakingPool extends Interface {
    private boolean ready;
    private boolean importReady;
    private String newAddress;
    private String pubkeyaddr;

    @Override
    public void init() {
        this.components.add(new com.hosvir.decredwallet.gui.Label("join", Constants.getLangValue("Stakepool-Label"), 40, 190));
        this.components.add(new com.hosvir.decredwallet.gui.Label("importlabel", Constants.getLangValue("Import-Script-Label"), 40, 240));

        this.components.add(new DropdownBox("poolInput", 250, 170, Engine.getWidth() - 520, 30, Constants.stakePools.toArray(new String[Constants.stakePools.size()])));
        this.components.add(new InputBox("importInput", 250, 220, Engine.getWidth() - 520, 30));

        this.components.add(new com.hosvir.decredwallet.gui.Button("getpublickey", Constants.getLangValue("Get-Public-Key-Button-Text"), Engine.getWidth() - 240, 168, 200, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
        this.components.add(new com.hosvir.decredwallet.gui.Button("import", Constants.getLangValue("Import-Button-Text"), Engine.getWidth() - 140, 218, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));

        this.components.add(new com.hosvir.decredwallet.gui.Dialog("errordiag", ""));

        getComponentByName("poolInput").text = Constants.stakePools.get(0);
        getComponentByName("errordiag").width = 800;
    }

    @Override
    public void update(long delta) {
        super.update(delta);

        //Ready to get the public key
        if(ready && Constants.accountNames.contains(getComponentByName("poolInput").text)) {
            newAddress = Api.getNewAddress(Constants.getAccountByName(getComponentByName("poolInput").text).name).trim();
            pubkeyaddr = Api.validateAddress(newAddress);
            Constants.setClipboardString(pubkeyaddr);

            Constants.log("New address: " + newAddress);
            Constants.log("Public key address: " + pubkeyaddr);
            getComponentByName("errordiag").text = Constants.getLangValue("Clipboard-Message") + ": " + pubkeyaddr;

            //Show dialog
            this.blockInput = true;
            Constants.navbar.blockInput = true;
            getComponentByName("errordiag").selectedId = 0;

            //Reset
            ready = false;
        }

        //Import script
        if(importReady && Constants.isPrivatePassphraseSet()) {
            //Unlock wallet
            Api.unlockWallet("30");
            String result = Api.importScript(getComponentByName("importInput").text);
            Constants.log("Import script result: " + result);

            getComponentByName("errordiag").text = Constants.getLangValue("Import-Script-Message");

            //Show dialog
            this.blockInput = true;
            Constants.navbar.blockInput = true;
            getComponentByName("errordiag").selectedId = 0;

            getComponentByName("importInput").text = "";
            importReady = false;
        }

        //For each component
        for(com.hosvir.decredwallet.gui.Component c : components) {
            if(c.containsMouse && c.enabled) Main.containsMouse = true;

            //Drop down
            if(c instanceof DropdownBox) {

            }

            //Buttons
            if(c instanceof com.hosvir.decredwallet.gui.Button) {
                if(c.selectedId == 0 && c.enabled){
                    switch(c.name){
                        case "getpublickey":
                            //Create new account if it doesn't already exist
                            if(!Constants.accountNames.contains(getComponentByName("poolInput").text)) {
                                blockInput = true;
                                Constants.navbar.blockInput = true;
                                Interface.class.cast(Constants.guiInterfaces.get(Constants.guiInterfaces.size() -3)).getComponentByName("account").text = getComponentByName("poolInput").text;
                                Constants.guiInterfaces.get(Constants.guiInterfaces.size() -3).selectedId = 0;
                                ready = true;
                            }else{
                                ready = true;
                            }
                            break;
                        case "import":
                            blockInput = true;
                            importReady = true;
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
            if(c instanceof InputBox) {
                if(c.clickCount > 0) Constants.unselectOtherInputs(components, c);
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
                120);

        g.drawImage(Images.getInterfaces()[7],
                Engine.getWidth() - 30,
                150,
                10,
                120,
                null);

        g.drawImage(Images.getInterfaces()[6],
                20,
                150,
                10,
                120,
                null);

        g.drawImage(Images.getInterfaces()[19],
                24,
                260,
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
        getComponentByName("poolInput").width = Engine.getWidth() - 520;
        getComponentByName("importInput").width = Engine.getWidth() - 520;
        getComponentByName("import").x = Engine.getWidth() - 140;
        getComponentByName("getpublickey").x = Engine.getWidth() - 240;

        getComponentByName("poolInput").resize();
        getComponentByName("importInput").resize();
        getComponentByName("import").resize();
        getComponentByName("getpublickey").resize();
    }

    @Override
    public boolean isActive() {
        return Constants.navbar.selectedId == 3 && Constants.guiInterfaces.get(3).selectedId == 2;
    }
}
