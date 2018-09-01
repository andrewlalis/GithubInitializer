package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.ui.view.dialogs.DelegateStudentTeamsDialog;

/**
 * An executable which opens up a dialog to allow a user to delegate how many student teams each TATeam gets to manage,
 * and actually generate the student repositories using the manager.
 */
public class DelegateStudentTeams extends GithubExecutable {

    /**
     * A reference to the main application frame, used as the parent for the dialog which is shown.
     */
    private InitializerApp app;

    public DelegateStudentTeams(InitializerApp app) {
        this.app = app;
    }

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        DelegateStudentTeamsDialog dialog = new DelegateStudentTeamsDialog(this.app, manager);
        dialog.begin();
        return true;
    }

}
