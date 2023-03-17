package client;

import jakarta.websocket.*;

import java.net.URI;

@ClientEndpoint
public class WebsocketClient {

    private Session session;

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

}
