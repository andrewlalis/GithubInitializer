package nl.andrewlalis.ui.view;

import nl.andrewlalis.Main;
import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.control.listeners.ViewChangeListener;
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

    /**
     * Constructs the starting view, with pre-defined organization and access tokens.
     * @param githubManager A reference to the github manager this application uses.
     * @param organizationName The name of the organization.
     * @param accessToken The access token from the user.
     */
    public StartView(GithubManager githubManager, String organizationName, String accessToken) {
        this(githubManager);
        this.organizationNameField.setText(organizationName);
        this.accessTokenField.setText(accessToken);
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
        this.accessTokenField = new JTextField();
        infoInputPanel.add(this.generateTextFieldPanel("Organization name:", this.organizationNameField));
        infoInputPanel.add(this.generateTextFieldPanel("Access token:", this.accessTokenField));

        JPanel buttonsPanel = new JPanel();
        // Create the button for going to the Create assignments repository view.
        JButton assignmentsViewButton = new JButton("Start New Course");
        CreateAssignmentsView assignmentsView = new CreateAssignmentsView(this.getGithubManager());
        this.addChildView(assignmentsView);
        assignmentsView.addParentView(this);
        assignmentsViewButton.addActionListener(new CreateAssignmentsRepoListener(this, assignmentsView));

        // Create the button for going straight to the management view.
        JButton managementViewButton = new JButton("Manage Existing Course");
        this.addChildView(Main.getManagementView());
        Main.getManagementView().addParentView(this);
        managementViewButton.addActionListener(new ViewChangeListener(this, Main.getManagementView()));

        buttonsPanel.add(assignmentsViewButton);
        buttonsPanel.add(managementViewButton);

        contentPane.add(infoInputPanel, BorderLayout.CENTER);
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        return contentPane;
    }
}
