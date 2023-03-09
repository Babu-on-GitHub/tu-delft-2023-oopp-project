package commons;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<Card> cards;

    @ManyToOne
    Board board;

    @SuppressWarnings("unused")
    public CardList(){

    }

    /**
     * Constructor with all parameters
     * @param id
     * @param title
     * @param cards
     */
    public CardList(long id, String title, List<Card> cards) {
        this.id = id;
        this.title = title;
        this.cards = cards;
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
     * Getter for cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter for cards
     *
     * @param cards Value for cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CardList cardList = (CardList) o;

        return new EqualsBuilder()
                .append(id, cardList.id)
                .append(title, cardList.title)
                .append(cards, cardList.cards)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(cards)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
