package client.scenes;


import commons.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    public static final DataFormat CARD = new DataFormat("myCardMimeType");
    @FXML
    private VBox cardListContainer;

    @FXML
    private ScrollPane scrollPane;



    @SuppressWarnings("unused")
    public ListController(){}

    @FXML
    public void addCardButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedCard.fxml"));
        AnchorPane newCard = loader.load();

        cardListContainer.getChildren().add(newCard);
    }

    @FXML
    public void deleteListButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        var toDelete = pressed.getParent().getParent();
        var listOfLists = (HBox) toDelete.getParent();

        listOfLists.getChildren().remove(toDelete);
    }

    private int whichIndexToDropIn(double absolutePosition) {
        Bounds boundsInScene = cardListContainer.localToScene(cardListContainer.getBoundsInLocal());
        var relativePosition = absolutePosition - boundsInScene.getMinY();

        var scrollPosition = scrollPane.getVvalue() / (scrollPane.getVmax() - scrollPane.getVmin());
        var scrollPaneHeight = scrollPane.getLayoutBounds().getMaxY() - scrollPane.getLayoutBounds().getMinY();
        var scrollPaneContentHeight = cardListContainer.getLayoutBounds().getMaxY() -
                cardListContainer.getLayoutBounds().getMinY();
        var scrollHeight = scrollPosition * (scrollPaneContentHeight - scrollPaneHeight);

        var position = relativePosition + scrollHeight;

        double minDistance = Double.MAX_VALUE;
        int minIndex = 0;
        for (var x : cardListContainer.getChildren()) {
            var card = (AnchorPane) x;
            var cardPosition = card.getLayoutY();

            var distanceToBottom = Math.abs(cardPosition - position);
            var distanceToTop = Math.abs(cardPosition + card.getHeight() - position);

            if (distanceToBottom < minDistance) {
                minDistance = distanceToBottom;
                minIndex = cardListContainer.getChildren().indexOf(card);
            }
            if (distanceToTop < minDistance) {
                minDistance = distanceToTop;
                minIndex = cardListContainer.getChildren().indexOf(card) + 1;
            }
        }

        return minIndex;
    }

    @FXML
    void dragOver(DragEvent event) {

        System.out.println("card chosen: " + whichIndexToDropIn(event.getSceneY()));

        if (event.getGestureSource() != cardListContainer && event.getDragboard().hasContent(CARD)) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    @FXML
    void drop(DragEvent event) throws IOException {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasContent(CARD)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedCard.fxml"));
            AnchorPane newCard = loader.load();

            Card card = (Card) dragboard.getContent(CARD);

            ((CardController)loader.getController()).cardTitle.setText(card.getTitle());
            cardListContainer.getChildren().add(newCard);
            success = true;
        }
        event.setDropCompleted(success);

        event.consume();

    }

    @FXML
    void dragDone(DragEvent event) {
//        cardListContainer = (VBox) ((TextField) event.getGestureSource()).getParent().getParent();
//        if (event.getGestureSource() != cardListContainer && event.getDragboard().hasContent(CARD)) {
//            var startPosition = cardTitle.getParent();
//            var listOfToDelete = (VBox) startPosition.getParent();
//            listOfToDelete.getChildren().remove(startPosition);
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardListContainer.setSpacing(10);
        scrollPane.setFitToWidth(true);
    }
}
