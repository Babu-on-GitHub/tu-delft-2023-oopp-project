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
        if (utils.chooseServer(userInput))
            ctrl.showMainPage();
        else
            serverTextField.setStyle("-fx-border-color: red;");
    }

    public ServerUtils getUtils() {
        return utils;
    }

    public MainCtrl getCtrl() {
        return ctrl;
    }

    public TextField getServerTextField() {
        return serverTextField;
    }

    public void setUtils(ServerUtils utils) {
        this.utils = utils;
    }

    public void setCtrl(MainCtrl ctrl) {
        this.ctrl = ctrl;
    }

    public void setServerTextField(TextField serverTextField) {
        this.serverTextField = serverTextField;
    }
}
