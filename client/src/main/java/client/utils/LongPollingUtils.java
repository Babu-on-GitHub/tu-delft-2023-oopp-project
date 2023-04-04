package client.utils;

import java.time.Duration;
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
            return "http://" + server.substring(7) + "/long";
        if (server.startsWith("https://"))
            return "http://" + server.substring(8) + "/long";
        return "http://" + server + "/long";
    }

    private ScheduledExecutorService executor;

    public void longPoll(String dest, Consumer<Optional<String>> callback) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(10L))
                .setReadTimeout(Duration.ofSeconds(10L))
                .build();

        if (executor != null)
            executor.shutdownNow();

        executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            try {
                String res = restTemplate.exchange(server + dest, HttpMethod.GET, null,
                        String.class).getBody();
                callback.accept(Optional.ofNullable(res));
            } catch (Exception e) {
                callback.accept(Optional.empty());
            }
        }, 0, 5, java.util.concurrent.TimeUnit.SECONDS);
    }

    public void stopLongPolling() {
        if (executor != null)
            executor.shutdownNow();
    }
}