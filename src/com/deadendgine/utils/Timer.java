package com.deadendgine.utils;

/**
 * 
 * @author troy
 * @version 1.00
 *
 */
public class Timer {
	public long startTime;
	public long endTime;
	public long timeLimit;
	
	private String timeString;
	private long runtime;
	private long TotalSecs;
	private long TotalMins;
	private long TotalHours;
	private int seconds;
	private int minutes;
	private int hours;
  
	public Timer(int timeLimit){
		this.timeLimit = timeLimit;
		startTime = System.currentTimeMillis();
		reset();
	}
  
	public void reset(){
		endTime = ((startTime = System.currentTimeMillis()) + timeLimit);
	}

	public long getTimeElapsed(){
		return System.currentTimeMillis() - startTime;
	}
  
 	public long getTimeLeft(){
 		return endTime - System.currentTimeMillis();
 	}
  
 	public boolean isUp(){
 		return System.currentTimeMillis() > endTime;
 	}
  
 	public long getTimeRemaining(){
 		return endTime - System.currentTimeMillis();
 	}
  
 	public long setTimerToEndIn(long endTime){
 		return this.endTime = System.currentTimeMillis() + endTime;
 	}
  
 	public String toStringTimeElapsed(){
 		return toStringTime(getTimeElapsed());
 	}
  
 	public String toStringTimeLeft(){
 		return toStringTime(getTimeLeft());
 	}
  
 	public long getTotalMins(){
 		return (getTimeElapsed() / 1000) / 60;
 	}
  
 	public String toStringTime(long time){
 		timeString = "";
 		runtime = time;
 		TotalSecs = runtime / 1000;
 		TotalMins = TotalSecs / 60;
 		TotalHours = TotalMins / 60;
 		seconds = (int) TotalSecs % 60;
 		minutes = (int) TotalMins % 60;
 		hours = (int) TotalHours % 60;
 		
 		if(hours < 10)
 			timeString += "0";
 		
 		timeString += hours;
 		timeString += " : ";
 		
 		if(minutes < 10)
 			timeString += "0";
 		
 		timeString += minutes;
 		timeString += " : ";
 		
 		if(seconds < 10)
 			timeString += "0";
 		
 		timeString += seconds;
 		
	 	return timeString;
 	}
  
}