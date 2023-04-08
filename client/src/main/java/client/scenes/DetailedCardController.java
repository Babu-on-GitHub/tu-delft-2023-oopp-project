package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import commons.ColorPair;
import commons.Tag;
import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static client.tools.ColorTools.toHexString;

public class DetailedCardController implements Initializable {
    private static Logger log = Logger.getLogger(DetailedCardController.class.getName());

    private CardController parent;
    private Card localCard;
    private ServerUtils server;

    private List<SubtaskController> subtaskControllers = new ArrayList<>();
    private List<DetailedCardTagController> detailedCardTagControllers = new ArrayList<>();

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

    @FXML
    private ScrollPane scrollPaneTags;

    @FXML
    private ScrollPane scrollPaneSubTasks;

    @FXML
    private Label subtasksTitle;

    @FXML
    private Label tagsTitle;

    @FXML
    private Button addSubTaskButton;

    @FXML
    private Button addTagButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

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

        updateCardDetailColors();

        title.setText(localCard.getTitle());
        description.setText(localCard.getDescription());
        showSubtasks();
        showTags();
    }

    public void updateCardDetailColors(){
        setBackgroundColorFXML(parent.getParent().getParent().getBoardColor());
        setFontColorFXML(parent.getParent().getParent().getBoardColor());
    }

    public void setFontColorFXML(ColorPair color){
        var fontColor = Color.valueOf(color.getFont());
        var backgroundColor = Color.valueOf(color.getBackground());
        var styleStr = "-fx-text-fill: " + toHexString(fontColor) + "; -fx-background-color:" + backgroundColor + ";";

        title.setStyle(styleStr);
        tagsTitle.setStyle(styleStr);
        subtasksTitle.setStyle(styleStr);
    }

    public void setBackgroundColorFXML(ColorPair color){
        var colorCode = Color.valueOf(color.getBackground());
        var fill = new Background(new BackgroundFill(colorCode, null, null));
        detailedCardBox.setBackground(fill);
        title.setBackground(fill);

        var textBoxFill =  new Background(new BackgroundFill(colorCode.brighter(), null, null));

        description.setBackground(textBoxFill);
        description.setStyle("-fx-background-color: transparent;");
        subtaskArea.setBackground(textBoxFill);
        tagArea.setBackground(textBoxFill);
        scrollPaneTags.setBackground(textBoxFill);
        scrollPaneSubTasks.setBackground(textBoxFill);

        var fontColor = Color.valueOf(color.getFont());
        var styleStr = "-fx-text-fill: " + toHexString(fontColor) +
                "; -fx-background-color:" + toHexString(colorCode.darker()) + ";";

        addSubTaskButton.setStyle(styleStr);
        addTagButton.setStyle(styleStr);
        saveButton.setStyle(styleStr);
        cancelButton.setStyle(styleStr);
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
        detailedCardTagControllers.clear();

        // we want to firstly show all the tags that are already on the card
        for (Tag tag : localCard.getTags()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CardDetailsTag.fxml"));
            var controller = new DetailedCardTagController(tag, this, true);
            loader.setController(controller);
            loader.load();

            tagArea.getChildren().add(loader.getRoot());
            detailedCardTagControllers.add(controller);
        }

        // and then we want to show all the tags that are not on the card, but on the board
        for (Tag tag : parent.getModel().getAllTags()) {
            if (localCard.getTags().contains(tag)) continue;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("CardDetailsTag.fxml"));
            var controller = new DetailedCardTagController(tag, this, false);
            loader.setController(controller);
            loader.load();

            tagArea.getChildren().add(loader.getRoot());
            detailedCardTagControllers.add(controller);
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

    public void deleteTagWithController(DetailedCardTagController controller) throws IOException {
        detailedCardTagControllers.remove(controller);
        tagArea.getChildren().remove(controller.getRoot());
        localCard.getTags().remove(controller.getTag());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
