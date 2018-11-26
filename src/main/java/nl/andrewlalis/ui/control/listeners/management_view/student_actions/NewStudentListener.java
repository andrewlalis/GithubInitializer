package nl.andrewlalis.ui.control.listeners.management_view.student_actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewStudentListener extends StudentListener {

    /**
     * Constructs a table row listener.
     *
     * @param table The table on which to get selected rows.
     */
    public NewStudentListener(JTable table) {
        super(table);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("New Student");
    }
}
