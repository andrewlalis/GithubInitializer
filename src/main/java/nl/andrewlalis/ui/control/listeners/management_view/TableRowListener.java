package nl.andrewlalis.ui.control.listeners.management_view;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * This abstract class defines listeners which listen to tables, that is, a table row is clicked on in the table, and
 * that is passed to children.
 */
public abstract class TableRowListener implements ActionListener {

    /**
     * The table of which to get the row.
     */
    private JTable table;

    /**
     * Constructs a table row listener.
     * @param table The table on which to get selected rows.
     */
    public TableRowListener(JTable table) {
        this.table = table;
    }

    /**
     * @return The selected row.
     */
    protected final int getSelectedRow() {
        return this.table.getSelectedRow();
    }

    /**
     * @return The table that this listener is attached to.
     */
    protected final JTable getTable() {
        return this.table;
    }

}
