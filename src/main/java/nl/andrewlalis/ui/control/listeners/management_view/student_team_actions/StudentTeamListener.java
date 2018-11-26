package nl.andrewlalis.ui.control.listeners.management_view.student_team_actions;

import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.ui.control.listeners.management_view.TableRowListener;
import nl.andrewlalis.ui.view.table_models.StudentTeamTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract table row listener designed for use with only the student teams model.
 */
public abstract class StudentTeamListener extends TableRowListener {
    /**
     * Constructs a table row listener.
     *
     * @param table The table on which to get selected rows.
     */
    public StudentTeamListener(JTable table) {
        super(table);
    }

    /**
     * @return The student teams model that this listener applies to.
     */
    public StudentTeamTableModel getModel() {
        return (StudentTeamTableModel) this.getTable().getModel();
    }

    /**
     * @return A list of teams which the user has selected.
     */
    public List<StudentTeam> getSelectedStudentTeams() {
        StudentTeamTableModel model = (StudentTeamTableModel) this.getTable().getModel();
        List<StudentTeam> selectedTeams = new ArrayList<>();
        for (int row : this.getTable().getSelectedRows()) {
            selectedTeams.add(model.getStudentTeamAt(this.getTable().convertRowIndexToModel(row)));
        }
        return selectedTeams;
    }
}
