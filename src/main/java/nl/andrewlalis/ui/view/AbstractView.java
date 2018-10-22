package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All views in the application will extend from this view, as a means of simplifying and organizing how visual
 * components are made.
 */
public abstract class AbstractView extends JFrame {

    /**
     * A GithubManager object which can be used to interact with github.
     */
    private GithubManager githubManager;

    /**
     * A list of views which are linked to this one via buttons in the component pane.
     */
    private List<AbstractView> childViews;

    /**
     * A list of views which lead to this one.
     */
    private List<AbstractView> parentViews;

    /**
     * Initializes the view by packing the content pane as it is defined by any child, and setting some generic swing
     * values.
     * @param title The window's title.
     * @param startVisible Whether or not to start the view as visible.
     * @param defaultCloseOperation What to do when the user closes the window.
     * @param preferredSize The preferred size of the view.
     * @param githubManager The manager used for this view.
     */
    AbstractView(String title, boolean startVisible, int defaultCloseOperation, Dimension preferredSize, GithubManager githubManager) {
        super(title);
        this.githubManager = githubManager;
        this.childViews = new ArrayList<>();
        this.parentViews = new ArrayList<>();
        this.setContentPane(this.buildContentPane());
        this.setDefaultCloseOperation(defaultCloseOperation);
        if (preferredSize != null) {
            this.setSize(preferredSize);
        }
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(startVisible);
    }

    /**
     * Constructs this view. Child classes will define how the content pane is constructed by returning that content
     * pane here.
     * @return The content pane containing the view to be rendered.
     */
    protected abstract JPanel buildContentPane();

    /**
     * Resets this view and all form components within it. It is the responsibility of child classes to define how to
     * reset themselves.
     */
    public void reset() {
        // Child classes can define custom behavior here.
    }

    /**
     * Extends the default expose behaviour by recursively disposing all views which are linked to this one.
     */
    public void dispose() {
        for (AbstractView view : this.childViews) {
            view.dispose();
        }
        super.dispose();
    }

    public GithubManager getGithubManager() {
        return githubManager;
    }

    /**
     * Generates a text input field panel.
     * @param labelText The text for the label above the panel.
     * @param textField A reference to the text field that is used in the panel.
     * @return A JPanel containing the label and text field.
     */
    final JPanel generateTextFieldPanel(String labelText, JTextField textField) {
        JPanel newPanel = new JPanel(new BorderLayout());
        newPanel.add(new JLabel(labelText), BorderLayout.NORTH);
        newPanel.add(textField);
        newPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
        return newPanel;
    }

    /**
     * Adds a view as linked to this one. That way, this view can be referenced elsewhere, even when hidden.
     * @param view The view to link.
     */
    protected final void addChildView(AbstractView view) {
        this.childViews.add(view);
    }

    /**
     * @return The list of children of this view.
     */
    protected final List<AbstractView> getChildViews() {
        return this.childViews;
    }

    /**
     * Adds a view as linked to this one as a parent.
     * @param view The parent view.
     */
    protected final void addParentView(AbstractView view) {
        this.parentViews.add(view);
    }

    /**
     * @return The list of parents of this view.
     */
    protected final List<AbstractView> getParentViews() {
        return this.parentViews;
    }

    /**
     * Removes all parents registered to this view.
     */
    protected final void removeParents() {
        this.parentViews.clear();
    }
}
