package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.FontConstants;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.Main;

/**
 * 
 * @author Troy
 *
 */
public class Logs extends Interface implements MouseWheelListener {	
	private int daemonStartLine = 0;
	private int walletStartLine = 0;
	private int guiStartLine = 0;
	private int daemonEndLine = 27;
	private int walletEndLine = 27;
	private int guiEndLine = 27;
	private int maxLines = 27;
	
	@Override
	public void init() {
		rectangles = new Rectangle[3];
		for(int i = 0; i < rectangles.length; i++){
			rectangles[i] = new Rectangle(i*170,60,170,70);
		}
		
		maxLines = (Engine.getHeight() - 220) / 18;
		
		Main.canvas.addMouseWheelListener(this);
	}
	
	@Override
	public void render(Graphics2D g) {
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
		g.drawString(Constants.getLangValue("Daemon-Button-Text"), 170 - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Daemon-Button-Text")) / 2), 105);
		g.drawString(Constants.getLangValue("Wallet-Button-Text"), (170 * 2) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("Wallet-Button-Text")) / 2), 105);
		g.drawString(Constants.getLangValue("GUI-Button-Text"), (170 * 3) - 85 - (g.getFontMetrics().stringWidth(Constants.getLangValue("GUI-Button-Text")) / 2), 105);
		
		
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
		
		
		//Draw log data
		g.setFont(FontConstants.labelFont);
		g.setColor(ColorConstants.labelColor);
		
		if(daemonStartLine > -1){
			switch(selectedId){
			case 0:
				if(Constants.getDcrdEndpoint() != null)
				for(int i = daemonStartLine; i < daemonEndLine; i++){
					if(i < Constants.getDcrdEndpoint().log.size())
						g.drawString(Constants.getDcrdEndpoint().log.get(i), 40, 175 + (i - daemonStartLine)*18);
				}
				break;
			case 1:
				if(Constants.getDcrwalletEndpoint() != null)
				for(int i = walletStartLine; i < walletEndLine; i++){
					if(i < Constants.getDcrwalletEndpoint().log.size())
						g.drawString(Constants.getDcrwalletEndpoint().log.get(i), 40, 175 + (i - walletStartLine)*18);
				}
				break;
			case 2:
				for(int i = guiStartLine; i < guiEndLine; i++){
					if(i < Constants.guiLog.size())
						g.drawString(Constants.guiLog.get(i), 40, 175 + (i - guiStartLine)*18);
				}
				break;
			}
		}
	}
	
	@Override
	public void resize() {
		if(Constants.getDcrdEndpoint() != null && Constants.getDcrwalletEndpoint() != null){
			maxLines = (Engine.getHeight() - 220) / 18;
			
			daemonStartLine = Constants.getDcrdEndpoint().log.size() - maxLines;
			walletStartLine = Constants.getDcrwalletEndpoint().log.size() - maxLines;
			guiStartLine = Constants.guiLog.size() - maxLines;
			daemonEndLine = daemonStartLine + maxLines;
			walletEndLine = walletStartLine + maxLines;
			guiEndLine = guiStartLine + maxLines;
			
			if(daemonStartLine < 0) daemonStartLine = 0;
			if(walletStartLine < 0) walletStartLine = 0;
			if(guiStartLine < 0) guiStartLine = 0;
			if(daemonEndLine > Constants.getDcrdEndpoint().log.size() | daemonEndLine < maxLines) daemonEndLine = Constants.getDcrdEndpoint().log.size();
			if(walletEndLine > Constants.getDcrwalletEndpoint().log.size() | walletEndLine < maxLines) walletEndLine = Constants.getDcrwalletEndpoint().log.size();
			if(guiEndLine > Constants.guiLog.size() | guiEndLine < maxLines) guiEndLine = Constants.guiLog.size();
		}
	}

	@Override
	public boolean isActive() {
		return Constants.navbar.selectedId == 1;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(isActive()){
			switch(selectedId){
			case 0:
				if(e.getUnitsToScroll() > 0){
					daemonStartLine += e.getScrollAmount();
				}else{
					daemonStartLine -= e.getScrollAmount();
				}
				
				if(daemonStartLine > Constants.getDcrdEndpoint().log.size()) daemonStartLine = Constants.getDcrdEndpoint().log.size()-1;
				if(daemonStartLine < 0) daemonStartLine = 0;
				daemonEndLine = daemonStartLine + maxLines;
				if(daemonEndLine > Constants.getDcrdEndpoint().log.size()) daemonEndLine = Constants.getDcrdEndpoint().log.size();
				break;
			case 1:
				if(e.getUnitsToScroll() > 0){
					walletStartLine += e.getScrollAmount();
				}else{
					walletStartLine -= e.getScrollAmount();
				}
				
				if(walletStartLine > Constants.getDcrwalletEndpoint().log.size()) walletStartLine = Constants.getDcrwalletEndpoint().log.size()-1;
				if(walletStartLine < 0) walletStartLine = 0;
				walletEndLine = walletStartLine + maxLines;
				if(walletEndLine > Constants.getDcrwalletEndpoint().log.size()) walletEndLine = Constants.getDcrwalletEndpoint().log.size();
				break;
			case 2:
				if(e.getUnitsToScroll() > 0){
					guiStartLine += e.getScrollAmount();
				}else{
					guiStartLine -= e.getScrollAmount();
				}
				
				if(guiStartLine > Constants.guiLog.size()) guiStartLine = Constants.guiLog.size()-1;
				if(guiStartLine < 0) guiStartLine = 0;
				guiEndLine = guiStartLine + maxLines;
				if(guiEndLine > Constants.guiLog.size()) guiEndLine = Constants.guiLog.size();
				break;

			}
		}
	}

}
