package commons;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {


    private List<ServerInfo> serverBoardsPairs;

    public void save(ObjectOutputStream p) throws IOException {
        p.writeObject(this);
    }

    public static User load(ObjectInputStream p) throws IOException, ClassNotFoundException {
        User u = null;
        u = (User) p.readObject();
        return u;
    }

    public void setUserBoardsForServer(String server, List<Long> boards) {
        var s = serverBoardsPairs.stream().filter(q -> q.getServerAddress().equals(server)).findFirst();
        if (s.isPresent()) {
            s.get().setBoardsIds(boards);
        } else {
            serverBoardsPairs.add(new ServerInfo(server, boards));
        }
    }

    public List<Long> getUserBoardsIds(String server) {
        if (serverBoardsPairs == null) {
            serverBoardsPairs = new ArrayList<>();
            List<Long> boards = new ArrayList<>();
            boards.add(1L);
            serverBoardsPairs.add(new ServerInfo(server, boards));
            return boards;
        }
        for (ServerInfo userServer : serverBoardsPairs) {
            if (userServer.getServerAddress().equals(server)) {
                var b = userServer.getBoardsIds();
                if (b == null || b.isEmpty()) {
                    List<Long> boards = new ArrayList<>();
                    boards.add(1L);
                    userServer.setBoardsIds(boards);
                    return boards;
                }
                if (b.stream().filter(id -> id == 1).findAny().isEmpty()) {
                    userServer.getBoardsIds().add(1L);
                    return userServer.getBoardsIds();
                }
                return userServer.getBoardsIds();
            }
        }
        List<Long> boards = new ArrayList<>();
        boards.add(1L);
        serverBoardsPairs.add(new ServerInfo(server, boards));
        return boards;
    }

    public User() {
        serverBoardsPairs = new ArrayList<>();
    }

    public User(List<ServerInfo> serverBoardsPairs) {
        this.serverBoardsPairs = serverBoardsPairs;
    }

    /**
     * Getter for userServers
     */
    public List<ServerInfo> getServerBoardsPairs() {
        return serverBoardsPairs;
    }

    /**
     * Setter for userServers
     *
     * @param serverBoardsPairs Value for userServers
     */
    public void setServerBoardsPairs(List<ServerInfo> serverBoardsPairs) {
        this.serverBoardsPairs = serverBoardsPairs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        return obj instanceof User && ((User) obj).getServerBoardsPairs().equals(serverBoardsPairs);
    }

    public static class ServerInfo implements Serializable {
        private String serverAddress;
        private List<Long> boardsIds;

        public ServerInfo() {
            boardsIds = new ArrayList<>();
        }

        public ServerInfo(String serverAddress) {
            this.serverAddress = serverAddress;
            boardsIds = new ArrayList<>();
        }

        public ServerInfo(String serverAddress, List<Long> boardsIds) {
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
        public List<Long> getBoardsIds() {
            return boardsIds;
        }

        /**
         * Setter for boardsIds
         *
         * @param boardsIds Value for boardsIds
         */
        public void setBoardsIds(List<Long> boardsIds) {
            this.boardsIds = boardsIds;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;

            return obj instanceof ServerInfo &&
                    ((ServerInfo) obj).getServerAddress().equals(serverAddress)
                    && ((ServerInfo) obj).getBoardsIds().equals(boardsIds);
        }
    }
}
