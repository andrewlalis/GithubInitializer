package nl.andrewlalis;

import nl.andrewlalis.model.Team;
import nl.andrewlalis.util.TeamGenerator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import nl.andrewlalis.util.CommandLine;

/**
 * Main program entry point.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Initializer for Github Repositories in Educational Organizations.");

        Map<String, String> userOptions = CommandLine.parseArgs(args);

        List<Team> teams = TeamGenerator.generateFromCSV(
                userOptions.get("input"),
                Integer.parseInt(userOptions.get("teamsize"))
        );
        System.out.println(teams);


    }
}
