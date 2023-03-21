package client.model;

import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private Board board;
    private List<ListModel> children;

    public void updateChild(CardList cardList){
        for(int i=0;i<board.getLists().size();i++){
            var list = board.getLists().get(i);
            if( list.getId() == cardList.getId()){
                board.getLists().set(i, cardList);
            }
        }
        this.update();
    }

    public BoardModel(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<ListModel> getChildren() {
        return children;
    }

    public void setChildren(List<ListModel> children) {
        this.children = children;
    }

    public void addList(ListModel listModel){
        if (this.getChildren() == null) {
            this.setChildren(new ArrayList<>());
        }
        if(board.getLists() == null){
            board.setLists(new ArrayList<>());
        }

        listModel.update();

        board.getLists().add(listModel.getCardList());

        children.add(listModel);

        this.update();
    }

    public void deleteList(ListModel listModel){
        long id = listModel.getCardList().getId();
        for(int i = 0; i<board.getLists().size(); i++){
            CardList list = board.getLists().get(i);
            if(list.getId()==id){
                board.getLists().remove(list);
                //utils.deleteCardListById(list.getId());
            }
        }
        children.remove(listModel);

        this.update();
    }

    public void update(){
        var utils = new ServerUtils();
        if(board.getId() == 0 ) board = utils.addBoard(board);
        else board = utils.updateBoardById(board.getId(), board);
        this.updateChildren();
    }


    public void updateChildren(){
        if(board.getLists()==null||children==null) return;
        for(var list: board.getLists()){
            for(var child: children){
                if(child.getCardList().getId() == list.getId()){
                    child.setCardList(list);
                    child.updateChildren();
                }
            }
        }
    }
}
