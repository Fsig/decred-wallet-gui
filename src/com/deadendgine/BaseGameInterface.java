package com.deadendgine;

import com.deadendgine.graphics.Paintable;

/**
 * This interface provides the methods used in BaseGame.
 * 
 * @author Troy
 * @version 1.00
 *
 */
public interface BaseGameInterface extends Paintable, Updatable{
	public void init();
	public void loop();
	public void resize();
	public void quit();
}
