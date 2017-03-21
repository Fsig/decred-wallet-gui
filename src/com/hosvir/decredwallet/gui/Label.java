package com.hosvir.decredwallet.gui;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class Label extends Component {
    public Label(String name, String text, int x, int y) {
        super(name, text, 1, x, y, 0, 0);

        this.selectedId = 0;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(textColor);
        g.setFont(textFont);
        g.drawString(text, x, y);
    }

    @Override
    public boolean isActive() {
        return this.selectedId == 0;
    }
}
