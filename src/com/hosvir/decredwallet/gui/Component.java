package com.hosvir.decredwallet.gui;

import java.awt.*;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public abstract class Component extends BaseGui {
    public String name;
    public String text;
    public String placeholderText;
    public int id;
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean enabled;
    public boolean textHidden;
    public Color textColor;
    public Color borderColor;
    public Color hoverColor;
    public Color disabledColor;
    public Font textFont;

    /**
     * Construct a new component.
     *
     * @param name
     * @param text
     * @param id
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Component(String name, String text, int id, int x, int y, int width, int height) {
        this.name = name;
        this.text = text;
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.enabled = true;
        this.textHidden = false;
        this.textColor = ColorConstants.labelColor;
        this.borderColor = ColorConstants.settingsSelectedColor;
        this.hoverColor = ColorConstants.flatBlue;
        this.disabledColor = ColorConstants.settingsSelectedColor;
        this.textFont = FontConstants.labelFont;
    }

    @Override
    public void update(long delta) {
        if(enabled) super.update(delta);
    }

    @Override
    public void resize() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTextHidden() {
        return textHidden;
    }

    public void setTextHidden(boolean textHidden) {
        this.textHidden = textHidden;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public Color getDisabledColor() {
        return disabledColor;
    }

    public void setDisabledColor(Color disabledColor) {
        this.disabledColor = disabledColor;
    }

    public Font getTextFont() {
        return textFont;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }
}
