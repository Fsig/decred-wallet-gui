package com.hosvir.decredwallet.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * @author Troy
 *
 */
public class LinkLabel extends Component {
	private String linkAddress;

	/**
	 * Construct a new link label.
	 * 
	 * @param name
	 * @param text
	 * @param x
	 * @param y
	 */
	public LinkLabel(String name, String text, String linkAddress, int x, int y) {
		super(name, text, 1, x, y, 0, 0);
		this.linkAddress = linkAddress;
		
		this.rectangles = new Rectangle[1];
		
		this.selectedId = 0;
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(textColor);
		g.setFont(textFont);
		g.drawString(text, x, y);
		
		if(rectangles[0] == null){
			rectangles[0] = new Rectangle(x,y - g.getFontMetrics().getHeight() + 2,g.getFontMetrics().stringWidth(text),g.getFontMetrics().getHeight());
		}
		
		g.drawLine(rectangles[0].x, rectangles[0].y + g.getFontMetrics().getHeight(), rectangles[0].x + rectangles[0].width -2, rectangles[0].y + g.getFontMetrics().getHeight());
	}

	@Override
	public boolean isActive() {
		return this.selectedId == 0;
	}
	
	@Override
	public void resize() {
		rectangles[0] = null;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

}
