package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.ui.control.command.Executable;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.util.FileUtils;

import java.util.List;

/**
 * Execute this class to read students from a supplied filename and teamsize, and store their
 * information in the application's organization model.
 * Requires the following arguments:
 *
 * 1. filename
 * 2. teamsize
 */
public class ReadStudentsFile implements Executable {

    @Override
    public boolean execute(String[] args) {
        if (args.length < 2) {
            return false;
        }
        String filename = args[0];
        int teamSize = Integer.parseUnsignedInt(args[1]);
        List<StudentTeam> teams = FileUtils.getStudentTeamsFromCSV(filename, teamSize);
        if (teams == null) {
            return false;
        }
        InitializerApp.organization.setStudentTeams(teams);
        return true;
    }
}
