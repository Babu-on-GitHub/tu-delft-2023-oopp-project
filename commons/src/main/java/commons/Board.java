package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import javafx.scene.paint.Color;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Timestamp timestamp;

    private String title = "Untitled board";

    private String boardColor = "0x91B7BF";

    private String listColor = "0xD2A295";

    private String cardColor = "0xF7EFD2";

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn
    private List<CardList> lists;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Tag> tags;

    @SuppressWarnings("unused")
    public Board() {
        lists = new ArrayList<>();
        tags = new HashSet<>();
    }

    /**
     * Constructor with all parameters
     *
     * @param id
     * @param title
     * @param lists
     */
    public Board(long id, String title, List<CardList> lists) {
        this.id = id;
        this.title = title;
        this.lists = lists;
        tags = new HashSet<>();
    }

    /**
     * Constructor without id
     *
     * @param title
     * @param lists
     */
    public Board(String title, List<CardList> lists) {
        this.title = title;
        this.lists = lists;
        tags = new HashSet<>();
    }

    /**
     * Constructor only with title
     *
     * @param title
     */
    public Board(String title) {
        this.title = title;
        lists = new ArrayList<>();
        tags = new HashSet<>();
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }


    public void setBoardColor(String boardColor) {
        this.boardColor = boardColor;
    }

    public String getBoardColor() {
        return boardColor;
    }

    public String getListColor() {
        return listColor;
    }

    public void setListColor(String listColor) {
        this.listColor = listColor;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public void sync() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Getter for id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter for id
     *
     * @param id Value for id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Getter for title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title
     *
     * @param title Value for title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for lists
     */
    public List<CardList> getLists() {
        return lists;
    }

    /**
     * Setter for lists
     *
     * @param lists Value for lists
     */
    public void setLists(List<CardList> lists) {
        this.lists = lists;
    }

    /**
     * Getter for tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Setter for tags
     *
     * @param tags Value for tags
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void add(CardList list) {
        if(lists == null) lists = new ArrayList<>();
        lists.add(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return new EqualsBuilder()
                .append(id, board.id)
                .append(title, board.title)
                .append(lists, board.lists)
                .append(boardColor, board.boardColor)
                .append(listColor, board.listColor)
                .append(cardColor, board.cardColor)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(lists)
                .append(boardColor)
                .append(listColor)
                .append(cardColor)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
