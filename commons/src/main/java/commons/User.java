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

    public void setUserBoardsForServer(String server, List<BoardIdWithColors> boards) {
        var s = serverBoardsPairs.stream().filter(q -> q.getServerAddress().equals(server)).findFirst();
        if (s.isPresent()) {
            s.get().setBoardsIds(boards);
        } else {
            serverBoardsPairs.add(new ServerInfo(server, boards));
        }
    }

    public List<BoardIdWithColors> getUserBoardsIds(String server) {
        if (serverBoardsPairs == null) {
            return makeDefaultBoard(server);
        }
        for (ServerInfo userServer : serverBoardsPairs) {
            if (userServer.getServerAddress().equals(server)) {
                var b = userServer.getBoardsIds();
                if (b == null || b.isEmpty()) {
                    return makeDefaultBoard(server);
                }
                if (b.stream().filter(info -> info.getBoardId() == 1).findAny().isEmpty()) {
                    userServer.getBoardsIds().add(new BoardIdWithColors(1L));
                    return userServer.getBoardsIds();
                }
                return userServer.getBoardsIds();
            }
        }
        List<BoardIdWithColors> boards = new ArrayList<>();
        boards.add(new BoardIdWithColors(1L));
        serverBoardsPairs.add(new ServerInfo(server, boards));
        return boards;
    }

    private List<BoardIdWithColors> makeDefaultBoard(String server) {
        serverBoardsPairs = new ArrayList<>();
        List<BoardIdWithColors> boards = new ArrayList<>();
        boards.add(new BoardIdWithColors(1L));
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
}
