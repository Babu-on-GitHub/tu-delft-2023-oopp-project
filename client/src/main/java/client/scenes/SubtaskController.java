package client.scenes;

import client.model.CardModel;
import client.utils.ServerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class SubtaskController {

    private ServerUtils server;
    private CardModel parent;
    @FXML
    private Button deleteSubtaskButton;

    @FXML
    private CheckBox subtaskContent;

    @FXML
    private HBox subtaskID;
    public SubtaskController(CardModel parent, ServerUtils server) {
        this.parent = parent;
        this.server = server;
    }
    @FXML
    void deteleSubtask(ActionEvent event) {
        //TODO delete specific subtask
    }

    @FXML
    void toggleStatus(ActionEvent event) {
        //TODO toggle check status of the tag
    }

}
