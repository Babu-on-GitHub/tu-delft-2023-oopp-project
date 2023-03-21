package client.scenes;

import client.model.BoardModel;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;

public class BoardsListItemCtrl {

    private MainPageCtrl mainPageCtrl;

    private Board board;

    public BoardsListItemCtrl(Board board,MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        this.board = board;
    }

    public void setBoardButton(){
        mainPageCtrl.setBoardOverview(board);
    }

}
