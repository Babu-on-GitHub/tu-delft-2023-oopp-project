package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageCtrl implements Initializable {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    @FXML
    private TextField boardName;

    @FXML
    private HBox listOfLists;

    @Inject
    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");

//        deleteListButton.setGraphic(new FontIcon(Feather.TRASH));
//        deleteCardButton.setGraphic(new FontIcon(Feather.TRASH));
//
//        addCardButton.setGraphic(new FontIcon(Feather.PLUS));
//        addListButton.setGraphic(new FontIcon(Feather.PLUS));
    }

    public void refresh() {
        // do nothing
    }

    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("List.fxml"));
        loader.setController(this);
        VBox newList = loader.load();

        listOfLists.getChildren().add(newList);
    }

    @FXML
    public void addCardButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        StackPane newCard = loader.load();

        Button pressed = (Button) event.getSource();
        VBox wholeList = (VBox) pressed.getParent().getParent();
        VBox listVbox = (VBox) ((ScrollPane) wholeList.getChildren().get(2)).getContent();

        listVbox.getChildren().add(newCard);
    }

    @FXML
    public void deleteCardButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        StackPane toDelete = (StackPane) pressed.getParent().getParent().getParent();
        VBox listOfToDelete = (VBox) toDelete.getParent();

        listOfToDelete.getChildren().remove(toDelete);
    }

    @FXML
    public void deleteListButton(ActionEvent event) {
        Button pressed = (Button) event.getSource();

        var toDelete = (VBox) pressed.getParent().getParent();
        var listOfLists = (HBox) toDelete.getParent();

        listOfLists.getChildren().remove(toDelete);
    }



}
