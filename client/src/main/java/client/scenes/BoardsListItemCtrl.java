package client.scenes;

import commons.Board;

import java.io.IOException;


public class BoardsListItemCtrl {

    private MainPageCtrl mainPageCtrl;

    private Board board;

    public BoardsListItemCtrl(Board board,MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        this.board = board;
    }

    public void setBoardButton() throws IOException {
        mainPageCtrl.setBoardOverview(board);
    }

}
