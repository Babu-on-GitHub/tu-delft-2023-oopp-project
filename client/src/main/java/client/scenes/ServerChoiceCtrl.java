package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerChoiceCtrl implements Initializable {
    private ServerUtils utils;

    private MainCtrl ctrl;

    @FXML
    private TextField serverTextField;

    @FXML
    private AnchorPane adminPanel;

    @FXML
    private CheckBox adminCheckBox;

    private boolean admin;

    @FXML
    private TextField adminPassword;

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


    public void adminPanelControl(){
        admin = adminCheckBox.isSelected();
        adminPanel.setVisible(adminCheckBox.isSelected());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminCheckBox.setSelected(false);
        admin = false;
        adminPanel.setVisible(false);
    }
}
