package nl.andrewlalis.ui.control.listeners.input_students_file_view;

import nl.andrewlalis.ui.control.listeners.ViewChangeListener;
import nl.andrewlalis.ui.view.AbstractView;

/**
 * Listens for when the user clicks 'Done' after selecting a file to input.
 */
public class DoneListener extends ViewChangeListener {

    public DoneListener(AbstractView previousView, AbstractView newView) {
        super(previousView, newView);
    }

}
