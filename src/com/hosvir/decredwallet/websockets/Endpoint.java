package com.hosvir.decredwallet.websockets;

import com.hosvir.decredwallet.utils.Param;
import org.json.simple.JSONObject;

/**
 * @author fsig
 * @version 1.00
 * @since 19/03/17
 */
public interface Endpoint {
    public boolean connect(String username, String password);
    public void disconnect();
    public JSONObject callMethod(String method, Param[] params);
}

