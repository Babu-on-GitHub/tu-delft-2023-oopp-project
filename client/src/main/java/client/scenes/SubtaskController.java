package client.scenes;

import commons.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import static client.utils.ColorTools.toHexString;
import static client.utils.SceneTools.applyToEveryNode;

public class SubtaskController implements Initializable {
    private DetailedCardController parent;
    private Task task;

    @FXML
    private CheckBox status;

    @FXML
    private TextField title;

    @FXML
    private HBox subtaskRoot;

    public HBox getRoot() {
        return subtaskRoot;
    }

    public Task getTask() {
        return task;
    }

    public SubtaskController(Task task, DetailedCardController parent) {
        this.parent = parent;
        this.task = task;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateSelected();

        title.setText(task.getTitle());

        title.textProperty().addListener((observable, oldValue, newValue) -> {
            task.setTitle(newValue);
        });
        title.setFocusTraversable(true);

        if (Objects.equals(title.getText(), (new Task()).getTitle())) {
            title.requestFocus();
        }

        var card = parent.getParent();
        var board = card.getParent().getParent();
        var backColor = board.getBoardColor().getBackground();
        var fontColor = board.getBoardColor().getFont();

        var styleStr = "-fx-background-color: " + toHexString(Color.valueOf(backColor)) +
                "; -fx-text-fill: " + toHexString(Color.valueOf(fontColor)) + ";";
        applyToEveryNode(subtaskRoot, node -> {
            if (node instanceof Button btn) {
                btn.setStyle(styleStr);
            }
        });
    }

    @FXML
    void deleteSubtask(ActionEvent event) {
        parent.removeSubtaskWithController(this);
    }

    @FXML
    void moveUp(ActionEvent event) {
        subtaskRoot.requestFocus();
        parent.moveUp(this);
    }

    @FXML
    void moveDown(ActionEvent event) {
        subtaskRoot.requestFocus();
        parent.moveDown(this);
    }

    @FXML
    void toggleStatus(ActionEvent event) {
        task.setChecked(status.isSelected());
        updateSelected();
    }

    @FXML
    void updateTitle(ActionEvent event) {
        task.setTitle(title.getText());
    }

    private void updateSelected() {
        status.setSelected(task.isChecked());
        if (status.isSelected()) {
            title.setDisable(true);
            title.setEditable(false);
        } else {
            title.setDisable(false);
            title.setEditable(true);
        }
    }
}
