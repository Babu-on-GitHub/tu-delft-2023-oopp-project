package client.scenes;

import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TagBarTagCtrl implements Initializable {

    @FXML
    private AnchorPane root;

    private Tag tag;

    public TagBarTagCtrl(Tag tag) {
        this.tag = tag;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setStyle("-fx-background-color: "+tag.getColor()+";");
    }
}
