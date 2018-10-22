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

        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));

        this.repositoryNameField = new JTextField();
        fieldPanel.add(this.generateTextFieldPanel("Assignments repository name:", this.repositoryNameField));
        contentPane.add(fieldPanel, BorderLayout.CENTER);


        JButton nextButton = new JButton("Next");
        InputStudentsFileView inputStudentsFileView = new InputStudentsFileView(this.getGithubManager());
        this.addChildView(inputStudentsFileView);
        nextButton.addActionListener(new NextListener(this, inputStudentsFileView));
        contentPane.add(nextButton, BorderLayout.SOUTH);

        return contentPane;
    }
}
