package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Listens for when the user performs an action to read all students from a file, and output the contents to a database.
 */
public class ReadStudentsFileListener extends ExecutableListener {

    public ReadStudentsFileListener(CommandExecutor executor, InitializerApp app) {
        super(executor, app);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
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
        int fileResponse = chooser.showOpenDialog(this.app);

        if (fileResponse == JFileChooser.APPROVE_OPTION) {
            String teamSizeString = JOptionPane.showInputDialog(this.app, "Enter the student team size.", "Team Size", JOptionPane.QUESTION_MESSAGE);
            if (teamSizeString != null) {
                this.executor.executeCommand("readstudents", new String[]{
                        chooser.getSelectedFile().getName(),
                        teamSizeString
                });
            }
        }
    }
}
