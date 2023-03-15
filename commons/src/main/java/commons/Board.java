package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Board implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<CardList> lists;

    @SuppressWarnings("unused")
    public Board() {
    }

    /**
     * Constructor with all parameters
     * @param id
     * @param title
     * @param lists
     */
    public Board(long id, String title, List<CardList> lists) {
        this.id = id;
        this.title = title;
        this.lists = lists;
    }

    /**
     * Constructor without id
     * @param title
     * @param lists
     */
    public Board(String title, List<CardList> lists) {
        this.title = title;
        this.lists = lists;
    }

    /**
     * Constructor only with title
     * @param title
     */
    public Board(String title) {
        this.title = title;
        lists = new ArrayList<>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Board board = (Board) o;

        return new EqualsBuilder()
                .append(id, board.id)
                .append(title, board.title)
                .append(lists, board.lists)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(lists)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
