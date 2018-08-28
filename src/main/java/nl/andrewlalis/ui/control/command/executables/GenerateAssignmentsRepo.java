package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;

/**
 * Generates the assignments repository, with the supplied github manager, as well as the following extra arguments:
 */
public class GenerateAssignmentsRepo extends GithubExecutable {

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {

        manager.setupAssignmentsRepo(args[0], args[1], );
    }

}
