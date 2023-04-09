package client.scenes;

import client.utils.ServerUtils;
import commons.ColorPair;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static client.utils.ColorTools.toHexString;

public class TagOverviewCtrl implements Initializable {
    @FXML
    private VBox tagArea;

    @FXML
    private VBox root;

    @FXML
    private Button done;

    @FXML
    private AnchorPane tagOverviewButton;

    @FXML
    private Text title;

    @FXML
    private Button addTagButton;

    @FXML
    private ScrollPane tagsScrollPane;

    @FXML
    private AnchorPane bottom;

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

        updateColors(parent.getBoardColor());
    }

    private void updateColors(ColorPair colorPair) {
        setTagOverviewFontFXML(colorPair.getFont());
        setTagOverviewBackgroundFXML(colorPair.getBackground());
    }

    private void setTagOverviewFontFXML(String color) {
        var colorCode = Color.valueOf(color);

        var styleStr = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" +
                "-fx-border-radius: 10px !important; " ;
        var styleStrWithoutBorder = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" ;
        addTagButton.setStyle(styleStr);
        done.setStyle(styleStr);
        title.setStyle("-fx-fill: " + toHexString(colorCode) + " !important; ");
    }

    public void setTagOverviewBackgroundFXML(String color) {
        var colorCode = Color.valueOf(color);
        var darker = colorCode.darker();
        var lighter = colorCode.desaturate().desaturate();
        var fill = new Background(new BackgroundFill(colorCode, null, null));
        var darkerFill = new Background(new BackgroundFill(darker, new CornerRadii(10), null));
        var darkerFillNotRound = new Background(new BackgroundFill(darker, null, null));
        var lighterFill = new Background(new BackgroundFill(lighter, new CornerRadii(20), null));

        addTagButton.setBackground(darkerFill);
        done.setBackground(darkerFill);
        root.setBackground(fill);
        tagsScrollPane.setBackground(lighterFill);
        bottom.setBackground(fill);
    }
}
