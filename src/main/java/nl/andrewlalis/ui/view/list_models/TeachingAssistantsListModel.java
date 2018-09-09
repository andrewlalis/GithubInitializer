package nl.andrewlalis.ui.view.list_models;

import nl.andrewlalis.model.TeachingAssistant;

import javax.swing.*;
import java.util.List;

/**
 * A list model for displaying a list of teaching assistants as members of an organization.
 */
public class TeachingAssistantsListModel extends AbstractListModel {

    /**
     * The list of teaching assistants which this model uses.
     */
    private List<TeachingAssistant> teachingAssistants;

    public TeachingAssistantsListModel(List<TeachingAssistant> teachingAssistants) {
        this.teachingAssistants = teachingAssistants;
    }

    @Override
    public int getSize() {
        return this.teachingAssistants.size();
    }

    @Override
    public Object getElementAt(int i) {
        return this.teachingAssistants.get(i);
    }
}
