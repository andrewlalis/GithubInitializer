package nl.andrewlalis.ui.view.components.tables;

import nl.andrewlalis.ui.view.components.DetailPanel;
import nl.andrewlalis.ui.view.table_models.AbstractEntityModel;

import javax.swing.*;

/**
 * An extension to the JTable component for displaying any arbitrary entity in the management view.
 *
 * Also sets some default settings for the table.
 */
public abstract class EntityTable extends JTable {

    /**
     * The panel on which to display details of the selected entity.
     */
    private DetailPanel detailPanel;

    /**
     * Constructs a new table with the given model and a reference to a detail panel to show extra details about the entity.
     * @param model The table model to apply to this table.
     * @param detailPanel The panel on which to display details of the selected entity.
     */
    protected EntityTable(AbstractEntityModel model, DetailPanel detailPanel) {
        super(model);
        this.detailPanel = detailPanel;
    }

    /**
     * This method should be called by child classes once all variables and additional requirements for the popup menu
     * are initialized. Also adds a list selection listener to display the selected entity by default.
     */
    protected void setupTable() {
        AbstractEntityModel model = (AbstractEntityModel) this.getModel();

        this.setFillsViewportHeight(true);
        this.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            int row = this.getSelectedRow();
            if (row >= 0 && row < model.getRowCount()) {
                this.detailPanel.setDetailableEntity(model.getEntityAt(this.convertRowIndexToModel(row)));
            }
        });

        EntityTableCellRenderer renderer = new EntityTableCellRenderer(model);
        this.setDefaultRenderer(Integer.class, renderer);
        this.setDefaultRenderer(String.class, renderer);
        this.setDefaultRenderer(int.class, renderer);

        this.setFont(this.getFont().deriveFont(14.0f));

        this.setComponentPopupMenu(this.getPopupMenu());
        this.setAutoCreateRowSorter(true);
    }

    /**
     * @return The popup menu for this table. This is defined by child classes.
     */
    protected abstract JPopupMenu getPopupMenu();

}
