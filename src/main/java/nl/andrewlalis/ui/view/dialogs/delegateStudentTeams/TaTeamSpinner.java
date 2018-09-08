package nl.andrewlalis.ui.view.dialogs.delegateStudentTeams;

import nl.andrewlalis.model.TATeam;

import javax.swing.*;

/**
 * A modified JSpinner which keeps track of a TATeam, and also has a default spinner number model in use.
 */
public class TaTeamSpinner extends JSpinner {

    private TATeam team;

    public TaTeamSpinner(TATeam team, int max) {
        super(new SpinnerNumberModel(0, 0, max, 1));
        ((DefaultEditor) this.getEditor()).getTextField().setEditable(false);
        this.team = team;
    }

    public TATeam getTeam() {
        return this.team;
    }
}
