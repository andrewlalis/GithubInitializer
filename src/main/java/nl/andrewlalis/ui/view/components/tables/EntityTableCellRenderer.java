package nl.andrewlalis.ui.view.components.tables;

import nl.andrewlalis.model.database.BaseEntity;
import nl.andrewlalis.ui.view.table_models.AbstractEntityModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * This class extends the default table cell renderer to allow for some custom rendering of table cells for entity
 * tables.
 */
public class EntityTableCellRenderer extends DefaultTableCellRenderer {

    /**
     * The model which is applied to the table that this cell renderer is applied to.
     */
    private AbstractEntityModel model;

    /**
     * Constructs a new cell renderer which is linked to the given model.
     * @param model A model of entities which is used for rendering cells.
     */
    public EntityTableCellRenderer(AbstractEntityModel model) {
        this.model = model;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component parentRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null && !isSelected) {
            BaseEntity entity = this.model.getEntityAt(row);
            if (entity.isArchived()) {
                parentRenderer.setBackground(Color.LIGHT_GRAY);
            } else {
                parentRenderer.setBackground(Color.WHITE);
            }
        }

        return parentRenderer;
    }

}
