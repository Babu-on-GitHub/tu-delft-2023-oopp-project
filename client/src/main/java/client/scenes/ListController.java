package client.scenes;


import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    @FXML
    private VBox cardListContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField listTitle;

    private CardList cardList;


    @SuppressWarnings("unused")
    public ListController(){}

    public ListController(CardList cardList){
        this.cardList = cardList;
    }

    public void addCardButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedCard.fxml"));

        Card newChild = new Card("caacdfe");
        if (cardList.getCards() == null) {
            cardList.setCards(new ArrayList<>());
        }

        ServerUtils utils = new ServerUtils();
        newChild = utils.addCard(newChild);

        cardList.getCards().add(newChild);
        cardList = utils.updateCardListById(cardList.getId(), cardList);

        AnchorPane newCard = loader.load();
        //newCard.setUserData(newChild.getId());

        cardListContainer.getChildren().add(newCard);
        cardList = utils.updateCardListById(cardList.getId(), cardList);
    }

    @FXML
    public void deleteListButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        var toDelete = pressed.getParent().getParent();
        var listOfLists = (HBox) toDelete.getParent();

        listOfLists.getChildren().remove(toDelete);

        ServerUtils utils = new ServerUtils();
        //utils.deleteCardListById();
    }

    public void updateTitle(){
        cardList.setTitle(listTitle.getText());
        ServerUtils utils = new ServerUtils();
        cardList = utils.updateCardListById(cardList.getId(),cardList);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardListContainer.setSpacing(10);
        scrollPane.setFitToWidth(true);

        listTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTitle();
            }
        });
    }
}