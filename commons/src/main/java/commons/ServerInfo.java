package commons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerInfo implements Serializable {
    private String serverAddress;
    private List<BoardIdWithColors> boardsIds;

    public ServerInfo() {
        boardsIds = new ArrayList<>();
    }

    public ServerInfo(String serverAddress) {
        this.serverAddress = serverAddress;
        boardsIds = new ArrayList<>();
    }

    public ServerInfo(String serverAddress, List<BoardIdWithColors> boardsIds) {
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
    public List<BoardIdWithColors> getBoardsIds() {
        return boardsIds;
    }

    /**
     * Setter for boardsIds
     *
     * @param boardsIds Value for boardsIds
     */
    public void setBoardsIds(List<BoardIdWithColors> boardsIds) {
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
