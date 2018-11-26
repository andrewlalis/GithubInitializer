package nl.andrewlalis.model.database;

import nl.andrewlalis.ui.view.components.Detailable;

import javax.persistence.*;

/**
 * Defines a base entity which all others in the database extend from.
 */
@MappedSuperclass
public abstract class BaseEntity implements Detailable {

    /**
     * The number for this entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Whether or not this entity is archived.
     */
    @Column(name = "archived", nullable = false)
    private boolean archived = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
