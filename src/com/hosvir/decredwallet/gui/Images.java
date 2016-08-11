package com.hosvir.decredwallet.gui;

import java.awt.image.BufferedImage;

import com.deadendgine.utils.ImageUtils;

/**
 * 
 * @author Troy
 * @version 1.00
 *
 */
public class Images {
	private static BufferedImage icon;
	private static BufferedImage[] icons;
	private static BufferedImage[] interfaces;
	private static BufferedImage downArrow;
	private static BufferedImage removeIcon;

	/**
	 * Load the images.
	 */
	public static void init(){
		icon = ImageUtils.loadBufferedImage("resources/icon.png");
		icons = ImageUtils.splitImage(ImageUtils.loadBufferedImage("resources/navicons.png"), 7, 2);
		interfaces = ImageUtils.splitImage(ImageUtils.loadBufferedImage("resources/interface.png"), 20, 1);
		downArrow = ImageUtils.loadBufferedImage("resources/down-arrow.png");
		removeIcon = ImageUtils.loadBufferedImage("resources/remove-icon.png");
	}
	
	public static BufferedImage getIcon() {
		return icon;
	}
	
	public static BufferedImage[] getIcons() {
		return icons;
	}

	public static BufferedImage[] getInterfaces() {
		return interfaces;
	}
	
	public static BufferedImage getDownArrow() {
		return downArrow;
	}
	
	public static BufferedImage getRemoveIcon() {
		return removeIcon;
	}


}
