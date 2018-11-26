package nl.andrewlalis.ui.view.table_models;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * This table model represents the list of student teams.
 */
public class StudentTeamTableModel extends AbstractEntityModel {

    /**
     * The container for the data objects.
     */
    private List<StudentTeam> studentTeamsList;

    /**
     * The column headers for this model. In addition to these headers, this model will dynamically create headers for
     * each additional student to be listed in the table.
     */
    private final String[] staticColumns = {"Number", "Repository Name", "TA Team"};

    /**
     * Dynamic columns which are generated depending on the teams.
     */
    private String[] columns = {};

    public StudentTeamTableModel() {
        this.studentTeamsList = new ArrayList<>();
    }

    public StudentTeamTableModel(List<StudentTeam> teams) {
        super();
        this.setStudentTeamsList(teams);
    }

    /**
     * Sets a new list of student teams as the data for this list model.
     * @param newList A list of student teams to display in the table model.
     */
    public void setStudentTeamsList(List<StudentTeam> newList) {
        this.studentTeamsList = newList;
        int maxMembers = this.getMaxMemberCount();
        if (this.columns.length != maxMembers) {
            this.generateColumnNames(maxMembers);
            this.fireTableStructureChanged();
        }
        this.fireTableDataChanged();
    }

    /**
     * Gets the student team in a particular row.
     * @param row The row of the table.
     * @return The student team object at the specified row, or null if none is found.
     */
    public StudentTeam getStudentTeamAt(int row) {
        if (row >= 0 && row < this.studentTeamsList.size()) {
            return this.studentTeamsList.get(row);
        }
        return null;
    }

    @Override
    public BaseEntity getEntityAt(int row) {
        return this.getStudentTeamAt(row);
    }

    /**
     * @return A list of all teams in this model.
     */
    public List<StudentTeam> getTeams() {
        return this.studentTeamsList;
    }

    @Override
    public int getRowCount() {
        return this.studentTeamsList.size();
    }

    @Override
    public int getColumnCount() {
        return this.columns.length;
    }

    @Override
    public String getColumnName(int i) {
        if (i >= 0 && i < this.columns.length) {
            return this.columns[i];
        } else {
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int i) {
        if (this.studentTeamsList.isEmpty()) {
            return Object.class;
        }
        Object value = this.getValueAt(0, i);
        if (value != null) {
            return value.getClass();
        } else {
            return null;
        }
    }

    @Override
    public Object getValueAt(int i, int i1) {
        StudentTeam team = this.getStudentTeamAt(i);

        switch (i1) {
            case 0:
                return team.getId();
            case 1:
                return (team.getRepositoryName() == null) ? "None" : team.getRepositoryName();
            case 2:
                return (team.getTaTeam() == null) ? "None" : team.getTaTeam().getDetailName();
            default:
                return this.getMemberInColumn(team, i1);
        }
    }

    /**
     * Gets a particular student name in a column of the table. This is used for the staticColumns which show all members in
     * the team.
     * @param team The team for which to search for a student in.
     * @param column The table column.
     * @return The student detail name in a particular column, or null if none exists.
     */
    private String getMemberInColumn(StudentTeam team, int column) {
        Student[] students = team.getStudents();
        int index = column - this.staticColumns.length; // Subtract the number of static staticColumns.
        if (index >= 0 && index < students.length) {
            return students[index].getDetailName();
        } else {
            return null;
        }
    }

    /**
     * Gets the highest member count in the list of student teams.
     * @return The maximum member count of all teams.
     */
    private int getMaxMemberCount() {
        int max = 0;
        for (StudentTeam team : this.studentTeamsList) {
            if (team.memberCount() > max) {
                max = team.memberCount();
            }
        }
        return max;
    }

    /**
     * Generates column names, including some procedurally generated headers based on the number of members in the team.
     * @param maxMembers The highest number of members a team has.
     */
    private void generateColumnNames(int maxMembers) {
        this.columns = new String[this.staticColumns.length + maxMembers];
        this.columns[0] = this.staticColumns[0];
        this.columns[1] = this.staticColumns[1];
        this.columns[2] = this.staticColumns[2];
        for (int i = 0; i < maxMembers; i++) {
            this.columns[i + this.staticColumns.length] = "Member " + (i + 1);
        }
    }
}
