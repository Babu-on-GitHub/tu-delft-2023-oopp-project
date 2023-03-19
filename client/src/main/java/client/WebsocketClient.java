package client;

import jakarta.websocket.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@Configuration
@Component
@ClientEndpoint
public class WebsocketClient {

    private StompSession stompSession;

    public WebsocketClient() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "ws://localhost:8080/websocket";
        try {
            stompSession = stompClient.connectAsync(url, new StompSessionHandlerAdapter() {}).get();
            stompSession.subscribe("/topic", new StompFrameHandler() {
                @Override
                public Class<String> getPayloadType(StompHeaders headers) {
                    return String.class;
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    String message = (String) payload;
                    System.out.println("Server message: " + message);
                }
            });
            System.out.println("The constructor works, should be connected to server");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen() {
        System.out.println("OnOpen method executed, message received");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Message from server: " + message);
    }

    @OnClose
    public void onClose(StompSession stompSession) {
        System.out.println("Disconnected from server");
    }

}
