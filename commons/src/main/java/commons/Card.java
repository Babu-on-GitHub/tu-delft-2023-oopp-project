package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.sql.Timestamp;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Card implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title = "Untitled card";
    private Timestamp timestamp;

    private String description;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderColumn
    private List<Task> subTasks;

    @ManyToMany(cascade = {}, fetch = FetchType.EAGER)
    private Set<Tag> tags;


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void sync() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @SuppressWarnings("unused")
    public Card() {
        tags = new HashSet<>();
        subTasks = new ArrayList<>();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Card card = (Card) super.clone();
        card.subTasks = new ArrayList<>();
        card.tags = new HashSet<>();
        for (Task task : subTasks)
            card.subTasks.add((Task) task.clone());

        for (Tag tag : tags)
            card.tags.add((Tag) tag.clone());
        return card;
    }

    /**
     * Constructor with given title
     *
     * @param title
     */
    public Card(String title) {
        this();

        this.title = title;
    }

    /**
     * Constructor with all parameters
     *
     * @param id
     * @param title
     * @param description
     * @param subTasks
     * @param tags
     */
    public Card(long id, String title, String description, List<Task> subTasks, Set<Tag> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subTasks = subTasks;
        this.tags = tags;
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
     * Getter for description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description
     *
     * @param description Value for description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for subTasks
     */
    public List<Task> getSubTasks() {
        return subTasks;
    }

    /**
     * Setter for subTasks
     *
     * @param subTasks Value for subTasks
     */
    public void setSubTasks(List<Task> subTasks) {
        this.subTasks = subTasks;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        return new EqualsBuilder()
                .append(id, card.id)
                .append(title, card.title)
                .append(description, card.description)
                .append(subTasks, card.subTasks)
                .append(tags, card.tags)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(title)
                .append(description)
                .append(subTasks)
                .append(tags)
                .toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
