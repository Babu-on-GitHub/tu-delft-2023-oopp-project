package client.scenes;


import client.model.CardModel;
import client.model.ListModel;
import client.utils.ServerUtils;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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

    private ServerUtils server;

    @FXML
    private AnchorPane listTop;

    @FXML
    private AnchorPane listBottom;

    @SuppressWarnings("unused")
    public ListController() {
    }

    public ListController(ListModel cardList, ServerUtils server) {
        this.listModel = cardList;
        this.server = server;
    }

    public ListController(ListModel cardList, MainPageCtrl parent) {
        this.listModel = cardList;
        listModel.setController(this);
        this.parent = parent;
        server = parent.getServer();
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
        var controller = new CardController(model, this, server);
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
        CardModel newCard = new CardModel(new Card(), listModel, server);
        addCard(newCard); // keep this order of add card and listModel.addCard
        listModel.addCard(newCard);
        newCard.getController().setCardColorFXML(parent.getModel().getBoard().getCardColor());
    }

    private int whichIndexToDropIn(double absolutePosition) {
        Bounds boundsInScene = cardListContainer.localToScene(cardListContainer.getBoundsInLocal());
        var position = absolutePosition - boundsInScene.getMinY();

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

        int index = whichIndexToDropIn(event.getSceneY());
        try {
            parent.getModel().forEveryCard(
                    x -> x.getController().highlightReset()
            );

            var children = listModel.getChildren();
            if (index == 0)
                children.get(0).getController().highlightTop();
            else if (index == listModel.getChildren().size())
                children.get(index - 1).getController().highlightBottom();
            else {
                children.get(index - 1).getController().highlightBottom();
                children.get(index).getController().highlightTop();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // but overall we don't really care about problems here
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
                if (oldIndex < index)
                    index--;
            }

            ClipboardContent content = new ClipboardContent();
            content.put(CARD_ID, cardId);
            content.put(TARGET_INDEX, index);
            content.put(TARGET_LIST, listModel.getCardList().getId());
            dragboard.setContent(content);

            parent.getModel().forEveryCard(
                    x -> x.getController().highlightReset()
            );
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

    public VBox getCardsContainer() {
        return cardListContainer;
    }

    public MainPageCtrl getParent() {
        return parent;
    }

    public void updateTitleModel() {
        listModel.updateTitle(listTitle.getText());
    }

    public void overwriteTitleNode(String title) {
        listTitle.setText(title);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listTitle.setText(listModel.getCardList().getTitle());

        listTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTitleModel();
            }
        });
    }

    public void setListColorFXML(String color) {
        var colorCode = Color.valueOf(color);
        var fillTop = new Background(new BackgroundFill(colorCode, new CornerRadii(10, 10, 0, 0, false), null));
        listTop.setBackground(fillTop);

        var fillBottom = new Background(new BackgroundFill(colorCode, new CornerRadii(0, 0, 10, 10, false), null));
        listBottom.setBackground(fillBottom);

        var desaturated = colorCode.desaturate().desaturate();
        var desaturatedFill = new Background(new BackgroundFill(desaturated, null, null));
        cardListContainer.setBackground(desaturatedFill);
        scrollPane.setBackground(desaturatedFill);
    }
}