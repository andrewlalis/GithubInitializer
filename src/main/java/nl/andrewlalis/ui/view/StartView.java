package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.control.listeners.start_view.CreateAssignmentsRepoListener;

import javax.swing.*;
import java.awt.*;

/**
 * At this view, the user is asked to first enter the name of the organization, and the access token they created for
 * their authenticated Github account.
 *
 * Then, the user must choose whether they are starting a new course setup, or managing an existing one.
 *
 * If they choose to start a new course, they are taken to the AssignmentsRepoView, otherwise if they want to manage
 * an existing course, they are taken to the ManagementView.
 */
public class StartView extends AbstractView {

    // Fields which hold information needed by the Github Manager.
    private JTextField organizationNameField;
    private JTextField accessTokenField;

    public StartView(GithubManager githubManager) {
        super("Github Initializer Startup",
                true,
                DISPOSE_ON_CLOSE,
                null,
                githubManager);
    }

    public String getOrganizationName() {
        return this.organizationNameField.getText();
    }

    public String getAccessToken() {
        return this.accessTokenField.getText();
    }

    @Override
    protected JPanel buildContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());

        JPanel infoInputPanel = new JPanel();
        infoInputPanel.setLayout(new BoxLayout(infoInputPanel, BoxLayout.PAGE_AXIS));
        this.organizationNameField = new JTextField();
        infoInputPanel.add(this.generateTextFieldPanel("Organization name:", this.organizationNameField));
        this.accessTokenField = new JTextField();
        infoInputPanel.add(this.generateTextFieldPanel("Access token:", this.accessTokenField));

        JPanel buttonsPanel = new JPanel();
        JButton assignmentsViewButton = new JButton("Start New Course");
        assignmentsViewButton.addActionListener(new CreateAssignmentsRepoListener(this, new CreateAssignmentsView(this.getGithubManager())));
        JButton managementViewButton = new JButton("Manage Existing Course");

        buttonsPanel.add(assignmentsViewButton);
        buttonsPanel.add(managementViewButton);

        contentPane.add(infoInputPanel, BorderLayout.CENTER);
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        return contentPane;
    }
}
