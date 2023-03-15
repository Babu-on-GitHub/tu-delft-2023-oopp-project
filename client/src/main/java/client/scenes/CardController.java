package client.scenes;

import commons.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;

import static client.scenes.ListController.CARD;


public class CardController {


    @SuppressWarnings("unused")
    public CardController(){}

    @FXML
    private VBox cardListContainer;
    @FXML
    public TextField cardTitle;
    @FXML
    public void deleteCardButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        var toDelete = pressed.getParent();
        var listOfToDelete = (VBox) toDelete.getParent();

        listOfToDelete.getChildren().remove(toDelete);
    }

    @FXML
    public void dragDetected(MouseEvent event) {

        var title = cardTitle.getText();
        Card card = new Card(title);
        Dragboard dragboard = cardTitle.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(CARD,card);
        dragboard.setContent(content);
        event.consume();
    }

    @FXML
    void dragDone(DragEvent event) {
        if (event.getGestureSource() != cardListContainer && event.getDragboard().hasContent(CARD)) {
            var startPosition = cardTitle.getParent();
            var listOfToDelete = (VBox) startPosition.getParent();
            listOfToDelete.getChildren().remove(startPosition);
        }
    }

    /**
     * TODO insert card at correct location
     * TODO fix card deletion when dragging away
     */

}
