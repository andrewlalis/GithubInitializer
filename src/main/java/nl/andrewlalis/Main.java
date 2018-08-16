package nl.andrewlalis;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.util.Logging;
import nl.andrewlalis.util.TeamGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.andrewlalis.util.CommandLine;

/**
 * Main program entry point.
 */
public class Main {

    private static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) {

        // Parsed command line arguments.
        Map<String, String> userOptions = CommandLine.parseArgs(args);

        // Initialize logger.
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        try {
            Logging.setup(true); // TODO: Replace true with command line arg.
            Handler[] handlers = logger.getHandlers();
            for (Handler h : handlers) {
                logger.removeHandler(h);
            }
            logger.setUseParentHandlers(false);
            logger.addHandler(handler);
        } catch (IOException e) {
            logger.severe("Unable to save log to file.");
        }

        logger.info("GithubManager for Github Repositories in Educational Organizations.");

        // Get studentTeams from CSV file.
        List<StudentTeam> studentTeams = getStudentTeamsFromCSV(userOptions.get("input"), Integer.parseInt(userOptions.get("teamsize")));

        GithubManager githubManager = new GithubManager(
                userOptions.get("organization"),
                userOptions.get("token"),
                "assignments_2018",
                "teaching-assistants",
                "advoop_2018"
        );

        try {
            githubManager.initializeGithubRepos(studentTeams);
            //githubManager.deleteAllRepositories();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Reads a list of students from a CSV file and compiles a list of teams based on their preferred partners.
     * @param filename The name of the CSV file.
     * @param teamSize The intended size of teams.
     * @return A list of student teams.
     */
    private static List<StudentTeam> getStudentTeamsFromCSV(String filename, int teamSize) {
        List<StudentTeam> studentTeams = null;
        try {
            studentTeams = TeamGenerator.generateFromCSV(filename, teamSize);
            logger.info("Teams created: " + studentTeams);
            return studentTeams;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            logger.severe("Unable to generate studentTeams from CSV file, exiting.");
            System.exit(1);
            return null;
        }
    }

}
