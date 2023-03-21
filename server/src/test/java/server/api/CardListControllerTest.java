package server.api;

import commons.Board;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CardListControllerTest {

    private TestCardListRepository repository;

    private CardListController controller;

}
