package client.scenes;

import client.model.CardModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.NotImplementedException;

import java.net.URL;
import java.util.ResourceBundle;

public class CardController implements Initializable {

    private CardModel card;

    private ListController parent;

    @FXML
    private AnchorPane cardContainer;

    @FXML
    private TextField cardTitle;

    @SuppressWarnings("unused")
    public CardController(){}

    public CardController(CardModel card, ListController parent){
        this.card = card;

        this.parent = parent;
    }

    @FXML
    public void deleteCardButton(ActionEvent event) {
        deleteCard();
    }

    public void deleteCard() {
        card.deleteCard();
        parent.getCardsContainer().getChildren().remove(cardContainer);
    }

    public void updateTitle(){
        throw new NotImplementedException();
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
