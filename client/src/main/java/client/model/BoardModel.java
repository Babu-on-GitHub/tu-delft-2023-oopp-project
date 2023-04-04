package client.model;

import client.scenes.MainPageCtrl;
import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import commons.Tag;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class BoardModel {
    static Logger log = Logger.getLogger(BoardModel.class.getName());
    private Board board;
    private List<ListModel> children = new ArrayList<>();
    private MainPageCtrl controller;

    private ServerUtils utils;

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

    public BoardModel(Board board, ServerUtils utils) {
        this.board = board;
        this.utils = utils;
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

    public void addList(ListModel listModel) {
        if (this.getChildren() == null) {
            this.setChildren(new ArrayList<>());
        }
        if (board.getLists() == null) {
            board.setLists(new ArrayList<>());
        }

        var newList = listModel.getCardList();
        var req = utils.addCardList(newList, board);
        if (req.isEmpty()) {
            log.warning("Adding new list failed");
            return;
        }
        newList = req.get();
        listModel.setCardList(newList);

        board.add(newList);
        children.add(listModel);

        update();
        quietTest();
    }

    public void deleteList(ListModel listModel) {
        long id = listModel.getCardList().getId();
        for (int i = 0; i < board.getLists().size(); i++) {
            CardList list = board.getLists().get(i);
            if (list.getId() == id) {
                board.getLists().remove(list);
            }
        }
        children.remove(listModel);

        utils.deleteCardListById(id, board.getId());

        quietTest();
    }

    public boolean update() {
        var res = utils.getBoardById(board.getId());
        if (res.isEmpty()) {
            log.info("Adding new board..");
            var req = utils.addBoard(board);
            if (req.isEmpty()) {
                log.warning("BoardModel update failed");
                return false;
            }
            board = req.get();

            updateChildren();
            return false;
        }

        var serverBoard = res.get();

        if (serverBoard.equals(board)) {
            log.info("Board is up to date");
            return false;
        }

        var serverTimestamp = serverBoard.getTimestamp();
        var localTimestamp = board.getTimestamp();

        if (serverTimestamp.after(localTimestamp)) {
            log.info("Server-side board is newer, overwriting local");
            board = serverBoard;
            controller.overwriteTitleNode(board.getTitle());

            //var color = board.getBoardColor();
            //controller.setBoardColor(color);

            updateChildren();

            return true;
        }

        log.info("Overwriting server-side board..");

        var newBoard = utils.updateBoardById(board.getId(), board);
        if (newBoard.isEmpty()) {
            log.warning("BoardModel update failed");
            return false;
        }

        return false;
    }

    public void updateWithNewBoard(Board newBoard) {
        if (board.equals(newBoard)) {
            log.info("Board is up to date in updateWithNewBoard");
            return;
        }

        if (newBoard == null) {
            log.info("Board is null in updateWithNewBoard");
            return;
        }

        board = newBoard;
        updateChildren();
    }

    public void updateChildren() {
        controller.overwriteTitleNode(board.getTitle());

        if (board.getLists() == null) return;

        if (tryToUpdateChildrenNaively()) return;

        log.info("Naive update failed, rebuilding fully..");

        var temp = new ArrayList<ListModel>();
        for (int i = 0; i < board.getLists().size(); i++)
            temp.add(null);

        for (int i = 0; i < board.getLists().size(); i++) {
            var model = new ListModel(board.getLists().get(i), this, utils);
            temp.set(i, model);
        }

        children = temp;
        try {
            controller.recreateChildren(temp);
        } catch (IOException e) {
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

    public void moveCard(CardModel card, ListModel to, int index) {
        card.disown();

        card.fosterBy(to, index);

        var res = utils.moveCard(card.getCard().getId(), to.getCardList().getId(),
                index, board.getId());
        if (res.isEmpty()) {
            log.warning("Moving card failed, updating whole board");
        }

        update();
        quietTest();
    }

    public void forEveryCard(Consumer<CardModel> consumer) {
        for (ListModel list : children) {
            for (CardModel card : list.getChildren()) {
                consumer.accept(card);
            }
        }
    }

    public void updateTitle(String text) {
        board.setTitle(text);
        var res = utils.updateBoardTitleById(board.getId(), board.getTitle());
        if (res.isEmpty()) {
            log.warning("Updating board title failed");
        }
        else {
            if (res.get().equals(board.getTitle())) {
                log.info("Board title is up to date");
            }
            else {
                log.severe("Board title is not up to date");

                board.setTitle(res.get());
                controller.overwriteTitleNode(board.getTitle());
            }
        }
    }

    public Tag addTag(Tag tag) {
        var res = utils.addTagToBoard(board.getId(), tag);
        if (res.isEmpty()) {
            log.warning("Adding tag to board failed");
            return null;
        }

        board.getTags().add(res.get());

        update();

        return res.get();
    }

    public Tag updateTag(Tag tag) {
        var res = utils.updateTag(board.getId(), tag);
        if (res.isEmpty()) {
            log.warning("Updating tag in board failed");
            return null;
        }

        // remove the tag that has been updated
        board.getTags().removeIf(t -> t.getId() == tag.getId());
        // add the updated tag
        board.getTags().add(res.get());

        update();

        return res.get();
    }

    public void deleteTag(Tag tag) {
        var res = utils.deleteTagFromBoard(board.getId(), tag.getId());
        if (res.isEmpty()) {
            log.warning("Deleting tag from board failed");
            return;
        }
        board.getTags().remove(tag);

        update();
    }
}
