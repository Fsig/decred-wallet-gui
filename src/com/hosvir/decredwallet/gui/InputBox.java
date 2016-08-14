package com.hosvir.decredwallet.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.deadendgine.input.Keyboard;
import com.deadendgine.utils.StringUtils;
import com.hosvir.decredwallet.Constants;

/**
 * 
 * @author Troy
 *
 */
public class InputBox extends Component implements KeyListener {
	
	/**
	 * Construct a new InputBox.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public InputBox(String name, int x, int y, int width, int height) {
		super(name, "", 3, x, y, width, height);
		this.textColor = ColorConstants.labelColor;
		this.textFont = FontConstants.settingsFont;
		
		this.rectangles = new Rectangle[1];
		this.rectangles[0] = new Rectangle(x,y,width,height);
		
		this.selectedId = -1;
		
		Main.canvas.addKeyListener(this);
	}

	@Override
	public void render(Graphics2D g) {
		if(!enabled) {
			g.setColor(borderColor);
			g.fillRect(x, y, width, height);
		}else{
			g.setColor(Color.WHITE);
			g.fillRect(x, y, width, height);
		}
		
		if(enabled && (hoverId == 0 || selectedId == 0)) g.setColor(hoverColor); else g.setColor(borderColor);
		g.drawRect(x, y, width, height);
		
		g.setColor(textColor);
		g.setFont(textFont);
		
		if(textHidden){
			String newText = "";
			
			for(int i = 0; i < text.length(); i++)
				newText += "*";
					
			g.drawString(newText, x + 10, y + (height / 2) + 8);
		}else{
			g.drawString(text, x + 10, y + (height / 2) + 8);
		}
		
		if(selectedId == -1 && placeholderText != null && placeholderText != "" && text == ""){
			g.setColor(textColor);
			g.setFont(FontConstants.labelFont);
			g.drawString(placeholderText, x + 10, y + (height / 2) + 6);
		}
	}
	
	@Override
	public void resize() {
		for(Rectangle r : rectangles){
			r.x = x;
			r.y = y;
		}
	}

	@Override
	public boolean isActive() {
		return this.selectedId == 0;
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(isActive()){
			if(Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_V)){
				text += Constants.getClipboardString();
			}else if(Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_C)){
				Constants.setClipboardString(text);
			}else{
				switch(e.getKeyCode()){
				case KeyEvent.VK_BACK_SPACE:
					text = StringUtils.backspace(text);
					break;
				case KeyEvent.VK_SHIFT:
					break;
				case KeyEvent.VK_CONTROL:
					break;
				case KeyEvent.VK_ALT:
					break;
				default:
					if(e.getKeyCode() != KeyEvent.VK_TAB && e.getKeyCode() != KeyEvent.VK_ENTER)
					text += e.getKeyChar();
					break;
				}
			}
		}
	}

}
