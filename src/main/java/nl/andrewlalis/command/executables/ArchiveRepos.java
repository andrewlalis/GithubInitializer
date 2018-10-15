package nl.andrewlalis.command.executables;

import nl.andrewlalis.git_api.GithubManager;

/**
 * Represents the action archive all repositories with a certain substring in their name.
 * It takes the following arguments:
 *
 * 1. Repo substring to archive by
 */
public class ArchiveRepos extends GithubExecutable {

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (args.length < 1) {
            return false;
        }
        manager.archiveAllRepositories(args[0]);
        return true;
    }
}
