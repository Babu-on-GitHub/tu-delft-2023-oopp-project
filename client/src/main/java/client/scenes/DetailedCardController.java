package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private List<SubtaskController> subtaskControllers;

    @FXML
    private TextArea description;

    @FXML
    private TextField title;

    @FXML
    private VBox detailedCardBox;

    @FXML
    private VBox subtaskArea;

    @FXML
    private HBox tagArea;

    public DetailedCardController(CardController cardController, ServerUtils server) {
        this.parent = cardController;
        this.localCard = cardController.getModel().getCard();
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

    /**
     * Removes a subtask from the card. This is called by the subtask controller when the user clicks the delete button.
     * @param controller the controller containing task to remove
     */
    @FXML
    void removeSubtaskWithController(SubtaskController controller) {
        subtaskControllers.remove(controller);
        subtaskArea.getChildren().remove(controller.getRoot());
    }

    @FXML
    void addTag(ActionEvent event) throws IOException {
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
        for (Task subtask : localCard.getSubTasks()) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Subtask.fxml"));
            var controller = new SubtaskController(subtask, this);
            loader.setController(controller);
            loader.load();

            subtaskArea.getChildren().add(loader.getRoot());
            subtaskControllers.add(controller);
        }
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

    private void showTags() {
        //TODO load tags properly
    }
}
