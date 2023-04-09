package client.scenes;

import commons.BoardIdWithColors;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import client.utils.ServerUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.utils.ColorTools;

import static client.utils.ImageTools.recolorImage;


public class BoardsListItemCtrl implements Initializable {

    @FXML
    private ImageView icon;

    @FXML
    private Button boardItemButton;

    @FXML
    private AnchorPane root;


    private MainPageCtrl mainPageCtrl;

    private long boardId;

    private String boardName;

    private BoardIdWithColors props;

    public BoardsListItemCtrl(BoardIdWithColors props, MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        this.props = props;
    }

    public void setBoardButton() throws IOException {
        mainPageCtrl.setBoardOverview(props.getBoardId());
    }

    public void updateProps(BoardIdWithColors props) {
        this.props = props;
        showColors();
    }

    public BoardIdWithColors getProps() {
        return props;
    }

    private void setImage(ImageView img, String path){
        File file = new File(path);
        Image image = recolorImage(file.toURI().toString(), Color.valueOf(props.getBoardPair().getFont()));
        img.setImage(image);
    }

    public void updateBoardButton(String newName){
        boardItemButton.setText(newName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ServerUtils utils = mainPageCtrl.getServer();
        var board = utils.getBoardById(props.getBoardId());
        if(board.isEmpty()){
            boardName = "???";
            // adding an entire style sheet here is redundant.
            // If the style is applied the scene will be deleted on first interaction anyways
            boardItemButton.setStyle("-fx-text-fill: red;");
        }
        else{
            boardName = board.get().getTitle();
            showColors();
        }
        boardItemButton.setText(boardName);
        updateIcon();
    }

    public void showColors() {
        var colorCode = Color.valueOf(props.getBoardPair().getBackground());
        var fill = new Background(new BackgroundFill(colorCode, new CornerRadii(20), null));
        root.setBackground(fill);
        var font = ColorTools.toHexString(Color.valueOf(props.getBoardPair().getFont()));
        var style = "-fx-text-fill: " + font + ";";
        boardItemButton.setStyle(style);
        updateIcon();
    }

    public void updateIcon() {
        if(props.getBoardId() == 1){
            setImage(icon,"src/main/resources/client/icons/PNG/home_FILL0_wght400_GRAD0_opsz48.png");
        }else{
            setImage(icon,"src/main/resources/client/icons/PNG/tab_FILL0_wght400_GRAD0_opsz48.png");
        }
    }
}
