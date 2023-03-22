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
import javafx.scene.input.*;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ListController implements Initializable {

    public static final DataFormat CARD_ID = new DataFormat("cardId");
    public static final DataFormat TARGET_LIST = new DataFormat("targetList");
    public static final DataFormat TARGET_INDEX = new DataFormat("targetIndex");
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

    public void moveCard(CardModel model, int index) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReworkedCard.fxml"));
        var controller = model.getController();
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
        if (event.getGestureSource() != cardListContainer && event.getDragboard().hasContent(CARD_ID)) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }

    @FXML
    void drop(DragEvent event) throws IOException {
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasContent(CARD_ID)) {
            var cardId = (Long) dragboard.getContent(CARD_ID);
            var index = whichIndexToDropIn(event.getSceneY());
            var cardOpt = listModel.getCardList().getCards().stream().
                    filter(x -> x.getId() == cardId).findFirst();
            if (cardOpt.isPresent()) {
                dragboard.setContent(null);
                var oldIndex = listModel.getCardList().getCards().
                        indexOf(cardOpt.get());
                if (oldIndex == index)
                    return;
                if (oldIndex < index)
                    index--;
            }

            ClipboardContent content = new ClipboardContent();
            content.put(CARD_ID, cardId);
            content.put(TARGET_INDEX, index);
            content.put(TARGET_LIST, listModel.getCardList().getId());
            dragboard.setContent(content);
        }

        event.consume();
    }

    @FXML
    void dragDone(DragEvent event) throws IOException {
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
    }

    public VBox getCardsContainer() {
        return cardListContainer;
    }

    public MainPageCtrl getParent() {
        return parent;
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