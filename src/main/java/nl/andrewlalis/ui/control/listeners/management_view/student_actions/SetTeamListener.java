package nl.andrewlalis.ui.control.listeners.management_view.student_actions;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.ui.control.listeners.management_view.TableRowListener;
import nl.andrewlalis.ui.view.dialogs.TeamChooserDialog;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user wishes to set the team of a certain student. This should do the following:
 * 1. User selects team to set student to, or chooses to create a new team.
 * 2. StudentTeam object is created or modified.
 * 3. The repository is updated automatically.
 */
public class SetTeamListener extends TableRowListener {

    public SetTeamListener(JTable table) {
        super(table);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        StudentTableModel model = (StudentTableModel) this.getTable().getModel();
        Student student = model.getStudentAt(this.getSelectedRow());

        StudentTeam chosenTeam = (StudentTeam) new TeamChooserDialog(SwingUtilities.getWindowAncestor(this.getTable())).getSelectedTeam();
    }
}
