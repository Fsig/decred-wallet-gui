package com.hosvir.decredwallet.websockets;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.deadendgine.utils.Random;
import com.deadendgine.utils.StringUtils;
import com.deadendgine.utils.Timer;
import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.utils.Json;
import com.hosvir.decredwallet.utils.JsonObject;
import com.hosvir.decredwallet.utils.Param;

/**
 * 
 * @author Troy
 *
 */

@ClientEndpoint
public class DecredEndpoint extends Thread implements Endpoint {
	public ArrayList<String> log;
	public boolean connected;
	public int error;
	private Random random;
	public String uri;
	private Session userSession;
	private CountDownLatch latch;
	private WebSocketContainer container;
	private Session session;
	private ArrayList<String> resultQueue;
	private ArrayList<String> readResultQueue;
	private ArrayList<String> removeResultQueue;
	private Timer connectTimer;
		
	/**
	 * Construct a new Decred Endpoint
	 * 
	 * @param uri
	 */
	public DecredEndpoint(String uri){
		this.log = new ArrayList<String>();
		this.uri = uri;
		random = new Random();
		latch = new CountDownLatch(1);
		resultQueue = new ArrayList<String>();
		readResultQueue = new ArrayList<String>();
		removeResultQueue = new ArrayList<String>();
		connectTimer = new Timer(5000);
		
		this.setName("JDecredWallet - Endpoint Thread");
		this.setPriority(NORM_PRIORITY);
		this.start();
	}
	
	/**
	 * Connect to the DCRD End point
	 * 
	 * @return boolean
	 */
	@Override
	public boolean connect(String username, String password) {
		try{
			int id = random.random(1, 9999);
			connectTimer.reset();
			container = ContainerProvider.getWebSocketContainer();
			session = container.connectToServer(this, URI.create(uri));

			session.getBasicRemote().sendText("" +
					"" +
					"{" +
						"\"method\":\"authenticate\"," +
						"\"params\":[" +
							"\"" + username + "\"," +
							"\""+ password + "\"" +
						"]," +
						"\"id\":" + id + "" +
					"}" +
				"");
			
			latch.await(1, TimeUnit.SECONDS);
			
			//Wait for result
			while(getResult(id) == null && !connectTimer.isUp()){
				try {
					latch.await(100, TimeUnit.MICROSECONDS);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//Final result after waiting
			if(getResult(id) == null){
				connected = false;
				error = 3;
			}else{
				error = -1;
				connected = true;
			}		
		}catch(DeploymentException e){
			e.printStackTrace();
			connected = false;
			error = 2;
		}catch(Exception ee){
			ee.printStackTrace();
			connected = false;
			error = 1;
		}
			
		return false;
	}
	
	/**
	 * Disconnect from the DCRD End point
	 */
	@Override
	public void disconnect() {	
		try {
			connected = false;
			if(session != null) session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE,"sesison close"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized String callMethod(String method, Param[] params) {
		if(connected){
			int id = random.random(1, 9999);
			String paramstring = "";
				
			if(params != null){
				for(Param p : params){
					switch(p.getType()){
					case 0: //String
						paramstring += "\"" + p.getValue() + "\",";
						break;
					case 1: //Anything not wrapped in brackets
						paramstring += p.getValue() + ",";
						break;
					}
				}
					
				paramstring = StringUtils.backspace(paramstring);
			}
				
			if(Constants.isDebug()) Constants.log("METHOD: " + method + " : " + paramstring);
				
			try {
				session.getBasicRemote().sendText(
						"{" +
								"\"method\":\"" + method + "\"," +
								"\"params\":[" + paramstring + "]," +
								"\"id\":" + id + "}");
					
				latch.await(200, TimeUnit.MICROSECONDS);
			}catch(IllegalStateException e) {
				e.printStackTrace();
				connected = false;
				return null;
			}catch(Exception ee) {
				ee.printStackTrace();
				return null;
			}
				
			//Wait for result
			while(getResult(id) == null){
				try {
					latch.await(100, TimeUnit.MICROSECONDS);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
				
			//Have the result
			String result = getResult(id);
			removeResultQueue.add(result);
			log.add(result);
			if(Constants.guiInterfaces != null && Constants.guiInterfaces.size() > 5) Constants.guiInterfaces.get(5).resize();
				
			return result;
		}else{
			Constants.log("Failed to connect to URI: " + uri);
			return null;
		}
	}
	
    /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
    @OnOpen
    public void onOpen(Session userSession) {
    	if(Constants.isDebug()) Constants.log("Connected to websocket: " + userSession.getId());
        this.userSession = userSession;
    }

    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
    	if(Constants.isDebug()) Constants.log("Closed websocket: " + userSession.getId());
        resultQueue.clear();
		readResultQueue.clear();
		removeResultQueue.clear();
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
    	if(Constants.isDebug()) Constants.log("MESSAGE: " + message + " - " + userSession.getId());
    	resultQueue.add(message);
    }
    
    /**
     * Get a result from the queue by id.
     * 
     * @param id
     * @return String
     */
    public synchronized String getResult(int id) {    	
    	if(removeResultQueue.size() > 0) {
    		resultQueue.removeAll(removeResultQueue);
    		removeResultQueue.clear();
    	}
    	
    	readResultQueue.clear();
    	readResultQueue.addAll(resultQueue);
    	
    	for(String s : readResultQueue){
    		ArrayList<JsonObject> jsonObjects = Json.parseJson(s);
    		if(jsonObjects.get(jsonObjects.size()-1).getValueByName("result").trim().equals(String.valueOf(id)))
    			return s;
    	}
    	
    	return null;
    }

}
