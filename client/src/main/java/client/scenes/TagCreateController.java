package client.scenes;

import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class TagCreateController {

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

    public TagCreateController(DetailedCardController parent) {
        this.parent = parent;
        this.tag = new Tag();
    }

    @FXML
    void save() throws IOException {
        tag.setTitle(title.getText());

        // yes, I am so proud of myself for this traversal. (again)
        tag = parent.getParent().getModel().getParent().getParent().addTag(tag);
        log.info("new tag id:" + tag.getId());

        parent.checkTag(tag);

        cancel();
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }
}
