package client.scenes;

import client.model.BoardModel;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class TagCreateController implements Initializable {

    private static Logger log = Logger.getLogger(TagCreateController.class.getName());

    private DetailedCardController detailedCardController;

    private TagOverviewCtrl tagOverviewCtrl;

    private BoardModel boardModel;

    @FXML
    public TextField title;

    @FXML
    public AnchorPane root;

    @FXML
    public ColorPicker colorPicker;

    private Tag tag;

    public TagCreateController(TagOverviewCtrl t, Tag tag) {
        this.tagOverviewCtrl = t;
        this.tag = tag;
        this.boardModel = tagOverviewCtrl.parent.getModel();
    }

    public Tag getTag() {
        return tag;
    }

    public TagCreateController(DetailedCardController detailedCardController, Tag tag) {
        this.detailedCardController = detailedCardController;
        this.tag = tag;
        this.boardModel = detailedCardController.getParent().getModel().getParent().getParent();
    }

    public TagCreateController(BoardModel boardModel, Tag tag){
        this.boardModel = boardModel;
        this.tag = tag;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.setText(tag.getTitle());
        String color = tag.getColor();
        if(color!=null) colorPicker.setValue(Color.web(color));
    }

    @FXML
    void save() throws IOException {
        tag.setTitle(title.getText());
        Color color = colorPicker.getValue();
        tag.setColor(toHexString(color));

        if(detailedCardController!=null){
            if (tag.getId() == 0) {
                tag = boardModel.addTag(tag);
                detailedCardController.checkTag(tag);
            }
            else {
                tag = boardModel.updateTag(tag);
                detailedCardController.reloadTag(tag);
            }
        }

        if(tagOverviewCtrl!=null){
            if (tag.getId() == 0) {
                tag = boardModel.addTag(tag);
            }
            else {
                tag = boardModel.updateTag(tag);
            }
            tagOverviewCtrl.showTags();
        }

        cancel();
    }

    @FXML
    void cancel() {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.close();
    }

    private String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    public String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue()));
    }
}
