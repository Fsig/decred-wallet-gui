package com.deadendgine.utils;



/**
 * 
 * @author Troy
 * @version 1.00
 *
 */
public class MathUtils {
	
	/**
	 * Check if a number is to the power of two.
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean isPowerOfTwo(int number){
		return ((number & -number) == number);
	}
	
	/**
	 * Check if a number is even.
	 * 
	 * @param number
	 * @return boolean
	 */
	public static boolean isEven(int number){
		return number%2 == 0;
	}
	
	/**
	 * Returns the angle between two points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return double
	 */
	public static double calcAngle(double x1, double y1, double x2, double y2) {
		return (90 + Math.toDegrees(Math.atan2(y1 - y2, x1 - x2))) % 360;
	}
	
	/**
	 * Returns the percentage
	 * 
	 * @param current
	 * @param max
	 * @param width
	 * @return Integer
	 */
	public static int getPercent(double current, double max, int percentSize){
		return (int) ((current / max) * percentSize);
	}
	
	/**
	 * Check if the String is numeric.
	 * 
	 * @param string
	 * @return Boolean
	 */
	public static boolean isNumeric(String string) {
		try {
			Integer.parseInt(string);
			return true;
		}catch(NumberFormatException e){
			return false;
		}
	}

}
