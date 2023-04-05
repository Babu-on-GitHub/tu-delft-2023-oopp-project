package client.scenes;

import client.utils.ServerUtils;
import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import client.model.CardModel;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.commons.lang3.NotImplementedException;

import static client.scenes.ListController.CARD_ID;
import static client.scenes.ListController.TARGET_INDEX;
import static client.scenes.ListController.TARGET_LIST;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardController implements Initializable {
    private Stage secondStage;
    private CardModel card;

    private ListController parent;

    private ServerUtils server;

    @FXML
    private AnchorPane cardContainer;

    @FXML
    private TextField cardTitle;

    @FXML
    private TextArea cardDescription;
    @FXML
    private VBox detailedCardBox;
    @FXML
    private VBox subtaskArea;

    @FXML
    private Button subtaskButton;

    @FXML
    private HBox tagArea;

    @FXML
    private Button tagButton;

    @SuppressWarnings("unused")
    public CardController() {
    }

    public ListController getParent() {
        return parent;
    }

    @FXML
    private VBox cardListContainer;

    public CardController(CardModel card, ListController parent, ServerUtils server) {
        this.card = card;
        this.parent = parent;
        this.server = server;
    }

    public CardModel getModel() {
        return card;
    }

    @FXML
    void checkDoubleClick(MouseEvent event) throws IOException {
        if (event.getClickCount() == 2) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailedCard.fxml"));
            var detailedCardController = new DetailedCardController(this, server);
            card.setDetailedController(detailedCardController);
            loader.setController(detailedCardController);

            Parent root = loader.load();
            secondStage = new Stage();
            secondStage.setScene(new Scene(root));
            secondStage.initOwner(cardContainer.getScene().getWindow());
            secondStage.show();

            getModel().getParent().getParent().update();
            detailedCardController.showDetails();
        }
    }

    @FXML
    public void dragDetected(MouseEvent event) {

        Dragboard dragboard = cardContainer.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(CARD_ID, card.getCard().getId());

        dragboard.setContent(content);
        event.consume();
    }

    @FXML
    void dragDone(DragEvent event) throws IOException {
        var dragboard = event.getDragboard();
        if (event.getGestureTarget() != null &&
                dragboard.getContent(CARD_ID) != null &&
                dragboard.getContent(TARGET_INDEX) != null &&
                dragboard.getContent(TARGET_LIST) != null) {

            Integer index = (Integer) dragboard.getContent(TARGET_INDEX);
            if (index == null)
                throw new NotImplementedException("dragDone: index is null");
            Long cardId = (Long) dragboard.getContent(CARD_ID);
            if (cardId == null)
                throw new NotImplementedException("dragDone: cardId is null");
            Long targetListId = (Long) dragboard.getContent(TARGET_LIST);
            if (targetListId == null)
                throw new NotImplementedException("dragDone: targetList is null");

            var board = getParent().getParent();
            var model = board.getModel();

            // find a list with the same id as target list in model.getChildren()
            var targetListOpt = model.getChildren().stream()
                    .filter(list -> targetListId.equals(list.getCardList().getId()))
                    .findFirst();

            if (targetListOpt.isEmpty())
                return;

            var targetList = targetListOpt.get();
            var newParentController = targetList.getController();

            parent.getCardsContainer().getChildren().remove(cardContainer);
            newParentController.moveCard(
                    card,
                    index
            );
            parent = newParentController;

            model.moveCard(
                    card,
                    targetList,
                    index
            );
        }
    }

    public void deleteCardButton(ActionEvent event) {
        deleteCard();
    }

    public void deleteCard() {
        card.deleteCard();
        parent.getCardsContainer().getChildren().remove(cardContainer);
    }

    public void overwriteTitleNode(String text) {
        cardTitle.setText(text);
    }

    public void updateTitleModel() {
        card.updateTitle(cardTitle.getText());
    }

    public void updateDescription(String newDescription) {
        if (newDescription == null) {
            //TODO remove indicator
        }
        //TODO add indicator that a card has description if not null
    }

    public void highlightBottom() {
        cardContainer.getStyleClass().remove("default-border");
        // highlight the bottom border of the card
        cardContainer.getStyleClass().add("highlight-bottom");
    }

    public void highlightTop() {
        cardContainer.getStyleClass().remove("default-border");
        // highlight the top border of the card
        cardContainer.getStyleClass().add("highlight-top");
    }

    public void highlightReset() {
        // reset the border of the card
        cardContainer.getStyleClass().remove("highlight-bottom");
        cardContainer.getStyleClass().remove("highlight-top");

        cardContainer.getStyleClass().add("default-border");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardTitle.setText(card.getCard().getTitle());

        cardTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTitleModel();
            }
        });
    }

    protected String getTasksProgress() {
        // number of subtasks completed + "/" + number of subtasks
        return card.getCard().getSubTasks().stream()
                .filter(Task::isChecked)
                .count() + "/" + card.getCard().getSubTasks().size();
    }

    public void setCardColorFXML(String color) {
        var colorCode = Color.valueOf(color);
        var fill = new Background(new BackgroundFill(colorCode, new CornerRadii(20), null));
        cardContainer.setBackground(fill);
    }
}
