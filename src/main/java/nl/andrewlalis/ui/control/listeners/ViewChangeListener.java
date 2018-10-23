package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.view.AbstractView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The ViewChangeListener is attached to buttons which should change the view to a new view. With this listener, one
 * needs to simply give the previous view, and the next view, and
 */
public class ViewChangeListener implements ActionListener {

    protected AbstractView previousView;
    protected AbstractView newView;

    public ViewChangeListener(AbstractView previousView, AbstractView newView) {
        this.previousView = previousView;
        this.newView = newView;
        this.newView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                back();
            }
        });
    }

    /**
     * This method is called just before the view is changed.
     * @return True if the change should happen, or false if some validation or check prevents the user from moving to
     * the next view.
     */
    protected boolean beforeChange() {
        // Child classes can implement extra behavior here.
        return true;
    }

    /**
     * Defines some default behavior for switching to a new view.
     * @param actionEvent The event which triggered this action.
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (this.beforeChange()) {
            this.forward();
        }
    }

    /**
     * Goes to the new view, and hides the previous view.
     */
    private void forward() {
        this.previousView.setVisible(false);
        this.newView.reset();
        this.newView.setVisible(true);
    }

    /**
     * Goes 'back in time', or rather, hides the current view and moves back to the one which sent us here.
     */
    private void back() {
        this.previousView.reset();
        this.previousView.setVisible(true);
        this.newView.setVisible(false);
    }
}
