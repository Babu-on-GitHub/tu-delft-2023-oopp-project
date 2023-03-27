package client.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Card;
import jakarta.ws.rs.core.GenericType;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ServerUtilsTest {


    private static MockWebServer mockServer;

    private ServerUtils serverUtils;

    private SocketUtils socketUtils;

    @BeforeAll
    public static void setup() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
    }

    @BeforeEach
    void setUp() {
        serverUtils = new ServerUtils();
        String baseUrl = String.format("http://localhost:%s", mockServer.getPort());
        serverUtils.chooseServer(baseUrl);
    }

    @AfterAll
    public static void teardown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void serverUtilsTest() {
        assertNotNull(serverUtils);
    }

    @Test
    void chooseServerNullTest() {
        assertFalse(serverUtils.chooseServer(null));
    }

    @Test
    void chooseServerInvalidServerTest() {
        assertFalse(serverUtils.chooseServer("thisIsNotAServer"));
    }

    @Test
    void chooseServerValidTest() throws IOException {
        MockWebServer mockServerConnect = new MockWebServer();
        mockServerConnect.start();
        serverUtils = new ServerUtils();
        String baseUrl = String.format("localhost:%s", mockServerConnect.getPort());
        assertTrue(serverUtils.chooseServer(baseUrl));
        //the get method, triggered by chooseServer, never stops running
        mockServerConnect.shutdown();
    }

    @Test
    void getServerUtils() {
        assertNotNull(serverUtils.getSocketUtils());
    }

    @Test
    void getTest() {
        mockServer.enqueue(new MockResponse().setBody("test response"));
        //String response = serverUtils.get("/", new GenericType<String>() {}); //get is private!
    }

    @Test
    void getCardsTest() throws JsonProcessingException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<List<Card>> mockCardList = Optional.of(new ArrayList<>());
        mockServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockCardList))
                .addHeader("Content-Type", "application/json"));

        Mono<Optional<List<Card>>> cards = Mono.just(serverUtils.getCards());

        StepVerifier.create(cards)
                .expectNextMatches(cardsOptional -> cardsOptional.isPresent())
                .verifyComplete();

        RecordedRequest request = mockServer.takeRequest();

        assertEquals("GET", request.getMethod());
        assertEquals("api/card", request.getPath());
    }
}
