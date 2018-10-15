package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import java.awt.event.ActionEvent;

/**
 * Listens for when a user performs an action to open the dialog to delegate student teams amongst the TA teams.
 */
public class DelegateStudentTeamsListener extends ExecutableListener {

    public DelegateStudentTeamsListener(CommandExecutor executor, InitializerApp app) {
        super(executor, app);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.executor.executeCommand("delegate_student_teams", new String[]{
                app.getOrganizationName(),
                app.getAccessToken()
        });
    }
}
