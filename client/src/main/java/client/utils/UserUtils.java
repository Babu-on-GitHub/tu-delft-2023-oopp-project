package client.utils;

import com.google.inject.Inject;
import commons.BoardIdWithColors;
import commons.User;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class UserUtils {

    static Logger log = Logger.getLogger(UserUtils.class.getName());
    private ServerUtils utils;
    private String USER_FILE = "src/main/resources/client/user.data";
    private User user;

    private BoardIdWithColors currentBoard;

    @Inject
    public UserUtils(ServerUtils utils) {
        this.utils = utils;
    }

    public void setUserFile(String file) {
        USER_FILE = file;
    }

    public BoardIdWithColors getCurrentBoardColors() {
        return currentBoard;
    }

    public List<BoardIdWithColors> getUserBoardsIds() {
        if (user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        return user.getUserBoardsIds(server);
    }

    public void updateUserBoards(List<BoardIdWithColors> boards) {
        if (user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        user.setUserBoardsForServer(server, boards);
        setMyId((int) currentBoard.getBoardId());
        storeUser();
    }

    public void updateSingleBoard(BoardIdWithColors board) {
        if (user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        user.updateSingleBoardForServer(server, board);
        setMyId((int) currentBoard.getBoardId());
        storeUser();
    }

    public void setMyId(int id) {
        // find the board with the same id as the current board
        if (user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        var boards = user.getUserBoardsIds(server);
        var boardOpt = boards.stream()
                .filter(b -> b.getBoardId() == id)
                .findFirst();
        if (boardOpt.isEmpty()) {
            log.warning("Couldn't find board with id " + currentBoard.getBoardId());
            return;
        }
        try {
            currentBoard = boardOpt.get().clone();
        } catch (CloneNotSupportedException e) {
            log.warning("Couldn't clone board");
            e.printStackTrace();
        }
    }

    private void retrieveSavedUser() {
        try {
            FileInputStream fin = new FileInputStream(USER_FILE);
            ObjectInputStream oin = new ObjectInputStream(fin);
            user = User.load(oin);
        } catch (IOException | ClassNotFoundException e1) {
            user = new User();
            storeUser();
        }
    }

    private void storeUser() {
        if (user == null) {
            log.warning("Tried to store null user");
            return;
        }
        try {
            FileOutputStream fout = new FileOutputStream(USER_FILE);
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            user.save(oout);
        } catch (IOException e) {
            log.warning("Couldn't save user");
        }
    }


}
