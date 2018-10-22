package nl.andrewlalis.ui.view.table_models;

import nl.andrewlalis.model.Student;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * This table model is used for the representation of a list of persons, with their basic information.
 */
public class StudentTableModel extends AbstractTableModel {

    /**
     * The list of data that is used in the table.
     */
    private List<Student> studentsList;

    /**
     * A default list of column headers for this table.
     */
    private String[] columns = {"Number", "Name", "Email", "Github", "Team"};

    /**
     * Constructs a new model based on the given list of students.
     * @param studentsList A list of students to display in the table model.
     */
    public StudentTableModel(List<Student> studentsList) {
        this.studentsList = studentsList;
    }

    /**
     * Sets a new list of students as the data for this list model.
     * @param newList The new list of students to use.
     */
    public void setStudentsList(List<Student> newList) {
        this.studentsList = newList;
        this.fireTableDataChanged();
    }

    /**
     * Gets the student in a particular row.
     * @param row The row of the table.
     * @return The student object at the specified row, or null if none is found.
     */
    public Student getStudentAt(int row) {
        return this.studentsList.get(row);
    }

    @Override
    public int getRowCount() {
        return studentsList.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    @Override
    public String getColumnName(int i) {
        return this.columns[i];
    }

    @Override
    public Object getValueAt(int row, int col) {
        Student student = this.getStudentAt(row);

        switch(col) {
            case 0:
                return student.getNumber();
            case 1:
                return student.getName();
            case 2:
                return student.getEmailAddress();
            case 3:
                return student.getGithubUsername();
            case 4:
                return student.getAssignedTeam().getId();
            default:
                return null;
        }
    }
}
