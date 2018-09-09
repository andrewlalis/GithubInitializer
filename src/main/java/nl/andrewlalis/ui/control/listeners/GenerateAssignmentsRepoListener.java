package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Listens for when the user does an action to generate the assignments repository.
 */
public class GenerateAssignmentsRepoListener extends ExecutableListener {

    public GenerateAssignmentsRepoListener(CommandExecutor executor, InitializerApp app) {
        super(executor, app);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String repoName = JOptionPane.showInputDialog(this.app, "Enter a name for the assignments repository.", "Repository Name", JOptionPane.QUESTION_MESSAGE);
        if (repoName != null) {
            String description = JOptionPane.showInputDialog(this.app, "Enter a description for the repository.", "Repository Description", JOptionPane.QUESTION_MESSAGE);
            String teamName = JOptionPane.showInputDialog(this.app, "Enter the name of the TA team containing all teaching assistants.", "TA Team Name", JOptionPane.QUESTION_MESSAGE);
            if (teamName != null) {
                this.executor.executeCommand("generate_assignments", new String[]{
                        this.app.getOrganizationName(),
                        this.app.getAccessToken(),
                        repoName,
                        description,
                        teamName
                });
            }
        }
    }

}
