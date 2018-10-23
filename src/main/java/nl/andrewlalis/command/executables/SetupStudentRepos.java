package nl.andrewlalis.command.executables;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
import nl.andrewlalis.ui.view.InitializerApp;
import org.kohsuke.github.GHRepository;

import java.util.List;

/**
 * This executable, when run, sets up all student repositories.
 */
public class SetupStudentRepos extends GithubExecutable {

    /**
     * A reference to the current application.
     */
    private InitializerApp app;

    public SetupStudentRepos(InitializerApp app) {
        this.app = app;
    }

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (args.length < 2) {
            return false;
        }
        List<TATeam> taTeams = this.app.getOrganization().getTaTeams();
        for (TATeam team : taTeams) {
            for (StudentTeam studentTeam : team.getStudentTeams()) {
                GHRepository assignmentsRepo = manager.getRepository(args[1]);
                if (assignmentsRepo == null) {
                    return false;
                }
                manager.setupStudentRepo(studentTeam, team, args[0], assignmentsRepo);
            }
        }
        return true;
    }
}
