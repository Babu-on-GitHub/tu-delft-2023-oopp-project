package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @ManyToOne
    Card card;

    @SuppressWarnings("unused")
    public Task(){

    }

    public Task(long id, String title) {
        this.id = id;
        this.title = title;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return new EqualsBuilder().append(id, task.id).append(title, task.title).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(title).toHashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
