package nl.andrewlalis.ui.control.listeners.management_view.student_actions;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;
import nl.andrewlalis.ui.view.dialogs.TeamChooserDialog;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;
import nl.andrewlalis.ui.view.table_models.StudentTeamTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Listens for when the user wishes to set the team of a certain student. This should do the following:
 * 1. User selects team to set student to, or chooses to create a new team.
 * 2. StudentTeam object is created or modified.
 * 3. The repository is updated automatically.
 */
public class SetTeamListener extends StudentListener {

    /**
     * A reference to the student teams model, which needs to update in conjunction with the students table.
     */
    private StudentTeamTableModel teamModel;

    /**
     * Creates a new listener for when a user sets a student's team to a new team.
     * @param table The table on which this listener is listening.
     * @param teamModel The model representing this list of teams. This is needed to update after a change.
     */
    public SetTeamListener(JTable table, StudentTeamTableModel teamModel) {
        super(table);
        this.teamModel = teamModel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        StudentTableModel model = this.getModel();
        Student student = model.getStudentAt(this.getTable().convertRowIndexToModel(this.getSelectedRow()));
        List<StudentTeam> teams = this.teamModel.getTeams();

        // Get the selected team via a dialog.
        StudentTeam chosenTeam = (StudentTeam) new TeamChooserDialog(
                SwingUtilities.getWindowAncestor(this.getTable()),
                teams
        ).getSelectedTeam();

        // Check that the new team is not the current team.
        if (chosenTeam.equals(student.getAssignedTeam())) {
            JOptionPane.showMessageDialog(this.getTable(), "You must choose a new team.", "Same team", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Perform the updates to the database here.
        DbHelper.executeTransactionContent(session -> {
            student.getAssignedTeam().removeMember(student);
            session.update(student.getAssignedTeam());

            chosenTeam.addMember(student);
            session.update(chosenTeam);

            student.assignToTeam(chosenTeam);
            session.update(student);
        });

        this.teamModel.setStudentTeamsList(DbHelper.getStudentTeams());
        model.setStudentsList(DbHelper.getStudents());
    }
}
