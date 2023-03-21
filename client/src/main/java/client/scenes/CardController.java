package client.scenes;

import commons.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import client.model.CardModel;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.apache.commons.lang3.NotImplementedException;

import static client.scenes.ListController.CARD;


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
    public CardController() {
    }

    @FXML
    private VBox cardListContainer;

    public CardController(CardModel card, ListController parent) {
        this.card = card;

        this.parent = parent;
    }

    @FXML
    public void dragDetected(MouseEvent event) {

        Dragboard dragboard = cardContainer.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(CARD, card.getCard());
        dragboard.setContent(content);
        event.consume();
    }

    @FXML
    void dragDone(DragEvent event) {
        if (event.getDragboard().hasContent(CARD) && event.getGestureTarget() != null) {
            deleteCard();
        }
    }

    public void deleteCardButton(ActionEvent event) {
        deleteCard();
    }

    public void deleteCard() {
        card.deleteCard();
        parent.getCardsContainer().getChildren().remove(cardContainer);
    }

    public void updateTitle() {
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
