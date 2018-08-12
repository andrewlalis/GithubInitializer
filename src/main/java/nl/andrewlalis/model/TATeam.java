package nl.andrewlalis.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a teaching assistant team, which is itself a 'team' in the organization. This class is used for parsing
 * json from requests to github to get a list of all teams in the organization.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TATeam {

    private List<TeachingAssistant> teachingAssistants;

    /**
     * The team's display name.
     */
    @JsonProperty("name")
    private String name;

    /**
     * The team's unique identifier.
     */
    @JsonProperty("id")
    private int id;

    /**
     * Constructs a team without any teaching assistant members.
     * @param name The name of the team.
     * @param id The unique identifier for this team.
     */
    @JsonCreator
    public TATeam(@JsonProperty("name") String name, @JsonProperty("id") int id) {
        this.name = name;
        this.id = id;
        this.teachingAssistants = new ArrayList<TeachingAssistant>();
    }

    /**
     * Constructs a team with a list of teaching assistants that are part of it.
     * @param teachingAssistants The list of teaching assistants that are part of the team.
     */
    public TATeam(List<TeachingAssistant> teachingAssistants, String name, int id) {
        this.teachingAssistants = teachingAssistants;
        this.name = name;
        this.id = id;
    }

    /**
     * Gets the unique identification for this TA team.
     * @return An integer representing the id of this team.
     */
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

}
