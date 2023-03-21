package client.scenes;

import client.model.BoardModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;

public class BoardsListItemCtrl {

    private MainPageCtrl mainPageCtrl;

    private BoardModel boardModel;

    public BoardsListItemCtrl(BoardModel boardModel,MainPageCtrl mainPageCtrl) {
        this.mainPageCtrl = mainPageCtrl;
        this.boardModel = boardModel;
    }

    @FXML
    public void setBoardButton(ActionEvent event){
        mainPageCtrl.setBoardOverview(boardModel);
    }

}
