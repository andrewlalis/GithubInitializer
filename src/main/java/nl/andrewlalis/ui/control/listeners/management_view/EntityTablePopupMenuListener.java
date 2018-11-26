package nl.andrewlalis.ui.control.listeners.management_view;

import nl.andrewlalis.ui.view.components.tables.EntityTable;
import nl.andrewlalis.ui.view.components.tables.popup_menu.EntityMenuItem;
import nl.andrewlalis.ui.view.components.tables.popup_menu.EntitySelectionType;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

/**
 * Listener to be attached to popup menus for entity tables, so that certain menu items are either enabled or disabled
 * depending on the type of selection in the table. Some menu items should always be enabled, some should only be
 * enabled for single selections, and some should be enabled for multi-selections.
 */
public class EntityTablePopupMenuListener implements PopupMenuListener {

    /**
     * The table on which the menu for this listener is attached.
     */
    private EntityTable table;

    public EntityTablePopupMenuListener(EntityTable table) {
        this.table = table;
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        JPopupMenu menu = (JPopupMenu) popupMenuEvent.getSource();

        // First, determine the selection type.
        int selectionType = 0;
        if (this.table.getSelectedRowCount() == 1) {
            selectionType |= EntitySelectionType.SINGLE;
        } else if (this.table.getSelectedRowCount() > 1) {
            selectionType |= EntitySelectionType.MULTIPLE;
        }

        // Iterate over all menu items, and let them decide to enable or disable themselves.
        for (Component component: menu.getComponents()) {
            if (component instanceof EntityMenuItem) {
                EntityMenuItem menuItem = (EntityMenuItem) component;
                menuItem.willBecomeVisible(selectionType);
            }
        }
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {

    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {

    }
}
