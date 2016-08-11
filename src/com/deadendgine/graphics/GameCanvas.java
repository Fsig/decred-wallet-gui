package com.deadendgine.graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import com.deadendgine.Engine;
import com.deadendgine.utils.ImageUtils;

/**
 * 
 * @author Troy
 * @version 1.00
 *
 */
public class GameCanvas extends Canvas{
	private static final long serialVersionUID = 1307018692316463796L;
	
	private BufferStrategy buffer;
	private Graphics2D g;
	private Rectangle clip = new Rectangle(0, 0, Engine.getWidth(), Engine.getHeight());
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Window w;
	private Font font = new Font("Arial", 1, 14);
	
	public GameCanvas(){
		super();
		this.setBackground(Color.BLACK);
		this.setIgnoreRepaint(true);
	}
	
	/**
	 * Initialise variables.
	 */
	public void init(){
		if(Engine.getGame() == null){
			Engine.log("GameCanvas - You have not set Game in Enginge. Engine.setGame(YourGame);");
			System.exit(-1);
		}
		
		setSize(Engine.getWidth(), Engine.getHeight());
		
		createBufferStrategy(2);
		buffer = getBufferStrategy();
	}
	
	/**
	 * The render method, this will be called by Render to display the graphics.
	 * 
	 * @param g
	 * @param backBuffer
	 */
	public void render(){	
		try{
			g = (Graphics2D) buffer.getDrawGraphics();  
			
			//Clear the drawing buffer
			g.clearRect(0, 0, getWidth(), getHeight());
			
			//Set clip
			g.setClip(clip);
			
			//Draw the game's graphics.
			Engine.getGame().render(g);
			
			//Memory Usage
			if(Engine.isShowMemoryStats()){
				g.setFont(font);
				g.setColor(Color.BLACK);
				g.fillRect(Engine.getWidth() - 220, 25, 200, 50);
				
				g.setColor(Color.YELLOW);
				g.drawRect(Engine.getWidth() - 220, 25, 200, 50);
				
				g.drawString("Total Memory: " + Engine.formatBytes(Engine.getRunTime().totalMemory()), Engine.getWidth() - 215, 40);
				g.drawString("Free Memory: " + Engine.formatBytes(Engine.getRunTime().freeMemory()), Engine.getWidth() - 215, 55);
				g.drawString("Used Memory: " + Engine.formatBytes((Engine.getRunTime().totalMemory() - Engine.getRunTime().freeMemory())), Engine.getWidth() - 215, 70);
			}
		 
		}finally{
			//Dispose of the graphics.
			g.dispose();
		}
		 
		//flip/draw the back buffer to the canvas component.
		if(!buffer.contentsLost())
			buffer.show();
		 
		//Synchronise with the display refresh rate.
		if(Engine.isVsync())
			toolkit.sync();
	}
	 
	/**
	 * Set the window to full screen.
	 * 
	 * @param window
	 */
	public void setFullscreen(Window window, boolean fullscreen){
		if(fullscreen){
			ImageUtils.graphicsDevice.setFullScreenWindow(window);
		}else{
			w = ImageUtils.graphicsDevice.getFullScreenWindow();
			
			if(w != null)
				w.dispose();
			
			ImageUtils.graphicsDevice.setFullScreenWindow(null);
		}
		
		//Make sure the canvas covers the window.
		resize();
		
		this.requestFocus();
	}
	
	/**
	 * This method will adjust the canvas size
	 * based on the width and height specified
	 * in Engine.
	 * 
	 * It will also adjust the clip area.
	 */
	public void resize(){
		setSize(Engine.getWidth(), Engine.getHeight());
		clip.width = Engine.getWidth();
		clip.height = Engine.getHeight();
	}

	public Rectangle getClip() {
		return clip;
	}

	public void setClip(Rectangle clip) {
		this.clip = clip;
	}

}
