package com.hosvir.decredwallet.gui;

import java.awt.*;
import java.io.IOException;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class FontConstants {
    public static Font walletNameFont = new Font("Arial", Font.BOLD, 25);
    public static Font walletBalanceFont = new Font("Arial", Font.BOLD, 18);
    public static Font dcrFont = new Font("Arial", Font.PLAIN, 30);
    public static Font totalBalanceFont = new Font("Arial", Font.PLAIN, 40);
    public static Font labelFont = new Font("Arial", Font.PLAIN, 16);
    public static Font addressFont = new Font("Arial", Font.PLAIN, 18);
    public static Font transactionFont = new Font("Arial", Font.PLAIN, 14);
    public static Font settingsFont = new Font("Arial", Font.BOLD, 20);
    public static Font tabFont = new Font("Arial", Font.BOLD, 20);

    public static void initialise() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontConstants.class.getResourceAsStream("/resources/fonts/inconsolata/Inconsolata-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontConstants.class.getResourceAsStream("/resources/fonts/inconsolata/Inconsolata-Bold.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontConstants.class.getResourceAsStream("/resources/fonts/source_sans_pro/SourceSansPro-Light.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontConstants.class.getResourceAsStream("/resources/fonts/source_sans_pro/SourceSansPro-Regular.ttf")));
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, FontConstants.class.getResourceAsStream("/resources/fonts/source_sans_pro/SourceSansPro-Bold.ttf")));

            walletNameFont = new Font("SourceSansPro-Regular", Font.BOLD, 25);
            walletBalanceFont = new Font("Inconsolata-Bold", Font.BOLD, 14);
            dcrFont = new Font("SourceSansPro-Regular", Font.PLAIN, 30);
            totalBalanceFont = new Font("SourceSansPro-Regular", Font.BOLD, 40);
            labelFont = new Font("SourceSansPro-Light", Font.PLAIN, 14);
            addressFont = new Font("SourceSansPro-Regular", Font.PLAIN, 18);
            transactionFont = new Font("SourceSansPro-Regular", Font.PLAIN, 12);
            settingsFont = new Font("SourceSansPro-Bold", Font.BOLD, 14);
            tabFont = new Font("SourceSansPro-Bold", Font.BOLD, 20);
        } catch (IOException | FontFormatException e) {
            System.out.println("Failed to load fonts, switching to defaults.");
            e.printStackTrace();
        }
    }
}
