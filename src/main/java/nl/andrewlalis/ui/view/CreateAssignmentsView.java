package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.control.listeners.create_assignments_view.NextListener;

import javax.swing.*;
import java.awt.*;

/**
 * In this view, the user will enter the name of an assignments repository to use for the course, or allows the user to
 * create a new one.
 *
 * Once the user is here, it is guaranteed that the github manager has been validated.
 */
public class CreateAssignmentsView extends AbstractView {

    private JTextField repositoryNameField;

    public CreateAssignmentsView(GithubManager manager) {
        super("Create/Set Assignments Repository",
                false,
                DISPOSE_ON_CLOSE,
                null,
                manager);
    }

    public String getRepositoryName() {
        return this.repositoryNameField.getText();
    }

    @Override
    protected JPanel buildContentPane() {
        JPanel contentPane = new JPanel();

        contentPane.setLayout(new BorderLayout());
        this.repositoryNameField = new JTextField();
        contentPane.add(this.generateTextFieldPanel("Repository name:", this.repositoryNameField), BorderLayout.CENTER);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(new NextListener(this, new InputStudentsFileView(this.getGithubManager())));
        contentPane.add(nextButton, BorderLayout.SOUTH);

        return contentPane;
    }
}
