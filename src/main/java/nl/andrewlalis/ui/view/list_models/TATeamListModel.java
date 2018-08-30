package nl.andrewlalis.ui.view.list_models;

import nl.andrewlalis.model.TATeam;

import javax.swing.*;
import java.util.List;

/**
 * A list model for displaying TATeams.
 */
public class TATeamListModel extends AbstractListModel {

    /**
     * A list of teams.
     */
    private List<TATeam> teams;

    public TATeamListModel(List<TATeam> taTeams) {
        this.teams = taTeams;
    }

    @Override
    public int getSize() {
        return this.teams.size();
    }

    @Override
    public Object getElementAt(int i) {
        return this.teams.get(i);
    }
}
