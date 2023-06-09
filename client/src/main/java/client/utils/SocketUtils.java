package client.utils;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class SocketUtils {
    private StompSession session;
    private String baseUrl;

    private StompSession.Subscription subscription;

    public void setServer(String server) {
        baseUrl = wrapWithWebsocket(server);
    }

    public String wrapWithWebsocket(String server) {
        if (server.startsWith("ws://") && server.endsWith("/websocket"))
            return server;
        if (server.startsWith("http://"))
            return "ws://" + server.substring(7) + "/websocket";
        if (server.startsWith("https://"))
            return "wss://" + server.substring(8) + "/websocket";
        return "ws://" + server + "/websocket";
    }

    public boolean isConnected(){
        return session != null;
    }

    public void connect() {
        disconnect();
        session = connect(baseUrl);
    }

    public void disconnect() {
        if (session != null && session.isConnected())
            session.disconnect();
    }

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connectAsync(url, new StompSessionHandlerAdapter() {
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        if (subscription != null)
            subscription.unsubscribe();

        subscription = session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                consumer.accept((T) payload);
            }
        });
    }

    public void send(String dest, Object o) {
        session.send(dest, o);
    }
}
