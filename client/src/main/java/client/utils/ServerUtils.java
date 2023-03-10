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

import java.util.List;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.glassfish.jersey.client.ClientConfig;

import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private String SERVER;

    public String choseServer(String server){
        String response = ClientBuilder.newClient(new ClientConfig()) //
                .target(server).path("/api/ServerChoice") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
        if (response.equals("successfully connected")) {
            SERVER = "http://localhost:8080";
            return "successfully connected";
        }else{
            return "connection failed";
        }
    }

    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }

    public Card addCard(Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public boolean deleteCardById(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card/remove/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Boolean.class);
    }

    public Card updateCardById(long id, Card card) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card/update/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public List<CardList> getCardLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }

    public CardList addCardList(CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    public boolean deleteCardListById(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list/remove/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Boolean.class);
    }

    public CardList updateCardListById(long id, CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list/update/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    public List<Board> getBoard() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<>() {});
    }

    public Board addBoard(Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board/add") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .post(Entity.entity(board, APPLICATION_JSON), Board.class);
    }

    public boolean deleteBoardById(long id) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board/remove/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Boolean.class);
    }

    public Board updateBoardById(long id, Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board/update/" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }
}