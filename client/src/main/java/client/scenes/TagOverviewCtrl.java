package client.scenes;

import client.utils.ServerUtils;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TagOverviewCtrl implements Initializable {
    @FXML
    private VBox tagArea;

    @FXML
    private VBox root;

    private ServerUtils server;

    MainPageCtrl parent;

    private List<OverviewTagCtrl> CardTagControllers = new ArrayList<>();

    public TagOverviewCtrl(MainPageCtrl parent){
        this.parent = parent;
        this.server = parent.getServer();
    }

    public void addTag() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagCreate.fxml"));
        loader.setController(new TagCreateController(this, new Tag()));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public void close(){
        Stage secondStage = (Stage) root.getScene().getWindow();
        secondStage.close();
    }

    public void showTags() throws IOException {
        tagArea.getChildren().clear();
        for (Tag tag : parent.getModel().getAllTags()) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("OverviewTag.fxml"));
            var controller = new OverviewTagCtrl(tag,this);
            loader.setController(controller);
            loader.load();

            tagArea.getChildren().add(loader.getRoot());
            CardTagControllers.add(controller);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showTags();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
