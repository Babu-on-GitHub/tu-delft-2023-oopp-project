package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelpScreenController {

    @FXML
    private Button okButton;

    @FXML
    void okPressed(ActionEvent event) {
        Stage secondStage = (Stage) okButton.getScene().getWindow();
        secondStage.close();
    }

}
