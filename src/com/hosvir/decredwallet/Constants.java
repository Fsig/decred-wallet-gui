package com.hosvir.decredwallet;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import com.deadendgine.utils.Random;
import com.hosvir.decredwallet.gui.BaseGui;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Main;
import com.hosvir.decredwallet.gui.interfaces.Navbar;
import com.hosvir.decredwallet.utils.FileUtils;
import com.hosvir.decredwallet.utils.FileWriter;
import com.hosvir.decredwallet.utils.JsonObject;
import com.hosvir.decredwallet.utils.JsonObjects;
import com.hosvir.decredwallet.utils.Keystore;
import com.hosvir.decredwallet.websockets.DecredEndpoint;

/**
 * 
 * @author Troy
 * 
 */
public class Constants {
	private static String version;
	private static String buildDate;
	private static String userHome;
	private static String keystore;
	private static String decredLocation;
	private static String daemonBin;
	private static String walletBin;
	private static String dcrctlBin;
	private static String daemonUsername;
	private static String daemonPassword;
	private static String publicPassPhrase;
	private static String privatePassPhrase;
	private static String keystorePassword;
	private static String extraDaemonArguments;
	private static String extraWalletArguments;
	private static String extraDcrctlArguments;
	private static String dcrdFolder;
	private static String dcrwalletFolder;
	private static String osQuote;
	private static String daemonCommand;
	private static String walletCommand;
	private static String dcrctlBaseCommand;
	private static String rpcProtocol;
	private static String rpcProtocolEnd;
	private static String rpcDaemonIp;
	private static String rpcWalletIp;
	private static String rpcDaemonPort;
	private static String rpcWalletPort;
	private static File settingsFile;
	private static File langFolder;
	private static File dcrdCert;
	private static File walletCert;
	
	private static boolean enableOpenGL;
	private static boolean enableFps;
	private static boolean debug;
	
	private static ArrayList<String> allowedPasswordClasses;
	
	private static Throwable throwable; 
	private static StackTraceElement[] elements; 
	private static String callerClassName; 
	
	private static Date date;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy - hh:mm:ss a");
	private static SimpleDateFormat wsdf = new SimpleDateFormat("MMM dd:hh!mm!ss a");
	
	private static LocalProcess daemonProcess;
	private static LocalProcess walletProcess;
	private static DecredEndpoint dcrdEndpoint;
	private static DecredEndpoint dcrwalletEndpoint;
	private static boolean daemonProcessReady, walletProcessReady, daemonReady, walletReady, requirePublicPass, testnet;
	private static Main mainGui;
	
	private static Random random;
	private static ArrayList<String> langFiles;
	public static String langFile;
	
	private static Properties properties;
	
	public static int doubleClickDelay = 400;
	public static int scrollDistance = 30;
	public static int maxLogLines = 500;
	public static int fpsMax = 30;
	public static int fpsMin = 1;
	
	private static Clipboard clipboard;
	public static ArrayList<String> guiLog = new ArrayList<String>();
	public static ArrayList<Account> accounts = new ArrayList<Account>();
	public static ArrayList<String> accountNames = new ArrayList<String>();
	public static ArrayList<BaseGui> guiInterfaces = new ArrayList<BaseGui>();
	public static ArrayList<Contact> contacts = new ArrayList<Contact>();
	public static ArrayList<String> langConfFiles = new ArrayList<String>();
	public static HashMap<String, String> langValues = new HashMap<String, String>();
	public static ArrayList<String> stakePools = new ArrayList<String>();
	public static Navbar navbar;
	public static GlobalCache globalCache;
	public static String accountToRename;

	
	/**
	 * Initialise constants.
	 */
	public static void initialise() {
		version = "0.2.6";
		buildDate = "11/08/2016";
		random = new Random();
		guiLog = new ArrayList<String>();
		langFiles = new ArrayList<String>();
		
		System.out.println("Created by Fsig.");
		System.out.println("Version: " + Constants.getVersion());
		System.out.println("Build date: " + Constants.getBuildDate() + "\n");
		Constants.guiLog.add("Created by Fsig.");
		Constants.guiLog.add("Version: " + Constants.getVersion());
		Constants.guiLog.add("Build date: " + Constants.getBuildDate() + "\n");
		
		//OS is Windows... poor fella
		if(isOsWindows()){
			osQuote = "\"";
			daemonBin = "dcrd.exe";
			walletBin = "dcrwallet.exe";
			dcrctlBin = "dcrctl.exe";
			dcrdFolder = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Dcrd";
			dcrwalletFolder = System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator +  "Dcrwallet";
		}else{
			osQuote = "'";
			daemonBin = "dcrd";
			walletBin = "dcrwallet";
			dcrctlBin = "dcrctl";
			dcrdFolder = System.getProperty("user.home") + File.separator + ".dcrd";
			dcrwalletFolder = System.getProperty("user.home") + File.separator + ".dcrwallet";
		}
		
		extraDaemonArguments = "";
		extraWalletArguments = "";
		extraDcrctlArguments = "";
		
		//Add stake pools (This wont stop users from joining any pool)
		stakePools.add("pos-bravo");
		stakePools.add("pos-delta");
		stakePools.add("pos-echo");
		stakePools.add("pos-foxtrot");
		stakePools.add("pos-golf");
		stakePools.add("pos-hotel");
		stakePools.add("pos-india");	
		stakePools.add("pos-juliett");
		
		
		userHome = System.getProperty("user.home") + File.separator + "DecredWallet" + File.separator;
		keystore = userHome + "decredWalletKeystore";
		settingsFile = new File(userHome + "settings.conf");
		langFolder = new File(userHome + File.separator + "lang");
		dcrdCert = new File(dcrdFolder + File.separator + "rpc.cert");
		walletCert = new File(dcrwalletFolder + File.separator + "rpc.cert");
		allowedPasswordClasses = new ArrayList<String>();
		allowedPasswordClasses.add("com.hosvir.decredwallet.DecredWallet");
		allowedPasswordClasses.add("com.hosvir.decredwallet.Api");
		allowedPasswordClasses.add("com.hosvir.decredwallet.utils.Keystore");
		allowedPasswordClasses.add("com.hosvir.decredwallet.gui.interfaces.Login");
		
		langConfFiles.add("English.conf");
		langConfFiles.add("German.conf");
		langConfFiles.add("Chinese.conf");
		langConfFiles.add("Japanese.conf");
		langConfFiles.add("Spanish.conf");
		langConfFiles.add("Russian.conf");
		
		properties = new Properties();
		
		//Create default settings
		if(!settingsFile.exists()) {
			createDefaultProperties();
		}else{
			try{
				properties.load(new FileInputStream(new File(userHome + ".version.conf")));
				String fileVersion = properties.getProperty("Version");
				
				if(!fileVersion.startsWith(version)){
					log("Version mismatch, GUI: " + version + ", File: " + fileVersion);
					updateConfFiles();
				}
			}catch(Exception e){
				log("Missing version file, it is recommended that you delete your settings.conf and allow the program to create a new settings file for you.");
				FileWriter.writeToFile(userHome + ".version.conf", "Version=" + version, false, false);
			}
		}

		//Create required folders
		createDefaultLanguages();
		
		//Get lang files
		for(File f : langFolder.listFiles()){
			langFiles.add(f.getName());
		}
		
		try{
			properties.load(new FileInputStream(settingsFile));
			
			decredLocation = properties.getProperty("Decred-Location");
			daemonUsername = properties.getProperty("Daemon-Username");
			daemonPassword = properties.getProperty("Daemon-Password");
			publicPassPhrase = properties.getProperty("Public-Password");
			testnet = Boolean.valueOf(properties.getProperty("Testnet"));
			enableOpenGL = Boolean.valueOf(properties.getProperty("Enable-OpenGL"));
			enableFps = Boolean.valueOf(properties.getProperty("Enable-FPS"));
			langFile = properties.getProperty("Language");
			doubleClickDelay = Integer.valueOf(properties.getProperty("Double-Click-Delay"));
			scrollDistance = Integer.valueOf(properties.getProperty("Scroll-Distance"));
			maxLogLines = Integer.valueOf(properties.getProperty("Max-Log-Lines"));
			keystorePassword = properties.getProperty("Keystore-Password");
			fpsMax = Integer.valueOf(properties.getProperty("FPS-Max"));
			fpsMin = Integer.valueOf(properties.getProperty("FPS-Min"));
			debug = Boolean.valueOf(properties.getProperty("Debug"));

			if(!decredLocation.endsWith(File.separator)) decredLocation += File.separator;
			
			if(!new File(decredLocation).exists()){
				log("Decred Location: '" + decredLocation + "' does not exist, update settings.conf");
				
				//Notify user about options as they may not have the process attached to a terminal to see output
				JOptionPane.showMessageDialog(null, 
						"Decred Location: '" + decredLocation + "' does not exist, update settings.conf",
						"Decred Wallet",
						JOptionPane.INFORMATION_MESSAGE);
				
				System.exit(1);
			}
			
			
			//Import local certificates
			if(isOsWindows()){
				if(new File(System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Dcrd" + File.separator + "rpc.cert").exists()){
					Keystore.importCertificate(System.getProperty("user.home") + File.separator + 
							"AppData" + File.separator + 
							"Local" + File.separator + 
							"Dcrd" + File.separator + 
							"rpc.cert", "dcrd", keystore);
					Keystore.importCertificate(System.getProperty("user.home") + File.separator + 
							"AppData" + File.separator + 
							"Local" + File.separator + 
							"Dcrwallet" + File.separator + 
							"rpc.cert", "dcrwallet", keystore);
				}
			}else{
				if(new File(System.getProperty("user.home") + File.separator + ".dcrd" + File.separator + "rpc.cert").exists()){
					Keystore.importCertificate(System.getProperty("user.home") + File.separator + 
							".dcrd" + File.separator + 
							"rpc.cert", "dcrd", keystore);
					Keystore.importCertificate(System.getProperty("user.home") + File.separator + 
							".dcrwallet" + File.separator + 
							"rpc.cert", "dcrwallet", keystore);
				}
				
				dcrdFolder = System.getProperty("user.home") + File.separator + ".dcrd";
				dcrwalletFolder = System.getProperty("user.home") + File.separator + ".dcrwallet";
			}
			
			//Change Key store		
			Keystore.loadKeystore(userHome + "decredWalletKeystore");
			System.setProperty("javax.net.ssl.trustStore", userHome + "decredWalletKeystore");
			System.setProperty("javax.net.ssl.trustStorePassword", keystorePassword);
			
			//Check for public pass
			if(publicPassPhrase != null && publicPassPhrase.length() > 1){
				requirePublicPass = true;
				extraWalletArguments += " --walletpass " + osQuote + publicPassPhrase + osQuote;
			}
			
			//Check for testnet
			if(testnet){
				extraDaemonArguments += " --testnet";
				extraWalletArguments += " --testnet";
				extraDcrctlArguments += " --testnet";
			}
		}catch(IOException e){
			System.out.println("Unable to find settings file.");
			e.printStackTrace();
		}
		
		
		//Web socket URIs
		rpcProtocol = "wss://";
		rpcProtocolEnd = "/ws";
		rpcDaemonPort = testnet ? "19109" : "9109";
		rpcWalletPort = testnet ? "19110" : "9110";
		rpcDaemonIp = "127.0.0.1";
		rpcWalletIp = "127.0.0.1";
		
		
		daemonCommand = osQuote + decredLocation + daemonBin + osQuote + " -u " + osQuote + 
						daemonUsername + osQuote + " -P " + osQuote +
						daemonPassword + osQuote + 
						extraDaemonArguments;
		walletCommand = osQuote + decredLocation + walletBin + osQuote + " -u " + osQuote +
						daemonUsername + osQuote + " -P " + osQuote +
						daemonPassword + osQuote + 
						extraWalletArguments;
		dcrctlBaseCommand = osQuote + decredLocation + dcrctlBin + osQuote + " -u " + osQuote +
						daemonUsername + osQuote + " -P " + osQuote +
						daemonPassword + osQuote +
						extraDcrctlArguments;
		
		clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		
		accounts = new ArrayList<Account>();
		guiInterfaces = new ArrayList<BaseGui>();
		
		globalCache = new GlobalCache();
		
		
		//Load language
		reloadLanguage();
		
		//Load contacts
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(userHome + "contacts.data")));
			String line = "";
			String[] splitLine;
			int count = 0;

			while((line = input.readLine()) != null){
				if(line.length() > 3){
					splitLine = line.split(":");
					contacts.add(new Contact(count, splitLine[0], splitLine[1], splitLine[2]));
					
					count++;
				}
			}
			
			input.close();
		}catch(Exception e){
			log("Unable to load contacts.");
			
			//Display error to user
			JOptionPane.showMessageDialog(null, 
					"Error: Unable to load contacts.",
					"Decred Wallet",
					JOptionPane.ERROR_MESSAGE);
			
			e.printStackTrace();
		}
	}

	/**
	 * Reload the language from file
	 */
	public static void reloadLanguage() {
		try{
			langValues.clear();
			properties.clear();
			properties.load(new FileInputStream(new File(langFolder + File.separator + langFile + ".conf")));
			
			for(Map.Entry<Object, Object> e : properties.entrySet()){
				langValues.put((String) e.getKey(),(String) e.getValue());
			}
		}catch(Exception e){
			log("Error loading language.");
			e.printStackTrace();
		}
	}

	/**
	 * Create default properties
	 */
	private static void createDefaultProperties() {
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#Decred settings", false, false);
		
		if(isOsWindows()){
			FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Decred-Location=C:/users/user/decred/", true, true);
		}else{
			FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Decred-Location=/home/ubuntu/decred/", true, true);
		}
		
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Daemon-Username=username", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Daemon-Password=password", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Public-Password=", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Testnet=false", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#GUI settings", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Language=English", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Double-Click-Delay=400", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Scroll-Distance=30", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Max-Log-Lines=500", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Keystore-Password=" + generateRandomString(32), true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#Display settings", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Enable-OpenGL=true", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Enable-FPS=false", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "FPS-Max=30", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "FPS-Min=10", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Debug=false", true, true);
		
		//Create version file
		FileWriter.writeToFile(userHome + ".version.conf", "Version=" + version, false, false);
		
		//Create contacts file
		FileWriter.writeToFile(userHome + "contacts.data", "Donate:fsig@hmamail.com:DsmcWt82aeraJ22bayUtMXm8dyRL8bFnBVY", false, false);
		
		//Create lang folder
		langFolder.mkdir();
		
		//Create Key store
		Keystore.createNewKeystore(userHome + "decredWalletKeystore");
	
		log("Default properties file has been created, edit settings.conf and then restart the program.");
		
		//Notify user about options as they may not have the process attached to a terminal to see output
		JOptionPane.showMessageDialog(null, 
				"Default properties file has been created, edit settings.conf and then restart the program.",
				"Decred Wallet",
				JOptionPane.INFORMATION_MESSAGE);
		
		//Exit
		System.exit(1);
	}
	
	/**
	 * Create default properties
	 */
	private static void createDefaultLanguages() {
		for(String s : langConfFiles){
			if(!new File(langFolder.getPath() + File.separator + s).exists()){
				FileUtils.exportResource("/resources/lang/" + s, langFolder.getPath());
				log("Added new Language file: " + s);
			}	
		}
	}
	
	/**
	 * Save the settings to file
	 */
	public static void saveSettings() {
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#Decred settings", false, false);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Decred-Location=" + decredLocation, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Daemon-Username=" + daemonUsername, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Daemon-Password=" + daemonPassword, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Public-Password=" + publicPassPhrase, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Testnet=" + testnet, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#GUI settings", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Language=" + langFile, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Double-Click-Delay=" + doubleClickDelay, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Scroll-Distance=" + scrollDistance, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Max-Log-Lines=" + maxLogLines, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Keystore-Password=" + keystorePassword, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "#Display settings", true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Enable-OpenGL=" + enableOpenGL, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Enable-FPS=" + enableFps, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "FPS-Max=" + fpsMax, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "FPS-Min=" + fpsMin, true, true);
		FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Debug=" + debug, true, true);
	}
	
	/**
	 * Update the conf files.
	 */
	public static void updateConfFiles() {
		//Add any new changes to conf files here after the first release.
		for(String s : langConfFiles){
			File file = new File(langFolder.getPath() + File.separator + s);
			if(file.exists()){
				if(file.delete()){
					log("Deleted outdated Language file: " + file.getName());
				}else{
					log("Failed to delete outdated Language file: " + file.getName());
				}
			}
		}
		
		//Create contacts file
		if(!new File(userHome + "contacts.data").exists()){
			FileWriter.writeToFile(userHome + "contacts.data", "Donate:fsig@hmamail.com:DsmcWt82aeraJ22bayUtMXm8dyRL8bFnBVY", false, false);
		}
		
		//Add debug flag
		try{
			properties.load(new FileInputStream(settingsFile));
			
			//New debug setting
			if(properties.getProperty("Debug") == null){
				FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Debug=false", true, true);
			}
			
			//New key store setting
			if(properties.getProperty("Keystore-Password") == null){
				keystorePassword = generateRandomString(32);
				FileWriter.writeToFile(settingsFile.getAbsolutePath(), "Keystore-Password=" + keystorePassword, true, true);

				//Create Key store
				Keystore.createNewKeystore(userHome + "decredWalletKeystore");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Last, update version
		FileWriter.writeToFile(userHome + ".version.conf", "Version=" + version, false, false);
	}
	
	/**
	 * Log a message
	 * 
	 * @param message
	 */
	public synchronized static void log(String message) {
		System.out.println(getDate() + ": " + message);
		guiLog.add(getDate() + ": " + message);
		
		if(guiInterfaces.size() > 5) guiInterfaces.get(5).resize();
	}
	
	/**
	 * Log a message
	 * 
	 * @param message
	 */
	public synchronized static void logWithoutSystem(String message) {
		guiLog.add(getDate() + ": " + message);
		
		if(guiInterfaces.size() > 5) guiInterfaces.get(5).resize();
	}
	
	/**
	 * Get lang value.
	 * 
	 * @param key
	 * @return String
	 */
	public static String getLangValue(String key) {
		for(Map.Entry<String, String> e : langValues.entrySet()){
			if(e.getKey().toLowerCase().equals(key.toLowerCase()))
				return e.getValue();
		}
		
		return "Missing lang conf";
	}
	
	/**
	 * Unselect the input boxes.
	 * 
	 * @param ibb
	 */
	public static synchronized void unselectOtherInputs(ArrayList<Component> components, Component exclude) {
		for(Component c : components)
			if(c instanceof InputBox)
				if(c != exclude) c.selectedId = -1;
	}
	
	/**
	 * Unselect all input boxes.
	 * 
	 * @param ibb
	 */
	public static synchronized void unselectAllInputs(ArrayList<Component> components) {
		for(Component c : components)
			if(c instanceof InputBox)
				c.selectedId = -1;
	}
	
	/**
	 * Check if the calling class is allowed to access the username / password.
	 * @return
	 */
	private static boolean allowedPasswordClass() {
		throwable = new Throwable(); 
		elements = throwable.getStackTrace(); 
		callerClassName = elements[2].getClassName(); 

		return allowedPasswordClasses.contains(callerClassName) ? true : false;
	}
	
	/**
	 * Get the date.
	 * 
	 * @return String
	 */
	public static String getDate(){
		date = new Date();
		return sdf.format(date);
	}
	
	/**
	 * Get the date.
	 * 
	 * @return String
	 */
	public static String getWalletDate(Long timestamp){
		date = new Date();
		date.setTime(timestamp*1000);
		return wsdf.format(date);
	}
	
	/**
	 * Format the timestamp into a date
	 * 
	 * @param timestamp
	 * @return String
	 */
	public static String formatDate(Long timestamp){
		date = new Date();
		date.setTime(timestamp*1000);
		return sdf.format(date);
	}
	
	/**
	 * Format the time into a readable string.
	 * 
	 * @param l
	 * @return String
	 */
	public static String formatTime(long l){
		DecimalFormat dec = new DecimalFormat("#.##");
		String result = "";
		
		if(l > 1000 && l < 60000){ //Seconds
			result += dec.format((double)l / 1000) + " Seconds";
		}else if(l > 60000 && l < 3600000){ //Minutes
			result += dec.format((double)l / 60000) + " Minutes";
		}else if(l > 3600000){ //Hours
			result += dec.format((double)l / 3600000) + " Hours";
		}else{
			result += dec.format((double) l) + " Milliseconds";
		}
		
		return result;
	}
	
	/**
	 * Add new contact
	 * 
	 * @param c
	 */
	public synchronized static void addContact(Contact c) {
		contacts.add(c);
		saveContacts();
	}
	
	/**
	 * Remove a contact 
	 * 
	 * @param c
	 */
	public synchronized static void removeContact(Contact c) {
		contacts.remove(c);
		saveContacts();
	}
	
	/**
	 * Save contacts to file
	 */
	public synchronized static void saveContacts() {
		FileWriter.writeToFile(userHome + "contacts.data", "", false, false);
		
		for(Contact c : contacts){
			FileWriter.writeToFile(userHome + "contacts.data", c.getName() + ":" + c.getEmail() + ":" + c.getAddress(), true, true);
		}
	}
	
	/**
	 * Generate a random string.
	 * 
	 * @param length
	 * @return String
	 */
	public static String generateRandomString(int length) {
		String characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder(length);
		Random random = new Random();
		
		for(int i = 0; i < length; i++){
			sb.append(characters.charAt(random.nextInt(characters.length())));
		}
		
		return sb.toString();
	}
	
	/**
	 * Get the Operating System name, architecture and version.
	 * 
	 * @return String
	 */
	private static String getOS(){
		return System.getProperty("os.name") + ", " + System.getProperty("os.arch") + ", " + System.getProperty("os.version");
	}
	
	public static boolean isOsWindows() {
		return getOS().toLowerCase().contains("windows");
	}
	
	public static boolean isOsLinux() {
		return getOS().toLowerCase().contains("linux");
	}
	
	/**
	 * Get the JVM architecture
	 * 
	 * @return String
	 */
	public static String getOSArch(){
		return System.getProperty("os.arch");
	}
	
	/**
	 * Get the current User.
	 * 
	 * @return String
	 */
	public static String getUser(){
		return System.getProperty("user.name");
	}
	
	/**
	 * Get a random number between the min and max values.
	 * 
	 * @param min
	 * @param max
	 * @return Integer
	 */
	public static int getRandomNumber(int min, int max) {
		return random.random(min, max);
	}
	
	/**
	 * Get the clipboard contents.
	 * 
	 * @return Clipboard string data
	 */
	public static String getClipboardString() {
		try{
			return (String) clipboard.getContents(null).getTransferData(DataFlavor.stringFlavor);
		}catch(Exception e){
			return "";
		}
	}
	
	/**
	 * Set the clipboard contents.
	 * 
	 * @param text
	 */
	public static void setClipboardString(String text) {
		clipboard.setContents(new StringSelection(text), null);
	}
	
	public static void reloadAccounts() {
		for(Account a : accounts) a.setRunning(false);
		
		accounts.clear();
		accountNames.clear();
		
		for(JsonObjects jos : Api.getAccounts().get(0).getJsonObjects()){
			if(!jos.getName().trim().equals("result")){
				accounts.add(new Account(jos.getName()));
				accountNames.add(jos.getName());
			}
		}
		
		//Reset rectangles for interfaces
		guiInterfaces.get(0).rectangles = null;
		guiInterfaces.get(3).rectangles = null;
		guiInterfaces.get(4).rectangles = null;
	}
	
	public static void forceUpdateAllAccounts() {
		for(Account a : accounts) a.forceUpdate = true;
	}
	
	public static void blockInterfaces(boolean block, BaseGui gui) {
		navbar.blockInput = block;
		
		for(BaseGui b : guiInterfaces)
			if(b != gui)
				b.blockInput = block;
	}
	
	//Temporary method to sort transactions by account until Decred implements this.
	public static ArrayList<JsonObject> getTransactionsByAccount(String name) {
		boolean isDefault = false;
		if(Constants.accounts.get(0).name.trim().equals(name.trim())) isDefault = true;
		ArrayList<JsonObject> transactions = new ArrayList<JsonObject>();
		
		if(name.trim().equals("imported")) {
			for(int i = 0; i < Constants.globalCache.transactions.size() -3; i++){
				if(Constants.globalCache.transactions.get(i).getValueByName("account").trim().equals(name.trim()) || 
						(Constants.globalCache.transactions.get(i).getValueByName("txtype").trim().equals("vote") || 
								Constants.globalCache.transactions.get(i).getValueByName("txtype").trim().equals("ticket")))
					
					if(Constants.globalCache.transactions.get(i).getValueByName("address") != null)
						transactions.add(Constants.globalCache.transactions.get(i));
			}
		}else{
			for(int i = 0; i < Constants.globalCache.transactions.size() -3; i++){
				if(Constants.globalCache.transactions.get(i).getValueByName("account").trim().equals(name.trim()) && 
						!Constants.globalCache.transactions.get(i).getValueByName("txtype").trim().equals("vote") && 
						!Constants.globalCache.transactions.get(i).getValueByName("txtype").trim().equals("ticket") || 
						(isDefault && Constants.globalCache.transactions.get(i).getValueByName("account").trim().length() == 0 && 
						!Constants.globalCache.transactions.get(i).getValueByName("txtype").trim().equals("vote") && 
						!Constants.globalCache.transactions.get(i).getValueByName("txtype").trim().equals("ticket")))
					transactions.add(Constants.globalCache.transactions.get(i));
			}
		}
		
		
			
		return transactions;
	}
	
	public static Account getAccountByName(String name) {
		for(Account a : accounts){
			if(a.name.trim().equals(name.trim()))
				return a;
		}
		
		return null;
	}
	
	/**
	 * Open the specified URL.
	 * 
	 * @param url
	 */
	public static void openLink(String url) {
		if(Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static boolean isPrivatePassphraseSet() {
		if(privatePassPhrase != null && privatePassPhrase != "") return true;
		
		return false;
	}

	public static String getVersion() {
		return version;
	}

	public static String getBuildDate() {
		return buildDate;
	}

	public static String getUserHome() {
		return userHome;
	}
	
	public static String getDecredLocation() {
		return decredLocation;
	}
	
	public static LocalProcess getDaemonProcess() {
		return daemonProcess;
	}

	public static void setDaemonProcess(LocalProcess daemonProcess) {
		Constants.daemonProcess = daemonProcess;
	}

	public static LocalProcess getWalletProcess() {
		return walletProcess;
	}

	public static void setWalletProcess(LocalProcess walletProcess) {
		Constants.walletProcess = walletProcess;
	}

	public static boolean isDaemonReady() {
		return daemonReady;
	}

	public static void setDaemonReady(boolean daemonReady) {
		Constants.daemonReady = daemonReady;
	}

	public static boolean isWalletReady() {
		return walletReady;
	}

	public static void setWalletReady(boolean walletReady) {
		Constants.walletReady = walletReady;
	}
	
	public static boolean isRequirePublicPass(){
		return requirePublicPass;
	}
	
	public static void setRequirePublicPass(boolean requirePublicPass){
		Constants.requirePublicPass = requirePublicPass;
	}

	public static Main getMainGui() {
		return mainGui;
	}

	public static void setMainGui(Main mainGui) {
		Constants.mainGui = mainGui;
	}

	public static String getDaemonUsername() {
		return allowedPasswordClass() ? daemonUsername : null;
	}

	public static String getDaemonPassword() {
		return allowedPasswordClass() ? daemonPassword : null;
	}
	
	public static String getPublicPassPhrase() {
		return allowedPasswordClass() ? publicPassPhrase : null;
	}

	public static void setPublicPassPhrase(String publicPassPhrase) {
		Constants.publicPassPhrase = publicPassPhrase;
	}

	public static String getPrivatePassPhrase() {
		return allowedPasswordClass() ? privatePassPhrase : null;
	}

	public static void setPrivatePassPhrase(String privatePassPhrase) {
		Constants.privatePassPhrase = privatePassPhrase;
	}

	public static String getExtraDaemonArguments() {
		return extraDaemonArguments;
	}

	public static String getExtraWalletArguments() {
		return extraWalletArguments;
	}
	
	public static String getExtraDcrctlArguments() {
		return extraDcrctlArguments;
	}
	
	public static String getDaemonCommand() {
		return allowedPasswordClass() ? daemonCommand : null;
	}

	public static String getWalletCommand() {
		return allowedPasswordClass() ? walletCommand : null;
	}
	
	public static String getDcrctlBaseCommand() {
		return allowedPasswordClass() ? dcrctlBaseCommand : null;
	}

	public static File getDcrdCert() {
		return dcrdCert;
	}

	public static File getWalletCert() {
		return walletCert;
	}

	public static Clipboard getClipboard() {
		return clipboard;
	}

	public static boolean isEnableOpenGL() {
		return enableOpenGL;
	}

	public static boolean isEnableFps() {
		return enableFps;
	}
	
	public static ArrayList<String> getLangFiles() {
		return langFiles;
	}
	
	public static boolean isContact(String name) {
		for(Contact c : contacts) 
			if(c.getName().toLowerCase().trim().contains(name.toLowerCase().trim()))
				return true;
		
		return false;		
	}
	
	public static Contact getContact(String name) {
		for(Contact c : contacts) 
			if(c.getName().toLowerCase().trim().contains(name.toLowerCase().trim()))
				return c;
		
		return null;		
	}
	
	public static String getOsQuote() {
		return osQuote;
	}

	public static DecredEndpoint getDcrdEndpoint() {
		return dcrdEndpoint;
	}

	public static void setDcrdEndpoint(DecredEndpoint dcrdEndpoint) {
		Constants.dcrdEndpoint = dcrdEndpoint;
	}
	
	public static void setDcrdEnpointURI(String uri){
		rpcDaemonIp = uri;
		dcrdEndpoint.uri = rpcProtocol + rpcDaemonIp + ":" + rpcDaemonPort + rpcProtocolEnd;
	}

	public static DecredEndpoint getDcrwalletEndpoint() {
		return dcrwalletEndpoint;
	}

	public static void setDcrwalletEndpoint(DecredEndpoint dcrwalletEndpoint) {
		Constants.dcrwalletEndpoint = dcrwalletEndpoint;
	}
	
	public static void setDcrwalletEnpointURI(String uri){
		rpcWalletIp = uri;
		dcrwalletEndpoint.uri = rpcProtocol + rpcWalletIp + ":" + rpcWalletPort + rpcProtocolEnd;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		Constants.debug = debug;
	}

	public static String getRpcProtocol() {
		return rpcProtocol;
	}

	public static void setRpcProtocol(String rpcProtocol) {
		Constants.rpcProtocol = rpcProtocol;
	}

	public static String getRpcProtocolEnd() {
		return rpcProtocolEnd;
	}

	public static void setRpcProtocolEnd(String rpcProtocolEnd) {
		Constants.rpcProtocolEnd = rpcProtocolEnd;
	}

	public static String getRpcDaemonIp() {
		return rpcDaemonIp;
	}

	public static void setRpcDaemonIp(String rpcDaemonIp) {
		Constants.rpcDaemonIp = rpcDaemonIp;
	}

	public static String getRpcWalletIp() {
		return rpcWalletIp;
	}

	public static void setRpcWalletIp(String rpcWalletIp) {
		Constants.rpcWalletIp = rpcWalletIp;
	}

	public static String getRpcDaemonPort() {
		return rpcDaemonPort;
	}

	public static void setRpcDaemonPort(String rpcDaemonPort) {
		Constants.rpcDaemonPort = rpcDaemonPort;
	}

	public static String getRpcWalletPort() {
		return rpcWalletPort;
	}

	public static void setRpcWalletPort(String rpcWalletPort) {
		Constants.rpcWalletPort = rpcWalletPort;
	}

	public static String getKeystorePassword() {
		return allowedPasswordClass() ? keystorePassword : null;
	}

	public static boolean isDaemonProcessReady() {
		return daemonProcessReady;
	}

	public static void setDaemonProcessReady(boolean daemonProcessReady) {
		Constants.daemonProcessReady = daemonProcessReady;
	}

	public static boolean isWalletProcessReady() {
		return walletProcessReady;
	}

	public static void setWalletProcessReady(boolean walletProcessReady) {
		Constants.walletProcessReady = walletProcessReady;
	}

	public static String getKeystore() {
		return keystore;
	}
	
}
