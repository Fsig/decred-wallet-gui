package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.DropdownBox;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Label;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class StakingPurchase extends Interface {
	private boolean ready;
	
	@Override
	public void init() {		
		this.components.add(new Label("from", Constants.getLangValue("From-Label"), 40, 190));
		this.components.add(new Label("limit", Constants.getLangValue("Limit-Label"), 40, 240));
		this.components.add(new Label("fee", Constants.getLangValue("Transaction-Fee-Label"), 40, 290));
		this.components.add(new Label("address", Constants.getLangValue("Address-Label"), 40, 340));
		this.components.add(new Label("tickets", Constants.getLangValue("Ticket-Count-Label"), 40, 390));
		this.components.add(new Label("pooladdr", Constants.getLangValue("Pool-Address-Label"), 40, 440));
		this.components.add(new Label("poolfee", Constants.getLangValue("Pool-Fee-Label"), 40, 490));
		
		this.components.add(new DropdownBox("fromInput", 250, 170, Engine.getWidth() - 295, 30, Constants.accountNames.toArray(new String[Constants.accountNames.size()])));
		this.components.add(new InputBox("limitInput", 250, 220, Engine.getWidth() - 295, 30));
		this.components.add(new InputBox("feeInput", 250, 270, Engine.getWidth() - 295, 30));
		this.components.add(new InputBox("addressInput", 250, 320, Engine.getWidth() - 295, 30));
		this.components.add(new InputBox("ticketInput", 250, 370, Engine.getWidth() - 295, 30));
		this.components.add(new InputBox("pooladdrInput", 250, 420, Engine.getWidth() - 295, 30));
		this.components.add(new InputBox("poolfeeInput", 250, 470, Engine.getWidth() - 295, 30));
		
		this.components.add(new Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 40, 520, 100, 35, ColorConstants.flatRed, ColorConstants.flatRedHover));
		this.components.add(new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 140, 520, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
		
		this.components.add(new Dialog("errordiag", ""));
		
		getComponentByName("limitInput").enabled = false;
		getComponentByName("errordiag").width = 800;
	}
	
	@Override
	public void update(long delta) {
		//Allow diag closing
		if(getComponentByName("errordiag").isActive()) getComponentByName("errordiag").update(delta);
				
		if(!blockInput){
			super.update(delta);
			
			//Update spend limit
			if(getComponentByName("limitInput").text == "" || (Constants.globalCache.stakeInfo.size() > 0 && 
					Double.valueOf(getComponentByName("limitInput").text) != Double.valueOf(Constants.globalCache.stakeInfo.get(0).getValueByName("difficulty")))) {
				getComponentByName("limitInput").text = Constants.globalCache.stakeInfo.get(0).getValueByName("difficulty");
			}
			
			//Check for ticket purchase
			if(Constants.isPrivatePassphraseSet() && getComponentByName("address").text != "") {
				if(Double.valueOf(getComponentByName("limitInput").text) >= Double.valueOf(Constants.globalCache.stakeInfo.get(0).getValueByName("difficulty"))){
					ready = true;
				}else{
					ready = false;
					
					Constants.log("Spend limit is less than current difficulty.");
					getComponentByName("errordiag").text = Constants.getLangValue("Spend-Limit-Message");
					
					//Show dialog
					this.blockInput = true;
					Constants.navbar.blockInput = true;
					getComponentByName("errordiag").selectedId = 0;
				}
				
				//Begin
				if(ready){				
					//Unlock wallet
					String unlock = getComponentByName("pooladdrInput").text != "" ? Api.unlockWallet("0") : Api.unlockWallet("30");
					
					if(unlock != null && !unlock.contains("-14")){
						String result = Api.purchaseTicket(getComponentByName("fromInput").text, 
								getComponentByName("limitInput").text, 
								getComponentByName("addressInput").text,
								getComponentByName("ticketInput").text,
								getComponentByName("pooladdrInput").text,
								getComponentByName("poolfeeInput").text);
							
						if(result.contains("not enough to purchase sstx")){
							Constants.log("Insufficient funds: " + result);
							getComponentByName("errordiag").text = Constants.getLangValue("Insufficient-Funds-Error");
								
							//Show dialog
							this.blockInput = true;
							Constants.navbar.blockInput = true;
							getComponentByName("errordiag").selectedId = 0;
						}else{
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
					}else {
						Constants.log("Error: " + unlock);
						getComponentByName("errordiag").text = Constants.getLangValue("Error") + " " + unlock;
						
						//Show dialog
						this.blockInput = true;
						Constants.navbar.blockInput = true;
						getComponentByName("errordiag").selectedId = 0;
					}
				}else{
					Constants.log("Error: Not ready to purchase?");
				}
				
				//Clear password
				Constants.setPrivatePassPhrase(null);
			}
	
			
			//For each component
			for(Component c : components) {
				if(c.containsMouse && c.enabled) Main.containsMouse = true;
				
				//Drop down
				if(c instanceof DropdownBox) {
					//Check if accounts are populated
					if(DropdownBox.class.cast(c).lineItems == null || DropdownBox.class.cast(c).lineItems.length == 0){
						DropdownBox.class.cast(c).setLineItems(Constants.accountNames.toArray(new String[Constants.accountNames.size()]));
						DropdownBox.class.cast(c).text = Constants.accountNames.get(0);
					}
				}
				
				//Buttons
				if(c instanceof Button) {
					if(c.selectedId == 0 && c.enabled){
						switch(c.name){
						case "cancel":
							getComponentByName("limitInput").text = "";
							getComponentByName("limitInput").selectedId = -1;
							getComponentByName("addressInput").text = "";
							getComponentByName("addressInput").selectedId = -1;
							getComponentByName("feeInput").text = "";
							getComponentByName("feeInput").selectedId = -1;
							getComponentByName("ticketInput").text = "";
							getComponentByName("ticketInput").selectedId = -1;
							getComponentByName("pooladdrInput").text = "";
							getComponentByName("pooladdrInput").selectedId = -1;
							getComponentByName("poolfeeInput").text = "";
							getComponentByName("poolfeeInput").selectedId = -1;
							
							
							getComponentByName("limitInput").text = Constants.globalCache.stakeInfo.get(0).getValueByName("difficulty");
							break;
						case "confirm":
							if(getComponentByName("addressInput").text == ""){
								getComponentByName("errordiag").text = Constants.getLangValue("Address-Null-Error");
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
							}else if(getComponentByName("ticketInput").text == ""){
								getComponentByName("errordiag").text = Constants.getLangValue("Ticket-Null-Error");
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
							}else{
								blockInput = true;
								Constants.navbar.blockInput = true;
								Constants.unselectAllInputs(components);
								Constants.guiInterfaces.get(Constants.guiInterfaces.size() -1).selectedId = 0;
							}
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
	}

	@Override
	public void render(Graphics2D g) {		
		//Content box
		g.setColor(Color.WHITE);
		g.fillRect(30, 
				150, 
				Engine.getWidth() - 60, 
				420);
		
		g.drawImage(Images.getInterfaces()[7], 
				Engine.getWidth() - 30, 
				150, 
				10,
				420,
				null);
		
		g.drawImage(Images.getInterfaces()[6], 
				20, 
				150, 
				10,
				420,
				null);
		
		g.drawImage(Images.getInterfaces()[19], 
				24, 
				560, 
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
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 3 && Constants.guiInterfaces.get(3).selectedId == 1;
	}

}
