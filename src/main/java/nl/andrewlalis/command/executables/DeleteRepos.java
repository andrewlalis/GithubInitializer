package nl.andrewlalis.command.executables;

import nl.andrewlalis.git_api.GithubManager;

/**
 * Deletes all repositories with a given substring.
 */
public class DeleteRepos extends GithubExecutable {
    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (args.length < 1) {
            return false;
        }
        manager.deleteAllRepositories(args[0]);
        return true;
    }
}
