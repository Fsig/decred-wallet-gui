package com.hosvir.decredwallet.gui;

import com.deadendgine.Updatable;
import com.deadendgine.graphics.Paintable;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public interface GuiInterface extends Paintable, Updatable {
    public void init();

    public void resize();

    public boolean isActive();
}
