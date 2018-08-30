package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.view.DefineTaTeamsDialog;
import nl.andrewlalis.ui.view.InitializerApp;

/**
 * This executable is slightly different from the others, in that it opens up a user interface to make editing TA teams
 * possible. Therefore, executing this command opens the 'DefineTaTeams' dialog, within which a user can make changes
 * to the TA teams in the organization.
 */
public class DefineTaTeams extends GithubExecutable {

    /**
     * An instance of the main application frame; used when constructing the dialog.
     */
    private InitializerApp app;

    public DefineTaTeams(InitializerApp app) {
        this.app = app;
    }

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        DefineTaTeamsDialog dialog = new DefineTaTeamsDialog(this.app, manager);
        dialog.begin();
        return true;
    }

}
