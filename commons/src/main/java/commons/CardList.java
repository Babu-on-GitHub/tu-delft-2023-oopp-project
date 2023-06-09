package commons;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class CardList implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Timestamp timestamp;
    private String title = "Untitled list";
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn
    private List<Card> cards;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @SuppressWarnings("unused")
    public CardList() {
        this.cards = new ArrayList<>();
    }

    /**
     * Constructor with all parameters
     *
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
     * Constructor without id
     *
     * @param title
     * @param cards
     */
    public CardList(String title, List<Card> cards) {
        this.title = title;
        this.cards = cards;
    }

    /**
     * Constructor only with title
     *
     * @param title
     */
    public CardList(String title) {
        this.title = title;
        cards = new ArrayList<>();
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

    public void add(Card card) {
        if (cards == null)
            cards = new ArrayList<>();
        cards.add(card);
    }

    public void sync() {
        timestamp = new Timestamp(System.currentTimeMillis());
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        CardList cardList = (CardList) super.clone();
        cardList.cards = new ArrayList<>();
        for (Card card : cards)
            cardList.cards.add((Card) card.clone());
        return cardList;
    }
}
