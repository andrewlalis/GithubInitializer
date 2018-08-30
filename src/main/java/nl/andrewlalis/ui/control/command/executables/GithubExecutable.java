package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.control.command.Executable;

/**
 * Represents an executable which interacts with github, and therefore needs access to a Github
 * manager to execute.
 *
 * Requires two arguments:
 * 1. The organization name.
 * 2. The organization's access token.
 *
 * Any additional arguments are added to a new String[] array which is passed along to child classes, so that they do
 * not have to filter out the mandatory first two arguments.
 */
public abstract class GithubExecutable implements Executable {

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            return false;
        }
        String[] extraArgs = new String[args.length-2];
        System.arraycopy(args, 2, extraArgs, 0, args.length-2);
        GithubManager manager = new GithubManager(args[0], args[1]);
        return this.executeWithManager(manager, extraArgs);
    }

    /**
     * Executes a command and provides a github manager with which to perform operations.
     * @param manager The GithubManager used to perform actions on the repositories.
     * @param args Any additional arguments provided to the executable.
     * @return True if successful, or false otherwise.
     */
    protected abstract boolean executeWithManager(GithubManager manager, String[] args);
}
