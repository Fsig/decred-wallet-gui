package com.hosvir.decredwallet.utils;

/**
 * 
 * @author Troy
 *
 */
public class JsonObjects {
	private String name;
	private String value;
	
	public JsonObjects(String name, String value){
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
