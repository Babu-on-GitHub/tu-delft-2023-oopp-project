package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import commons.Tag;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DetailedCardTagController implements Initializable {

    @FXML
    private Text title;

    @FXML
    private CheckBox status;

    @FXML
    private HBox tagRoot;

    private DetailedCardController detailedCardController;
    private Tag tag;

    private boolean initiallyChecked;

    public DetailedCardTagController(Tag tag, DetailedCardController parent, boolean checked) {
        this.detailedCardController = parent;
        this.tag = tag;
        this.initiallyChecked = checked;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setText(tag.getTitle());
        tagRoot.setStyle("-fx-background-color: "+tag.getColor()+";");

        status.setSelected(initiallyChecked);
        statusHint(initiallyChecked);
    }

    @FXML
    void toggleStatus(ActionEvent event) throws IOException {
        statusHint(status.isSelected());

        if (status.isSelected()) {
            detailedCardController.checkTag(tag);
        } else {
            detailedCardController.uncheckTag(tag);
        }
    }

    @FXML
    void deleteTag(ActionEvent event) throws IOException {
        detailedCardController.deleteTagWithController(this);

        // yes, I am so proud of myself for this traversal. (again)
        detailedCardController.getParent().getModel().getParent().getParent().deleteTag(tag);
    }

    @FXML
    void editTag(ActionEvent event) throws IOException {
        // open new window, with layout of TagCreate.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagCreate.fxml"));
        loader.setController(new TagCreateController(detailedCardController, tag));

        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public Tag getTag() {
        return tag;
    }

    public HBox getRoot() {
        return tagRoot;
    }

    public DetailedCardController getDetailedCardController() {
        return detailedCardController;
    }

    private void statusHint(boolean checked) {
        if (checked) {
            tagRoot.getStyleClass().remove("checked");
        } else {
            tagRoot.getStyleClass().add("checked");
        }
    }
}

