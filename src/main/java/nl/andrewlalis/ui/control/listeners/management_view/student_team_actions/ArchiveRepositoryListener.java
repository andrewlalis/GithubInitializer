package nl.andrewlalis.ui.control.listeners.management_view.student_team_actions;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user wants to archive one or more repositories.
 */
public class ArchiveRepositoryListener extends StudentTeamListener {

    /**
     * The manager, for interacting with Github, to perform the archiving.
     */
    private GithubManager manager;

    /**
     * Constructs a table row listener.
     *
     * @param table The table on which to get selected rows.
     * @param manager The manager used to interact with Github.
     */
    public ArchiveRepositoryListener(JTable table, GithubManager manager) {
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
            if (team.getRepositoryName() == null) {
                JOptionPane.showMessageDialog(
                        this.getTable().getTopLevelAncestor(),
                        "Team " + team.getNumber() + " does not have a repository to archive." ,
                        "No repository",
                        JOptionPane.WARNING_MESSAGE
                );
            }

            if (!team.isArchived() && team.getRepositoryName() != null) {
                new Thread(() -> {
                    boolean success = this.manager.archiveRepository(team.getRepositoryName());
                    if (success) {
                        DbHelper.executeTransactionContent(session -> {
                            team.setArchived(true);
                            session.update(team);
                            this.getModel().fireTableDataChanged();
                        });
                    }
                }).start();
            }
        }
    }
}
