package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.database.Database;
import nl.andrewlalis.ui.control.command.Executable;
import nl.andrewlalis.util.FileUtils;

import java.util.List;

/**
 * Execute this class to read students from a supplied filename and teamsize, and store their
 * information in the database.
 */
public class ReadStudentsFileToDB implements Executable {

    /**
     * The database used to store the students.
     */
    private Database db;

    public ReadStudentsFileToDB(Database db) {
        this.db = db;
    }


    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            return false;
        }
        String filename = args[0];
        int teamSize = Integer.parseUnsignedInt(args[1]);
        List<StudentTeam> teams = FileUtils.getStudentTeamsFromCSV(filename, teamSize);
        return this.db.storeStudentTeams(teams);
    }
}
