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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {

    private static final String SERVER = "http://localhost:8080/";

    public List<Card> getCards() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/card") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<List<Card>>() {});
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
                .target(SERVER).path("api/card/update") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(card, APPLICATION_JSON), Card.class);
    }

    public CardList getCardLists() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<CardList>() {});
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
                .target(SERVER).path("api/list/remove" + id) //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Boolean.class);
    }

    public CardList updateCardListById(long id, CardList list) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/list/update") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(list, APPLICATION_JSON), CardList.class);
    }

    public Board getBoard() {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(new GenericType<Board>() {});
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
                .target(SERVER).path("api/board/remove") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .delete(Boolean.class);
    }

    public Board updateBoardById(long id, Board board) {
        return ClientBuilder.newClient(new ClientConfig()) //
                .target(SERVER).path("api/board/update") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .put(Entity.entity(board, APPLICATION_JSON), Board.class);
    }
}