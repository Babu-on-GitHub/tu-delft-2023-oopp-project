package client.scenes;


import client.model.CardModel;
import client.model.ListModel;
import commons.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import org.apache.commons.lang3.NotImplementedException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    public static final DataFormat CARD = new DataFormat("myCardMimeType");
    @FXML
    private VBox cardListContainer;

    @FXML
    private BorderPane listContainer;

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField listTitle;
    private ListModel listModel;
    private MainPageCtrl parent;

    @SuppressWarnings("unused")
    public ListController() {
    }

    public ListController(ListModel cardList) {
        this.listModel = cardList;
    }

    public ListController(ListModel cardList, MainPageCtrl parent) {
        this.listModel = cardList;
        listModel.setController(this);
        this.parent = parent;
    }

    public void recreateChildren(ArrayList<CardModel> temp) throws IOException {
        cardListContainer.getChildren().clear();
        for (CardModel card : temp)
            addCard(card);
    }

    public void addCard(CardModel model) throws IOException {
        insertCard(model, cardListContainer.getChildren().size());
    }

    public void insertCard(CardModel model, int index) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedCard.fxml"));
        var controller = new CardController(model, this);
        model.setController(controller);
        loader.setController(controller);

        AnchorPane newCard = loader.load();
        cardListContainer.getChildren().add(index, newCard);
    }

    public void addCardButton(ActionEvent event) throws IOException {
        CardModel newCard = new CardModel(new Card(), listModel);
        addCard(newCard); // keep this order of add card and listModel.addCard
        listModel.addCard(newCard);
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
        if (dragboard.hasContent(CARD)) {
            var card = (Card) dragboard.getContent(CARD);
            var model = new CardModel(card, listModel);

            insertCard(model, whichIndexToDropIn(event.getSceneY()));

            listModel.insertCard(model, whichIndexToDropIn(event.getSceneY()));
        }
        event.setDropCompleted(true);

        event.consume();
    }

    @FXML
    void dragDone(DragEvent event) {

    }

    @FXML
    public void deleteListButton(ActionEvent event) {
        deleteList();
    }

    public void deleteList() {
        listModel.deleteList();
        parent.getListsContainer().getChildren().remove(listContainer);
    }

    public void updateTitle() {
        // not implemented
        throw new NotImplementedException();
    }

    public VBox getCardsContainer() {
        return cardListContainer;
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