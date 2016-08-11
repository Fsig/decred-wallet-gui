package com.hosvir.decredwallet.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import com.hosvir.decredwallet.Constants;

/**
 * 
 * @author Troy
 *
 */
public class Keystore {
	private static KeyStore ks;
	
	/**
	 * Create a blank Key store.
	 * 
	 * @param fileName
	 */
    public static void createNewKeystore(String keystore) {
        try {
        	ks = KeyStore.getInstance(KeyStore.getDefaultType());

    		char[] password = Constants.getKeystorePassword().toCharArray();
    		ks.load(null, password);

    		FileOutputStream fos = new FileOutputStream(keystore);
    		ks.store(fos, password);
    		fos.close();
    		
    		Thread.sleep(500);
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
	/**
	 * Load the specified Key store.
	 * 
	 * @param fileName
	 */
    public static void loadKeystore(String keystore) {
        try {
        	ks = KeyStore.getInstance(KeyStore.getDefaultType());

    		char[] password = Constants.getKeystorePassword().toCharArray();
    		ks.load(null, password);

    		FileInputStream fis = new FileInputStream(keystore);
    		ks.load(fis, password);
    		fis.close();
        }catch(Exception e) {
        	e.printStackTrace();
        }
    }
    
    /**
     * Import the specified certificate into the Key store.
     * 
     * @param fileName
     */
    public static boolean importCertificate(String certificate, String certificateAlias, String keystore) {
    	try{
    		 FileInputStream certIs = new FileInputStream(certificate);
    		 char[] password = Constants.getKeystorePassword().toCharArray();

    		 //Check if the certificate is already imported
    		 if(containsAlias(certificateAlias, keystore)){
    			 certIs.close();
    			 return true;
    		 }

    		 BufferedInputStream bis = new BufferedInputStream(certIs);
    		 CertificateFactory cf = CertificateFactory.getInstance("X.509");
    		 while(bis.available() > 0) {
    			 Certificate cert = cf.generateCertificate(bis);
    			 ks.setCertificateEntry(certificateAlias, cert);
    		 }

    		 certIs.close();

    		 OutputStream out = new FileOutputStream(keystore);
    		 ks.store(out, password);
    		 out.close();
    		 
    		 return true;
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	return false;
    }
    
    /**
     * Check if the specified Key store contains the specified certificate alias.
     * 
     * @param certificateAlias
     * @param keystore
     * @return boolean
     */
    public static boolean containsAlias(String certificateAlias, String keystore) {
    	try{    		
	    	ks = KeyStore.getInstance(KeyStore.getDefaultType());
	
			char[] password = Constants.getKeystorePassword().toCharArray();
			ks.load(null, password);
	
			FileInputStream fis = new FileInputStream(keystore);
			ks.load(fis, password);
			
			if(ks.containsAlias(certificateAlias)) {
				fis.close();
				return true;
			 }
			
			fis.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	return false;
    }

}
