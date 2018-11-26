package nl.andrewlalis.ui.control.listeners.management_view.student_team_actions;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user wishes to create a repository for one or more selected teams.
 */
public class GenerateRepositoryListener extends StudentTeamListener {

    /**
     * The GithubManager object used to manipulate repositories on Github.
     */
    private GithubManager manager;

    /**
     * Constructs a table row listener.
     *
     * @param table The table on which to get selected rows.
     */
    public GenerateRepositoryListener(JTable table, GithubManager manager) {
        super(table);
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        String prefix = JOptionPane.showInputDialog(
                SwingUtilities.getWindowAncestor(this.getTable()),
                "Please give a prefix for the repository(s).",
                "Repository Prefix",
                JOptionPane.QUESTION_MESSAGE
        );
        if (prefix == null) return;

        for (StudentTeam team : this.getSelectedStudentTeams()) {

            if (team.isArchived()) {
                JOptionPane.showMessageDialog(
                        this.getTable().getTopLevelAncestor(),
                        "Team " + team.getNumber() + " is archived.",
                        "Team archived",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            if (team.getRepositoryName() != null) {
                JOptionPane.showMessageDialog(
                        this.getTable().getTopLevelAncestor(),
                        "Team " + team.getNumber() + " already has a repository.",
                        "Repository exists",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            if (!team.isArchived() && team.getRepositoryName() == null) {
                new Thread(() -> {
                    DbHelper.executeTransactionContent(session -> {
                        String name = this.manager.setupStudentRepo(team, prefix);
                        team.setRepositoryName(name);
                        this.getModel().fireTableDataChanged();
                        session.update(team);
                    });
                }).start();
            }
        }
    }
}
