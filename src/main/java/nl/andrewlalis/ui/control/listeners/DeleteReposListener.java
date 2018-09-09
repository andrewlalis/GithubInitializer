package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * A listener for when the user wants to delete repositories.
 */
public class DeleteReposListener extends ExecutableListener {

    public DeleteReposListener(CommandExecutor executor, InitializerApp app) {
        super(executor, app);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String input = JOptionPane.showInputDialog(this.app, "Please enter a string which, if a repository contains it, results in deleting the repository.", "Enter Substing", JOptionPane.QUESTION_MESSAGE);
        if (input != null) {
            int decision = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all repositories that contain \"" + "\" in their name?", "Delete Repositories", JOptionPane.YES_NO_OPTION);
            if (decision == JOptionPane.YES_OPTION) {
                this.executor.executeCommand("delete_repos", new String[]{
                        app.getOrganizationName(),
                        app.getAccessToken(),
                        input
                });
            }
        }
    }
}
