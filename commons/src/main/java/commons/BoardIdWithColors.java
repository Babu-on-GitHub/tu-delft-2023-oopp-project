package commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardIdWithColors implements Serializable, Cloneable {

    long boardId;

    private ColorPair boardPair = new ColorPair("#aaaaff", "#111111");

    private ColorPair listPair = new ColorPair("#ffaaaa", "#111111");

    private ColorPair cardPair = new ColorPair("#aaffaa", "#111111");

    private Map<Long, ColorPair> cardHighlightColors = new HashMap<>();

    public BoardIdWithColors(long boardId) {
        this.boardId = boardId;
    }

    public Map<Long, ColorPair> getCardHighlightColors() {
        return cardHighlightColors;
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

    public ColorPair getListPair() {
        return listPair;
    }

    public void setListPair(ColorPair listPair) {
        this.listPair = listPair;
    }

    public ColorPair getCardPair() {
        return cardPair;
    }

    public void setCardPair(ColorPair cardPair) {
        this.cardPair = cardPair;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardIdWithColors that = (BoardIdWithColors) o;

        if (boardId != that.boardId) return false;
        if (!Objects.equals(boardPair, that.boardPair)) return false;
        if (!Objects.equals(listPair, that.listPair)) return false;
        return Objects.equals(cardPair, that.cardPair);
    }

    @Override
    public int hashCode() {
        int result = (int) (boardId ^ (boardId >>> 32));
        result = 31 * result + (boardPair != null ? boardPair.hashCode() : 0);
        result = 31 * result + (listPair != null ? listPair.hashCode() : 0);
        result = 31 * result + (cardPair != null ? cardPair.hashCode() : 0);
        return result;
    }


    @Override
    public BoardIdWithColors clone() throws CloneNotSupportedException {
        // copy the map separetely
        BoardIdWithColors clone = (BoardIdWithColors) super.clone();
        clone.cardHighlightColors = new HashMap<>(cardHighlightColors);
        clone.boardPair = boardPair.clone();
        clone.listPair = listPair.clone();
        clone.cardPair = cardPair.clone();

        return clone;
    }
}
