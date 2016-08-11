package com.hosvir.decredwallet.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Troy
 *
 */
public class CustomFileFilter extends FileFilter {
	private String extension;
	private String description;
	
	public CustomFileFilter(String extension, String description) {
		this.extension = extension;
		this.description = description;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()) return true;
		
		return f.getName().toLowerCase().endsWith(extension);
	}

	@Override
	public String getDescription() {
		return description;
	}

}
