package com.hosvir.decredwallet.gui;

import java.awt.Graphics2D;

/**
 * 
 * @author Troy
 *
 */
public class Label extends Component {

	/**
	 * Construct a new label.
	 * 
	 * @param name
	 * @param text
	 * @param x
	 * @param y
	 */
	public Label(String name, String text, int x, int y) {
		super(name, text, 1, x, y, 0, 0);
		
		this.selectedId = 0;
	}

	@Override
	public void render(Graphics2D g) {
		g.setColor(textColor);
		g.setFont(textFont);
		g.drawString(text, x, y);
	}

	@Override
	public boolean isActive() {
		return this.selectedId == 0;
	}

}
