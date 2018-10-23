package nl.andrewlalis.ui.control.listeners.management_view.student_actions;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;
import nl.andrewlalis.ui.control.listeners.management_view.TableRowListener;
import nl.andrewlalis.ui.view.ManagementView;
import nl.andrewlalis.ui.view.dialogs.TeamChooserDialog;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;
import nl.andrewlalis.ui.view.table_models.StudentTeamTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user wishes to set the team of a certain student. This should do the following:
 * 1. User selects team to set student to, or chooses to create a new team.
 * 2. StudentTeam object is created or modified.
 * 3. The repository is updated automatically.
 */
public class SetTeamListener extends TableRowListener {

    /**
     * The teamModel is a reference to the current team model for student teams.
     */
    private StudentTeamTableModel teamModel;

    /**
     * A reference to the management view, which is used to tell the management view when to update the models.
     */
    private ManagementView managementView;

    public SetTeamListener(ManagementView managementView, JTable table, StudentTeamTableModel teamModel) {
        super(table);
        this.managementView = managementView;
        this.teamModel = teamModel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        StudentTableModel model = (StudentTableModel) this.getTable().getModel();
        Student student = model.getStudentAt(this.getSelectedRow());

        StudentTeam[] teams = new StudentTeam[this.teamModel.getRowCount()];
        this.teamModel.getTeams().toArray(teams);

        StudentTeam chosenTeam = (StudentTeam) new TeamChooserDialog(
                SwingUtilities.getWindowAncestor(this.getTable()),
                teams
        ).getSelectedTeam();

        // Perform the updates to the database here.
        DbHelper.executeTransactionContent(session -> {
            student.getAssignedTeam().removeMember(student);
            session.update(student.getAssignedTeam());

            chosenTeam.addMember(student);
            session.update(chosenTeam);

            student.assignToTeam(chosenTeam);
            session.update(student);
        });

        this.managementView.updateModels();
    }
}
