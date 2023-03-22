package client.scenes;

import commons.Board;


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
