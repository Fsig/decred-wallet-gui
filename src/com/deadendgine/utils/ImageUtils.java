package com.deadendgine.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 
 * @author Troy
 * @version 1.0
 *
 */
public class ImageUtils {
	private static final ImageUtils instance = new ImageUtils();
	public static GraphicsEnvironment graphicsEnviroment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	public static GraphicsDevice graphicsDevice = graphicsEnviroment.getDefaultScreenDevice(); 
	public static GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
	private static AffineTransform affineTransform = new AffineTransform();
	
	private static BufferedImage bimg;
	private static BufferedImage cimg;
	private static BufferedImage imgs[];
	private static BufferedImage imgss[][];
	private static VolatileImage rt;
	private static Graphics g;
	private static Graphics2D g2;
	private static int w;  
	private static int h;
	private static int num;
	private static int imageType;
	
	/**
	 * Load a image.
	 * 
	 * Note: Use loadCompatibleImage where possible.
	 * 
	 * @param ref
	 * @return BufferedImage
	 */
	public static BufferedImage loadBufferedImage(String ref){
		bimg = null;
		
		try{
			if(ref.startsWith("res"))
				bimg = ImageIO.read(instance.getClass().getClassLoader().getResource(ref));
			else
				bimg = ImageIO.read(new File(ref));
		}catch(Exception e){e.printStackTrace();}
		
		return bimg;
	}
	
	/**
	 * Load a image from resources and save it to a compatible
	 * image for best performance from the graphics card.
	 * 
	 * @param ref
	 * @return BufferedImage
	 */
	public static BufferedImage loadCompatibleBufferedImage(String ref){
		bimg = null;
		cimg = null;
		try{
			if(ref.startsWith("res"))
				bimg = ImageIO.read(instance.getClass().getClassLoader().getResource(ref));
			else
				bimg = ImageIO.read(new File(ref));
			
			cimg = getCompatibleBufferedImage(bimg.getWidth(), bimg.getHeight());
			
		    g = cimg.getGraphics();
		    g.drawImage(bimg, 0, 0, null);
		    g.dispose();
		}catch(Exception e){e.printStackTrace();}
		
		return cimg;
	}
	
	/**
	 * Load a image from resources and save it to a compatible
	 * image for best performance from the graphics card.
	 * 
	 * @param ref
	 * @return VolatileImage
	 */
	public static VolatileImage loadCompatibleVolatileImage(String ref){
		bimg = null;
		rt = null;
		
		try{
			bimg = ImageIO.read(instance.getClass().getClassLoader().getResource(ref));
			rt = getCompatibleVolatileImage(bimg.getWidth(), bimg.getHeight());
			
			g = rt.getGraphics();
			g.drawImage(bimg, 0, 0, null);
			g.dispose();
		}catch(Exception e){e.printStackTrace();}
		
		return rt;
	}
	
	/**
	 * This will create a compatible image to be used
	 * by the graphics card.
	 * 
	 * @param width
	 * @param height
	 * @return BufferedImage
	 */
	public static BufferedImage getCompatibleBufferedImage(int width, int height){
		return graphicsConfiguration.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	}
	
	/**
	 * This will create a compatible Volatile image to be used
	 * by the graphics card.
	 * 
	 * @param width
	 * @param height
	 * @return BufferedImage
	 */
	public static VolatileImage getCompatibleVolatileImage(int width, int height){
		return graphicsConfiguration.createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
	}
	
	public static BufferedImage getSubImage(BufferedImage img, int x, int y){
		img.getSubimage(x * 32 + x, y * 32 + y, 32, 32);
		return null;
	}
	
	/**
	 * Load a image array.
	 * 
	 * @param img
	 * @param cols
	 * @param rows
	 * @return BufferedImage[]
	 */
    public static BufferedImage[] splitImage(BufferedImage img, int cols, int rows) { 	
    	w = img.getWidth()/cols;
        h = img.getHeight()/rows;
        num = 0;
		imageType = img.getType();
		if(imageType == 0) imageType = 5;
        imgs = new BufferedImage[cols*rows];

        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
                imgs[num] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                g2 = imgs[num].createGraphics();
                g2.setComposite(AlphaComposite.Src);  
                g2.drawImage(img, 0, 0, w, h, w*x, h*y, w*x+w, h*y+h, null);
                g2.dispose();
                num++;
            }
        }
        
        
        
        return imgs;
    }
    
	/**
	 * Load a image array.
	 * 
	 * @param img
	 * @param cols
	 * @param rows
	 * @return BufferedImage[]
	 */
    public static BufferedImage[][] splitImage2D(BufferedImage img, int cols, int rows) { 	
    	w = img.getWidth()/cols;
        h = img.getHeight()/rows;
		imageType = img.getType();
		if(imageType == 0) imageType = 5;
		imgss = new BufferedImage[w][h];
        
        for(int y = 0; y < rows; y++) {
            for(int x = 0; x < cols; x++) {
            	imgss[x][y] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                g2 = imgss[x][y].createGraphics();
                g2.setComposite(AlphaComposite.Src);  
                g2.drawImage(img, 0, 0, w, h, w*x, h*y, w*x+w, h*y+h, null);
                g2.dispose();
            }
        }
        
        return imgss;
    }
	
	/**
	 * Make's a certain colour transparent.
	 * @param source
	 * @param color
	 * @return BufferedImage
	 */
    public static BufferedImage makeColorTransparent(String ref, Color color) {
    	bimg = loadBufferedImage(ref);
        cimg = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TYPE_INT_ARGB);  
        g2 = cimg.createGraphics();  
        g2.setComposite(AlphaComposite.Src);  
        g2.drawImage(bimg, null, 0, 0);  
        g2.dispose();  
        
        for(int i = 0; i < cimg.getHeight(); i++) {  
            for(int j = 0; j < cimg.getWidth(); j++) {  
                if(cimg.getRGB(j, i) == color.getRGB()) {  
                	cimg.setRGB(j, i, 0x8F1C1C);  
                }  
            }  
        }  
        
        return cimg;  
    }
    
    /**
     * Load a image with a certain transparency.
     * 
     * @param url
     * @param transperancy
     * @return BufferedImage
     */
    public static BufferedImage loadTranslucentImage(String url, float transperancy) {   
        bimg = loadBufferedImage(url);   
        cimg = new BufferedImage(bimg.getWidth(), bimg.getHeight(), BufferedImage.TRANSLUCENT);  
        g2 = cimg.createGraphics();   
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));   
        g2.drawImage(bimg, null, 0, 0);   
        g2.dispose();    
        
        return cimg;  
    }
    
    /**
     * Change a image with a certain transparency.
     * 
     * @param url
     * @param transperancy
     * @return BufferedImage
     */
    public static BufferedImage changeTranslucentImage(BufferedImage img, float transperancy) {     
        bimg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TRANSLUCENT);  
        g2 = bimg.createGraphics();   
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));   
        g2.drawImage(img, null, 0, 0);   
        g2.dispose();  
        
        return bimg;  
    }
	
	/**
	 * Flips a given image horizontally.
	 * @param source The source image.
	 * @return BufferedImage
	 */
	public static BufferedImage flipHorizontally(BufferedImage source) {  
		w = source.getWidth();  
		h = source.getHeight();  
		bimg = new BufferedImage(w, h, source.getType());  
		g2 = bimg.createGraphics();  
		g2.drawImage(source, 0, 0, w, h, w, 0, 0, h, null);  
		g2.dispose();  
		
		return bimg;  
	}  

	/**
	 * Flips a given image vertically.
	 * @param source The source image.
	 * @return BufferedImage
	 */
	public static BufferedImage flipVertically(BufferedImage source) {  
		w = source.getWidth();  
		h = source.getHeight();  
		bimg = new BufferedImage(w, h, source.getType());  
		g2 = bimg.createGraphics();  
		g2.drawImage(source, 0, 0, w, h, 0, h, w, 0, null);  
		g2.dispose();  
		
		return bimg;  
	}  

	/**
	 * Rotates a given image to a given angle.
	 * @param source The source image.
	 * @param angle The angle to rotate by (in degrees).
	 * @return BufferedImage
	 */
	public static BufferedImage rotate(BufferedImage source, int angle) {  
		w = source.getWidth();  
		h = source.getHeight();  
		bimg = new BufferedImage(w, h, source.getType());  
		g2 = bimg.createGraphics();  
		g2.rotate(Math.toRadians(angle), w / 2, h / 2);  
		g2.drawImage(source, null, 0, 0); 
		
		return bimg;  
	}  

	/**
	 * Resizes a given image to a given width and height.
	 * @param source The source image.
	 * @param width The new width.
	 * @param height The new height.
	 * @return BufferedImage
	 */
	public static BufferedImage resize(BufferedImage source, int width, int height) {  
		w = source.getWidth();  
		h = source.getHeight();  
		bimg = new BufferedImage(width, height, source.getType());  
		g2 = bimg.createGraphics();  
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
		g2.drawImage(source, 0, 0, width, height, 0, 0, w, h, null);  
		g2.dispose();  
		
		return bimg;  
	}
	
	/**
	 * Create a polygon based on a image.
	 * 
	 * 	creates an outline of a transparent image, points are stored in an array
	 *	arg0 - BufferedImage source image 
	 *	arg1 - Int detail (lower = better)
	 *	arg2 - Int angle threshold in degrees (will remove points with angle differences below this level; 15 is a good value)
	 *	making this larger will make the body faster but less accurate;
	 * 
	 * @param spr
	 * @param d
	 * @param angle
	 * @return Point[]
	 */
	public static Polygon MakePoly(BufferedImage spr, int d, int angle, int baseX, int baseY){
		Polygon p = new Polygon();
		
		int w = spr.getWidth(null);  int h= spr.getHeight(null);

		try{
		// increase array size from 255 if needed
		int[] vertex_x =new int[255], vertex_y=new int[255], vertex_k=new int[255]; 
		int numPoints=0, tx=0,ty=0,fy=-1,lx=0,ly=0; vertex_x[0]=0; vertex_y[0]=0; vertex_k[0]=1; 
		
		for(tx = 0; tx < w; tx += d)
			for(ty = 0; ty < h; ty += 1)
				if((spr.getRGB(tx,ty) >> 24) != 0x00){
					vertex_x[numPoints]=tx; vertex_y[numPoints]=h-ty; 
					vertex_k[numPoints]=1; numPoints++; 
					
					if(fy < 0) fy=ty; lx=tx; ly=ty; break;  
				}      
		
		for(ty = 0; ty < h; ty += d)  
			for(tx = w-1; tx >= 0; tx -= 1)
				if((spr.getRGB(tx,ty) >> 24) != 0x00 && ty > ly){
					vertex_x[numPoints]=tx; 
					vertex_y[numPoints]=h-ty; 
					vertex_k[numPoints]=1; 
					numPoints++; 
					lx=tx; 
					ly=ty; 
					break;  
				}
		
		for(tx = w-1; tx >= 0; tx -= d)
			for(ty = h-1; ty >= 0; ty -= 1)
				if((spr.getRGB(tx,ty) >> 24) != 0x00 && tx < lx){
					vertex_x[numPoints]=tx; 
					vertex_y[numPoints]=h-ty; 
					vertex_k[numPoints]=1; 
					numPoints ++; 
					lx=tx; 
					ly=ty; 
					break; 
				} 
		
		for(ty = h-1; ty >= 0; ty -= d)
			for(tx = 0; tx < w; tx += 1)
				if((spr.getRGB(tx,ty) >> 24) != 0x00 && ty < ly && ty > fy){
					vertex_x[numPoints]=tx; 
					vertex_y[numPoints]=h-ty; 
					vertex_k[numPoints]=1; 
					numPoints ++; 
					lx=tx; 
					ly=ty; 
					break; 
				}      
		
		double ang1,ang2;       
		
		for(int i=0; i < numPoints -2; i++) {
			ang1 = PointDirection(vertex_x[i],vertex_y[i], vertex_x[i+1],vertex_y[i+1]);
			ang2 = PointDirection(vertex_x[i+1],vertex_y[i+1], vertex_x[i+2],vertex_y[i+2]);
			
			if(Math.abs(ang1-ang2) <= angle)
				vertex_k[i+1] = 0;         
		}
		
		ang1 = PointDirection(vertex_x[numPoints-2],vertex_y[numPoints-2], vertex_x[numPoints-1],vertex_y[numPoints-1]);
		ang2 = PointDirection(vertex_x[numPoints-1],vertex_y[numPoints-1], vertex_x[0],vertex_y[0]);
		
		if(Math.abs(ang1-ang2) <= angle)
			vertex_k[numPoints-1] = 0; 
		
		ang1 = PointDirection(vertex_x[numPoints-1],vertex_y[numPoints-1], vertex_x[0],vertex_y[0]);
		ang2 = PointDirection(vertex_x[0],vertex_y[0], vertex_x[1],vertex_y[1]);
		
		if(Math.abs(ang1-ang2) <= angle)
			vertex_k[0] = 0;
		
		int n = 0;
		
		for(int i= 0; i < numPoints; i++)
			if(vertex_k[i] == 1)n++;
				Point[] poly = new Point[n]; 
				
		n=0; 
		
		for(int i= 0; i < numPoints; i++)
			if(vertex_k[i] == 1){ 
				poly[n] = new Point(); 
				poly[n].x = vertex_x[i]; 
				poly[n].y = h-vertex_y[i];
				n++;
			} 
		
		for(Point pp : poly)
			p.addPoint(baseX + pp.x, baseY + pp.y);
		}catch(Exception e){
			return null;
		}
		
		return p;
	}
	
	public static double PointDirection(double xfrom,double yfrom,double xto,double yto){
		return Math.atan2(yto-yfrom,xto-xfrom)*180/Math.PI ;
	}

	public static AffineTransform getAffineTransform() {
		return affineTransform;
	}

	public static void setAffineTransform(AffineTransform affineTransform) {
		ImageUtils.affineTransform = affineTransform;
	}  
	
}