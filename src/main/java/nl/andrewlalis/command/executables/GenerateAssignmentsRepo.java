package nl.andrewlalis.command.executables;

import nl.andrewlalis.git_api.GithubManager;

import java.io.IOException;

/**
 * Generates the assignments repository, with the supplied github manager, as well as the following extra arguments:
 *
 * 1. The name of the repository.
 * 2. Description of the repository.
 * 3. Name of TA team containing all members.
 */
public class GenerateAssignmentsRepo extends GithubExecutable {

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (args.length < 3) {
            return false;
        }
        try {
            manager.setupAssignmentsRepo(args[0], args[1], args[2]);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
