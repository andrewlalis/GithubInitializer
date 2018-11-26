package nl.andrewlalis;

import nl.andrewlalis.command.CommandExecutor;
import nl.andrewlalis.command.executables.*;
import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.DbHelper;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.ui.view.ManagementView;
import nl.andrewlalis.ui.view.StartView;
import nl.andrewlalis.util.CommandLine;
import nl.andrewlalis.util.Logging;
import nl.andrewlalis.util.TeamGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Main program entry point.
 */
public class Main {

    private static final Logger logger = Logger.getGlobal();

    /**
     * The main application's view, which should be able to be referenced in many places.
     */
    private static ManagementView managementView;

    public static void main(String[] args) {

        // Parsed command line arguments.
        Map<String, String> userOptions = CommandLine.parseArgs(args);

        // Initialize logger.
        Logging.setup();

        //startOldVersion(userOptions);

        logger.info("GithubManager for Github Repositories in Educational Organizations.\n" +
                "© Andrew Lalis (2018), All rights reserved.\n" +
                "Program initialized.");

        GithubManager manager = new GithubManager();
        managementView = new ManagementView(manager);

        initializeTestingData();
        StartView startView = new StartView(manager, "InitializerTesting", userOptions.get("token"));
        manager.setAccessToken(startView.getAccessToken());
        manager.setOrganizationName(startView.getOrganizationName());
        manager.deleteAllRepositories("AOOP");
    }

    /**
     * @return The management view used for the application.
     */
    public static ManagementView getManagementView() {
        return managementView;
    }

    private static void initializeTestingData() {
        try {
            List<StudentTeam> teams = TeamGenerator.generateFromCSV("/home/andrew/Documents/School/ta/GithubInitializer/student-groups.csv", 2);
            DbHelper.saveStudentTeams(teams);
            managementView.updateModels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Legacy code to run the old version of the application.
     * @param userOptions The options the user has entered in the command line.
     */
    public static void startOldVersion(Map<String, String> userOptions) {
        // Command executor which will be used by all actions the user can do.
        CommandExecutor executor = new CommandExecutor();

        // Initialize User Interface.
        InitializerApp app = new InitializerApp(executor);
        app.begin();
        app.setAccessToken(userOptions.get("token"));

        // Initialize executable commands.
        executor.registerCommand("read_students", new ReadStudentsFile(app));
        executor.registerCommand("archive_all", new ArchiveRepos());
        executor.registerCommand("generate_assignments", new GenerateAssignmentsRepo());
        executor.registerCommand("define_ta_teams", new DefineTaTeams(app));
        executor.registerCommand("list_errors", new ListErrors(app));
        executor.registerCommand("delete_repos", new DeleteRepos());
        executor.registerCommand("delegate_student_teams", new DelegateStudentTeams(app));
        executor.registerCommand("setup_student_repos", new SetupStudentRepos(app));
        executor.registerCommand("list_repos", new ListRepos());
    }

}
