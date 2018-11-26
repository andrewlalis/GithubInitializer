package nl.andrewlalis.ui.view.components.tables.popup_menu;

import javax.swing.*;

/**
 * An extension of the default menu item, with this type being able to enable/disable depending on the current selection
 * type of the table.
 */
public class EntityMenuItem extends JMenuItem {

    /**
     * An integer representing the bitwise OR of possibly many selection types.
     */
    private int enabledSelectionTypes;

    public EntityMenuItem(String name, int enabledSelectionTypes) {
        super(name);
        this.enabledSelectionTypes = enabledSelectionTypes;
    }

    /**
     * This method is called just before the menu item is shown, thus allowing this menu item to enable or disable
     * itself depending on the selection type.
     * @param selectionType The type of selection that the user currently has on the table.
     */
    public void willBecomeVisible(int selectionType) {
        this.setEnabled((selectionType & this.enabledSelectionTypes) > 0);
    }

}
