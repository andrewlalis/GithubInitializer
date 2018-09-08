package nl.andrewlalis.model;

import nl.andrewlalis.model.error.Error;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * This class represents the overarching model container for the entire application, and holds in memory all student
 * teams created, teaching assistant teams, and any other state information that would be needed by the user interface
 * or runtime executables.
 */
public class Organization extends Observable {

    /**
     * A list of all student teams in this organization. This is generated from a CSV file supplied after many students
     * have filled in a Google form.
     */
    private List<StudentTeam> studentTeams;

    /**
     * A list of all teaching assistant teams in this organization. These are generated from requests to the Github API
     * and possibly supplementary information. Each teaching assistant team maintains a list of all student teams for
     * which it is responsible.
     */
    private List<TATeam> taTeams;

    /**
     * A queue of errors that accumulates as the program runs. These will be output to the user after execution of
     * critical sections, so that inevitable errors due to input imperfections are not overlooked.
     */
    private List<Error> errors;

    /**
     * Constructs a new Organization object with all instance variables initialized to empty values.
     */
    public Organization() {
        this.studentTeams = new ArrayList<>();
        this.taTeams = new ArrayList<>();
        this.errors = new ArrayList<>();
    }

    /**
     * Determines if there are student teams available.
     * @return True if there is at least one student team, or false otherwise.
     */
    public boolean hasStudentTeams() {
        return this.studentTeams.isEmpty();
    }

    /**
     * Adds an error to the list of accumulated errors.
     * @param newError The newly generated error to add.
     */
    public void addError(Error newError) {
        this.errors.add(newError);
        this.setChanged();
        this.notifyObservers();
    }

    // GETTERS
    public List<StudentTeam> getStudentTeams() {
        return this.studentTeams;
    }

    public List<Error> getErrors() {
        return this.errors;
    }

    public List<TATeam> getTaTeams() {
        return this.taTeams;
    }

    // SETTERS
    public void setStudentTeams(List<StudentTeam> teams) {
        this.studentTeams = teams;
        this.setChanged();
        this.notifyObservers();
    }

    public void setTaTeams(List<TATeam> teams) {
        this.taTeams = teams;
        this.setChanged();
        this.notifyObservers();
    }

}
