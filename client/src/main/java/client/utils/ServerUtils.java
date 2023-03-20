/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ServerUtils {

    private String SERVER;

    private String websocketUrl;

    private <T> T get(String endpoint, GenericType<T> type) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(endpoint) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(type);
    }

    private <T> T post(String endpoint, Object body, GenericType<T> type) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(endpoint) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(body, APPLICATION_JSON), type);
    }

    private <T> T put(String endpoint, Object body, GenericType<T> type) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(endpoint) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(body, APPLICATION_JSON), type);
    }

    private <T> T delete(String endpoint, GenericType<T> type) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path(endpoint) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(type);
    }

    public ServerUtils(){
        this.SERVER = "http://localhost:8080";
        this.websocketUrl = "ws://localhost:8080/websocket";
    }

    public boolean chooseServer(String server) {
        if (server == null)
            return false;

        var oldServer = SERVER;
        var oldWebsocketurl = websocketUrl;
        SERVER = "http://" + server;
        websocketUrl = "ws://" + server + "/websocket";

        try {
            String response = get("api/status", new GenericType<>() {});
            if (response.equals("Running")) {
                return true;
            } else {
                SERVER = oldServer;
                websocketUrl = oldWebsocketurl;
                return false;
            }
        } catch (Exception e) {
            SERVER = oldServer;
            websocketUrl = oldWebsocketurl;
            return false;
        }
    }

    public List<Card> getCards() {
        return get("api/card", new GenericType<>() {});
    }

    public Card addCard(Card card) {
        return post("api/card/add", card, new GenericType<>() {});
    }

    public boolean deleteCardById(long id) {
        return delete("api/card/delete/" + id, new GenericType<>() {});
    }

    public Card updateCardById(long id, Card card) {
        return put("api/card/update/" + id, card, new GenericType<>() {});
    }

    public List<CardList> getCardLists() {
        return get("api/list", new GenericType<>() {});
    }

    public CardList addCardList(CardList list) {
        return post("api/list/add", list, new GenericType<>() {});
    }

    public boolean deleteCardListById(long id) {
        return delete("api/list/remove/" + id, new GenericType<>() {});
    }

    public CardList updateCardListById(long id, CardList list) {
        return put("api/list/update/" + id, list, new GenericType<>() {});
    }

    public List<Board> getBoard() {
        return get("api/board", new GenericType<>() {});
    }

    public Board addBoard(Board board) {
        return post("api/board/add", board, new GenericType<>() {});
    }

    public boolean deleteBoardById(long id) {
        return delete("api/board/delete/" + id, new GenericType<>() {});
    }

    public Board updateBoardById(long id, Board board) {
        return put("api/board/update/" + id, board, new GenericType<>() {});
    }

    private  StompSession session = connect(websocketUrl);

    private StompSession connect(String url) {
        var client = new StandardWebSocketClient();
        var stomp = new WebSocketStompClient(client);
        stomp.setMessageConverter(new MappingJackson2MessageConverter());
        try {
            return stomp.connectAsync(url, new StompSessionHandlerAdapter() {}).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException();
    }

    public <T> void registerForMessages(String dest, Class<T> type, Consumer<T> consumer) {
        session.subscribe(dest, new StompFrameHandler() {
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