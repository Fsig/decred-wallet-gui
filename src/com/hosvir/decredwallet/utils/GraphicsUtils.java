package com.hosvir.decredwallet.utils;

import java.awt.Graphics2D;

/**
 * 
 * @author Troy
 *
 */
public class GraphicsUtils {
	
	public static void drawString(Graphics2D g, String string, int x, int y){
		for(String s : string.split("\n")){
			g.drawString(s, x, y += g.getFontMetrics().getHeight());
		}
	}

}
