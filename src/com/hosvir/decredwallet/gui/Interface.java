package com.hosvir.decredwallet.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * 
 * @author Troy
 *
 */
public abstract class Interface extends BaseGui {
	public boolean blockComponents;
	public ArrayList<Component> components = new ArrayList<Component>();
	
	@Override
	public void update(long delta) {
		super.update(delta);
		if(!blockComponents) for(Component c : components) c.update(delta);
	}
	
	@Override
	public void render(Graphics2D g) {
		for(Component c : components) c.render(g);
	}
	
	@Override
	public void resize() {
		for(Component c : components) c.resize();
	}

	/**
	 * Get a component by its name.
	 * 
	 * @param name
	 * @return Component
	 */
	public Component getComponentByName(String name) {
		for(Component c : components)
			if(c.name == name)
				return c;
		
		return null;
	}

}
