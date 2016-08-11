package com.hosvir.decredwallet.utils;

/**
 * 
 * @author Troy
 *
 */
public class Param {
	private int type;
	private String name;
	private String value;
	
	public Param(int type, String value) {
		this.type = type;
		this.name = "";
		this.value = value;
	}
	
	public Param(int type, String name, String value) {
		this.type = type;
		this.name = name;
		this.value = value;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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
