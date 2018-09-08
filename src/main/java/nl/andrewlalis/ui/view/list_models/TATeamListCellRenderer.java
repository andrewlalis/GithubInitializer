package nl.andrewlalis.ui.view.list_models;

import nl.andrewlalis.model.TATeam;
import nl.andrewlalis.model.TeachingAssistant;

import javax.swing.*;
import java.awt.*;

/**
 * Determines how list cells are rendered for a list of TATeams. In this case, it shows the team name, and the tooltip
 * text provides a list of members.
 */
public class TATeamListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> jList, Object o, int i, boolean b, boolean b1) {
        super.getListCellRendererComponent(jList, o, i, b, b1);
        if (o instanceof TATeam) {
            TATeam team = (TATeam) o;
            this.setText(team.getName());
            StringBuilder sb = new StringBuilder();
            TeachingAssistant[] teachingAssistants = team.getTeachingAssistants();
            for (int j = 0; j < teachingAssistants.length; j++) {
                sb.append(teachingAssistants[j].getGithubUsername());
                if (j < teachingAssistants.length - 1) {
                    sb.append('\n');
                }
            }
            this.setToolTipText(sb.toString());
        }
        return this;
    }
}
