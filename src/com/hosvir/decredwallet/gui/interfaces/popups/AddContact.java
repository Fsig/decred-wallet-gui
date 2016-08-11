package com.hosvir.decredwallet.gui.interfaces.popups;

import java.awt.Color;
import java.awt.Graphics2D;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.Contact;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.FontConstants;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Label;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class AddContact extends Interface {
	
	public void init() {
		selectedId = -1;
		
		this.components.add(new Label("nameLabel", Constants.getLangValue("Name-Label"), (Engine.getWidth() / 2) - 340, (Engine.getHeight() / 2) - 30));
		this.components.add(new Label("emailLabel", Constants.getLangValue("Email-Label"), (Engine.getWidth() / 2) - 340, (Engine.getHeight() / 2) +10));
		this.components.add(new Label("addressLabel", Constants.getLangValue("Address-Label"), (Engine.getWidth() / 2) - 340, (Engine.getHeight() / 2) + 50));
		
		this.components.add(new InputBox("name", (Engine.getWidth() / 2) - 180,(Engine.getHeight() / 2) - 50,500,30));
		this.components.add(new InputBox("email", (Engine.getWidth() / 2) - 180,(Engine.getHeight() / 2) - 10,500,30));
		this.components.add(new InputBox("address", (Engine.getWidth() / 2) - 180,(Engine.getHeight() / 2) + 30,500,30));
		
		this.components.add(new Button("cancel", Constants.getLangValue("Cancel-Button-Text"), 30, (Engine.getHeight() / 2) + 70, 100, 35, ColorConstants.flatRed, ColorConstants.flatRedHover));
		this.components.add(new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() - 130, (Engine.getHeight() / 2) + 70, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
	}
	
	@Override
	public void update(long delta) {
		getComponentByName("addressLabel").x = (Engine.getWidth() / 2) - 340;
		
		//Update
		super.update(delta);
		
		//For each component
		for(Component c : components) {
			if(c.containsMouse && c.enabled) Main.containsMouse = true;
					
			//Buttons
			if(c instanceof Button) {
				if(c.selectedId == 0){
					switch(c.name){
					case "cancel":
						Constants.blockInterfaces(false, this);
						resetForm();
						this.selectedId = -1;
						break;
					case "confirm":
						Constants.addContact(new Contact(Constants.contacts.size() + 1, 
								getComponentByName("name").text, 
								getComponentByName("email").text, 
								getComponentByName("address").text));
						Constants.blockInterfaces(false, this);
						resetForm();
						Constants.guiInterfaces.get(1).rectangles = null;
						this.selectedId = -1;
						break;
					}
				}
					
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
		g.setColor(ColorConstants.transparentBlack);
		g.fillRect(0, 0, Engine.getWidth(), Engine.getHeight());
		
		g.setColor(Color.WHITE);
		g.fillRect(0, (Engine.getHeight() / 2) - 120, Engine.getWidth(), 240);
		
		//Label
		g.setFont(FontConstants.labelFont);
		g.setColor(ColorConstants.labelColor);
		
		g.drawString(Constants.getLangValue("Add-Contact-Message"), (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(Constants.getLangValue("Add-Contact-Message")) / 2), (Engine.getHeight() / 2) - 70);
		
		
		//Render
		super.render(g);
	}
	
	@Override
	public boolean isActive() {
		return selectedId == 0;
	}
	
	@Override
	public void resize() {
		getComponentByName("nameLabel").x = (Engine.getWidth() / 2) - 340;
		getComponentByName("nameLabel").y = (Engine.getHeight() / 2) - 30;
		getComponentByName("emailLabel").x = (Engine.getWidth() / 2) - 340;
		getComponentByName("emailLabel").y = (Engine.getHeight() / 2) + 10;
		getComponentByName("addressLabel").x = (Engine.getWidth() / 2) - 340;
		getComponentByName("addressLabel").y = (Engine.getHeight() / 2) + 50;
		
		getComponentByName("name").x = (Engine.getWidth() / 2) - 180;
		getComponentByName("name").y = (Engine.getHeight() / 2) - 50;
		getComponentByName("email").x = (Engine.getWidth() / 2) - 180;
		getComponentByName("email").y = (Engine.getHeight() / 2) - 10;
		getComponentByName("address").x = (Engine.getWidth() / 2) - 180;
		getComponentByName("address").y = (Engine.getHeight() / 2) + 30;
		
		getComponentByName("cancel").y = (Engine.getHeight() / 2) + 70;
		getComponentByName("confirm").y = (Engine.getHeight() / 2) + 70;
		getComponentByName("confirm").x = Engine.getWidth() - 130;
		
		getComponentByName("nameLabel").resize();
		getComponentByName("emailLabel").resize();
		getComponentByName("addressLabel").resize();
		getComponentByName("name").resize();
		getComponentByName("email").resize();
		getComponentByName("address").resize();
		getComponentByName("cancel").resize();
		getComponentByName("confirm").resize();
	}
	
	/**
	 * Reset all the fields on the form.
	 */
	public void resetForm() {
		for(Component c : components)
			if(c instanceof InputBox){
				c.text = "";
				c.selectedId = -1;
			}
	}

}
