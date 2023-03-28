package client.utils;

import client.scenes.MainPageCtrl;
import com.google.inject.Inject;
import commons.User;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

public class UserUtils {

    static Logger log = Logger.getLogger(MainPageCtrl.class.getName());
    private ServerUtils utils;

    @Inject
    public UserUtils(ServerUtils utils) {
        this.utils = utils;
    }

    private User user;

    public List<Long> getUserBoardsIds(){
        if(user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        return user.getUserBoardsIds(server);
    }

    public void updateUserBoards(List<Long> boards){
        if(user == null) retrieveSavedUser();
        String server = utils.getSERVER();
        user.setUserBoardsForServer(server,boards);
        storeUser();
    }

    private void retrieveSavedUser() {
        try{
            FileInputStream fin = new FileInputStream("client/src/main/resources/client/user.data");
            ObjectInputStream oin = new ObjectInputStream(fin);
            user = User.load(oin);
        }catch (IOException | ClassNotFoundException e1){
            user = new User();
            storeUser();
        }
    }

    private void storeUser() {
        if(user==null){
            log.warning("Tried to store null user");
            return;
        }
        try {
            FileOutputStream fout = new FileOutputStream("client/src/main/resources/client/user.data");
            ObjectOutputStream oout = new ObjectOutputStream(fout);
            user.save(oout);
        } catch (IOException e) {
            log.warning("Couldn't save user");
        }
    }


}
