package com.hosvir.decredwallet.gui;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.deadendgine.input.Mouse;
import com.deadendgine.utils.Timer;
import com.hosvir.decredwallet.Constants;

/**
 * 
 * @author Troy
 *
 */
public abstract class BaseGui implements GuiInterface {
	public Rectangle[] rectangles;
	public int hoverId = -1;
	public int hoverItem = -1;
	public int selectedId = 0;
	public int selectedItem = 0;
	public int previousSelectedItem = 0;
	public int clickCount;
	public boolean doubleClicked;
	public boolean containsMouse;
	public boolean blockInput;
	private Timer clickTimer = new Timer(Constants.doubleClickDelay);
	
	@Override
	public void init() {}
	
	@Override
	public void update(long delta) {
		if(!blockInput){
			containsMouse = false;
			
			if(clickTimer.isUp()){
				clickCount = 0;
				clickTimer.reset();
				doubleClicked = false;
			}
			
			if(rectangles != null)
			for(int i = 0; i < rectangles.length; i++){
				if(rectangles[i] != null && rectangles[i].contains(Mouse.point)){
					containsMouse = true;
					hoverId = i;
	
					if(Mouse.isMouseDown(MouseEvent.BUTTON1)){
						selectedId = i;
						clickCount++;
						Mouse.release(MouseEvent.BUTTON1);
						
						if(clickCount > 1) doubleClicked = true;
					}
				}
			}
			
			if(!containsMouse) hoverId = -1;
		}
	}
	
}
