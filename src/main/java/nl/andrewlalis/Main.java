package nl.andrewlalis;

import nl.andrewlalis.model.Organization;
import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.control.command.executables.ArchiveRepos;
import nl.andrewlalis.ui.control.command.executables.DefineTaTeams;
import nl.andrewlalis.ui.control.command.executables.GenerateAssignmentsRepo;
import nl.andrewlalis.ui.control.command.executables.ReadStudentsFile;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.util.CommandLine;
import nl.andrewlalis.util.Logging;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Main program entry point.
 */
public class Main {

    private static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) {

        // Parsed command line arguments.
        Map<String, String> userOptions = CommandLine.parseArgs(args);

        // Initialize logger.
        Logging.setup();

        // Command executor which will be used by all actions the user can do.
        CommandExecutor executor = new CommandExecutor();

        // Main application model.
        Organization organization = new Organization();

        // Initialize User Interface.
        InitializerApp app = new InitializerApp(executor, organization);
        app.begin();
        app.setAccessToken(userOptions.get("token"));

        // Initialize executable commands.
        executor.registerCommand("read_students", new ReadStudentsFile(organization));
        executor.registerCommand("archive_all", new ArchiveRepos());
        executor.registerCommand("generate_assignments", new GenerateAssignmentsRepo());
        executor.registerCommand("define_ta_teams", new DefineTaTeams(app));

        logger.info("GithubManager for Github Repositories in Educational Organizations. Program initialized.");
    }

}
