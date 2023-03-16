package client.model;

import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;

import java.util.List;

public class BoardModel {
    private Board board;
    private List<ListModel> children;

    public void updateCardListById(long id, CardList cardList){
        var list = board.getLists().stream().filter(c -> c.getId() == id).findFirst();
        if (list.isPresent())
           board.getLists().set(board.getLists().indexOf(list), cardList);
    }

    public void update(){
        var utils = new ServerUtils();
        board = utils.updateBoardById(board.getId(), board);
        for(var child : children){
            child.update();
        }
    }
}
