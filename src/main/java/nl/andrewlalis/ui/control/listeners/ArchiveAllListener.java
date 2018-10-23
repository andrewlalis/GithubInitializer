package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user performs an action with the intent to archive all repositories.
 */
public class ArchiveAllListener extends ExecutableListener {

    public ArchiveAllListener(CommandExecutor executor, InitializerApp app) {
        super(executor, app);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String response = JOptionPane.showInputDialog(this.app, "Enter a substring to archive repositories by.", "Enter a substring", JOptionPane.QUESTION_MESSAGE);
        if (response != null) {
            this.executor.executeCommand("archive_all", new String[]{
                    this.app.getOrganizationName(),
                    this.app.getAccessToken(),
                    response
            });
        }
    }
}
