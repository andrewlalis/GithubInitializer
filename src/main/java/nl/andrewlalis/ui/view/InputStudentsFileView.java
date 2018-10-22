package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.control.listeners.input_students_file_view.FileSelectListener;

import javax.swing.*;
import java.awt.*;

/**
 * In this view, the user will select a file to read a list of students from, and generates the list of teams from that.
 */
public class InputStudentsFileView extends AbstractView {

    private JTextField studentsPerTeamField;

    InputStudentsFileView(GithubManager manager) {
        super("Input Students CSV",
                false,
                DISPOSE_ON_CLOSE,
                null,
                manager);
    }

    public int getStudentsPerTeam() {
        return Integer.parseUnsignedInt(this.studentsPerTeamField.getText());
    }

    @Override
    protected JPanel buildContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());

        JLabel helpLabel = new JLabel("Please select the CSV file containing student sign-up responses.");
        contentPane.add(helpLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));

        JButton selectFileButton = new JButton("Select File");
        this.studentsPerTeamField = new JTextField("2");
        inputPanel.add(this.generateTextFieldPanel("How many students per team?", this.studentsPerTeamField));
        selectFileButton.addActionListener(new FileSelectListener(this));
        inputPanel.add(selectFileButton);

        contentPane.add(inputPanel, BorderLayout.CENTER);

        JButton doneButton = new JButton("Done");
        contentPane.add(doneButton, BorderLayout.SOUTH);

        return contentPane;
    }
}
