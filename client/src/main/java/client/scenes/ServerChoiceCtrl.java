package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ServerChoiceCtrl {
    private Stage primaryStage;
    private ServerChoiceCtrl serverChoicePage;
    private Scene resources;

    public void initialize(Stage primaryStage, Pair<ServerChoiceCtrl, Parent> resources) {
        this.primaryStage = primaryStage;
        this.serverChoicePage = resources.getKey();
        this.resources = new Scene(resources.getValue());

        showMainPage();
        primaryStage.show();
    }

    private void showMainPage() {
        primaryStage.setTitle("Board Overview");
        primaryStage.setScene(resources);
        //serverChoicePage.refresh();
    }

    @FXML
    private void handleConnectButton(ActionEvent event) {
        // TODO
    }

    @FXML
    private void handleExitButton(ActionEvent event) {
        primaryStage.close();
    }


}
