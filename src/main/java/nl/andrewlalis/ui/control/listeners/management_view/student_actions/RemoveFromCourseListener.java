package nl.andrewlalis.ui.control.listeners.management_view.student_actions;

import nl.andrewlalis.ui.control.listeners.management_view.TableRowListener;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user intends to remove a selected student from the course entirely. This entails a few things:
 * 1. Remove them from any team they are in.
 * 2. Archive any repository that is empty as a result of removing them.
 * 3. Remove the student from the list of students.
 *      (This should not actually remove the record, just set it as removed.)
 */
public class RemoveFromCourseListener extends TableRowListener {

    public RemoveFromCourseListener(JTable table) {
        super(table);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        StudentTableModel model = (StudentTableModel) this.getTable().getModel();

        System.out.println(model.getStudentAt(this.getSelectedRow()));
    }
}
