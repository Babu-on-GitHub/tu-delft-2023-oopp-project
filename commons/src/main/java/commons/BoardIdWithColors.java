package commons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoardIdWithColors implements Serializable, Cloneable {

    long boardId;

    private ColorPair boardPair = new ColorPair("#ffffff", "#000000");

    private ColorPair listPair = new ColorPair("#ffffff", "#000000");

    private ColorPair cardPair = new ColorPair("#ffffff", "#000000");

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
        return boardId == that.boardId && Objects.equals(boardPair, that.boardPair)
                && Objects.equals(listPair, that.listPair)
                && Objects.equals(cardPair, that.cardPair)
                && Objects.equals(cardHighlightColors, that.cardHighlightColors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId, boardPair, listPair, cardPair, cardHighlightColors);
    }

    @Override
    public BoardIdWithColors clone() throws CloneNotSupportedException {
        // copy the map separately
        BoardIdWithColors clone = (BoardIdWithColors) super.clone();
        clone.cardHighlightColors = new HashMap<>();
        for (var entry : cardHighlightColors.entrySet())
            clone.cardHighlightColors.put(entry.getKey(), entry.getValue().clone());
        clone.boardPair = boardPair.clone();
        clone.listPair = listPair.clone();
        clone.cardPair = cardPair.clone();

        return clone;
    }

    public void setCardHighlightColors(HashMap<Long, ColorPair> objects) {
        this.cardHighlightColors = objects;
    }
}
