package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.deadendgine.Engine;
import com.deadendgine.input.Mouse;
import com.hosvir.decredwallet.Api;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.FontConstants;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Receive extends Interface implements MouseWheelListener {
	private int headerThird;
	private String newAddress;
	private int scrollOffset = 0;
	private Rectangle[] receiveRectangles;
	public int receiveHoverId = -1;
	private int scrollMinHeight = 160;
	private int scrollMaxHeight;
	private double scrollCurrentPosition = 160;
	
	@Override
	public void init() {
		headerThird = (Engine.getWidth() - 200) / 4;
		
		this.components.add(new Button("new", Constants.getLangValue("Get-New-Button-Text"), Engine.getWidth() - 150, 200, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
		this.components.add(new Dialog("newaddr", Constants.getLangValue("New-Address-Message")));
		
		scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);
		
		Main.canvas.addMouseWheelListener(this);
	}

	@Override
	public void update(long delta) {
		//Update
		super.update(delta);
		
		if(!blockInput){
			if(rectangles == null && Constants.accounts.size() > 0){
				rectangles = new Rectangle[Constants.accounts.size()];
				
				for(int i = 0; i < rectangles.length; i++){
					rectangles[i] = new Rectangle(0,
							60 + i*60,
							295,
							60);
				}
			}			
			
			//Get new address
			if(getComponentByName("new").containsMouse) Main.containsMouse = true;
			
			if(getComponentByName("new").selectedId == 0 ){
				newAddress = Api.getNewAddress(Constants.accounts.get(selectedId).name).trim();
				Constants.setClipboardString(newAddress);
				
				//Release button
				getComponentByName("new").selectedId = -1;
				
				//Update
				Constants.accounts.get(selectedId).forceUpdate = true;
				
				getComponentByName("newaddr").text = Constants.getLangValue("New-Address-Message");
				
				//Show dialog
				this.blockInput = true;
				Constants.navbar.blockInput = true;
				getComponentByName("newaddr").selectedId = 0;
			}
			
			//Rename account
			if(doubleClicked && !Constants.accounts.get(selectedId).name.startsWith("default") && !Constants.accounts.get(selectedId).name.startsWith("imported")){
				Constants.accountToRename = Constants.accounts.get(selectedId).name;
				blockInput = true;
				Constants.navbar.blockInput = true;
				Constants.guiInterfaces.get(Constants.guiInterfaces.size() - 2).selectedId = 0;
			}
		}
		
		
		//Receive rectangles
		if(receiveRectangles == null && Constants.accounts.get(selectedId).addresses != null && Constants.accounts.get(selectedId).addresses.length > 0){
			receiveRectangles = new Rectangle[Constants.accounts.get(selectedId).addresses.length];
			
			for(int i = 0; i < receiveRectangles.length; i++){
				receiveRectangles[i] = new Rectangle(340, 198 + i*30 - scrollOffset, 450,20);
			}
		}
		
		if(receiveRectangles != null){
			for(int i = 0; i < receiveRectangles.length; i++){
				if(receiveRectangles[i] != null && receiveRectangles[i].contains(Mouse.point)){
					containsMouse = true;
					receiveHoverId = i;
	
					if(Mouse.isMouseDown(MouseEvent.BUTTON1)){
						Constants.setClipboardString(Constants.accounts.get(selectedId).addresses[receiveHoverId].trim());
						getComponentByName("newaddr").text = Constants.getLangValue("Clipboard-Message") + ": " + Constants.getClipboardString();
						
						//Show dialog
						this.blockInput = true;
						Constants.navbar.blockInput = true;
						getComponentByName("newaddr").selectedId = 0;
						
						Mouse.release(MouseEvent.BUTTON1);
					}
				}
			}
			
			if(!containsMouse) receiveHoverId = -1;
		}

		
	}

	@Override
	public void render(Graphics2D g) {
		//Content box
		g.drawImage(Images.getInterfaces()[6], 
				320, 
				180, 
				10,
				Engine.getHeight() - 230,
				null);
		
		g.setColor(Color.WHITE);
		g.fillRect(330, 
				180, 
				Engine.getWidth() - 360, 
				Engine.getHeight() - 230);
		
		g.drawImage(Images.getInterfaces()[7], 
				Engine.getWidth() - 30, 
				180, 
				10,
				Engine.getHeight() - 230,
				null);
		
		g.drawImage(Images.getInterfaces()[19], 
				324, 
				Engine.getHeight() - 60, 
				Engine.getWidth() - 348,
				60,
				null);
		
		
		//Draw address		
		g.setColor(ColorConstants.walletBalanceColor);
		g.setFont(FontConstants.addressFont);
		
		for(int i = 0; i < Constants.accounts.get(selectedId).addresses.length; i++) {
			if(215 + i*30 - scrollOffset < Engine.getHeight() - 40 && 215 + i*30 - scrollOffset > 200) {
				if(receiveHoverId == i) g.setColor(ColorConstants.flatBlue); else g.setColor(ColorConstants.walletBalanceColor);
				
				g.drawString(Constants.accounts.get(selectedId).addresses[i], 340, 215 + i*30 - scrollOffset);
			}
		}	
		
		//Scroll bar
		if(Constants.accounts.get(selectedId).addresses.length > 0) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(Engine.getWidth() - 10, 100, Engine.getWidth() - 10, Engine.getHeight());
			g.fillRect(Engine.getWidth() - 10, (int)scrollCurrentPosition, 10, 60);
		}
		
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
		
		
		if(rectangles != null){
			for(int i = 0; i < rectangles.length; i++){				
				//Selected wallet
				g.setColor(ColorConstants.settingsSelectedColor);
				if(i == selectedId || i == hoverId){
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
				g.drawString(Constants.accounts.get(i).name, 6, 98 + i*60);
				
				
				//Wallet Balance
				g.setColor(ColorConstants.walletBalanceColor);
				g.setFont(FontConstants.walletBalanceFont);
				g.drawString("" + Constants.accounts.get(i).totalBalance, 285 - g.getFontMetrics().stringWidth("" + Constants.accounts.get(i).totalBalance), 98 + i*60);
			}
		}
	
		
		
		if(Constants.accounts.size() > 0){
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
			g.drawString("" + Constants.accounts.get(selectedId).balance, Engine.getWidth() - (headerThird * 3) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).balance) / 2), 145);
			g.drawString("" + Constants.accounts.get(selectedId).pendingBalance, Engine.getWidth() - (headerThird * 2) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).pendingBalance) / 2), 145);
			g.drawString("" + Constants.accounts.get(selectedId).lockedBalance, Engine.getWidth() - (headerThird * 1) + 30 - (g.getFontMetrics().stringWidth("" + Constants.accounts.get(selectedId).lockedBalance) / 2), 145);
			
			
			//Render
			super.render(g);
		}
		
		
	}
	
	@Override
	public void resize() {
		receiveRectangles = null;
		headerThird = (Engine.getWidth() - 200) / 4;
		
		getComponentByName("new").x = Engine.getWidth() - 150;
		getComponentByName("new").resize();
		
		super.resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 4;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isActive()){
			if(e.getUnitsToScroll() > 0){
				scrollOffset += Constants.scrollDistance;
				if(Constants.accounts.get(selectedId).addresses.length > 1)
				scrollCurrentPosition += (Engine.getHeight() - scrollMinHeight - 60) / (Constants.accounts.get(selectedId).addresses.length -1);
			}else{
				scrollOffset -= Constants.scrollDistance;
				if(Constants.accounts.get(selectedId).addresses.length > 1)
				scrollCurrentPosition -= (Engine.getHeight() - scrollMinHeight - 60) / (Constants.accounts.get(selectedId).addresses.length -1);
			}
			
			if(scrollOffset < 0) scrollOffset = 0;
			if(scrollOffset > (Constants.accounts.get(selectedId).addresses.length-1)*30) scrollOffset = (Constants.accounts.get(selectedId).addresses.length-1)*30;
			
			if(Constants.accounts.get(selectedId).addresses.length > 0){
				scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2);
				if(scrollCurrentPosition < scrollMinHeight) scrollCurrentPosition = scrollMinHeight;
				if(scrollCurrentPosition > scrollMaxHeight) scrollCurrentPosition = scrollMaxHeight;
			}
			
			receiveRectangles = null;
		}
	}

}
