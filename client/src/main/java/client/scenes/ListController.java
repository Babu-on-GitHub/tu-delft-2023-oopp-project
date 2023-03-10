package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ListController {
    @FXML
    private VBox list;
    @FXML
    public void addCardButtonPress(ActionEvent event) throws IOException {
        System.out.println("test button click");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        System.out.println("test button click 2");
        list.getChildren().add(loader.load());

    }
}
