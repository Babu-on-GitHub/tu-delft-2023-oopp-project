package commons;

import java.io.Serializable;
import java.util.Objects;

public class BoardIdWithColors implements Serializable {

    long boardId;

    ColorPair boardPair;

    public BoardIdWithColors() {
    }

    public BoardIdWithColors(long boardId) {
        this.boardId = boardId;
    }

    public long getBoardId() {
        return boardId;
    }

    public void setBoardId(long boardId) {
        this.boardId = boardId;
    }

    public ColorPair getBoardPair() {
        return boardPair;
    }

    public void setBoardPair(ColorPair boardPair) {
        this.boardPair = boardPair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardIdWithColors that = (BoardIdWithColors) o;

        if (boardId != that.boardId) return false;
        return Objects.equals(boardPair, that.boardPair);
    }

    @Override
    public int hashCode() {
        int result = (int) (boardId ^ (boardId >>> 32));
        result = 31 * result + (boardPair != null ? boardPair.hashCode() : 0);
        return result;
    }
}
