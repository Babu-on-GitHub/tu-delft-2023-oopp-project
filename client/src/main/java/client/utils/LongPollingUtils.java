package client.utils;

import java.time.Duration;

import commons.Card;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class LongPollingUtils {

    private static Logger log = Logger.getLogger(LongPollingUtils.class.getName());

    String server;

    public LongPollingUtils() {
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = wrapWithLongPollingProtocol(server);
    }

    public static String wrapWithLongPollingProtocol(String server) {
        if (server.startsWith("http://"))
            return "http://" + server.substring(7);
        if (server.startsWith("https://"))
            return "http://" + server.substring(8);
        return "http://" + server;
    }

    private ScheduledExecutorService executor;

    public void longPoll(String dest, Consumer<Optional<String>> callback) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10L))
                .setReadTimeout(Duration.ofSeconds(10L))
                .build();

        if(executor!=null){
            executor.shutdownNow();
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                String res = restTemplate.exchange(server + dest, HttpMethod.GET, null,String.class).getBody();
                //T res = restTemplate.getForObject(server + dest, T.class);
                callback.accept(Optional.ofNullable(res));
            } catch (Exception e) {
                callback.accept(Optional.empty());
            }
        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    private static final ExecutorService EXEC = Executors.newSingleThreadScheduledExecutor();

    public void longPollCard(String dest, Consumer<Card> callback){

        EXEC.submit(()->{
           while(!Thread.interrupted()){
               var res = ClientBuilder.newClient(new ClientConfig())
                       .target(server).path("/api/card/poll")
                       .request(APPLICATION_JSON)
                       .accept(APPLICATION_JSON)
                       .get(Response.class);
               if(res.getStatus() == 204){
                   continue;
               }
               var q = res.readEntity(Card.class);
               callback.accept(q);
           }
        });
    }

    public void stopCardPolling(){
        EXEC.shutdown();
    }

    public void stopLongPolling() {
        executor.shutdownNow();
        stopCardPolling();
    }
}