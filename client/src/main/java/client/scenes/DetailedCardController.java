package client.scenes;

import client.model.CardModel;
import client.utils.ServerUtils;
import commons.Tag;
import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class DetailedCardController {

    private CardController cardController;
    private Stage secondStage;
    private CardModel card;
    private ListController parent;

    private ServerUtils server;
    @FXML
    private TextArea cardDescription;

    @FXML
    private TextField cardTitle;

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

    public DetailedCardController(CardController cardController, CardModel card, ServerUtils server) {
        this.cardController = cardController;
        this.card = card;
        this.server = server;
    }

    public CardController getController() {
        return cardController;
    }

    @FXML
    void addSubtask(ActionEvent event) throws IOException {
        Task subtask = new Task("New Subtask");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Subtask.fxml"));

        var controller = new SubtaskController(subtask, card, server);
        loader.setController(controller);
        HBox newSubtask = loader.load();

        subtaskArea.getChildren().add(newSubtask);
        card.getCard().getSubTasks().add(subtask);
        //TODO properly add subtasks
    }

    @FXML
    void addTag(ActionEvent event) throws IOException {
//        Tag tag = new Tag("New tag");
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tag.fxml"));
//
//        var controller = new TagController(card,server);
//        loader.setController(controller);
//
//        AnchorPane newTag = loader.load();
//        tagArea.getChildren().add(0,newTag);

        // TODO open a tag editin menu to create or add new cards
    }

    @FXML
    void cancelButtonAction(ActionEvent event) {
        card.setController(cardController);
        boolean isUpdate = false;
        Stage secondStage = (Stage) detailedCardBox.getScene().getWindow();
        secondStage.close();
    }

    @FXML
    void saveButtonAction(ActionEvent event) {
        boolean isUpdate = true;
        Stage secondStage = (Stage) detailedCardBox.getScene().getWindow();
        secondStage.close();

        String newTitle = cardTitle.getText();
        String newDescription = cardDescription.getText();
        List<Task> subtasks = card.getCard().getSubTasks();
        Set<Tag> tags = card.getCard().getTags();

        card.setController(this.getController());

        card.updateCardDetails(isUpdate,newTitle, newDescription, subtasks,tags);
    }

    public void showDetails() {
        cardTitle.setText(card.getCard().getTitle());
        cardDescription.setText(card.getCard().getDescription());
        loadSubtasks();
        loadTags();
    }

    private void loadSubtasks() {
        for (Task subtask:
             card.getCard().getSubTasks()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Subtask.fxml"));
            var controller = new SubtaskController(subtask, card, server);
            loader.setController(controller);
            try {
                HBox existingSubtask = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void loadTags() {
        //TODO load tags properly
    }
}
