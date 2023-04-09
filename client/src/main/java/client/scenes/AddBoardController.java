package client.scenes;

import client.utils.ServerUtils;
import client.utils.UserUtils;
import commons.Board;
import commons.BoardIdWithColors;
import commons.ColorPair;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import static client.utils.ColorTools.toHexString;

public class AddBoardController implements Initializable {

    static Logger log = Logger.getLogger(AddBoardController.class.getName());

    @FXML
    private TextField idBar;
    @FXML
    private TextField titleBar;

    @FXML
    private Label addLabel;

    @FXML
    private AnchorPane root;

    @FXML
    private Label joinLabel;

    @FXML
    private Label createLabel;

    @FXML
    private Button joinButton;

    @FXML
    private Button createButton;

    private ServerUtils utils;

    private MainPageCtrl parent;

    private UserUtils userUtils;



    public AddBoardController(MainPageCtrl parent){
        this.parent = parent;
        utils = parent.getServer();
        userUtils = parent.getUserUtils();
    }

    @FXML
    public void joinButton(ActionEvent event) {
        try{
            long id = Long.parseLong(idBar.getText());
            Optional<Board> added = utils.getBoardById(id);
            if (added.isEmpty()) {
                idBar.setStyle("-fx-border-color: red;");
                return;
            }
            var boards = parent.getBoardList();
            if(boards.stream().filter(q->q.getBoardId() == added.get().getId()).findAny().isEmpty()){
                var col = new BoardIdWithColors(added.get().getId());
                boards.add(col);
                userUtils.updateUserBoards(parent.getBoardList());
                parent.addBoardListItemToList(col);
            }
            closeWindow(event);
        } catch (NumberFormatException | IOException e) {
            idBar.setStyle("-fx-border-color: red;");
        }
    }

    public void createButton(ActionEvent event){
        if(titleBar.getText().isEmpty()){
            titleBar.setStyle("-fx-border-color: red;");
            return;
        }
        Board board = new Board();
        board.setTitle(titleBar.getText());
        var added = utils.addBoard(board);
        if (added.isEmpty()) {
            titleBar.setStyle("-fx-border-color: red;");
            log.warning("Failed to add board");
            return;
        }
        var col = new BoardIdWithColors(added.get().getId());
        parent.getBoardList().add(col);
        userUtils.updateUserBoards(parent.getBoardList());
        try {
            parent.addBoardListItemToList(col);
        } catch (IOException e) {
            log.warning("Failed to show newly created board");
        }
        closeWindow(event);
    }

    public void closeWindow(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void updateColors(ColorPair colorPair) {
        setAddBoardFontFXML(colorPair.getFont());
        setAddBoardBackgroundFXML(colorPair.getBackground());
    }

    private void setAddBoardFontFXML(String color) {
        var colorCode = Color.valueOf(color);

        var styleStr = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" +
                "-fx-border-radius: 10px !important; " ;
        var styleStrWithoutBorder = "-fx-text-fill: " + toHexString(colorCode) + " !important; " +
                "-fx-background-color: inherit !important;" ;
        addLabel.setStyle(styleStrWithoutBorder);
        joinLabel.setStyle(styleStrWithoutBorder);
        createLabel.setStyle(styleStrWithoutBorder);
        joinButton.setStyle(styleStr);
        createButton.setStyle(styleStr);
        titleBar.setStyle(styleStrWithoutBorder);
        idBar.setStyle(styleStrWithoutBorder);
    }

    public void setAddBoardBackgroundFXML(String color) {
        var colorCode = Color.valueOf(color);
        var darker = colorCode.darker();
        var lighter = colorCode.desaturate().desaturate();
        var fill = new Background(new BackgroundFill(colorCode, null, null));
        var darkerFill = new Background(new BackgroundFill(darker, new CornerRadii(10), null));
        var lighterFill = new Background(new BackgroundFill(lighter, new CornerRadii(10), null));

        joinButton.setBackground(darkerFill);
        createButton.setBackground(darkerFill);
        root.setBackground(fill);
        addLabel.setBackground(fill);
        joinLabel.setBackground(fill);
        createLabel.setBackground(fill);
        titleBar.setBackground(lighterFill);
        idBar.setBackground(lighterFill);
    }

    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
        updateColors(parent.getBoardColor());
    }
}
