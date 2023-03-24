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
import java.util.Optional;
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

    public ServerUtils() {
        this.SERVER = "http://localhost:8080";
        this.websocketUrl = "ws://localhost:8080/websocket";
        session = connect(websocketUrl);
    }

    public boolean chooseServer(String server) {
        if (server == null)
            return false;

        var oldServer = SERVER;
        //var oldWebsocketurl = websocketUrl;
        SERVER = "http://" + server;
        //websocketUrl = "ws://" + server + "/websocket";

        try {
            String response = get("api/status", new GenericType<>() {
            });
            if (response.equals("Running")) {
                //session = connect(websocketUrl);
                return true;
            } else {
                SERVER = oldServer;
                //websocketUrl = oldWebsocketurl;
                return false;
            }
        } catch (Exception e) {
            SERVER = oldServer;
            //websocketUrl = oldWebsocketurl;
            return false;
        }
    }

    public Optional<List<Card>> getCards() {
        try {
            return Optional.of(get("api/card", new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Card> getCardById(long id) {
        try {
            return Optional.of(get("api/card/" + id, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Card> addCard(Card card, CardList list) {
        try {
            return Optional.of(post("api/list/add/" + list.getId(), card, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Card> insertCard(Card card, int position, CardList list) {
        try {
            return Optional.of(post("api/list/insert/" + list.getId() + "/to/" + position, card, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> deleteCardById(long cardId, long listId) {
        try {
            return Optional.of(delete("api/list/delete/" + cardId + "/from/" + listId, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Card> updateCardById(long id, Card card) {
        try {
            return Optional.of(put("api/card/update/" + id, card, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<CardList>> getCardLists() {
        try {
            return Optional.of(get("api/list", new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<CardList> getCardListById(long id) {
        try {
            return Optional.of(get("api/list/" + id, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<CardList> addCardList(CardList list, Board board) {
        try {
            return Optional.of(post("api/board/add/" + board.getId(), list, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> deleteCardListById(long listId, long boardId) {
        try {
            return Optional.of(delete("api/board/delete/" + listId + "/from/" + boardId, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<CardList> updateCardListById(long id, CardList list) {
        try {
            return Optional.of(put("api/list/update/" + id, list, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<Board>> getBoards() {
        try {
            return Optional.of(get("api/board", new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Board> getBoardById(long id) {
        try {
            return Optional.of(get("api/board/" + id, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Board> addBoard(Board board) {
        try {
            return Optional.of(post("api/board/create", board, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> deleteBoardById(long id) {
        try {
            return Optional.of(delete("api/board/delete/" + id, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Board> updateBoardById(long id, Board board) {
        try {
            return Optional.of(put("api/board/update/" + id, board, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Boolean> moveCard(long cardId, long listId, int position, long boardId) {
        try {
            var reqString = "api/board/moveCard/" + cardId + "/to/" + listId + "/at/" + position +
                    "/located/" + boardId;
            return Optional.of(post(reqString, null, new GenericType<>() {
            }));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private StompSession session;

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
        session.subscribe(dest, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return type;
            }

            @SuppressWarnings("unchecked")
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("handle frame");
                consumer.accept((T) payload);
            }
        });
    }

    public void send(String dest, Object o) {
        System.out.println("send something");
        session.send(dest, o);
    }


}