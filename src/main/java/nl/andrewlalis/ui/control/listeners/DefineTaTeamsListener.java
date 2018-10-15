package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import java.awt.event.ActionEvent;

/**
 * Listens for when the user wants to open the 'DefineTaTeams' dialog.
 */
public class DefineTaTeamsListener extends ExecutableListener {

    public DefineTaTeamsListener(CommandExecutor executor, InitializerApp app) {
        super(executor, app);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        this.executor.executeCommand("define_ta_teams", new String[]{
                this.app.getOrganizationName(),
                this.app.getAccessToken()
        });
    }
}
