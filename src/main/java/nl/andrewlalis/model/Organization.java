package nl.andrewlalis.model;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the overarching model container for the entire application, and holds in memory all student
 * teams created, teaching assistant teams, and any other state information that would be needed by the user interface
 * or runtime executables.
 */
public class Organization {

    /**
     * A list of all student teams in this organization. This is generated from a CSV file supplied after many students
     * have filled in a Google form.
     */
    private List<StudentTeam> studentTeams;

    /**
     * Constructs a new Organization object with all instance variables initialized to empty values.
     */
    public Organization() {
        this.studentTeams = new ArrayList<>();
    }

    /**
     * Determines if there are student teams available.
     * @return True if there is at least one student team, or false otherwise.
     */
    public boolean hasStudentTeams() {
        return this.studentTeams.isEmpty();
    }

    // GETTERS
    public List<StudentTeam> getStudentTeams() {
        return this.studentTeams;
    }

    // SETTERS
    public void setStudentTeams(List<StudentTeam> teams) {
        this.studentTeams = teams;
    }

}
