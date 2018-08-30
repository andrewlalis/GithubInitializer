package nl.andrewlalis.model;

import org.kohsuke.github.GHTeam;

import java.util.ArrayList;
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
     * Constructs a team without any teaching assistant members.
     * @param name The name of the team.
     * @param id The unique identifier for this team.
     */
    public TATeam(String name, int id) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public GHTeam getGithubTeam() {
        return this.githubTeam;
    }

    public void setGithubTeam(GHTeam team) {
        this.githubTeam = team;
    }
}
