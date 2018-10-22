package nl.andrewlalis.ui.control.listeners.input_students_file_view;

import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.ui.view.InputStudentsFileView;
import nl.andrewlalis.util.TeamGenerator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Listens for when the user selects a CSV file to use to populate the teams list.
 */
public class FileSelectListener implements ActionListener {

    private InputStudentsFileView fileView;

    public FileSelectListener(InputStudentsFileView parent) {
        this.fileView = parent;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        // First check if the user has entered a valid team size.
        if (this.fileView.getStudentsPerTeam() < 1) {
            JOptionPane.showMessageDialog(this.fileView, "Invalid or missing team size.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // It is assumed that the team size is valid, so the user can choose a file.
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.addChoosableFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                return file.getName().toLowerCase().endsWith(".csv");
            }

            @Override
            public String getDescription() {
                return "CSV Files (*.csv)";
            }
        });
        int fileResponse = chooser.showOpenDialog(this.fileView);

        if (fileResponse == JFileChooser.APPROVE_OPTION) {
            int teamSize = this.fileView.getStudentsPerTeam();
            try {
                List<StudentTeam> teams = TeamGenerator.generateFromCSV(chooser.getSelectedFile().getAbsolutePath(), teamSize);
                System.out.println(teams);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
