package client;

import jakarta.websocket.*;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class WebsocketClient {

    private Session session;

    public WebsocketClient() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            URI uri = new URI("ws://localhost:8080/websocket");
            container.connectToServer(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Connected to server");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Disconnected from server");
    }

    public Session getSession() {
        return session;
    }
}
