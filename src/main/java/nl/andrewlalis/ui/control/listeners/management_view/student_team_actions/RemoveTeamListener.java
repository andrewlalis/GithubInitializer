package nl.andrewlalis.ui.control.listeners.management_view.student_team_actions;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user wants to remove a team from the list of teams. This should then archive that team, so that
 * it cannot be interacted with further, and all its information essentially becomes read-only.
 */
public class RemoveTeamListener extends StudentTeamListener {

    /**
     * The Github manager for interacting with Github.
     */
    private GithubManager manager;

    /**
     * Constructs a table row listener.
     *
     * @param table The table on which to get selected rows.
     * @param manager The manager to interact
     */
    public RemoveTeamListener(JTable table, GithubManager manager) {
        super(table);
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        for (StudentTeam team : this.getSelectedStudentTeams()) {

            if (team.isArchived()) {
                JOptionPane.showMessageDialog(
                        this.getTable().getTopLevelAncestor(),
                        "Team " + team.getNumber() + " is already archived.",
                        "Already archived",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            if (!team.isArchived()) {
                new Thread(() -> {
                    DbHelper.executeTransactionContent(session -> {
                        if (team.getRepositoryName() != null) {
                            boolean success = this.manager.archiveRepository(team.getRepositoryName());
                            if (!success) {
                                JOptionPane.showMessageDialog(
                                        this.getTable().getTopLevelAncestor(),
                                        "Could not archive team " + team.getNumber() + "'s repository.",
                                        "Could not archive",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                return;
                            }
                        }
                        team.setArchived(true);
                        session.update(team);
                    });
                }).start();
            }
        }
    }
}
