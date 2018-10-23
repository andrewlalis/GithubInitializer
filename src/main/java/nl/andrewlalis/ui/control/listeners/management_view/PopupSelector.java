package nl.andrewlalis.ui.control.listeners.management_view;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

/**
 * This listener, when added to a JPopupMenu, will select the clicked row when a user right-clicks on the table.
 */
public class PopupSelector implements PopupMenuListener {

    /**
     * The table on which to select rows.
     */
    private JTable table;

    public PopupSelector(JTable table) {
        this.table = table;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        JPopupMenu popupMenu = (JPopupMenu) popupMenuEvent.getSource();
        SwingUtilities.invokeLater(() -> {
            int rowAtPoint = table.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), table));
            if (rowAtPoint > -1) {
                table.setRowSelectionInterval(rowAtPoint, rowAtPoint);
            }
        });
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

    }
}
