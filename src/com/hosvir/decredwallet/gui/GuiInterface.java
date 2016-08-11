package com.hosvir.decredwallet.gui;

import com.deadendgine.Updatable;
import com.deadendgine.graphics.Paintable;

/**
 * 
 * @author Troy
 *
 */
public interface GuiInterface extends Paintable, Updatable {
	public void init();
	public void resize();
	public boolean isActive();
}
