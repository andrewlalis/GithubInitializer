package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;

/**
 * An executable which opens up a dialog to allow a user to delegate how many student teams each TATeam gets to manage,
 * and actually generate the student repositories using the manager.
 */
public class GenerateStudentRepos extends GithubExecutable {

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        return false;
    }

}
