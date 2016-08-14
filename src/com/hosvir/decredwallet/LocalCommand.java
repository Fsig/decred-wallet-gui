package com.hosvir.decredwallet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * 
 * @author Troy
 *
 */
public class LocalCommand {
	private StringBuilder sb = new StringBuilder();
    private String[] commands;
    private Process process;
    private BufferedReader stdInput;
    private BufferedReader stdError;
    private PrintWriter stdOut;
    private String s;

    /**
     * Construct a new Local Command.
     */
    public LocalCommand(){}
    
    /**
     * Execute a command and get the result.
     * 
     * @param command
     * @return String
     */
	public String execute(String command){
		sb.delete(0, sb.length());
        
        try {
        	if(command.contains("dcrctl")) Constants.logWithoutSystem(command.replaceAll("-u '(.*?)'", "-u '***'").replaceAll("-P '(.*?)'", "-P '***'").replaceAll("--walletpass '(.*?)'", "--walletpass '***'").replaceAll("walletpassphrase '(.*?)'", "walletpassphrase '***'"));
        	
        	if(Constants.isOsWindows()){
        		commands = new String[]{"cmd","/c", command};
        	}else{
        		commands = new String[]{"/bin/sh","-c", command};
        	}
        	
        	process = new ProcessBuilder(commands).start();

            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            stdOut = new PrintWriter(process.getOutputStream());
            s = null;
            
            while((s = stdInput.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }

            while((s = stdError.readLine()) != null) {     
            	sb.append(s);
            	sb.append("\n");
            }
            
            stdInput.close();
            stdError.close();
            stdOut.flush();
            stdOut.close();
            process.destroy();
        }catch(IOException e) {
            e.printStackTrace();
        }
        
        return sb.toString();
    }


}
