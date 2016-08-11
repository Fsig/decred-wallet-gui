package com.deadendgine;

import java.text.DecimalFormat;

import com.deadendgine.utils.Timer;

/**
 * These are variables accessible by all classes.
 * 
 * @author Troy
 * @version 1.00
 *
 */
public class Engine {
	private static Runtime runTime = Runtime.getRuntime();
	
	private static String name = "Dead EnDgine";
	private static String buildDate = "28/Oct/2013";
	private static double version = 0.001;
	private static boolean debug = false;
	
	private static String operatingSystem;
	private static String javaVersion;
	
	private static int width = 1280;
	private static int height = 800;
	private static int preferedWidth = 1280;
	private static int preferedHeight = 800;
	private static int minimumWidth = 800;
	private static int minimumHeight = 600;
	private static int screenWidth = 1280;
	private static int screenHeight = 800;
	
	private static String result = "";
	private static DecimalFormat shrinkBytes = new DecimalFormat("#.####");
	
	private static BaseGame game;
	
	private static Timer selectedTimer = new Timer(1000);
	private static Timer selectedTimerDelay = new Timer(1500);
	
	private static boolean openGL;
	private static boolean vsync;
	private static boolean showMemoryStats;
	
	/**
	 * Called internally by the engine to display output.
	 */
	public static void log(String message){
		System.out.println(name + " - " + version + ": " + message);
	}
	
	/**
	 * Initialise the Engine.
	 * 
	 * This will also enable hardware acceleration.
	 */
	public static void init(){
		System.setProperty("sun.java2d.opengl", String.valueOf(openGL));
		operatingSystem = System.getProperty("os.name");
		javaVersion = System.getProperty("java.version");
		
		if(debug){
			log("Operating System - " + operatingSystem);
			log("Java Version - " + javaVersion);
		}
	}
	
	/**
	 * This will output some information about Dead EnDgine.
	 */
	public static void info(){
		System.out.println(name);
		System.out.println("Version: " + version);
		System.out.println("Build Date: " + buildDate);
		System.out.println("Operating System: " + operatingSystem);
		System.out.println("Java Version: " + javaVersion);
	}
	
	/**
	 * Format bytes into a readable string value.
	 * 
	 * @param value
	 * @return String
	 */
	public static String formatBytes(long value){
		result = Double.toString(value);
    	
    	if(value > 0 && value < 1048576){
    		result = shrinkBytes.format((double)(value / (double)1024)) + " KB";
    	}else if(value > 1048576 && value < 1073741824){
    		result = shrinkBytes.format((double)(value / (double)1048576)) + " MB";
    	}else if(value > 1073741824){
    		result = shrinkBytes.format((double)(value / (double)1073741824)) + " GB";
    	}
    	
    	return result;
	}
	
	public static Runtime getRunTime() {
		return runTime;
	}

	public static void setRunTime(Runtime runTime) {
		Engine.runTime = runTime;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		Engine.debug = debug;
	}

	public static int getWidth() {
		return width;
	}
	
	public static int getHeight() {
		return height;
	}
	
	public static int getPreferedWidth() {
		return preferedWidth;
	}
	
	public static int getPreferedHeight() {
		return preferedHeight;
	}
	
	public static int getMinimumWidth() {
		return minimumWidth;
	}
	
	public static int getMinimumHeight() {
		return minimumHeight;
	}
	
	public static int getScreenWidth() {
		return screenWidth;
	}
	
	public static int getScreenHeight() {
		return screenHeight;
	}
	
	public static void setWidth(int width) {
		Engine.width = width;
	}
	
	public static void setHeight(int height) {
		Engine.height = height;
	}
	
	public static void setPreferedWidth(int preferedWidth) {
		Engine.preferedWidth = preferedWidth;
	}
	
	public static void setPreferedHeight(int preferedHeight) {
		Engine.preferedHeight = preferedHeight;
	}
	
	public static void setMinimumWidth(int minimumWidth) {
		Engine.minimumWidth = minimumWidth;
	}
	
	public static void setMinimumHeight(int minimumHeight) {
		Engine.minimumHeight = minimumHeight;
	}
	
	public static void setScreenWidth(int screenWidth) {
		Engine.screenWidth = screenWidth;
	}
	
	public static void setScreenHeight(int screenHeight) {
		Engine.screenHeight = screenHeight;
	}

	public static BaseGame getGame() {
		return game;
	}

	public static void setGame(BaseGame game) {
		Engine.game = game;
	}

	public static Timer getSelectedTimer() {
		return selectedTimer;
	}

	public static void setSelectedTimer(Timer selectedTimer) {
		Engine.selectedTimer = selectedTimer;
	}

	public static Timer getSelectedTimerDelay() {
		return selectedTimerDelay;
	}

	public static void setSelectedTimerDelay(Timer selectedTimerDelay) {
		Engine.selectedTimerDelay = selectedTimerDelay;
	}

	public static String getOperatingSystem() {
		return operatingSystem;
	}

	public static String getJavaVersion() {
		return javaVersion;
	}

	public static boolean isVsync() {
		return vsync;
	}

	public static void setVsync(boolean vsync) {
		Engine.vsync = vsync;
	}

	public static boolean isShowMemoryStats() {
		return showMemoryStats;
	}

	public static void setShowMemoryStats(boolean showMemoryStats) {
		Engine.showMemoryStats = showMemoryStats;
	}

	public static String getBuildDate() {
		return buildDate;
	}

	public static void setBuildDate(String buildDate) {
		Engine.buildDate = buildDate;
	}

	public static boolean isOpenGL() {
		return openGL;
	}

	public static void setOpenGL(boolean openGL) {
		Engine.openGL = openGL;
	}
	
}
