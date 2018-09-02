package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
import nl.andrewlalis.ui.view.InitializerApp;

import java.util.List;

public class SetupStudentRepos extends GithubExecutable {
    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (args.length < 1) {
            return false;
        }
        List<TATeam> taTeams = InitializerApp.organization.getTaTeams();
        for (TATeam team : taTeams) {
            for (StudentTeam studentTeam : team.getStudentTeams()) {
                manager.setupStudentRepo(studentTeam, team, args[0]);
            }
        }
        return true;
    }
}
