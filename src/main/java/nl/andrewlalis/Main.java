package nl.andrewlalis;

import nl.andrewlalis.model.Team;
import nl.andrewlalis.util.TeamGenerator;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main program entry point.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Initializer for Github Repositories in Educational Organizations.");

        Map<String, String> userOptions = parseArgs(args);

        List<Team> teams = TeamGenerator.generateFromCSV(
                userOptions.get("input"),
                Integer.parseInt(userOptions.get("teamsize"))
        );
        System.out.println(teams);


    }

    /**
     * Parses the command line arguments and gets all the needed values.
     * @param args The command line arguments as they are given to main.
     * @return A map of keys and values for each option that the user must give.
     */
    private static Map<String, String> parseArgs(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        Options options = setupCommandOptions();
        Map<String, String> userOptions = new HashMap<>();
        try {
            CommandLine cmd = parser.parse(options, args);
            userOptions.put("token", cmd.getOptionValue("token"));
            userOptions.put("input", cmd.getOptionValue("input"));
            userOptions.put("organization", cmd.getOptionValue("organization"));
            // The optional teamsize argument must be handled.
            String teamSizeInput = cmd.getOptionValue("teamsize");
            System.out.println(teamSizeInput);
            if (teamSizeInput == null ) {
                userOptions.put("teamsize", "2");
            } else {
                userOptions.put("teamsize", teamSizeInput);
            }
        } catch (ParseException e) {
            formatter.printHelp("java -jar GithubInitializer.jar", options);
            System.exit(1);
        }
        return userOptions;
    }

    /**
     * Sets up the command line interface options using Apache Commons CLI library.
     * @return The Options object used when parsing the arguments.
     */
    private static Options setupCommandOptions() {
        Options options = new Options();

        // Authentication token for github.
        Option tokenInput = new Option("t", "token", true, "The authentication token, with which you are authenticated on Github. See the Github OAuth information section for more information.");
        tokenInput.setRequired(true);
        options.addOption(tokenInput);

        // CSV file of responses to form.
        Option fileInput = new Option("i", "input", true, "The input file. Should be in CSV format with the following columns:\n" +
                "\t Two Student numbers, two Github usernames, two email addresses, and \n" +
                "\t whether the first student has a partner.");
        fileInput.setRequired(true);
        options.addOption(fileInput);

        // The github organization to add the repositories to.
        Option organizationInput = new Option("o", "organization", true, "The name of the organization for which this program is being run.");
        organizationInput.setRequired(true);
        options.addOption(organizationInput);

        // The maximum team size.
        Option teamSizeInput = new Option("s", "teamsize", true, "The maximum size of teams to generate.");
        teamSizeInput.setRequired(false);
        options.addOption(teamSizeInput);

        return options;
    }
}
