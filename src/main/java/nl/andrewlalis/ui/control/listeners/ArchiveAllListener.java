package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ArchiveAllListener extends ExecutableListener {

    InitializerApp app;

    public ArchiveAllListener(CommandExecutor executor, InitializerApp app) {
        super(executor);
        this.app = app;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String response = JOptionPane.showInputDialog(null, "Enter a substring to archive repositories by.", "Enter a substring", JOptionPane.QUESTION_MESSAGE);
        if (response != null) {
            this.executor.executeCommand("archiveall", new String[]{
                    this.app.getOrganizationName(),
                    this.app.getAccessToken(),
                    response
            });
        }
    }
}
