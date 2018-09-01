package nl.andrewlalis.ui.view.dialogs;

import javax.swing.*;

/**
 * A custom JTextField which displays an integer value, and can be set and read more efficiently than if one were to
 * parse back and forth between strings and integers.
 */
public class NumberIndicatorField extends JTextField {

    /**
     * The value currently displayed in the text field.
     */
    private int value;

    public NumberIndicatorField(int initialValue) {
        this.setEditable(false);
        this.setValue(initialValue);
    }

    /**
     * Sets the displayed value to a new integer value.
     * @param newValue The new value to set.
     */
    public void setValue(int newValue) {
        this.value = newValue;
        this.setText(String.valueOf(newValue));
    }

    public void decrement() {
        this.setValue(this.value - 1);
    }

    public void increment() {
        this.setValue(this.value + 1);
    }

    public int getValue() {
        return this.value;
    }

}
