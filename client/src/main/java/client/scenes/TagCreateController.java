package client.scenes;

import client.model.BoardModel;
import commons.ColorPair;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static client.tools.ColorTools.toHexString;

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
    public ColorPicker backgroundColorPicker;

    @FXML
    public ColorPicker fontColorPicker;

    @FXML
    public Label fontText;

    @FXML
    public Label bgText;

    @FXML
    public Button cancel;

    @FXML
    public Button save;

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
        String bgColor = tag.getColorPair().getBackground();
        String fontColor = tag.getColorPair().getFont();
        if(bgColor!=null) backgroundColorPicker.setValue(Color.web(bgColor));
        if(fontColor!=null) fontColorPicker.setValue(Color.web(fontColor));
        updateColors(tagOverviewCtrl.parent.getBoardColor());
    }

    @FXML
    void save() throws IOException {
        tag.setTitle(title.getText());
        Color bgColor = backgroundColorPicker.getValue();
        Color fontColor = fontColorPicker.getValue();
        tag.getColorPair().setBackground(toHexString(bgColor));
        tag.getColorPair().setFont(toHexString(fontColor));

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

    private void updateColors(ColorPair colorPair) {
        setTagCreateFontFXML(colorPair.getFont());
        setTagCreateBackgroundFXML(colorPair.getBackground());
    }

    private void setTagCreateFontFXML(String color) {
        var colorCode = Color.valueOf(color);

        var styleStr = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" +
                "-fx-border-radius: 10px !important; " ;
        var styleStrWithoutBorder = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" ;
        bgText.setStyle(styleStrWithoutBorder);
        fontText.setStyle(styleStrWithoutBorder);
        cancel.setStyle(styleStr);
        save.setStyle(styleStr);
        fontColorPicker.setStyle(styleStrWithoutBorder);
        backgroundColorPicker.setStyle(styleStrWithoutBorder);
        title.setStyle(styleStrWithoutBorder);
    }

    public void setTagCreateBackgroundFXML(String color) {
        var colorCode = Color.valueOf(color);
        var darker = colorCode.darker();
        var fill = new Background(new BackgroundFill(colorCode, null, null));
        var darkerFill = new Background(new BackgroundFill(darker, new CornerRadii(10), null));
        cancel.setBackground(darkerFill);
        save.setBackground(darkerFill);
        fontColorPicker.setBackground(darkerFill);
        backgroundColorPicker.setBackground(darkerFill);
        title.setBackground(darkerFill);
        root.setBackground(fill);
        bgText.setBackground(fill);
        fontText.setBackground(fill);
    }
}
