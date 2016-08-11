package com.hosvir.decredwallet.gui.interfaces;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import com.deadendgine.Engine;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.LocalProcess;
import com.hosvir.decredwallet.Processes;
import com.hosvir.decredwallet.gui.Button;
import com.hosvir.decredwallet.gui.ColorConstants;
import com.hosvir.decredwallet.gui.Component;
import com.hosvir.decredwallet.gui.Dialog;
import com.hosvir.decredwallet.gui.FontConstants;
import com.hosvir.decredwallet.gui.Images;
import com.hosvir.decredwallet.gui.InputBox;
import com.hosvir.decredwallet.gui.Interface;
import com.hosvir.decredwallet.gui.LinkLabel;
import com.hosvir.decredwallet.gui.Main;
import com.hosvir.decredwallet.utils.CustomFileFilter;
import com.hosvir.decredwallet.utils.GraphicsUtils;
import com.hosvir.decredwallet.utils.Keystore;

/**
 * 
 * @author Troy
 *
 */
public class Login extends Interface {	
	private JFileChooser fileChooser;
	private String loginMessage = "Decred Wallet requires an active DCRD RPC server\nfor sending and receiving transactions on the Decred\nnetwork. Enter your RPC connection information and\ncredentials below.";
	
	@Override
	public void init() {
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new CustomFileFilter("cert","Certificates"));
		
		InputBox addressInput = new InputBox("address", Engine.getWidth() / 2 - 180, Engine.getHeight() / 2 - 40, 360, 30);
		InputBox usernameInput = new InputBox("username", Engine.getWidth() / 2 - 180, Engine.getHeight() / 2, 360, 30);
		InputBox passwordInput = new InputBox("password", Engine.getWidth() / 2 - 180, Engine.getHeight() / 2 + 40, 360, 30);
		LinkLabel helpLabel = new LinkLabel("help","Need Help?", "https://forum.decred.org/threads/decred-wallet-gui.1119", Engine.getWidth() / 2 - 40, Engine.getHeight() / 2 + 200);
		LinkLabel importLabel = new LinkLabel("import", "Import Certificate", "", Engine.getWidth() / 2 - 60, Engine.getHeight() / 2 + 90);
		helpLabel.setTextColor(new Color(0,204,255));
		importLabel.setTextColor(ColorConstants.flatBlue);
		Dialog errorDialog = new Dialog("errordiag", "");
		
		addressInput.placeholderText = "localhost";
		usernameInput.placeholderText = "username";
		passwordInput.placeholderText = "password";
		passwordInput.setTextHidden(true);
		errorDialog.width = 800;
		
		this.components.add(addressInput);
		this.components.add(usernameInput);
		this.components.add(passwordInput);	
		this.components.add(new Button("confirm", Constants.getLangValue("Confirm-Button-Text"), Engine.getWidth() / 2 - 75, Engine.getHeight() / 2 + 110, 150, 35, ColorConstants.flatBlue, ColorConstants.flatBlueHover));
		this.components.add(helpLabel);
		this.components.add(importLabel);
		this.components.add(errorDialog);
	}
	
	@Override
	public void update(long delta) {
		//Update
		super.update(delta);
		
		
		//For each component
		for(Component c : components) {
			if(c.containsMouse && c.enabled) Main.containsMouse = true;
					
			//Buttons
			if(c instanceof Button) {
				if(c.selectedId == 0 && c.enabled){
					switch(c.name){
					case "confirm":
						//Don't allow multiple presses
						getComponentByName("confirm").enabled = false;
						
						//Update end points
						if(getComponentByName("address").text != "") {
							Constants.setDcrdEnpointURI(getComponentByName("address").text);
							Constants.setDcrwalletEnpointURI(getComponentByName("address").text);
						}
						
						//If we are on localhost
						if(getComponentByName("address").text == "" || getComponentByName("address").text == "localhost" || getComponentByName("address").text == "127.0.0.0.1") {
							
							//No local DCRD
							if(Processes.getClosestProcess("dcrd") == -1){ 								
								getComponentByName("errordiag").text = "Starting local DCRD process, this may take a minute.";
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
								
								//Start DCRD
								Constants.log("Starting Daemon.");
								Constants.setDaemonProcess(new LocalProcess(Constants.getDaemonCommand()));
									
								//Check to see if the daemon is ready
								while(!Constants.isDaemonProcessReady()){
									for(String s : Constants.getDaemonProcess().log)
										if(s.contains("RPC server listening"))
											Constants.setDaemonProcessReady(true);
										
									//Sleep
									sleep(100);
								}
							}
							
							//No local DCRWALLET
							if(Processes.getClosestProcess("dcrwallet") == -1) {
								JOptionPane.showMessageDialog(null, 
										"Starting local DCRWALLET process, this may take a minute.",
										"Decred Wallet",
										JOptionPane.INFORMATION_MESSAGE);
								
								//Start DCRWALLET
								Constants.log("Starting Wallet.");
								Constants.setWalletProcess(new LocalProcess(Constants.getWalletCommand()));
							
								//Check to see if the daemon is ready
								while(!Constants.isWalletProcessReady()){
									for(String s : Constants.getWalletProcess().log)
										if(s.contains("Opened wallet"))
											Constants.setWalletProcessReady(true);
										else if(s.contains("invalid passphrase for master public key"))
											Constants.log("Need public key, edit settings.conf");
										
									//Sleep
									sleep(100);
								}
								
								sleep(2000);
							}
						}
						
						//Attempt to connect to DCRD
						Constants.getDcrdEndpoint().connect(getComponentByName("username").text, getComponentByName("password").text);
						if(Constants.getDcrdEndpoint().connected && Constants.getDcrdEndpoint().error == -1){
							Constants.setDaemonReady(true);
							
							//Attempt to connect to DCRWALLET
							Constants.getDcrwalletEndpoint().connect(getComponentByName("username").text, getComponentByName("password").text);
							if(Constants.getDcrwalletEndpoint().connected && Constants.getDcrwalletEndpoint().error == -1){
								Constants.setWalletReady(true);
								Constants.navbar.selectedId = 6;
								getComponentByName("password").text = "";
							}else {
								switch(Constants.getDcrwalletEndpoint().error){
								case 1: //Connection error
									Constants.log("ERROR Connection Refused, check to make sure DCRWALLET is running on the specified IP.");
									getComponentByName("errordiag").text = Constants.getLangValue("Error") + " Connection Refused, check to make sure DCRWALLET is running on the specified IP Address.";
									
									//Show dialog
									this.blockInput = true;
									Constants.navbar.blockInput = true;
									getComponentByName("errordiag").selectedId = 0;
									break;
								case 2: //SSL error
									Constants.log("DCRWALLET SSL Error, check you specified to correct certificate.");
									getComponentByName("errordiag").text = Constants.getLangValue("Error") + " DCRWALLET SSL Error, check you specified the correct certificate";
									
									//Show dialog
									this.blockInput = true;
									Constants.navbar.blockInput = true;
									getComponentByName("errordiag").selectedId = 0;
									break;
								case 3: //Authentication error
									Constants.log("Failed to connect to DCRWALLET, check your username and password are correct.");
									getComponentByName("errordiag").text = Constants.getLangValue("Error") + " DCRWALLET Authentication Failed, check your username and password are correct.";
									
									//Show dialog
									this.blockInput = true;
									Constants.navbar.blockInput = true;
									getComponentByName("errordiag").selectedId = 0;
									break;
								}
							}
						}else {
							switch(Constants.getDcrdEndpoint().error){
							case 1: //Connection error
								Constants.log("ERROR Connection Refused, check to make sure DCRD is running on the specified IP.");
								getComponentByName("errordiag").text = Constants.getLangValue("Error") + " Connection Refused, check to make sure DCRD is running on the specified IP Address.";
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
								break;
							case 2: //SSL error
								Constants.log("SSL Error, check you specified to correct certificate.");
								getComponentByName("errordiag").text = Constants.getLangValue("Error") + " DCRD SSL Error, check you specified the   correct certificate";
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
								break;
							case 3: //Authentication error
								Constants.log("Failed to connect to DCRD, check your username and password are correct.");
								getComponentByName("errordiag").text = Constants.getLangValue("Error") + " DCRD Authentication Failed, check your username and password are correct.";
								
								//Show dialog
								this.blockInput = true;
								Constants.navbar.blockInput = true;
								getComponentByName("errordiag").selectedId = 0;
								break;
							}
						}
						

						//Enable button again
						getComponentByName("confirm").enabled = true;
						break;
					}
				}
						
				//Release button
				c.selectedId = -1;
			}
					
			//Input boxes
			if(c instanceof InputBox) {
				if(c.clickCount > 0) Constants.unselectOtherInputs(components, c);
			}
			
			//Link label
			if(c instanceof LinkLabel) {
				if(c.clickCount > 0) {
					switch(c.getName()){
					case "help":
						if(c.clickCount > 0) Constants.openLink(((LinkLabel) c).getLinkAddress());
						break;
					case "import":
						int returnValue = fileChooser.showDialog(null, "Import");
						
						if(returnValue == JFileChooser.APPROVE_OPTION) {
							if(fileChooser.getSelectedFile().getName().toLowerCase().endsWith("cert")) {
								if(Keystore.importCertificate(fileChooser.getSelectedFile().getAbsolutePath(), 
										Constants.generateRandomString(8), 
										Constants.getKeystore())){
									Constants.log("Added certificate to keystore.");
								}
							}else{
								Constants.log("Invalid file type.");
							}
						}
						break;
					}
					
					c.clickCount = 0;
				}
			}
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		//Background color
		g.setColor(ColorConstants.backgroundColor);
		g.fillRect(0, 
				0, 
				Engine.getWidth(), 
				Engine.getHeight());
		
		//Icon and title
		g.drawImage(Images.getIcon(),
				Engine.getWidth() / 2 - 120,
				Engine.getHeight() / 2 - 240,
				null);
		
		g.setFont(FontConstants.totalBalanceFont);
		g.setColor(Color.WHITE);
		g.drawString("DECRED", 
				Engine.getWidth() / 2 - 40,
				Engine.getHeight() / 2 - 190);
		
		//Login form
		g.setColor(ColorConstants.loggedinBackgroundColor);
		g.fillRoundRect(Engine.getWidth() / 2 - 200, 
				Engine.getHeight() / 2 - 160, 
				400, 
				320,
				20,
				20);
		
		//Login text
		g.setFont(FontConstants.labelFont);
		g.setColor(ColorConstants.walletBalanceColor);
		GraphicsUtils.drawString(g, loginMessage, Engine.getWidth() / 2 - 180, Engine.getHeight() / 2 - 140);
		
		
		//Render
		super.render(g);
	}
	
	@Override
	public void resize() {
		if(getComponentByName("address") != null) {
			getComponentByName("address").x = Engine.getWidth() / 2 - 180;
			getComponentByName("username").x = Engine.getWidth() / 2 - 180;
			getComponentByName("password").x = Engine.getWidth() / 2 - 180;
			getComponentByName("confirm").x = Engine.getWidth() / 2 - 75;
			getComponentByName("help").x = Engine.getWidth() / 2 - 40;
			getComponentByName("import").x = Engine.getWidth() / 2 - 60;
			
			getComponentByName("address").y = Engine.getHeight() / 2 - 40;
			getComponentByName("username").y = Engine.getHeight() / 2;
			getComponentByName("password").y = Engine.getHeight() / 2 + 40;
			getComponentByName("confirm").y = Engine.getHeight() / 2 + 110;
			getComponentByName("help").y = Engine.getHeight() / 2 + 200;
			getComponentByName("import").y = Engine.getHeight() / 2 + 90;
		
			getComponentByName("address").resize();
			getComponentByName("username").resize();
			getComponentByName("password").resize();
			getComponentByName("confirm").resize();
			getComponentByName("help").resize();
			getComponentByName("import").resize();
		}
	}

	@Override
	public boolean isActive() {
		return (!Constants.getDcrdEndpoint().connected || !Constants.getDcrwalletEndpoint().connected);
	}
	
	/**
	 * Sleep the thread
	 * 
	 * @param time
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
