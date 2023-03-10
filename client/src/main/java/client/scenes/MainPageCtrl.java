package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private Button deleteListButton;

    @FXML
    private Button addListButton;

    @FXML
    private Button addCardButton;

    @FXML
    private StackPane card;

    @FXML
    private VBox list;

    @FXML
    private HBox listList;

    @FXML
    private Button deleteCardButton;

    @Inject
    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");

        //deleteListButton.setGraphic(new FontIcon(Feather.TRASH));
        //deleteCardButton.setGraphic(new FontIcon(Feather.TRASH));

        //addCardButton.setGraphic(new FontIcon(Feather.PLUS));
        //addListButton.setGraphic(new FontIcon(Feather.PLUS));
    }

    public void refresh() {
        // do nothing
    }

    @FXML
    public void addCardButtonPress(ActionEvent event) throws IOException {
        System.out.println("test button click add card");

        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/client/scenes/Card.fxml"));
        loader.setController(this);
        StackPane newCard = loader.load();

        //StackPane originalCard = (StackPane) loader.getNamespace().get("#card");

        list.getChildren().add(newCard);
    }
    @FXML
    public void addListButton(ActionEvent event) throws IOException {
        System.out.println("test button click");

        FXMLLoader loader = new FXMLLoader(MainPageCtrl.class.getResource("List.fxml"));

        loader.setController(this);
        System.out.println("test button click 2");
        VBox newList = loader.load();
        listList.getChildren().add(newList);

    }

    // TODO: 08/03/2023  this is for the purpose of duplicating cards
    // a new class that will take care of duplicating cards is needed


}
