package nl.andrewlalis.model;

import org.kohsuke.github.GHTeam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a teaching assistant team, which is itself a 'team' in the organization. This class is used for parsing
 * json from requests to github to get a list of all teams in the organization.
 */
public class TATeam extends Team {

    /**
     * The team's display name.
     */
    private String name;

    /**
     * The Github team associated with this team.
     */
    private GHTeam githubTeam;

    /**
     * A list of all student teams for which this TA team is responsible.
     */
    private List<StudentTeam> studentTeams;

    /**
     * Constructs a team without any teaching assistant members.
     * @param name The name of the team.
     * @param id The unique identifier for this team.
     */
    public TATeam(String name, int id) {
        super(id);
        this.name = name;
    }

    /**
     * Gets a list of teaching assistants, as a convenience method to avoid having to do an array cast.
     * @return An array of all teaching assistant members of this team.
     */
    public TeachingAssistant[] getTeachingAssistants() {
        return Arrays.copyOf(this.getMembers(), this.memberCount(), TeachingAssistant[].class);
    }

    /**
     * Adds the given student team to the list of teams that this TA team is responsible for.
     * @param team A student team.
     */
    public void addStudentTeam(StudentTeam team) {
        this.studentTeams.add(team);
    }

    // GETTERS
    public String getName() {
        return this.name;
    }

    public GHTeam getGithubTeam() {
        return this.githubTeam;
    }

    public List<StudentTeam> getStudentTeams() {
        return this.studentTeams;
    }

    // SETTERS
    public void setGithubTeam(GHTeam team) {
        this.githubTeam = team;
    }
}
