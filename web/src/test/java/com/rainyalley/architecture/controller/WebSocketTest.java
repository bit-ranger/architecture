package com.rainyalley.architecture.controller;

import org.junit.Test;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

public class WebSocketTest {

    @Test
    public void test(){
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://localhost:8080/endpointSang/547/58g30z1d/websocket";
            System.out.println("Connecting to " + uri);
            Session session = container.connectToServer(WebsocketClientEndpoint.class, URI.create(uri));
            session.getBasicRemote().sendText("hello world");
            java.io.BufferedReader r=new  java.io.BufferedReader(new java.io.InputStreamReader( System.in));
            while(true){
                String line=r.readLine();
                if(line.equals("quit")) break;
                session.getBasicRemote().sendText(line);
            }

        } catch ( Exception ex) {
            ex.printStackTrace();
        }
    }

}
