package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
public class CardController {


    @SuppressWarnings("unused")
    public CardController(){}

    @FXML
    public void deleteCardButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        var toDelete = pressed.getParent();
        var listOfToDelete = (VBox) toDelete.getParent();

        listOfToDelete.getChildren().remove(toDelete);
    }

}
