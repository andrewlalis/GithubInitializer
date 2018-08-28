package nl.andrewlalis;

import nl.andrewlalis.model.database.Database;
import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.control.command.Executable;
import nl.andrewlalis.ui.control.command.executables.ArchiveRepos;
import nl.andrewlalis.ui.control.command.executables.GenerateAssignmentsRepo;
import nl.andrewlalis.ui.control.command.executables.ReadStudentsFileToDB;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.util.CommandLine;
import nl.andrewlalis.util.Logging;
import nl.andrewlalis.util.TeamGenerator;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
        try {
            Logging.setup(true); // TODO: Replace true with command line arg.

        } catch (IOException e) {
            logger.severe("Unable to save log to file.");
        }

        // Command executor which will be used by all actions the user can do.
        CommandExecutor executor = new CommandExecutor();

        // Initialize User Interface.
        InitializerApp app = new InitializerApp(executor);
        app.begin();

        Database db = new Database("database/initializer.sqlite");
        db.initialize();

        executor.registerCommand("readstudents", new ReadStudentsFileToDB(db));
        executor.registerCommand("archiveall", new ArchiveRepos());
        executor.registerCommand("generateassignments", new GenerateAssignmentsRepo());

        logger.info("GithubManager for Github Repositories in Educational Organizations. Program initialized.");



        // Get studentTeams from CSV file.
//        List<StudentTeam> studentTeams = getStudentTeamsFromCSV(userOptions.get("input"), Integer.parseInt(userOptions.get("teamsize")));
//
//        GithubManager githubManager = new GithubManager(
//                userOptions.get("organization"),
//                userOptions.get("token"),
//                "assignments_2018",
//                "teaching-assistants",
//                "advoop_2018"
//        );

        try {
            //githubManager.initializeGithubRepos(studentTeams);
            //githubManager.archiveAllRepositories("team");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}