package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Listens for when the user performs an action to read all students from a file, and output the contents to a database.
 *
 * Because filename and team size are not provided via arguments when a user clicks on a button, these are obtained via
 * a JFileChooser and JOptionPane input dialog. If all inputs are valid, then the command is executed.
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
                this.executor.executeCommand("read_students", new String[]{
                        chooser.getSelectedFile().getAbsolutePath(),
                        teamSizeString
                });
            }
        }
    }
}
