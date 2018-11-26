package nl.andrewlalis.ui.control.listeners.management_view.student_team_actions;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user wishes to delete one or more repositories.
 */
public class DeleteRepositoryListener extends StudentTeamListener {

    /**
     * The GithubManager used for deleting the repositories.
     */
    private GithubManager manager;

    /**
     * Constructs a table row listener.
     *
     * @param table The table on which to get selected rows.
     */
    public DeleteRepositoryListener(JTable table, GithubManager manager) {
        super(table);
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        for (StudentTeam team : this.getSelectedStudentTeams()) {

            if (team.getRepositoryName() == null) {
                JOptionPane.showMessageDialog(
                        this.getTable().getTopLevelAncestor(),
                        "Team " + team.getNumber() + " does not have a repository.",
                        "No repository",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            if (team.getRepositoryName() != null) {
                new Thread(() -> {
                    boolean success = this.manager.deleteRepository(team.getRepositoryName());
                    if (success) {
                        DbHelper.executeTransactionContent(session -> {
                            team.setRepositoryName(null);
                            this.getModel().fireTableDataChanged();
                            session.update(team);
                        });
                    }
                }).start();
            }
        }
    }
}
