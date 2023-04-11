package client.scenes;

import client.model.ListModel;
import client.utils.ServerUtils;
import commons.ColorPair;
import commons.Tag;
import commons.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
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
import static client.utils.ColorTools.toHexString;
import static client.utils.ImageTools.recolorImage;
import static client.utils.SceneTools.applyToEveryNode;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class CardController implements Initializable {

    static final Logger log = Logger.getLogger(CardController.class.getName());

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

    @FXML
    private Label descLabel;

    @FXML
    private Label subtaskInfo;

    @FXML
    private HBox tagBar;


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
            showDetailedCardScene(false, false);
        }
    }

    void showDetailedCardScene(boolean showOnlyTag,
                               boolean showOnlyColors) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailedCard.fxml"));
        var detailedCardController = new DetailedCardController(this, server);
        card.setDetailedController(detailedCardController);
        loader.setController(detailedCardController);

        Parent root = loader.load();
        secondStage = new Stage();
        secondStage.setScene(new Scene(root));
        secondStage.initOwner(cardContainer.getScene().getWindow());

        detailedCardController.setToBeSave(showOnlyTag, showOnlyColors);

        if (showOnlyTag || showOnlyColors) {
            detailedCardController.hideProperties(showOnlyColors);
        }

        secondStage.show();

        getModel().getParent().getParent().update();
        detailedCardController.showDetails();
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

    @FXML
    void keyPress(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.BACK_SPACE || event.getCode() == KeyCode.DELETE) {
            deleteCard();
        }
        if (event.getCode() == KeyCode.E && !event.isShortcutDown()) {
            Platform.runLater(() -> {
                cardTitle.requestFocus();
                cardTitle.setText(card.getCard().getTitle());
            });
        }
        if (event.getCode() == KeyCode.ENTER) {
            showDetailedCardScene(false, false);
        }
        if (event.getCode() ==KeyCode.T) {
            showDetailedCardScene(true, false);
        }
        if (event.getCode() ==KeyCode.C) {
            showDetailedCardScene(false, true);
        }
        up(event);
        down(event);
        right(event);
        left(event);
    }

    private void right(KeyEvent event){
        if(event.getCode() == KeyCode.RIGHT){
            var l = card.getParent();
            var b = card.getParent().getParent();
            var lists = b.getChildren();
            int lIndex = lists.indexOf(l);
            for(int i = lIndex +1; i< lists.size(); i++){
                if(lists.get(i).getChildren()!=null){
                    for(CardModel c: lists.get(i).getChildren()){
                        c.getController().focus();
                        return;
                    }
                }
            }
        }
    }

    private void left(KeyEvent event){
        if(event.getCode() == KeyCode.LEFT){
            var l = card.getParent();
            var b = card.getParent().getParent();
            var lists = b.getChildren();
            int lIndex = lists.indexOf(l);
            for(int i = lIndex -1; i>=0; i--){
                if(lists.get(i).getChildren()!=null){
                    for(CardModel c: lists.get(i).getChildren()){
                        c.getController().focus();
                        return;
                    }
                }
            }
        }
    }


    private void down(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.DOWN){
            int index = card.getParent().getCardList().getCards().indexOf(this.card.getCard());
            if(index>=card.getParent().getCardList().getCards().size()-1) return;
            if(event.isShiftDown()){
                var board = getParent().getParent();
                var model = board.getModel();

                ListModel targetList = this.getModel().getParent();
                var newParentController = targetList.getController();

                parent.getCardsContainer().getChildren().remove(cardContainer);
                newParentController.moveCard(
                        card,
                        index+1
                );
                parent = newParentController;

                model.moveCard(
                        card,
                        targetList,
                        index+1
                );

                card.getController().focus();
            }else{
                long id = card.getParent().getCardList().getCards().get(index+1).getId();
                CardController c = card.getParent().getChildren().stream()
                        .filter(q->q.getCard().getId()==id)
                        .findFirst().
                        get().getController();
                c.focus();
            }
        }
    }

    private void up(KeyEvent event) throws IOException {
        if(event.getCode() == KeyCode.UP){
            int index = card.getParent().getCardList().getCards().indexOf(this.card.getCard());
            if(index<=0) return;
            if(event.isShiftDown()){
                var board = getParent().getParent();
                var model = board.getModel();

                ListModel targetList = this.getModel().getParent();
                var newParentController = targetList.getController();

                parent.getCardsContainer().getChildren().remove(cardContainer);
                newParentController.moveCard(
                        card,
                        index-1
                );
                parent = newParentController;

                model.moveCard(
                        card,
                        targetList,
                        index-1
                );

                card.getController().focus();
            }else{
                long id = card.getParent().getCardList().getCards().get(index-1).getId();
                CardController c = card.getParent().getChildren().stream()
                        .filter(q->q.getCard().getId()==id)
                        .findFirst().
                        get().getController();
                c.focus();
            }
        }
    }

    @FXML
    void highlight(MouseEvent event) {
        focus();
    }

    public void focus(){
        cardContainer.requestFocus();
    }

    public void deleteCardButton(ActionEvent event) {
        deleteCard();
    }

    public void deleteCard() {
        card.deleteCard();
        parent.getCardsContainer().getChildren().remove(cardContainer);
    }

    public void overwriteWithModel() {
        try {
            cardTitle.setText(card.getCard().getTitle());
            descLabel.setText(card.getCard().getDescription());
            updateSubTaskInfo();
            showTags();
            updateCardColors(card.getParent().getParent().getController().getCardColor(card.getCard().getId()));
            log.info("Overwrote card " + card.getCard().getId() + " with model");
        } catch (Exception e) {
            log.info("Ignoring overwrite since not initialized yet (" + card.getCard().getId() + ")");
        }
    }
    public void updateSubTaskInfo() {
        if (card.getCard().getSubTasks() == null || card.getCard().getSubTasks().size() == 0) {
            subtaskInfo.setText("");
        } else {
            subtaskInfo.setText(getTasksProgress());
        }
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

    public void showTags() throws IOException {
        try{
            tagBar.getChildren().clear();
            for (Tag tag : card.getCard().getTags()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("TagBarTag.fxml"));
                var controller = new TagBarTagCtrl(tag);
                loader.setController(controller);

                Node newTag = loader.load();
                tagBar.getChildren().add(newTag);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardTitle.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                updateTitleModel();
            }
        });

        overwriteWithModel();
    }

    public String getTasksProgress() {
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

    public void setFontColorFXML(String color) {
        var colorCode = Color.valueOf(color);
        cardTitle.setStyle("-fx-text-fill: " + toHexString(colorCode) + "; -fx-background-color: inherit;");
        descLabel.setTextFill(colorCode);
        subtaskInfo.setTextFill(colorCode);
    }

    public void updateCardColors(ColorPair cardColor) {
        setCardColorFXML(cardColor.getBackground());
        setFontColorFXML(cardColor.getFont());
        updateIcons();
    }

    public void updateIcons() {
        applyToEveryNode(cardContainer, (Node x) -> {
            if (x instanceof ImageView settable) {
                var color = getParent().getParent().getCardColor(card.getCard().getId()).getFont();
                settable.setImage(recolorImage(settable.getImage(), Color.valueOf(color)));
            }
        });
    }

}
