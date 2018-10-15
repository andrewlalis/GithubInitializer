package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;

import javax.swing.*;
import java.awt.*;

/**
 * In this view, the user will select a file to read a list of students from, and generates the list of teams from that.
 */
public class InputStudentsFileView extends AbstractView {

    InputStudentsFileView(GithubManager manager) {
        super("Input Students CSV",
                false,
                DISPOSE_ON_CLOSE,
                null,
                manager);
    }

    @Override
    protected JPanel buildContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());

        JButton selectFileButton = new JButton("Select File");
        contentPane.add(selectFileButton, BorderLayout.CENTER);

        JButton doneButton = new JButton("Done");
        contentPane.add(doneButton, BorderLayout.SOUTH);

        return contentPane;
    }
}
