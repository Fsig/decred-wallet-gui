package com.hosvir.decredwallet.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author Troy
 *
 */
public class FileUtils {
	private static InputStream stream = null;
	private static OutputStream resStreamOut = null;
	private static int readBytes;
	private static byte[] buffer;
	private static File outFile;
	
	/**
	 * Export the resource to the specified destination.
	 * 
	 * @param name
	 * @param destination
	 * @return Boolean
	 */
	public static boolean exportResource(String name, String destination) {
		stream = null;
		resStreamOut = null;
		buffer = new byte[1024];
		readBytes = -1;
		
        try {
            stream = FileUtils.class.getResourceAsStream(name);
            
            if(stream == null) {
                throw new Exception("Cannot get resource " + name + " from Jar file.");
            }

            outFile = new File(destination + File.separator + name.replaceAll("/resources/lang/", ""));
            if(outFile.exists()) outFile.createNewFile();
            
            resStreamOut = new FileOutputStream(outFile.getAbsolutePath());
            
            while((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
            
            stream.close();
            resStreamOut.close();
            
            return true;
        }catch(Exception e) {
        	e.printStackTrace();
        }
        
        return false;
	}

}
