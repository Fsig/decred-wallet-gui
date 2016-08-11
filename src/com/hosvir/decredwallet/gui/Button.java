package com.hosvir.decredwallet.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * @author Troy
 *
 */
public class Button extends Component {
	
	/**
	 * Construct a new button.
	 * 
	 * @param name
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param backgroundColor
	 * @param hoverColor
	 */
	public Button(String name, String text, int x, int y, int width, int height, Color backgroundColor, Color hoverColor) {
		super(name, text, 2, x, y, width, height);
		this.textColor = Color.WHITE;
		this.textFont = FontConstants.settingsFont;
		this.borderColor = backgroundColor;
		this.hoverColor = hoverColor;
		
		this.rectangles = new Rectangle[1];
		this.rectangles[0] = new Rectangle(x,y,width,height);
		
		this.selectedId = -1;
	}

	@Override
	public void render(Graphics2D g) {
		if(hoverId != -1) g.setColor(hoverColor); else g.setColor(borderColor);
		if(!enabled) g.setColor(disabledColor);
		
		g.fillRoundRect(x, y, width, height, 10, 10);
		
		g.setColor(textColor);
		g.setFont(textFont);
		g.drawString(text, x + (width / 2) - (g.getFontMetrics().stringWidth(text) / 2), y + (height / 2) + 8);
	}
	
	@Override
	public void resize() {
		for(Rectangle r : rectangles){
			r.x = x;
			r.y = y;
			r.width = width;
			r.height = height;
		}
	}

	@Override
	public boolean isActive() {
		return this.selectedId == 0;
	}

}
