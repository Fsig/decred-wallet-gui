package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.Engine;
import com.deadendgine.utils.MathUtils;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.DropdownBox;
import com.hosvir.decredwallet.gui.FontConstants;
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
public class Settings extends Interface {
	
	@Override
	public void init() {		
		rectangles = new Rectangle[3];
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i] = new Rectangle(i*170,60,170,70);
		}
		
		this.components.add(new Label("langLabel", Constants.getLangValue("Language-Label"), 40, 190));
		this.components.add(new Label("doubleClickDelayLabel", Constants.getLangValue("Double-Click-Delay-Label"), 40, 230));
		this.components.add(new Label("scrollDistanceLabel", Constants.getLangValue("Scroll-Distance-Label"), 40, 270));
		this.components.add(new Label("maxLogLinesLabel", Constants.getLangValue("Max-Log-Lines-Label"), 40, 310));
		this.components.add(new Label("fpsMaxLabel", Constants.getLangValue("FPS-Max-Label"), 40, 350));
		this.components.add(new Label("fpsMinLabel", Constants.getLangValue("FPS-Min-Label"), 40, 390));
		
		DropdownBox dropbox = new DropdownBox("langSelect", 250, 170, Engine.getWidth() - 295, 30, Constants.getLangFiles().toArray(new String[Constants.getLangFiles().size()]));
		dropbox.text = Constants.langFile;
		
		InputBox doubleClickBox = new InputBox("doubleClickDelayInput", 250,210,Engine.getWidth() - 295,30);
		InputBox scrollDistanceInput = new InputBox("scrollDistanceInput", 250,250,Engine.getWidth() - 295,30);
		InputBox maxLogInput = new InputBox("maxLogLinesInput", 250,290,Engine.getWidth() - 295,30);
		InputBox fpsMaxInput = new InputBox("fpsMaxInput", 250,330,Engine.getWidth() - 295,30);
		InputBox fpsMinInput = new InputBox("fpsMinInput", 250,370,Engine.getWidth() - 295,30);
		
		doubleClickBox.text = String.valueOf(Constants.doubleClickDelay);
		scrollDistanceInput.text = String.valueOf(Constants.scrollDistance);
		maxLogInput.text = String.valueOf(Constants.maxLogLines);
		fpsMaxInput.text = String.valueOf(Constants.fpsMax);
		fpsMinInput.text = String.valueOf(Constants.fpsMin);
		
		this.components.add(dropbox);
		this.components.add(doubleClickBox);
		this.components.add(scrollDistanceInput);
		this.components.add(maxLogInput);
		this.components.add(fpsMaxInput);
		this.components.add(fpsMinInput);
		
		this.components.add(new Button("save", "Save", Engine.getWidth() - 150, Engine.getHeight() - 105, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
		this.components.add(new Dialog("errordiag", ""));
	}
	
	@Override
	public synchronized void update(long delta) {
		//Allow diag closing
		if(getComponentByName("errordiag").isActive()) getComponentByName("errordiag").update(delta);
		
		//Update rectangles
		super.update(delta);
		
		switch(selectedId){
		case 0: //Main
			if(!blockInput) {	
				if(blockComponents) blockComponents = false;
				
				//For each component
				for(Component c : components) {
					if(selectedId == 0 && c.containsMouse) Main.containsMouse = true;
									
					//Drop down
					if(c instanceof DropdownBox) {
						if(!c.text.replace(".conf", "").contains(Constants.langFile)) {
							Constants.langFile = c.text.replace(".conf", "");
							Constants.reloadLanguage();
						}
					}
					
					//Input
					if(c instanceof InputBox) {
						if(c.clickCount > 0) Constants.unselectOtherInputs(components, c);
					}
					
					//Button
					if(c instanceof Button) {
						if(c.selectedId == 0){
							if(!MathUtils.isNumeric(getComponentByName("doubleClickDelayInput").text) | 
									!MathUtils.isNumeric(getComponentByName("scrollDistanceInput").text) |
									!MathUtils.isNumeric(getComponentByName("maxLogLinesInput").text) |
									!MathUtils.isNumeric(getComponentByName("fpsMaxInput").text) |
									!MathUtils.isNumeric(getComponentByName("fpsMinInput").text)){
								
								getComponentByName("errordiag").text = Constants.getLangValue("Integer-Error");
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
								
							}else{
								Constants.doubleClickDelay = Integer.valueOf(getComponentByName("doubleClickDelayInput").text);
								Constants.scrollDistance = Integer.valueOf(getComponentByName("scrollDistanceInput").text);
								Constants.maxLogLines = Integer.valueOf(getComponentByName("maxLogLinesInput").text);
								Constants.fpsMax = Integer.valueOf(getComponentByName("fpsMaxInput").text);
								Constants.fpsMin = Integer.valueOf(getComponentByName("fpsMinInput").text);
								
								Constants.saveSettings();
								
								getComponentByName("errordiag").text = Constants.getLangValue("Settings-Saved-Message");
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
							}
						}
						
						c.selectedId = -1;
					}
				}
			}
			break;
		default:
			if(!blockComponents) blockComponents = true;
			Constants.unselectAllInputs(components);
			break;
		}
	}

	@Override
	public synchronized void render(Graphics2D g) {
		switch(selectedId){
		case 0: //Main
			//Content box
			g.drawImage(Images.getInterfaces()[6], 
					20, 
					150, 
					10,
					Engine.getHeight() - 200,
					null);
			
			g.setColor(Color.WHITE);
			g.fillRect(30, 
					150, 
					Engine.getWidth() - 60, 
					Engine.getHeight() - 200);
			
			g.drawImage(Images.getInterfaces()[7], 
					Engine.getWidth() - 30, 
					150, 
					10,
					Engine.getHeight() - 200,
					null);
			
			g.drawImage(Images.getInterfaces()[19], 
					24, 
					Engine.getHeight() - 60, 
					Engine.getWidth() - 48,
					60,
					null);
			
			break;
		default:		
			break;
		}
		
		//Second nav
		g.setColor(Color.WHITE);
		g.fillRect(0, 
				60, 
				Engine.getWidth(), 
				60);
		
		g.drawImage(Images.getInterfaces()[19], 
				0, 
				120, 
				Engine.getWidth(),
				60,
				null);
		
		for(int i = 0; i < rectangles.length; i++){			
			g.setColor(ColorConstants.settingsSelectedColor);
			
			if(selectedId == i || hoverId == i){
				g.fillRect(rectangles[i].x, 
						rectangles[i].y, 
						rectangles[i].width, 
						rectangles[i].height);
			}

			g.fillRect(rectangles[i].x + 170, 
					rectangles[i].y, 
					2, 
					rectangles[i].height);
		}
		
		g.setFont(FontConstants.settingsFont);
		g.setColor(ColorConstants.walletBalanceColor);
		g.drawString(Constants.getLangValue("Main-Button-Text"), 170 - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Main-Button-Text")) / 2), 105);
		g.drawString(Constants.getLangValue("Security-Button-Text"), (170 * 2) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Security-Button-Text")) / 2), 105);
		g.drawString(Constants.getLangValue("Network-Button-Text"), (170 * 3) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Network-Button-Text")) / 2), 105);
		
		
		if(selectedId == 0){
			//Render
			super.render(g);
			
			//Render lang dropdown last
			getComponentByName("langSelect").render(g);
		}
	}
	
	@Override
	public void resize() {
		getComponentByName("langSelect").width = Engine.getWidth() - 295;
		getComponentByName("doubleClickDelayInput").width = Engine.getWidth() - 295;
		getComponentByName("scrollDistanceInput").width = Engine.getWidth() - 295;
		getComponentByName("maxLogLinesInput").width = Engine.getWidth() - 295;
		getComponentByName("fpsMaxInput").width = Engine.getWidth() - 295;
		getComponentByName("fpsMinInput").width = Engine.getWidth() - 295;
		getComponentByName("save").x = Engine.getWidth() - 150;
		getComponentByName("save").y = Engine.getHeight() - 105;

		getComponentByName("langSelect").resize();
		getComponentByName("doubleClickDelayInput").resize();
		getComponentByName("scrollDistanceInput").resize();
		getComponentByName("maxLogLinesInput").resize();
		getComponentByName("fpsMaxInput").resize();
		getComponentByName("fpsMinInput").resize();
		getComponentByName("save").resize();
		
		super.resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 0;
	}

}
