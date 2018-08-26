package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;

import java.io.IOException;

/**
 * Represents the action archive all repositories with a certain substring in their name.
 * It takes the following arguments:
 *
 * 1. Organization name
 * 2. Access Token
 * 3. Repo substring to archive by
 */
public class ArchiveRepos extends GithubExecutable {

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (args.length < 1) {
            return false;
        }
        try {
            manager.archiveAllRepositories(args[0]);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
