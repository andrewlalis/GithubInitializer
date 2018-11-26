package nl.andrewlalis.ui.view.components.tables;

import nl.andrewlalis.ui.control.listeners.management_view.EntityTablePopupMenuListener;
import nl.andrewlalis.ui.control.listeners.management_view.student_actions.NewStudentListener;
import nl.andrewlalis.ui.control.listeners.management_view.student_actions.RemoveFromCourseListener;
import nl.andrewlalis.ui.control.listeners.management_view.student_actions.SetTeamListener;
import nl.andrewlalis.ui.view.components.DetailPanel;
import nl.andrewlalis.ui.view.components.tables.popup_menu.EntityMenuItem;
import nl.andrewlalis.ui.view.components.tables.popup_menu.EntitySelectionType;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;
import nl.andrewlalis.ui.view.table_models.StudentTeamTableModel;

import javax.swing.*;

/**
 * This table displays a list of students, and offers the user the ability to interact with entities in the list.
 */
public class StudentsTable extends EntityTable {

    /**
     * A reference to the model for student teams.
     */
    private StudentTeamTableModel studentTeamModel;

    public StudentsTable(StudentTableModel studentTableModel, StudentTeamTableModel studentTeamModel, DetailPanel detailPanel) {
        super(studentTableModel, detailPanel);
        this.studentTeamModel = studentTeamModel;
        this.setupTable();
    }

    @Override
    protected JPopupMenu getPopupMenu() {
        // A context menu for the table.
        JPopupMenu menu = new JPopupMenu("Menu");

        // Item for creating a new student (for example, if someone registered for the course late).
        JMenuItem newStudentItem = new EntityMenuItem("New Student", EntitySelectionType.SINGLE | EntitySelectionType.MULTIPLE);
        newStudentItem.addActionListener(new NewStudentListener(this));

        // Item for setting a student's team.
        JMenuItem setTeamItem = new EntityMenuItem("Set team", EntitySelectionType.SINGLE);
        setTeamItem.addActionListener(new SetTeamListener(this, this.studentTeamModel));
        menu.add(setTeamItem);

        // Item for removing a student from the course.
        JMenuItem removeItem = new EntityMenuItem("Remove from course", EntitySelectionType.SINGLE | EntitySelectionType.MULTIPLE);
        removeItem.addActionListener(new RemoveFromCourseListener(this, this.studentTeamModel));
        menu.add(removeItem);

        menu.addPopupMenuListener(new EntityTablePopupMenuListener(this));

        return menu;
    }
}
