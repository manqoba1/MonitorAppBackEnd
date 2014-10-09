/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.monitor.gate;

import com.boha.monitor.dto.transfer.RequestDTO;
import com.boha.monitor.dto.transfer.ResponseDTO;
import com.boha.monitor.util.DataException;
import com.boha.monitor.util.DataUtil;
import com.boha.monitor.util.GZipUtility;
import com.boha.monitor.util.ListUtil;
import com.boha.monitor.util.TrafficCop;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author aubreyM
 */
@ServerEndpoint(value = "/wsproject")
@Stateful
public class ProjectWebSocket {

    @EJB
    DataUtil dataUtil;
    @EJB
    ListUtil listUtil;
    static final String SOURCE = "ProjectWebSocket";
    //TODO - clean up expired sessions!!!!
    public static final Set<Session> peers
            = Collections.synchronizedSet(new HashSet<Session>());

    @OnMessage
    public ByteBuffer onMessage(String message) {
        log.log(Level.WARNING, "onMessage: {0}", message);
        ResponseDTO resp = new ResponseDTO();
        ByteBuffer bb = null;
        try {

            try {
                RequestDTO dto = gson.fromJson(message, RequestDTO.class);
                resp = TrafficCop.processRequest(dto, dataUtil, listUtil);

            } catch (DataException e) {
                resp.setStatusCode(101);
                resp.setMessage("Data service failed to process your request");
                log.log(Level.SEVERE, "Database related failure", e);
                bb = GZipUtility.getZippedResponse(resp);
            }
            bb = GZipUtility.getZippedResponse(resp);
        } catch (IOException ex) {
            Logger.getLogger(ProjectWebSocket.class.getName()).log(Level.SEVERE, null, ex);
            resp.setStatusCode(111);
            resp.setMessage("Problem processing request on server");
            try {
                bb = GZipUtility.getZippedResponse(resp);
            } catch (IOException ex1) {
                Logger.getLogger(ProjectWebSocket.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        return bb;
    }

    @OnOpen 
    public void onOpen(Session session, EndpointConfig ec) {

        peers.add(session);
        try {
            ResponseDTO r = new ResponseDTO();
            r.setSessionID(session.getId());
            session.getBasicRemote().sendText(gson.toJson(r));
            log.log(Level.WARNING, "onOpen...sent session id: {0}", session.getId());
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Failed to send websocket sessionID", ex);
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.log(Level.WARNING, "onClose - remove session: {0}", session.getId());
        for (Session mSession : peers) {
            if (session.getId().equalsIgnoreCase(mSession.getId())) {
                peers.remove(mSession);
                break;
            }
        }
    }

    @OnError
    public void onError(Session s, Throwable t) {
        log.log(Level.SEVERE, null, t);

    }

    Gson gson = new Gson();
    static final Logger log = Logger.getLogger(ProjectWebSocket.class.getSimpleName());
}
