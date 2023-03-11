package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerChoiceCtrl {
    private ServerUtils utils;

    private MainCtrl ctrl;

    @FXML
    private TextField serverTextField;

    @Inject
    public ServerChoiceCtrl(ServerUtils utils, MainCtrl ctrl) {
        this.utils = utils;
        this.ctrl = ctrl;
    }

    public void handleConnectButton() {
        String userInput = serverTextField.getText();
        try {
            if (utils.chooseServer(userInput))
                ctrl.showMainPage();
        }
        catch (Exception e) {
            serverTextField.setStyle("-fx-border-color: red;");
        }
    }
}
