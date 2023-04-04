package client.scenes;

import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class TagCreateController implements Initializable {

    private static Logger log = Logger.getLogger(TagCreateController.class.getName());

    private DetailedCardController parent;

    @FXML
    public TextField title;

    @FXML
    public VBox root;

    private Tag tag;

    public Tag getTag() {
        return tag;
    }

    public TagCreateController(DetailedCardController parent, Tag tag) {
        this.parent = parent;
        this.tag = tag;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setText(tag.getTitle());
    }

    @FXML
    void save() throws IOException {
        tag.setTitle(title.getText());

        // yes, I am so proud of myself for this traversal. (again)
        var root = parent.getParent().getModel().getParent().getParent();
        if (tag.getId() == 0) {
            tag = root.addTag(tag);
            parent.checkTag(tag);
        }
        else {
            tag = root.updateTag(tag);
            parent.reloadTag(tag);
        }

        log.info("new tag id:" + tag.getId());

        cancel();
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
