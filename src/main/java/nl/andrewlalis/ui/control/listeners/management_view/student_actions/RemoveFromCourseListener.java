package nl.andrewlalis.ui.control.listeners.management_view.student_actions;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;
import nl.andrewlalis.ui.view.table_models.StudentTeamTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Listens for when the user intends to remove a selected student from the course entirely. This entails a few things:
 * 1. Remove them from any team they are in.
 * 2. Remove the student from the list of students.
 *      (This should not actually remove the record, just set it as removed.)
 */
public class RemoveFromCourseListener extends StudentListener {

    /**
     * A reference to the team table model, used for updating that table when a student is removed.
     */
    private StudentTeamTableModel teamModel;

    public RemoveFromCourseListener(JTable table, StudentTeamTableModel teamModel) {
        super(table);
        this.teamModel = teamModel;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        StudentTableModel model = this.getModel();

        // Get all the selected students.
        List<Student> studentsToRemove = this.getSelectedStudents();

        if (studentsToRemove.isEmpty()) {
            return;
        }

        // First get confirmation that the student should be removed.
        int response = JOptionPane.showConfirmDialog(
                SwingUtilities.getWindowAncestor(this.getTable()),
                "Are you sure you wish to remove the student(s)?\nThis action is permanent.",
                "Remove students",
                JOptionPane.YES_NO_OPTION
        );
        if (response == JOptionPane.NO_OPTION) {
            return;
        }

        // First remove the student from any team they were in.
        // Then set the student as archived.
        DbHelper.executeTransactionContent(session -> {
            for (Student student : studentsToRemove) {

                StudentTeam team = student.getAssignedTeam();
                // Only if the student's team is still active, should they be removed from it.
                if (team != null && !team.isArchived()) {
                    student.getAssignedTeam().removeMember(student);
                    session.update(student.getAssignedTeam());
                }

                student.setArchived(true);
                session.update(student);
            }
        });

        // Update the models afterwards.
        model.setStudentsList(DbHelper.getStudents());
        this.teamModel.setStudentTeamsList(DbHelper.getStudentTeams());
    }
}
