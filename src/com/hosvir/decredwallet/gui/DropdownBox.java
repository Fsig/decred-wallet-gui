package com.hosvir.decredwallet.gui;

import com.deadendgine.input.Keyboard;
import com.deadendgine.input.Mouse;
import com.deadendgine.utils.StringUtils;
import com.hosvir.decredwallet.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public class DropdownBox extends Component implements KeyListener {
    public String[] lineItems;
    public Rectangle[] itemRectangles;

    /**
     * Construct a new Drop down box.
     *
     * @param name
     * @param x
     * @param y
     * @param width
     * @param height
     * @param lineItems
     */
    public DropdownBox(String name, int x, int y, int width, int height, String[] lineItems) {
        super(name, "", 5, x, y, width, height);
        this.textColor = ColorConstants.labelColor;
        this.textFont = FontConstants.settingsFont;

        this.rectangles = new Rectangle[1];
        this.rectangles[0] = new Rectangle(x, y, width, height);

        this.selectedId = -1;
        this.lineItems = lineItems;

        if (lineItems != null) {
            this.itemRectangles = new Rectangle[lineItems.length];

            for (int i = 0; i < lineItems.length; i++) {
                itemRectangles[i] = new Rectangle(x, y + ((i + 1) * height), width, height);
            }
        }

        Main.canvas.addKeyListener(this);
    }

    @Override
    public void update(long delta) {
        if (isActive()) {
            if (itemRectangles != null) {
                for (int i = 0; i < itemRectangles.length; i++) {
                    if (itemRectangles[i].contains(Mouse.point)) {
                        containsMouse = true;
                        hoverItem = i;

                        if (Mouse.isMouseDown(MouseEvent.BUTTON1)) {
                            text = lineItems[i];
                            selectedItem = i;
                            selectedId = -1;
                            Mouse.release(MouseEvent.BUTTON1);
                        }
                    }
                }
            } else if (lineItems != null && itemRectangles == null) {
                this.itemRectangles = new Rectangle[lineItems.length];

                for (int i = 0; i < lineItems.length; i++) {
                    itemRectangles[i] = new Rectangle(x, y + ((i + 1) * height), width, height);
                }
            }
        }

        super.update(delta);
    }

    @Override
    public void render(Graphics2D g) {
        //Draw line items
        if (selectedId == 0) {
            g.setColor(Color.WHITE);
            g.fillRect(x,
                    y + height,
                    width,
                    height * lineItems.length);

            g.setColor(borderColor);
            g.drawRect(x,
                    y + height,
                    width,
                    height * lineItems.length);
        }


        //Draw first box
        if (enabled && hoverId == 0) g.setColor(hoverColor);
        else g.setColor(borderColor);

        g.drawRect(x, y, width, height);

        if (!enabled) {
            g.setColor(borderColor);
            g.fillRect(x, y, width, height);
        }

        g.setColor(textColor);
        g.setFont(textFont);

        //Draw selected item
        g.drawString(text.replace(".conf", ""), x + 10, y + (height / 2) + 8);

        //Draw drop down arrow
        g.drawImage(Images.getDownArrow(),
                x + width - Images.getDownArrow().getWidth() - 5,
                y,
                Images.getDownArrow().getWidth(),
                Images.getDownArrow().getHeight(),
                null);


        //Draw item strings
        if (selectedId == 0) {
            //Draw line item strings
            g.setFont(textFont);

            if (lineItems != null) {
                for (int i = 0; i < lineItems.length; i++) {
                    g.setColor(textColor);
                    g.drawString(lineItems[i].replace(".conf", ""), x + 10, y + (height / 2) + 8 + ((i + 1) * height));

                    if (enabled && hoverItem == i) {
                        g.setColor(hoverColor);
                        g.drawRect(itemRectangles[i].x, itemRectangles[i].y, itemRectangles[i].width, itemRectangles[i].height);
                    }
                }
            }
        }
    }

    @Override
    public void resize() {
        for (Rectangle r : rectangles) {
            r.x = x;
            r.y = y;
            r.width = width;
            r.height = height;
        }

        if (itemRectangles != null) {
            for (Rectangle r : itemRectangles) {
                r.width = width;
                r.height = height;
            }
        }
    }

    @Override
    public boolean isActive() {
        return this.selectedId == 0;
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (selectedId == 0) {
            if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_V)) {
                text = Constants.getClipboardString();
            } else if (Keyboard.isKeyDown(KeyEvent.VK_CONTROL) && Keyboard.isKeyDown(KeyEvent.VK_C)) {
                Constants.setClipboardString(text);
            } else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE:
                        text = StringUtils.backspace(text);
                        break;
                    case KeyEvent.VK_SHIFT:
                        break;
                    case KeyEvent.VK_CONTROL:
                        break;
                    case KeyEvent.VK_ALT:
                        break;
                    default:
                        text += e.getKeyChar();
                        break;
                }
            }
        }
    }

    public String[] getLineItems() {
        return lineItems;
    }

    public void setLineItems(String[] lineItems) {
        this.lineItems = lineItems;

        if (lineItems != null) {
            this.itemRectangles = new Rectangle[lineItems.length];

            for (int i = 0; i < lineItems.length; i++) {
                itemRectangles[i] = new Rectangle(x, y + ((i + 1) * height), width, height);
            }
        }
    }
}
