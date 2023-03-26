package client.scenes;


import java.io.IOException;


public class BoardsListItemCtrl {

    private MainPageCtrl mainPageCtrl;

    private long boardId;

    public BoardsListItemCtrl(long boardId,MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        this.boardId = boardId;
    }

    public void setBoardButton() throws IOException {
        mainPageCtrl.setBoardOverview(boardId);
    }

}
