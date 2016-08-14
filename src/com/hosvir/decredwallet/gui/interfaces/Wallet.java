package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.deadendgine.Engine;
import com.deadendgine.input.Mouse;
import com.hosvir.decredwallet.Account;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.FontConstants;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;
import com.hosvir.decredwallet.gui.ReloadButton;

/**
 * 
 * @author Troy
 *
 */
public class Wallet extends Interface implements MouseWheelListener {
	private int headerThird;
	private int scrollOffset = 0;
	private String[] dateString;
	private Rectangle[] txRectangles;
	private int txHoverId = -1;
	private int scrollMinHeight = 160;
	private int scrollMaxHeight;
	private int scrollCurrentPosition = 160;
	private boolean reloadStarted = false;
	private boolean reloadComplete = true;
	
	
	@Override
	public void init() {
		headerThird = (Engine.getWidth() - 200) / 4;
		
		this.components.add(new Button("add", Constants.getLangValue("Add-Button-Text"), 20, Engine.getHeight() - 110, 255, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
		this.components.add(new ReloadButton("reload", Engine.getWidth() - 48, 95, 32, 32));
		
		Dialog errorDiag = new Dialog("errordiag", "");
		errorDiag.width = 800;
		errorDiag.resize();
		this.components.add(errorDiag);
		
		scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2) - 35;
		
		Main.canvas.addMouseWheelListener(this);
	}

	@Override
	public void update(long delta) {		
		//Allow diag closing
		if(getComponentByName("errordiag").isActive()) getComponentByName("errordiag").update(delta);
		
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
			
			//Update
			super.update(delta);
			
			//For each component
			for(Component c : components) {
				if(c.containsMouse && c.enabled) Main.containsMouse = true;
				
				if(c instanceof Button) {
					if(c.selectedId == 0 && c.enabled){
						switch(c.name){
						case "add":
							blockInput = true;
							Constants.navbar.blockInput = true;
							Constants.guiInterfaces.get(Constants.guiInterfaces.size() -3).selectedId = 0;
							break;
						}
						
						//Release button
						c.selectedId = -1;
					}
				}
				
				//Manual reload
				if(c instanceof ReloadButton) {
					if(c.selectedId == 0 && c.enabled && !reloadStarted){
						Constants.reloadAccounts();
						reloadStarted = true;
					}
					
					if(reloadStarted) {
						reloadComplete = true;
						
						for(Account a : Constants.accounts) {
							if(!a.reloadComplete) reloadComplete = false;
						}
						
						//Release button
						if(reloadComplete) {
							c.selectedId = -1;
							reloadStarted = false;
						}
					}
				}
			}
			
			//Rename account
			if(doubleClicked && !Constants.accounts.get(selectedId).name.startsWith("default") && !Constants.accounts.get(selectedId).name.startsWith("imported")){
				Constants.accountToRename = Constants.accounts.get(selectedId).name;
				blockInput = true;
				Constants.navbar.blockInput = true;
				Constants.guiInterfaces.get(Constants.guiInterfaces.size() - 2).selectedId = 0;
			}
			
			
			
			//Receive rectangles
			if(txRectangles == null && Constants.accounts.get(selectedId).transactions.size() > 0){
				txRectangles = new Rectangle[Constants.accounts.get(selectedId).transactions.size()];
				
				for(int i = 0; i < txRectangles.length; i++){
					txRectangles[i] = new Rectangle((Engine.getWidth() / 2) - 150, 210 + i*70 - scrollOffset, 530,20);
				}
			}
			
			if(txRectangles != null){
				for(int i = 0; i < txRectangles.length; i++){
					if(txRectangles[i] != null && txRectangles[i].contains(Mouse.point)){
						containsMouse = true;
						txHoverId = i;
		
						if(Mouse.isMouseDown(MouseEvent.BUTTON1)){
							Constants.setClipboardString(Constants.accounts.get(selectedId).transactions.get(txHoverId).getValueByName("txid").trim());
							getComponentByName("errordiag").text = Constants.getLangValue("Clipboard-Message") + ": " + Constants.getClipboardString();
							
							//Show dialog
							this.blockInput = true;
							Constants.navbar.blockInput = true;
							getComponentByName("errordiag").selectedId = 0;
							
							Mouse.release(MouseEvent.BUTTON1);
						}
					}
				}
				
				if(!containsMouse) txHoverId = -1;
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		//Transactions
		if(Constants.accounts.size() > 0 && Constants.accounts.get(selectedId).transactions.size() > 0){
			for(int i = 0; i < Constants.accounts.get(selectedId).transactions.size(); i++){
				if(180 + i*70 - scrollOffset < Engine.getHeight() && 180 + i*70 - scrollOffset > 80){
					g.drawImage(Images.getInterfaces()[6], 
							310, 
							180 + i*70 - scrollOffset, 
							10,
							50,
							null);
					
					g.setColor(Color.WHITE);
					g.fillRect(320, 
							180 + i*70 - scrollOffset, 
							Engine.getWidth() - 345,
							45);
					
					g.drawImage(Images.getInterfaces()[7], 
							Engine.getWidth() - 25, 
							180 + i*70 - scrollOffset, 
							10,
							50,
							null);
					
					g.drawImage(Images.getInterfaces()[19], 
							315, 
							180 + i*70 - scrollOffset + 41, 
							Engine.getWidth() - 334,
							50,
							null);
					
					//Category
					switch(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("category")){
					case "send":
						g.setColor(ColorConstants.flatRed);
						break;
					case "receive":
						g.setColor(ColorConstants.flatGreen);
						break;
					case "generate":
						g.setColor(ColorConstants.flatYellow);
						break;
					
					}
					
					//TX Type
					switch(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("txtype")){
					case "ticket":
						g.setColor(ColorConstants.flatGray);
						break;
					case "vote":
						g.setColor(ColorConstants.flatPurple);
						break;
					default:
						break;
					}
					
					//Pending
					if(Integer.valueOf(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("confirmations")) < 1) {
						g.setColor(ColorConstants.flatBlue);
					}
					
					g.fillRect(314, 
							180 + i*70 - scrollOffset, 
							5,
							50);
					
					
					//Draw date
					dateString = Constants.getWalletDate(Long.parseLong(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("timereceived"))).split(":");
					g.setColor(ColorConstants.walletBalanceColor);
					g.setFont(FontConstants.walletBalanceFont);
					g.drawString(dateString[0], 330, 212 + i*70 - scrollOffset);
					
					g.setColor(ColorConstants.labelColor);
					g.setFont(FontConstants.transactionFont);
					g.drawString(dateString[1].replaceAll("!", ":"), 322, 226 + i*70 - scrollOffset);
					
					//Draw confirmations
					g.drawString(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("confirmations"), 322, 196 + i*70 - scrollOffset);
					
					//TX TYPE
					g.drawString(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("txtype"), Engine.getWidth() - 30 - g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("txtype")), 196 + i*70 - scrollOffset);
					
					//Draw address
					g.setColor(ColorConstants.walletBalanceColor);
					g.setFont(FontConstants.addressFont);
					if(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("address") != null){
						g.drawString(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("address"), 
								(Engine.getWidth() - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("address")) / 2)) / 2 + 60, 
								204 + i*70 - scrollOffset);
					}
					
					//Draw transaction
					if(txHoverId == i) g.setColor(ColorConstants.flatBlue); else g.setColor(ColorConstants.labelColor);
					g.setFont(FontConstants.transactionFont);
					g.drawString(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("txid"), 
							(Engine.getWidth() - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("txid")) / 2)) / 2 + 30, 
							226 + i*70 - scrollOffset);
					
					//Draw amount
					g.setColor(ColorConstants.walletBalanceColor);
					g.setFont(FontConstants.walletBalanceFont);
					g.drawString(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("amount").replace("-", "- "), (Engine.getWidth() - 30 - g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("amount").replace("-", "- "))), 212 + i*70 - scrollOffset);
					
					//Draw tx fee
					if(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("fee") != null){
						g.setColor(ColorConstants.labelColor);
						g.setFont(FontConstants.transactionFont);
						g.drawString(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("fee").replace("-", "- "), (Engine.getWidth() - 30 - g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).transactions.get(i).getValueByName("fee").replace("-", "- "))), 226 + i*70 - scrollOffset);
					}
				}
			}
		}
		
		//Scroll bar
		if(Constants.accounts.size() > 0 && Constants.accounts.get(selectedId).transactions.size() > 0) {
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(Engine.getWidth() - 10, 100, Engine.getWidth() - 10, Engine.getHeight());
			g.fillRect(Engine.getWidth() - 10, scrollCurrentPosition, 10, 60);
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
			g.drawString(Constants.accounts.get(selectedId).totalBalance, (Engine.getWidth() + 150) / 2, 100);
			
			
			//Available, Pending and Locked
			g.setColor(ColorConstants.labelColor);
			g.setFont(FontConstants.labelFont);
			g.drawString(Constants.getLangValue("Available-Label"), Engine.getWidth() - (headerThird * 3), 125);
			g.drawString(Constants.getLangValue("Pending-Label"), Engine.getWidth() - (headerThird * 2), 125);
			g.drawString(Constants.getLangValue("Locked-Label"), Engine.getWidth() - (headerThird * 1), 125);
			
			g.setColor(ColorConstants.walletBalanceColor);
			g.setFont(FontConstants.walletBalanceFont);
			g.drawString(Constants.accounts.get(selectedId).balance, Engine.getWidth() - (headerThird * 3) + 30 - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).balance) / 2), 145);
			g.drawString(Constants.accounts.get(selectedId).pendingBalance, Engine.getWidth() - (headerThird * 2) + 30 - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).pendingBalance) / 2), 145);
			g.drawString(Constants.accounts.get(selectedId).lockedBalance, Engine.getWidth() - (headerThird * 1) + 30 - (g.getFontMetrics().stringWidth(Constants.accounts.get(selectedId).lockedBalance) / 2), 145);
		}
		
		//Render
		super.render(g);
	}
	
	@Override
	public void resize() {
		txRectangles = null;
		headerThird = (Engine.getWidth() - 200) / 4;
		
		if(getComponentByName("add") != null) {
			getComponentByName("add").y = Engine.getHeight() - 110;
			getComponentByName("add").resize();
			
			getComponentByName("reload").x = Engine.getWidth() - 48;
			getComponentByName("reload").resize();
		}
		
		super.resize();
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 6;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isActive()){
			if(e.getUnitsToScroll() > 0){
				scrollOffset += 70;
				if(Constants.accounts.get(selectedId).transactions.size() > 0)
				scrollCurrentPosition += (Engine.getHeight() - scrollMinHeight - 60) / (Constants.accounts.get(selectedId).transactions.size() -1);
			}else{
				scrollOffset -= 70;
				if(Constants.accounts.get(selectedId).transactions.size() > 0)
				scrollCurrentPosition -= (Engine.getHeight() - scrollMinHeight - 60) / (Constants.accounts.get(selectedId).transactions.size() -1);
			}
			
			if(scrollOffset < 0) scrollOffset = 0;
			if(scrollOffset > (Constants.accounts.get(selectedId).transactions.size()-1)*70) scrollOffset = (Constants.accounts.get(selectedId).transactions.size()-1)*70;
			
			if(Constants.accounts.get(selectedId).transactions.size() > 0){
				scrollMaxHeight = Engine.getHeight() - (scrollMinHeight / 2) - 35;
				if(scrollCurrentPosition < scrollMinHeight) scrollCurrentPosition = scrollMinHeight;
				if(scrollCurrentPosition > scrollMaxHeight) scrollCurrentPosition = scrollMaxHeight;
			}
			
			txRectangles = null;
		}
	}

}
