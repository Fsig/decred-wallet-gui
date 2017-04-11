package com.hosvir.decredwallet.gui;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class Dialog extends Component {
    private Component okButton;

    /**
     * Construct a new label.
     *
     * @param name
     * @param text
     * @param x
     * @param y
     */
    public Dialog(String name, String text) {
        super(name, text, 4, 0, 0, 600, 150);
        this.textFont = FontConstants.labelFont;
        this.textColor = ColorConstants.walletBalanceColor;
        this.selectedId = -1;

        this.okButton = new Button("ok", "Ok", (Engine.getWidth() / 2) + (width / 2) - 120, (Engine.getHeight() / 2) + (height / 2) - 55, 100, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover);
    }

    @Override
    public void update(long delta) {
        super.update(delta);

        if (isActive()) {
            okButton.update(delta);

            if (okButton.containsMouse) Main.containsMouse = true;

            if (okButton.selectedId == 0) {
                okButton.selectedId = -1;
                Constants.blockInterfaces(false, null);
                this.selectedId = -1;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (isActive()) {
            g.setColor(ColorConstants.transparentBlack);
            g.fillRect(0, 0, Engine.getWidth(), Engine.getHeight());

            g.setColor(Color.WHITE);
            g.fillRoundRect((Engine.getWidth() / 2) - (width / 2), (Engine.getHeight() / 2) - (height / 2), width, height, 10, 10);

            g.setColor(textColor);
            g.setFont(textFont);
            g.drawString(text, (Engine.getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2), (Engine.getHeight() / 2));

            //Render
            okButton.render(g);
        }
    }

    @Override
    public boolean isActive() {
        return this.selectedId == 0;
    }

    @Override
    public void resize() {
        okButton.x = (Engine.getWidth() / 2) + (width / 2) - okButton.width - 20;
        okButton.y = (Engine.getHeight() / 2) + (height / 2) - okButton.height - 20;
        okButton.resize();
    }
}
