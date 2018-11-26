package nl.andrewlalis.ui.view.table_models;

import nl.andrewlalis.model.database.BaseEntity;

import javax.swing.table.AbstractTableModel;

/**
 * A table model representing any possible entity that would be displayed in a table.
 */
public abstract class AbstractEntityModel extends AbstractTableModel {

    /**
     * Gets the entity at a particular row in the model.
     * @param row The row at which to get the entity.
     * @return The entity in a certain row in the model.
     */
    public abstract BaseEntity getEntityAt(int row);

}
