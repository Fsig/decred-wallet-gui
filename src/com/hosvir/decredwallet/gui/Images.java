package com.hosvir.decredwallet.gui;

import com.deadendgine.utils.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class Images {
    private static BufferedImage icon;
    private static BufferedImage logo;
    private static BufferedImage logoSmall;
    private static BufferedImage[] icons;
    private static BufferedImage[] interfaces;
    private static BufferedImage downArrow;
    private static BufferedImage removeIcon;
    private static BufferedImage[] loading;
    private static BufferedImage[] footerIcons;
    private static BufferedImage arrowDown;
    private static BufferedImage loginBg;

    /**
     * Load the images.
     */
    public static void init() {
        icon = ImageUtils.loadBufferedImage("resources/icon.png");
        logo = ImageUtils.loadBufferedImage("resources/logo.png");
        logoSmall = ImageUtils.loadBufferedImage("resources/logo-small.png");
        icons = ImageUtils.splitImage(ImageUtils.loadBufferedImage("resources/navicons.png"), 7, 2);
        interfaces = ImageUtils.splitImage(ImageUtils.loadBufferedImage("resources/interface.png"), 20, 1);
        downArrow = ImageUtils.loadBufferedImage("resources/down-arrow.png");
        removeIcon = ImageUtils.loadBufferedImage("resources/remove-icon.png");
        loading = ImageUtils.splitImage(ImageUtils.loadBufferedImage("resources/loading.png"), 6, 2);
        footerIcons = ImageUtils.splitImage(ImageUtils.loadBufferedImage("resources/footericons.png"), 4, 2);
        arrowDown = ImageUtils.loadBufferedImage("resources/arrowdown.png");
        loginBg = ImageUtils.loadBufferedImage("resources/login-bg.jpg");
    }

    public static BufferedImage getIcon() {
        return icon;
    }

    public static BufferedImage getLogo() {
        return logo;
    }

    public static BufferedImage getLogoSmall() {
        return logoSmall;
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

    public static BufferedImage[] getLoading() {
        return loading;
    }

    public static BufferedImage[] getFooterIcons() {
        return footerIcons;
    }

    public static BufferedImage getArrowDown() {
        return arrowDown;
    }

    public static BufferedImage getLoginBg() {
        return loginBg;
    }
}
