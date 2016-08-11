package com.hosvir.decredwallet.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Troy
 *
 */
public class Json {
	public static String RESULT_PATTERN = "\"result\":(.*?),\"error\":(.*?),\"id\":(.*?)[\\}]";
	private static Pattern pattern;
	private static Matcher regexMatcher;
	private static ArrayList<JsonObject> jsonObjects;
	
	public static ArrayList<JsonObject> parseJson(String string) {
		jsonObjects = new ArrayList<JsonObject>();
		if(string == null) return jsonObjects;
		
		
		pattern = Pattern.compile(RESULT_PATTERN);
		regexMatcher = pattern.matcher(string);
		if(regexMatcher.find()){ 
			for(int i = 1; i < regexMatcher.groupCount()+1; i++){
				//System.out.println("GROUP " + i + ": " + regexMatcher.group(i));
				processString(regexMatcher.group(i));
			}
		}else{
			System.out.println("No pattern found, malformed Json String.");
		}

		return jsonObjects;
	}
	
	private static void processString(String string) {
		JsonObject jo = new JsonObject();
		String[] splitString;
		String[] splitLines;
		String[] splitLine;
		
		if(!regexMatcher.group(1).contains("}")){
			//Split objects
			splitString = string.split(",\"");
		}else{
			//Split objects
			splitString = string.split("}");
		}
		
		//Check if there are named results
		if(!splitString[0].contains(":")){
			jo = new JsonObject();
			jo.addJsonObject(new JsonObjects("result",cleanString(string)));
			jsonObjects.add(jo);
		}else{
			//Append Json objects
			for(String s : splitString){
				jo = new JsonObject();
				//splitLines = s.split(",\\[?![^()]*\\)\\]");
				splitLines = s.split(",(?=[^\\]]*(?:\\[|$))");
				//splitLines = s.split(",");
				

				for(String ss : splitLines){
					splitLine = ss.split("\":");
					
					if(splitLine.length > 1){
						String name = cleanString(splitLine[0]);
						String value = cleanString(splitLine[1]);
							
						jo.addJsonObject(new JsonObjects(name,value));
					}
				}
				
				jsonObjects.add(jo);
			}
		}
	}
	
	private static String cleanString(String string){
		return string.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\{", "").replaceAll("\\}", "").trim();
	}

}
