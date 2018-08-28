package nl.andrewlalis.util;

import nl.andrewlalis.model.StudentTeam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Logger;

/**
 * Contains some methods which come in handy in lots of other places.
 */
public class FileUtils {

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(FileUtils.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    /**
     * Reads the contents of the file specified by the filename into a String.
     * @param filename The filename to read the file of, either relative or absolute.
     * @return A string containing the file's contents.
     */
    public static String readStringFromFile(String filename) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(FileUtils.class.getResourceAsStream(filename)))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads a list of students from a CSV file and compiles a list of teams based on their preferred partners.
     * @param filename The name of the CSV file.
     * @param teamSize The intended size of teams.
     * @return A list of student teams.
     */
    public static List<StudentTeam> getStudentTeamsFromCSV(String filename, int teamSize) {
        List<StudentTeam> studentTeams;
        try {
            studentTeams = TeamGenerator.generateFromCSV(filename, teamSize);
            logger.fine("Teams created:\n" + studentTeams);
            return studentTeams;
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            logger.severe("Unable to generate studentTeams from CSV file, exiting. " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

}
