package client.scenes;

import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class OverviewTagCtrl implements Initializable {

    private TagOverviewCtrl tagOverviewCtrl;

    public TagOverviewCtrl getTagOverviewCtrl() {
        return tagOverviewCtrl;
    }

    private Tag tag;

    @FXML
    private Text title;

    @FXML
    private AnchorPane tagRoot;

    public OverviewTagCtrl(Tag tag, TagOverviewCtrl tagOverviewCtrl) {
        this.tag = tag;
        this.tagOverviewCtrl = tagOverviewCtrl;
    }

    public void editTag() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagCreate.fxml"));
        loader.setController(new TagCreateController(this.tagOverviewCtrl, tag));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public void deleteTag() throws IOException {
        tagOverviewCtrl.parent.getModel().deleteTag(this.tag);
        tagOverviewCtrl.showTags();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tagRoot.setStyle("-fx-background-color: " + tag.getColorPair().getBackground() + ";");
        title.setText(tag.getTitle());
        title.setStyle("-fx-fill: " + tag.getColorPair().getFont() + ";");
    }
}
