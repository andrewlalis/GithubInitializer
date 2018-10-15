package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;

import javax.swing.*;
import java.awt.*;

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
     * Initializes the view by packing the content pane as it is defined by any child, and setting some generic swing
     * values.
     * @param title The window's title.
     * @param startVisible Whether or not to start the view as visible.
     * @param defaultCloseOperation What to do when the user closes the window.
     * @param preferredSize The preferred size of the view.
     */
    AbstractView(String title, boolean startVisible, int defaultCloseOperation, Dimension preferredSize, GithubManager githubManager) {
        super(title);
        this.githubManager = githubManager;
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
}
