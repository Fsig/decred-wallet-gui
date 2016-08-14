package com.hosvir.decredwallet.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.deadendgine.utils.Timer;

/**
 * 
 * @author Troy
 *
 */
public class ReloadButton extends Component {
	private int animateCounter;
	private Timer animateTimer;
	
	/**
	 * Construct a new reload button.
	 * 
	 * @param name
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public ReloadButton(String name, int x, int y, int width, int height) {
		super(name, "", 7, x, y, width, height);
		
		this.animateCounter = 0;
		this.animateTimer = new Timer(80);
		this.rectangles = new Rectangle[1];
		this.rectangles[0] = new Rectangle(x,y,width,height);
		
		this.selectedId = -1;
	}
	
	public void update(long delta) {
		super.update(delta);
		
		if(selectedId == 0 && animateTimer.isUp()) {
			if(animateCounter == 0) animateCounter = 6;
			animateCounter++;
			animateTimer.reset();
			if(animateCounter >= Images.getLoading().length) animateCounter = 6;
		}else if(selectedId == -1) {
			animateCounter = 0;
		}
	}

	@Override
	public void render(Graphics2D g) {
		if(hoverId != -1 && selectedId == -1) animateCounter = 6;
		
		g.drawImage(Images.getLoading()[animateCounter], 
				x, 
				y, 
				width, 
				height,
				null);
	}
	
	@Override
	public void resize() {
		for(Rectangle r : rectangles){
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
