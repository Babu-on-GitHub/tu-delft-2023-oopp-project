package client.scenes;

import client.model.CardModel;
import client.utils.ServerUtils;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TagController implements Initializable {

    @FXML
    private Text title;

    @FXML
    private CheckBox status;

    @FXML
    private HBox tagRoot;

    private DetailedCardController parent;
    private Tag tag;

    private boolean initiallyChecked;

    public TagController(Tag tag, DetailedCardController parent, boolean checked) {
        this.parent = parent;
        this.tag = tag;
        this.initiallyChecked = checked;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setText(tag.getTitle());
        status.setSelected(initiallyChecked);
    }

    @FXML
    void toggleStatus(ActionEvent event) throws IOException {
        if (status.isSelected()) {
            parent.checkTag(tag);
        } else {
            parent.uncheckTag(tag);
        }
    }

    @FXML
    void deleteTag(ActionEvent event) throws IOException {
        parent.deleteTagWithController(this);

        // yes, I am so proud of myself for this traversal. (again)
        parent.getParent().getModel().getParent().getParent().deleteTag(tag);
    }

    public Tag getTag() {
        return tag;
    }

    public HBox getRoot() {
        return tagRoot;
    }

    public DetailedCardController getParent() {
        return parent;
    }
}

