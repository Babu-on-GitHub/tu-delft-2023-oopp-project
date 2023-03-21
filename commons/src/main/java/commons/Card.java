package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private Timestamp timestamp;

    private String description;
    @ManyToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<Task> subTasks;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Tag> tags;


    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void sync() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @SuppressWarnings("unused")
    public Card() {
    }

    /**
     * Constructor with given title
     *
     * @param title
     */
    public Card(String title) {
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

    public void assign(Card other) {
        this.title = other.title;
        this.description = other.description;
        if (this.tags == null)
            this.tags = new HashSet<>();
        this.tags.clear();

        if (other.tags != null)
            this.tags.addAll(other.tags);

        if (other.subTasks == null) {
            if (this.subTasks != null) {
                this.subTasks.clear();
            }
            return;
        }

        // delete all subtasks that are not in the other card
        List<Task> tasksToRemove = new ArrayList<>();
        for (Task task : subTasks) {
            boolean found = false;
            for (Task otherTask : other.subTasks) {
                if (task.getId() == otherTask.getId()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                tasksToRemove.add(task);
            }
        }
        subTasks.removeAll(tasksToRemove);

        // assign all subtasks that are in the other card
        for (Task otherTask : other.subTasks) {
            boolean found = false;
            for (Task task : subTasks) {
                if (task.getId() == otherTask.getId()) {
                    task.assign(otherTask);
                    found = true;
                    break;
                }
            }
            if (!found) {
                subTasks.add(otherTask);
            }
        }
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
