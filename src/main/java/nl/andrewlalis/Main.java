package nl.andrewlalis;

import nl.andrewlalis.model.Team;
import nl.andrewlalis.util.Logging;
import nl.andrewlalis.util.TeamGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import nl.andrewlalis.util.CommandLine;

/**
 * Main program entry point.
 */
public class Main {

    private static final Logger logger = Logger.getGlobal();

    public static void main(String[] args) throws IOException {

        // Parsed command line arguments.
        Map<String, String> userOptions = CommandLine.parseArgs(args);

        // Initialize logger.
        try {
            Logging.setup(true); // TODO: Replace true with command line arg.
        } catch (IOException e) {
            logger.severe("Unable to save log to file.");
        }

        logger.info("Initializer for Github Repositories in Educational Organizations.");

        List<Team> teams = TeamGenerator.generateFromCSV(
                userOptions.get("input"),
                Integer.parseInt(userOptions.get("teamsize"))
        );
        logger.info("Teams created: " + teams);

    }
}
