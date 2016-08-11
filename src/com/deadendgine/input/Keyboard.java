package com.deadendgine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * This class stores the key's that are currently in use.
 * 
 * @author Troy
 * @version 1.00
 *
 */
public class Keyboard implements KeyListener{
	private static ArrayList<KeyEvent> keys = new ArrayList<KeyEvent>();
	
	/**
	 * Check if a key is down by it's key code.
	 * 
	 * Example: isKeyDown(KeyEvent.VK_UP);
	 * 
	 * @param keyCode
	 * @return boolean
	 */
	public static synchronized boolean isKeyDown(int keyCode){
		for(KeyEvent e : keys)
			if(e.getKeyCode() == keyCode)
				return true;
		
		return false;
	}
	
	/**
	 * Release a key.
	 * 
	 * This is best used when you want to check if a key is being pressed
	 * once only.
	 * 
	 * An example of this would be checking for the a key when going
	 * full screen. You do not want to get the key more then once and
	 * go full screen then back to normal.
	 * 
	 * @param keyCode
	 */
	public static void release(int keyCode){
		if(isKeyDown(keyCode))
			remove(keyCode);
	}
	
	/**
	 * Remove a key from the key list.
	 * 
	 * @param keyCode
	 */
	private static void remove(int keyCode){
		for(KeyEvent e : keys)
			if(e.getKeyCode() == keyCode){
				keys.remove(e);
				break;
			}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!isKeyDown(e.getKeyCode()))
			keys.add(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(isKeyDown(e.getKeyCode()))
			remove(e.getKeyCode());
	}

}
