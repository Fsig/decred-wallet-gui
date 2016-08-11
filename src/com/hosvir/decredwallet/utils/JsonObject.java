package com.hosvir.decredwallet.utils;

import java.util.ArrayList;

/**
 * 
 * @author Troy
 *
 */
public class JsonObject {
	private ArrayList<JsonObjects> jsonObjects;
	
	public JsonObject(){
		this.jsonObjects = new ArrayList<JsonObjects>();
	}

	public void addJsonObject(JsonObjects jo) {
		this.jsonObjects.add(jo);
	}
	
	public JsonObjects getByName(String name) {
		for(JsonObjects jo : this.jsonObjects)
			if(jo.getName().toLowerCase().trim().startsWith(name.toLowerCase().trim()))
				return jo;
		
		return null;	
	}
	
	public String getValueByName(String name) {
		for(JsonObjects jo : this.jsonObjects)
			if(jo.getName().toLowerCase().trim().startsWith(name.toLowerCase().trim()))
				return jo.getValue();
		
		return null;	
	}
	
	public int getObjectCount() {
		return this.jsonObjects.size();
	}
	
	public ArrayList<JsonObjects> getJsonObjects() {
		return this.jsonObjects;
	}

}
