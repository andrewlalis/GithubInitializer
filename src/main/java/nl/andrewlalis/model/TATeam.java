package nl.andrewlalis.model;

import java.util.List;

public class TATeam {

    private List<TeachingAssistant> teachingAssistants;

    public TATeam(List<TeachingAssistant> teachingAssistants) {
        this.teachingAssistants = teachingAssistants;
    }

    /**
     * Gets the unique identification for this TA team.
     * @return A string representing the id of this team.
     */
    public String getId() {
        return null;
    }

}
