package com.deadendgine.input;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

/**
 * This class stores which mouse buttons are in use and also
 * allows access to the current x,y cords plus provides a dragging
 * area so you can see what has been selected.
 * 
 * @author Troy
 * @version 1.00
 *
 */
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener{
	public static ArrayList<MouseEvent> clicks = new ArrayList<MouseEvent>();	
	public static int x = 0;
	public static int y = 0;
	public static int dragSX = 0;
	public static int dragSY = 0;
	public static int dragEX = 0;
	public static int dragEY = 0;
	public static Point point = new Point(0,0);
	public static boolean dragging = false;
	public static boolean inGame = false;
	public static Rectangle dragRect = new Rectangle(0,0,0,0);
	public static int scrollAmount;
	
	/**
	 * Check if a mouse button is down by it's button id.
	 * 
	 * Example: isMouseDown(MouseEvent.BUTTON1);
	 * 
	 * @param mouseButton
	 * @return boolean
	 */
	public static boolean isMouseDown(int mouseButton){
		for(MouseEvent e : clicks)
			if(e.getButton() == mouseButton)
				return true;
		
		return false;
	}
	
	/**
	 * Release a mouse button.
	 * 
	 * @param mouseButton
	 */
	public static void release(int mouseButton){
		if(isMouseDown(mouseButton))
			remove(mouseButton);
	}
	
	/**
	 * Remove a key from the key list.
	 * 
	 * @param keyCode
	 */
	private static void remove(int mouseButton){
		for(MouseEvent e : clicks)
			if(e.getButton() == mouseButton){
				clicks.remove(e);
				break;
			}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		
		point.x = x;
		point.y = y;
		
		dragging = true;
		dragEX = e.getX();
		dragEY = e.getY();
		
		dragRect.x = Math.min(dragSX, dragEX);
		dragRect.y = Math.min(dragSY, dragEY);
		dragRect.width = Math.abs(dragSX - dragEX);
		dragRect.height = Math.abs(dragSY - dragEY);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		x = e.getX();
		y = e.getY();
		
		point.x = x;
		point.y = y;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!isMouseDown(e.getButton()))
			clicks.add(e);
		
		dragSX = e.getX();
		dragSY = e.getY();
		dragEX = e.getX();
		dragEY = e.getY();
		
		dragRect.x = dragSX;
		dragRect.y = dragSY;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(isMouseDown(e.getButton()))
			remove(e.getButton());
		
		dragging = false;
		dragRect.width = 0;
		dragRect.height = 0;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		inGame = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		inGame = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getUnitsToScroll() > 0)
			scrollAmount = 2;
		else
			scrollAmount = -2;
	}
	
}
