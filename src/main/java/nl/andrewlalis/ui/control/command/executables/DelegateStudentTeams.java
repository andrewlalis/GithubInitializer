package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.ui.view.dialogs.delegateStudentTeams.DelegateStudentTeamsDialog;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/**
 * An executable which opens up a dialog to allow a user to delegate how many student teams each TATeam gets to manage,
 * and actually generate the student repositories using the manager.
 */
public class DelegateStudentTeams extends GithubExecutable {

    /**
     * A reference to the main application frame, used as the parent for the dialog which is shown.
     */
    private InitializerApp app;

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(DelegateStudentTeams.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    public DelegateStudentTeams(InitializerApp app) {
        this.app = app;
    }

    @Override
    protected boolean executeWithManager(GithubManager manager, String[] args) {
        if (InitializerApp.organization.getStudentTeams().isEmpty()) {
            JOptionPane.showMessageDialog(this.app, "There are no student teams! Please read some from a CSV file first.", "No Student Teams", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        DelegateStudentTeamsDialog dialog = new DelegateStudentTeamsDialog(this.app, manager);
        dialog.begin();

        if (dialog.isSuccessful()) {
            Map<TATeam, Integer> results = dialog.getResult();
            List<StudentTeam> teams = InitializerApp.organization.getStudentTeams();
            int initialTeamsSize = teams.size();
            Stack<StudentTeam> teamsStack = new Stack<>();
            // Randomize the ordering of the student teams here.
            for (int i = 0; i < initialTeamsSize; i++) {
                teamsStack.push(teams.remove(ThreadLocalRandom.current().nextInt(0, teams.size())));
            }

            for (Map.Entry<TATeam, Integer> entry : results.entrySet()) {
                TATeam team = entry.getKey();
                logger.fine("Team: " + team.getName() + " has " + entry.getValue() + " student teams.");
                for (int i = 0; i < entry.getValue(); i++) {
                    team.addStudentTeam(teamsStack.pop());
                }
                InitializerApp.organization.getTaTeams().add(team);
            }
        } else {
            return false;
        }

        return true;
    }

}
