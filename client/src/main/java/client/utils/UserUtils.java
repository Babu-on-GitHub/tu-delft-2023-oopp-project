package client.utils;

import client.scenes.MainPageCtrl;
import com.google.inject.Inject;
import commons.User;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class UserUtils {

    static Logger log = Logger.getLogger(UserUtils.class.getName());
    private ServerUtils utils;
    private String USER_FILE = "client/src/main/resources/client/user.data";
    private User user;

    @Inject
    public UserUtils(ServerUtils utils) {
        this.utils = utils;
    }

    public void setUserFile(String file) {
        USER_FILE = file;
    }

    public List<Long> getUserBoardsIds() {
        if (user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        return user.getUserBoardsIds(server);
    }

    public void updateUserBoards(List<Long> boards) {
        if (user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        user.setUserBoardsForServer(server, boards);
        storeUser();
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
