package com.hosvir.decredwallet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.deadendgine.utils.StringUtils;
import com.deadendgine.utils.Timer;

/**
 * 
 * @author Troy
 *
 */
public class Processes {
	private static LocalCommand localCommand = new LocalCommand();
	private static Timer processTimer = new Timer(5000);
	private static Pattern pattern;
	private static Matcher regexMatcher;
	private static String PROCESS_PATTERN = "(.*?)(\r|\n)";
	private static String string,result;
	private static String[] split;
	
	/**
	 * Kill ALL processes with the specified name.
	 * 
	 * @param processName
	 * @return boolean
	 */
	public static synchronized boolean killByName(String processName){
		processTimer.reset();

		//Try to kill all processes with the name
		while(getClosestProcess(processName) != -1 && !processTimer.isUp()){
			kill(getClosestProcess(processName));
		}
		
		//Return the results	
		return getClosestProcess(processName) == -1;
	}
	
	/**
	 * Kill the process by pid.
	 * 
	 * @param pid
	 * @return boolean
	 */
	public static synchronized boolean kill(int pid){
		if(Constants.isOsLinux()){
			processTimer.reset();
	
			//Try to kill all processes with the name
			while(localCommand.execute("ps aux | awk '{print $2 }' | grep " + pid).length() > 0 && !processTimer.isUp()){
				localCommand.execute("kill -2 " + pid); // -2 should send SIGINT and create a safe shutdown for DCRD
			}
			
			//Return the results	
			return localCommand.execute("ps aux | awk '{print $2 }' | grep " + pid).length() == 0;
		}else if(Constants.isOsWindows()){
			while(localCommand.execute("tasklist /fi " + pid + "").length() > 0 && !processTimer.isUp()){
				localCommand.execute("taskkill /PID " + pid + " /F");
			}
			
			//Return the results	
			return localCommand.execute("tasklist /fi " + pid + "").length() == 0;
		}
		
		return false;
	}
	
	/**
	 * Attempt to find a matching process based on the process name.
	 * 
	 * @param processName
	 * @return String
	 */
	public static synchronized int getClosestProcess(String processName){
		String[] processes = getAllForUser().split(";");
		String rp = null;
	
		for(String s : processes)
			if(s != null && s != "" && (processName.toLowerCase().contains(s.split(",")[4].toLowerCase()) || s.split(",")[4].toLowerCase().contains(processName.toLowerCase())))
				rp = s.split(",")[0].toLowerCase();

		if(rp == null) return -1;
		
		return Integer.valueOf(rp);
	}
		
	/**
	 * Get all processes for the logged in user.
	 * 
	 * @return String
	 */
	public static synchronized String getAllForUser() {
		if(Constants.isOsLinux()){
			pattern = Pattern.compile(PROCESS_PATTERN);
			regexMatcher = pattern.matcher(localCommand.execute("top -b -n1 -u " + Constants.getUser()));
			result = "";
	
			while(regexMatcher.find()) {
				if(!regexMatcher.group().contains(",") && !regexMatcher.group().contains("%")){
					string = StringUtils.backspace(regexMatcher.group().replaceAll("\\s+", ","));
					
					if(string.startsWith(","))
						string = string.replaceFirst(",", "");
					
					if(string != null && string != ""){
						split = string.split(",");
						result += split[0] + "," + split[1] + "," + split[8] + "," + split[9] + "," + split[11] + ";";
					}
				}
			}
		}else if(Constants.isOsWindows()){
			pattern = Pattern.compile(PROCESS_PATTERN);
			regexMatcher = pattern.matcher(localCommand.execute("tasklist /V"));
			result = "";
	
			while(regexMatcher.find()) {
				if(regexMatcher.group().contains(":")){
					string = StringUtils.backspace(regexMatcher.group().replaceAll(",", "-COMMA-").replaceAll("\\s+", ","));
					
					if(string.startsWith(","))
						string = string.replaceFirst(",", "");
					
					if(string != null && string != ""){
						split = string.split(",");
						result += split[1] + "," + split[6] + "," + split[7] + "," + split[4] + "," + split[0] + ";";
					}
				}
			}
		}else { 
			pattern = Pattern.compile(PROCESS_PATTERN);
			regexMatcher = pattern.matcher(localCommand.execute("top -l 1 -U " + Constants.getUser()));
			result = "";
	
			while(regexMatcher.find()) {
				if(!regexMatcher.group().contains(",") && !regexMatcher.group().contains("%") && regexMatcher.group().length() > 25){
					string = StringUtils.backspace(regexMatcher.group().replaceAll("\\s+", ","));
					
					if(string.startsWith(","))
						string = string.replaceFirst(",", "");
					
					if(string != null && string != ""){
						split = string.split(",");
						result += split[0] + "," + split[27] + "," + split[2]+ "," + split[7] + "," + split[1] + ";";
					}
				}
			}
		}
		
		return StringUtils.backspace(result);
	}

}
