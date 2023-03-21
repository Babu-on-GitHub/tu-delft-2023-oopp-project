package client.model;

import client.scenes.MainPageCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;

public class BoardModel {
    private Board board;
    private List<ListModel> children;

    private MainPageCtrl controller;

    /**
     * Getter for controller
     */
    public MainPageCtrl getController() {
        return controller;
    }

    /**
     * Setter for controller
     *
     * @param controller Value for controller
     */
    public void setController(MainPageCtrl controller) {
        this.controller = controller;
    }

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
        var temp = new ArrayList<ListModel>();
        for (int i = 0; i < board.getLists().size(); i++)
            temp.add(null);

        for (int i = 0; i < board.getLists().size(); i++) {
            var list = board.getLists().get(i);
            for (ListModel child : children) {
                if (child.getCardList().getId() == list.getId()) {
                    child.setCardList(list);
                    temp.set(i, child);
                }
            }
        }

        for (int i = 0; i < temp.size(); i++) {
            if (temp.get(i) == null) {
                var model = new ListModel(board.getLists().get(i), this);
                temp.set(i, model);
            }
        }

        children = temp;
        try {
            controller.recreateChildren(temp);
        }
        catch (Exception e) {
            log.warning("Problems during board children recreation..");
        }

        for (ListModel child : children) {
            child.updateChildren();
            child.setParent(this);
        }
    }
}
