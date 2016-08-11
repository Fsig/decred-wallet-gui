package com.deadendgine;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.deadendgine.graphics.GameCanvas;
import com.deadendgine.utils.MathUtils;

/**
 * The Frame.
 * We use a standard JFrame and add some features
 * so we can consume the mouse and make it full screen.
 * 
 * @author Troy
 * @version 1.00
 *
 */
public class GameFrame extends JFrame implements ComponentListener, WindowListener {
	private static final long serialVersionUID = -8942902372792699702L;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private Dimension dim;
	private BufferedImage cursorImg;
	private Graphics2D g2d;
	
	/**
	 * Constructor.
	 * 
	 * @param title
	 * @param canvas
	 */
	public GameFrame(String title, GameCanvas canvas){
		this.setTitle(title);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource("/resources/icon.png")));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setIgnoreRepaint(true);
		this.setLayout(null);
		
		dim = toolkit.getScreenSize();
		Engine.setScreenWidth((int)dim.getWidth());
		Engine.setScreenHeight((int)dim.getHeight());
		
		this.addComponentListener(this);
		this.addWindowListener(this);
		this.add(canvas);
		
		this.setPreferredSize(new Dimension(Engine.getPreferedWidth(), Engine.getPreferedHeight()));
		this.setSize(Engine.getWidth(), Engine.getHeight());
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}
	
	/**
	 * Consume the mouse to allow for a custom drawn
	 * mouse on screen.
	 * 
	 * @param consume
	 */
	public void consumeMouse(boolean consume){
		if(consume){
			dim = toolkit.getBestCursorSize(1, 1);
			cursorImg = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
			g2d = cursorImg.createGraphics();
			g2d.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
			g2d.clearRect(0, 0, dim.width, dim.height);
			g2d.dispose();
			this.setCursor(toolkit.createCustomCursor(cursorImg, new Point(0,0), "hiddenCursor"));
		}else if(this.getCursor() != Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)){
			cursorImg = null;
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	/**
	 * Set the cursor to the specified one.
	 * 
	 * @param cursor
	 */
	public void setCursor(BufferedImage cursor){
		if(cursorImg != cursor){
			cursorImg = cursor;
			this.setCursor(toolkit.createCustomCursor(cursor, new Point(0,0), "hiddenCursor"));
		}
	}
	
	/**
	 * Make the frame full screen.
	 * @param fullscreen
	 */
	public void setFullscreen(boolean fullscreen){
		this.dispose();
		this.setUndecorated(fullscreen);
		
		if(fullscreen){
			this.setSize(Engine.getScreenWidth(), Engine.getScreenHeight());
			Engine.setWidth(Engine.getScreenWidth());
			Engine.setHeight(Engine.getScreenHeight());
		}else{
			this.setSize(Engine.getPreferedWidth(), Engine.getPreferedHeight());
			Engine.setWidth(Engine.getPreferedWidth());
			Engine.setHeight(Engine.getPreferedHeight());
		}
		
		this.setVisible(true);
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		if(Engine.getWidth() != Engine.getScreenWidth() && Engine.getHeight() != Engine.getScreenHeight()){
			while(!MathUtils.isEven(getWidth()))
				this.setSize(getWidth() +1, getHeight());
			
			while(!MathUtils.isEven(getHeight()))
				this.setSize(getWidth(), getHeight() +1);
		}
		
		Engine.setWidth(getWidth());
		Engine.setHeight(getHeight());
		Engine.getGame().resize();
	}

	public void componentHidden(ComponentEvent arg0) {}
	public void componentMoved(ComponentEvent arg0) {}
	public void componentShown(ComponentEvent arg0) {}
	public void windowActivated(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	
	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowClosing(WindowEvent e) {
		Engine.getGame().quit();
	}

}
