package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DetailedCardController {
    private static Logger log = Logger.getLogger(DetailedCardController.class.getName());

    private CardController parent;
    private Card localCard;
    private ServerUtils server;

    private List<SubtaskController> subtaskControllers = new ArrayList<>();
    private List<TagController> tagControllers = new ArrayList<>();

    @FXML
    private TextArea description;

    @FXML
    private TextField title;

    @FXML
    private VBox detailedCardBox;

    @FXML
    private VBox subtaskArea;

    @FXML
    private VBox tagArea;

    public DetailedCardController(CardController cardController, ServerUtils server) {
        cardController.getModel().update(); // just in case

        this.parent = cardController;
        try {
            this.localCard = (Card) cardController.getModel().getCard().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        this.server = server;

        subtaskControllers = new ArrayList<>();
    }

    /**
     * Adds a new subtask to the card. This is called by the FXML event listener, for this controller.
     * @param event the event that triggered this method
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    void addTask(ActionEvent event) throws IOException {
        Task subtask = new Task();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Subtask.fxml"));

        var controller = new SubtaskController(subtask, this);
        loader.setController(controller);
        HBox newSubtask = loader.load();

        subtaskControllers.add(controller);

        subtaskArea.getChildren().add(newSubtask);
    }

    @FXML
    void createTag(ActionEvent event) throws IOException {
        // open new window, with layout of TagCreate.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagCreate.fxml"));
        loader.setController(new TagCreateController(this, new Tag()));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    /**
     * Removes a subtask from the card. This is called by the subtask controller when the user clicks the delete button.
     * @param controller the controller containing task to remove
     */
    @FXML
    void removeSubtaskWithController(SubtaskController controller) {
        subtaskControllers.remove(controller);
        subtaskArea.getChildren().remove(controller.getRoot());
    }

    /**
     * Closes the detailed card view. Since all the changes are stored locally, we don't need to do anything else.
     * @param event the event that triggered this action
     */
    @FXML
    void cancel(ActionEvent event) {
        Stage secondStage = (Stage) detailedCardBox.getScene().getWindow();
        secondStage.close();
    }

    /**
     * Saves the changes made to the card. This includes just overwriting
     *          the state of the card in the parent controller with the local copy.
     * @param event the event that triggered this action
     */
    @FXML
    void save(ActionEvent event) {
        log.info("Save button pressed, closing..");
        cancel(event);

        localCard.setTitle(title.getText());
        localCard.setDescription(description.getText());

        List<Task> subtasks = new ArrayList<>();
        for (SubtaskController controller : subtaskControllers)
            subtasks.add(controller.getTask());
        localCard.setSubTasks(subtasks);


        parent.getModel().overwriteWith(localCard);
    }

    public void showDetails() throws IOException {
        log.info("Showing details");

        title.setText(localCard.getTitle());
        description.setText(localCard.getDescription());
        showSubtasks();
        showTags();
    }

    private void showSubtasks() throws IOException {
        subtaskArea.getChildren().clear();
        for (Task subtask : localCard.getSubTasks()) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Subtask.fxml"));
            var controller = new SubtaskController(subtask, this);
            loader.setController(controller);
            loader.load();

            subtaskArea.getChildren().add(loader.getRoot());
            subtaskControllers.add(controller);
        }
    }

    public void reloadTag(Tag tag) throws IOException {
        // replace the tag in localCard with the new one
        // remove the tag with id of tag
        localCard.getTags().removeIf(t -> t.getId() == tag.getId());
        localCard.getTags().add(tag);

        showTags();
    }

    public void showTags() throws IOException {
        tagArea.getChildren().clear();
        tagControllers.clear();

        // we want to firstly show all the tags that are already on the card
        for (Tag tag : localCard.getTags()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tag.fxml"));
            var controller = new TagController(tag, this, true);
            loader.setController(controller);
            loader.load();

            tagArea.getChildren().add(loader.getRoot());
            tagControllers.add(controller);
        }

        // and then we want to show all the tags that are not on the card, but on the board
        for (Tag tag : parent.getModel().getAllTags()) {
            if (localCard.getTags().contains(tag)) continue;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tag.fxml"));
            var controller = new TagController(tag, this, false);
            loader.setController(controller);
            loader.load();

            tagArea.getChildren().add(loader.getRoot());
            tagControllers.add(controller);
        }
    }

    public CardController getParent() {
        return parent;
    }

    public void moveUp(SubtaskController controller) {
        int index = subtaskControllers.indexOf(controller);
        if (index == 0) return;

        subtaskControllers.remove(index);
        subtaskControllers.add(index - 1, controller);

        subtaskArea.getChildren().clear();
        for (SubtaskController subtaskController : subtaskControllers)
            subtaskArea.getChildren().add(subtaskController.getRoot());
    }

    public void moveDown(SubtaskController controller) {
        int index = subtaskControllers.indexOf(controller);
        if (index == subtaskControllers.size() - 1) return;

        subtaskControllers.remove(index);
        subtaskControllers.add(index + 1, controller);

        subtaskArea.getChildren().clear();
        for (SubtaskController subtaskController : subtaskControllers)
            subtaskArea.getChildren().add(subtaskController.getRoot());
    }

    public void checkTag(Tag tag) throws IOException {
        if (localCard.getTags().contains(tag)) {
            log.warning("Tag already checked");
            return;
        }

        localCard.getTags().add(tag);
        showTags();
    }

    public void uncheckTag(Tag tag) throws IOException {
        if (!localCard.getTags().contains(tag)) {
            log.warning("Tag already unchecked");
        }

        // remove tag with the same id as tag
        localCard.getTags().removeIf(t -> t.getId() == tag.getId());

        showTags();
    }

    public void deleteTagWithController(TagController controller) throws IOException {
        tagControllers.remove(controller);
        tagArea.getChildren().remove(controller.getRoot());
        localCard.getTags().remove(controller.getTag());
    }
}
