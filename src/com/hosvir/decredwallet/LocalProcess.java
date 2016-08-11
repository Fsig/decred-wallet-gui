package com.hosvir.decredwallet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * Create a process in a new thread.
 * 
 * @author Troy
 *
 */
public class LocalProcess extends Thread {
	private boolean running;
	private Process process;
    private BufferedReader stdInput;
    private PrintWriter stdOut;
    private String command;
    private String line;
    public ArrayList<String> log;
    private String[] commands;
	
	/**
	 * Construct a new process thread.
	 */
	public LocalProcess(String command) {
		this.command = command;
		this.log = new ArrayList<String>();
		this.setName("JDecredWallet - Process Thread");
		this.setPriority(NORM_PRIORITY);
		this.start();
	}
	
	public void run() {
		try{
			running = true;
			
			if(Constants.isOsLinux()){
	    		commands = new String[]{"/bin/sh","-c", command};
	    	}else if(Constants.isOsWindows()){
	    		commands = new String[]{"cmd","/c", command};
	    	}
			
			process = new ProcessBuilder(commands).start();
	
	        stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        stdOut = new PrintWriter(process.getOutputStream());
	        line = null;
	        
			//While running
			while(running && (line = stdInput.readLine()) != null){
				
				//Save output
				log.add(line);
				if(Constants.guiInterfaces != null && Constants.guiInterfaces.size() > 5) Constants.guiInterfaces.get(5).resize();
	
				try {
					Thread.sleep(50);
				}catch(InterruptedException e) {
					System.out.println("Error sleeping?");
					e.printStackTrace();
				}
			}
			
	        stdInput.close();
	        stdOut.flush();
	        stdOut.close();
	        process.destroy();
		}catch(IOException e){
			running = false;
	        stdOut.flush();
	        stdOut.close();
	        process.destroy();
			e.printStackTrace();
		}
		
		running = false;
	}
	
	public void killProcess(){
		try{
			stdOut.write((char)3);
			stdOut.flush();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}
