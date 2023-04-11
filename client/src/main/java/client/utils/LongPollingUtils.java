package client.utils;

import java.time.Duration;

import commons.Card;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.logging.Logger;


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

        if (executor != null) {
            executor.shutdownNow();
        }
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                String res = restTemplate.exchange(server + dest, HttpMethod.GET, null, String.class).getBody();
                //T res = restTemplate.getForObject(server + dest, T.class);
                callback.accept(Optional.ofNullable(res));
            } catch (Exception e) {
                callback.accept(Optional.empty());
            }
        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();

    public void longPollCard(String dest, Consumer<Card> callback) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(1L))
                .setReadTimeout(Duration.ofSeconds(1L))
                .build();

        if (exec != null) {
            exec.shutdownNow();
        }
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            try {
                Card res = restTemplate.exchange(server + dest, HttpMethod.GET, null, Card.class).getBody();
                callback.accept(res);
            } catch (Exception e) {
                callback.accept(null);
            }
        }, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    private static ScheduledExecutorService execLong = Executors.newSingleThreadScheduledExecutor();

    public void longPollId(String dest, Consumer<Long> callback) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(1L))
                .setReadTimeout(Duration.ofSeconds(1L))
                .build();

        if (execLong != null) {
            execLong.shutdownNow();
        }
        execLong = Executors.newSingleThreadScheduledExecutor();
        execLong.scheduleAtFixedRate(() -> {
            try {
                Long res = restTemplate.exchange(server + dest, HttpMethod.GET, null, Long.class).getBody();
                callback.accept(res);
            } catch (Exception e) {
                callback.accept(null);
            }
        }, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void stopIdPolling() {
        execLong.shutdownNow();
    }

    public void stopCardPolling() {
        exec.shutdownNow();
    }

    public void stopLongPolling() {
        executor.shutdownNow();
        stopCardPolling();
        stopIdPolling();
    }
}