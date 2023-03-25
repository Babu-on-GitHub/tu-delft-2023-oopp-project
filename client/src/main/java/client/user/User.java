package client.user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {


    private List<ServerInfo> userServers;

    public void save(ObjectOutputStream p) throws IOException {
        p.writeObject(this);
    }

    public static User load(ObjectInputStream p) throws IOException, ClassNotFoundException {
        User u = null;
        u = (User) p.readObject();
        return u;
    }

    public List<Integer> getUserBoardsIds(String server){
        if(userServers == null){
            userServers = new ArrayList<>();
            List<Integer> boards = new ArrayList<>();
            boards.add(1);
            userServers.add(new ServerInfo(server,boards));
            return boards;
        }
        for (ServerInfo userServer : userServers) {
            if (userServer.getServerAddress().equals(server)) {
                var b = userServer.getBoardsIds();
                if (b == null || b.isEmpty()){
                    List<Integer> boards = new ArrayList<>();
                    boards.add(1);
                    userServer.setBoardsIds(boards);
                    return boards;
                }
                if(b.stream().filter(id -> id == 1).findAny().isEmpty()) {
                    userServer.getBoardsIds().add(1);
                    return userServer.getBoardsIds();
                }
                return userServer.getBoardsIds();
            }
        }
        List<Integer> boards = new ArrayList<>();
        boards.add(1);
        userServers.add(new ServerInfo(server,boards));
        return boards;
    }

    public User() {
        userServers = new ArrayList<>();
    }

    public User(List<ServerInfo> userServers) {
        this.userServers = userServers;
    }

    /**
     * Getter for userServers
     */
    public List<ServerInfo> getUserServers() {
        return userServers;
    }

    /**
     * Setter for userServers
     *
     * @param userServers Value for userServers
     */
    public void setUserServers(List<ServerInfo> userServers) {
        this.userServers = userServers;
    }

    private static class ServerInfo implements Serializable{
        private String serverAddress;
        private List<Integer> boardsIds;

        public ServerInfo() {
            boardsIds = new ArrayList<>();
        }

        public ServerInfo(String serverAddress) {
            this.serverAddress = serverAddress;
            boardsIds = new ArrayList<>();
        }

        public ServerInfo(String serverAddress, List<Integer> boardsIds) {
            this.serverAddress = serverAddress;
            this.boardsIds = boardsIds;
        }

        /**
         * Getter for serverAddress
         */
        public String getServerAddress() {
            return serverAddress;
        }

        /**
         * Setter for serverAddress
         *
         * @param serverAddress Value for serverAddress
         */
        public void setServerAddress(String serverAddress) {
            this.serverAddress = serverAddress;
        }

        /**
         * Getter for boardsIds
         */
        public List<Integer> getBoardsIds() {
            return boardsIds;
        }

        /**
         * Setter for boardsIds
         *
         * @param boardsIds Value for boardsIds
         */
        public void setBoardsIds(List<Integer> boardsIds) {
            this.boardsIds = boardsIds;
        }
    }
}
