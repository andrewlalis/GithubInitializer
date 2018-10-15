package nl.andrewlalis.ui.control.listeners.create_assignments_view;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.control.listeners.ViewChangeListener;
import nl.andrewlalis.ui.view.AbstractView;
import nl.andrewlalis.ui.view.CreateAssignmentsView;

import javax.swing.*;
import java.io.IOException;

/**
 * Listens for when the user clicks 'next' in the CreateAssignmentsView. This listener is responsible for checking that
 * the user enters a correct repository name, or if not, asks if the user wishes to create the repository with that
 * name.
 */
public class NextListener extends ViewChangeListener {

    public NextListener(AbstractView previousView, AbstractView newView) {
        super(previousView, newView);
    }

    /**
     * Validate that the repository the user has entered exists in the organization.
     * @return True if the repository exists, or if the user creates a repository with that name, or false if an
     * Assignments repository was not created.
     */
    @Override
    protected boolean beforeChange() {
        CreateAssignmentsView assignmentsView = (CreateAssignmentsView) this.previousView;
        String repoName = assignmentsView.getRepositoryName();
        GithubManager manager = assignmentsView.getGithubManager();
        if (manager.repoExists(repoName)) {
            return true;
        } else {
            int reply = JOptionPane.showConfirmDialog(
                    assignmentsView,
                    "The repository you gave does not exist.\nWould you like to create it?",
                    "Create new repository?",
                    JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    assignmentsView.getGithubManager().setupAssignmentsRepo(repoName, "", this.getTeachingAssistantsTeamName());
                    return true;
                } catch (IOException e) {
                    //e.printStackTrace();
                    JOptionPane.showMessageDialog(assignmentsView, "Could not create repository:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    private String getTeachingAssistantsTeamName() {
        String name = JOptionPane.showInputDialog(this.previousView, "Please enter (exactly) the name of Github team\nthat contains all teaching assistants.", "Select TA Team", JOptionPane.QUESTION_MESSAGE);
        return name;
    }
}
