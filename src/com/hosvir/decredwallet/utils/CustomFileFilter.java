package com.hosvir.decredwallet.utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
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
        if (f.isDirectory()) return true;

        return f.getName().toLowerCase().endsWith(extension);
    }

    @Override
    public String getDescription() {
        return description;
    }
}
