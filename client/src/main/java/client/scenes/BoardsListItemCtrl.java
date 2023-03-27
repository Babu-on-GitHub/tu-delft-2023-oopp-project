package client.scenes;

import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class BoardsListItemCtrl implements Initializable {

    private MainPageCtrl mainPageCtrl;

    private Board board;

    @FXML
    private Button boardButton;

    public BoardsListItemCtrl(Board board,MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        this.board = board;
    }

    public void setBoardButton() throws IOException {
        mainPageCtrl.setBoardOverview(board);
    }

    public void updateBoardButton(String newName){
        boardButton.setText(newName);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        boardButton.setText(board.getTitle());

    }
}
