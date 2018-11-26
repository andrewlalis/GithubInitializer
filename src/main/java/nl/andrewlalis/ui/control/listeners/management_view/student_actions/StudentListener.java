package nl.andrewlalis.ui.control.listeners.management_view.student_actions;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.ui.control.listeners.management_view.TableRowListener;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract listener for action which is triggered from the Students table.
 */
public abstract class StudentListener extends TableRowListener {

    /**
     * Constructs a table row listener.
     *
     * @param table The table on which to get selected rows.
     */
    public StudentListener(JTable table) {
        super(table);
    }

    /**
     * @return The students model that this listener applies to.
     */
    public StudentTableModel getModel() {
        return (StudentTableModel) this.getTable().getModel();
    }

    /**
     * @return A list of selected students.
     */
    public List<Student> getSelectedStudents() {
        StudentTableModel model = (StudentTableModel) this.getTable().getModel();
        List<Student> selectedStudents = new ArrayList<>();
        for (int row : this.getTable().getSelectedRows()) {
            selectedStudents.add(model.getStudentAt(this.getTable().convertRowIndexToModel(row)));
        }
        return selectedStudents;
    }

}
