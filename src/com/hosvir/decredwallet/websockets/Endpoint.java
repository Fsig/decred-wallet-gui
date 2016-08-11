package com.hosvir.decredwallet.websockets;

import com.hosvir.decredwallet.utils.Param;


/**
 * 
 * @author Troy
 *
 */
public interface Endpoint {
	public boolean connect(String username, String password);
	public void disconnect();
	public String callMethod(String method, Param[] params);
}
