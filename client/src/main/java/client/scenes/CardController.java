package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardController implements Initializable {

    private Card card;

    @FXML
    private TextField cardTitle;

    @SuppressWarnings("unused")
    public CardController(){}

    public CardController(Card card){
        this.card = card;
    }

//    @FXML
//    public void deleteCardButton(ActionEvent event) {
//        Button pressed = (Button) event.getSource();
//
//        var toDelete = pressed.getParent();
//        var listOfToDelete = (VBox) toDelete.getParent();
//
//        listOfToDelete.getChildren().remove(toDelete);
//    }

    @FXML
    public void deleteCardButton(ActionEvent event) throws IOException {
        Button pressed = (Button) event.getSource();

        AnchorPane cardToDelete = (AnchorPane) pressed.getParent();
        //long cardId = (long) cardToDelete.getUserData();

        VBox cardListContainer = (VBox) cardToDelete.getParent();
        cardListContainer.getChildren().remove(cardToDelete);

        ServerUtils utils = new ServerUtils();
        //utils.deleteCardById();
    }

    public void updateTitle(){
        card.setTitle(cardTitle.getText());
        ServerUtils utils = new ServerUtils();
        card = utils.updateCardById(card.getId(),card);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cardTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTitle();
            }
        });
    }
}
