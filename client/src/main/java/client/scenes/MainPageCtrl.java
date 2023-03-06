package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

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
    private Button deleteCardButton;

    @Inject
    public MainPageCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardName.setText("Default board");

        deleteListButton.setGraphic(new FontIcon(Feather.TRASH));
        deleteCardButton.setGraphic(new FontIcon(Feather.TRASH));

        addCardButton.setGraphic(new FontIcon(Feather.PLUS));
    }

    public void refresh() {
        // do nothing
    }
}
