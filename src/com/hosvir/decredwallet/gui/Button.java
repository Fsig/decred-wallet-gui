package com.hosvir.decredwallet.gui;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class Button extends Component {

    public Button(String name, String text, int x, int y, int width, int height, Color backgroundColor, Color hoverColor) {
        super(name, text, 2, x, y, width, height);
        this.textColor = Color.WHITE;
        this.textFont = FontConstants.settingsFont;
        this.borderColor = backgroundColor;
        this.hoverColor = hoverColor;

        this.rectangles = new Rectangle[1];
        this.rectangles[0] = new Rectangle(x, y, width, height);

        this.selectedId = -1;
    }

    @Override
    public void render(Graphics2D g) {
        if (hoverId != -1) g.setColor(hoverColor);
        else g.setColor(borderColor);
        if (!enabled) g.setColor(disabledColor);

        g.fillRoundRect(x, y, width, height, 10, 10);

        g.setColor(textColor);
        g.setFont(textFont);
        g.drawString(text, x + (width / 2) - (g.getFontMetrics().stringWidth(text) / 2), y + (height / 2) + 6);
    }

    @Override
    public void resize() {
        for (Rectangle r : rectangles) {
            r.x = x;
            r.y = y;
            r.width = width;
            r.height = height;
        }
    }

    @Override
    public boolean isActive() {
        return this.selectedId == 0;
    }
}
