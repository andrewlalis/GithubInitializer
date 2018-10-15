package nl.andrewlalis.ui.control.listeners.start_view;

import nl.andrewlalis.ui.control.listeners.ViewChangeListener;
import nl.andrewlalis.ui.view.AbstractView;
import nl.andrewlalis.ui.view.StartView;

/**
 * Listener for when the user intends to create repositories for a new course.
 */
public class CreateAssignmentsRepoListener extends ViewChangeListener {

    public CreateAssignmentsRepoListener(AbstractView previousView, AbstractView newView) {
        super(previousView, newView);
    }

    /**
     * All that needs to be done here is check that the github manager can work with the given info.
     * @return True if the github manager accepts the organization name and access token, false otherwise.
     */
    @Override
    protected boolean beforeChange() {
        StartView startView = (StartView) this.previousView;
        startView.getGithubManager().setOrganizationName(startView.getOrganizationName());
        startView.getGithubManager().setAccessToken(startView.getAccessToken());
        return startView.getGithubManager().validate();
    }
}
