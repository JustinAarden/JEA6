/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import service.KService;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@ServerEndpoint(
        value = "/socket/api"
)
public class kwettersocket {

    /**
     * All open WebSocket sessions
     */
    static Map<String, ArrayList<Session>> peers = new HashMap<>();

    private Session wsSession;

    @Inject
    KService kwetterService;

    @OnOpen
    public void openConnection(Session session) {
        if (session.getUserPrincipal() == null) {
            return;
        }
        addValues(session.getUserPrincipal().getName(), session);

        this.wsSession = session;

        send("New session started "
                + this.wsSession.getId()
                + " by user with username: "
                + session.getUserPrincipal().getName());
    }

    @OnClose
    public void closedConnection(Session session) {
        /* Remove this connection from the queue */
        for (String key : peers.keySet()) {
            for (Session s : peers.get(key)) {
                if (s == session) {
                    ArrayList tempList = peers.get(key);
                    tempList.remove(session);
                    peers.put(key, tempList);
                }
            }
        }
    }

    public static void send(String msg) {
        for (String key : peers.keySet()) {
            for (Session session : peers.get(key)) {
                session.getAsyncRemote().sendObject(msg);
            }
        }
    }

    private void addValues(String key, Session value) {
        ArrayList tempList;
        if (peers.containsKey(key)) {
            tempList = peers.get(key);
            if (tempList == null) {
                tempList = new ArrayList();
            }
            tempList.add(value);
        } else {
            tempList = new ArrayList();
            tempList.add(value);
        }
        peers.put(key, tempList);
    }
}
