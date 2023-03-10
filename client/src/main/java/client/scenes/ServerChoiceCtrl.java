package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

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



    public void handleConnectButton() throws IOException {
        this.utils = new ServerUtils();
        String userInput =serverTextField.getText();
        userInput = utils.choseServer(userInput);
        if (userInput.equals("successfully connected")) {
            ctrl.showMainPage();
        }
    }


}
