package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;
import org.kohsuke.github.GHRepository;

import java.util.List;
import java.util.logging.Logger;

/**
 * This executable shows a list of repositories with a given substring.
 */
public class ListRepos extends GithubExecutable {


    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(ListRepos.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (args.length < 1) {
            return false;
        }

        List<GHRepository> repos = manager.listReposWithPrefix(args[0]);
        logger.info(outputRepoList(repos));

        return true;
    }

    /**
     * Prints a nicely formatted list of repositories to a string.
     * @param repos The list of repositories.
     * @return A string representation of the list of repos.
     */
    private static String outputRepoList(List<GHRepository> repos) {
        StringBuilder sb = new StringBuilder();
        sb.append("List of ").append(repos.size()).append(" repositories:\n");
        for (GHRepository repo : repos) {
            sb.append('\t').append(repo.getName()).append('\n');
        }
        return sb.toString();
    }
}
