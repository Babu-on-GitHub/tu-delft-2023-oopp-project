package client.model;

import client.scenes.MainPageCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BoardModel {
    static Logger log = Logger.getLogger(BoardModel.class.getName());
    private Board board;
    private List<ListModel> children = new ArrayList<>();
    private MainPageCtrl controller;

    public MainPageCtrl getController() {
        return controller;
    }

    public void setController(MainPageCtrl controller) {
        this.controller = controller;
    }


    public void updateChild(CardList cardList) {
        for (int i = 0; i < board.getLists().size(); i++) {
            var list = board.getLists().get(i);
            if (list.getId() == cardList.getId()) {
                board.getLists().set(i, cardList);
            }
        }
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

        var newList = listModel.getCardList();
        ServerUtils utils = new ServerUtils();
        newList = utils.addCardList(newList);

        listModel.setCardList(newList);

        board.add(newList);
        children.add(listModel);

        if (update())
            log.warning("BoardModel was overriden by server");
        quietTest();
    }

    public void deleteList(ListModel listModel){
        ServerUtils utils = new ServerUtils();
        long id = listModel.getCardList().getId();
        for (int i = 0; i < board.getLists().size(); i++) {
            CardList list = board.getLists().get(i);
            if (list.getId() == id) {
                board.getLists().remove(list);
            }
        }
        children.remove(listModel);

        utils.deleteCardListById(id);

        if (update()) {
            log.warning("BoardModel was overriden by server");
        }

        quietTest();
    }

    public boolean update() {
        ServerUtils utils = new ServerUtils();
        var res = utils.getBoardById(board.getId());
        if (res == null) {
            log.info("Adding new board..");
            var newBoard = utils.addBoard(board);
            if (newBoard == null) {
                log.warning("BoardModel update failed");
                return false;
            }
            board = utils.getBoardById(newBoard.getId());

            updateChildren();
            return false;
        }

        if (res.equals(board))
            return false;

        var newBoard = utils.updateBoardById(board.getId(), board);
        if (newBoard == null) {
            log.warning("BoardModel update failed");
            return false;
        }

        board = utils.getBoardById(newBoard.getId());
        updateChildren();

        return false;
    }

    public void updateChildren() {
        if (board.getLists() == null) return;

        if (tryToUpdateChildrenNaively()) return;

        log.info("Naive update failed, rebuilding fully..");

        var temp = new ArrayList<ListModel>();
        for (int i = 0; i < board.getLists().size(); i++)
            temp.add(null);

        for (int i = 0; i < board.getLists().size(); i++) {
            var model = new ListModel(board.getLists().get(i), this);
            temp.set(i, model);
        }

        children = temp;
        try {
            controller.recreateChildren(temp);
        }
        catch (Exception e) {
            log.warning("Problems during board children recreation..");
        }

        for (ListModel child : children)
            child.updateChildren();

        quietTest();
    }

    public boolean tryToUpdateChildrenNaively() {
        if (board == null || board.getLists() == null) return false;
        if (board.getLists().size() != children.size()) return false;

        for (int i = 0; i < board.getLists().size(); i++) {
            var list = board.getLists().get(i);
            var child = children.get(i);

            if (list.getId() != child.getCardList().getId()) return false;
        }

        for (int i = 0; i < board.getLists().size(); i++) {
            var list = board.getLists().get(i);
            var child = children.get(i);

            child.setCardList(list);
            child.updateChildren();
        }

        return true;
    }

    private boolean coherencyTest() {
        if (board.getLists() == null && children == null) return true;
        if (board.getLists() == null || children == null) return false;
        if (board.getLists().size() != children.size()) return false;
        for (int i = 0; i < board.getLists().size(); i++) {
            var first = board.getLists().get(i);
            var second = children.get(i).getCardList();
            if (first != second) return false;
        }
        return true;
    }

    public void quietTest() {
        var isCoherent = coherencyTest();
        if (!isCoherent)
            log.severe("BoardModel is not coherent");
    }
}
