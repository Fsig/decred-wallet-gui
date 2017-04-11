package com.hosvir.decredwallet.utils;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class GraphicsUtils {
    public static void drawString(Graphics2D g, String string, int x, int y) {
        for (String s : string.split("\n")) {
            g.drawString(s, x, y += g.getFontMetrics().getHeight());
        }
    }
}
