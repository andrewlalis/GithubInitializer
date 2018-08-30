package nl.andrewlalis.ui.view.list_models;

import nl.andrewlalis.model.TeachingAssistant;

import javax.swing.*;
import java.awt.*;

public class TeachingAssistantListCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> jList, Object o, int i, boolean b, boolean b1) {
        super.getListCellRendererComponent(jList, o, i, b, b1);
        if (o instanceof TeachingAssistant) {
            TeachingAssistant ta = (TeachingAssistant) o;
            this.setText(ta.getGithubUsername());
            this.setToolTipText(ta.getName());
        }
        return this;
    }
}
