package nl.andrewlalis.ui.view.components.tables;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.control.listeners.management_view.student_team_actions.ArchiveRepositoryListener;
import nl.andrewlalis.ui.control.listeners.management_view.student_team_actions.DeleteRepositoryListener;
import nl.andrewlalis.ui.control.listeners.management_view.student_team_actions.GenerateRepositoryListener;
import nl.andrewlalis.ui.control.listeners.management_view.student_team_actions.RemoveTeamListener;
import nl.andrewlalis.ui.view.components.DetailPanel;
import nl.andrewlalis.ui.view.components.tables.popup_menu.EntityMenuItem;
import nl.andrewlalis.ui.view.components.tables.popup_menu.EntitySelectionType;
import nl.andrewlalis.ui.view.table_models.AbstractEntityModel;

import javax.swing.*;

public class StudentTeamsTable extends EntityTable {

    /**
     * A reference to the github manager, for cases where it's needed to interact with github repositories.
     */
    private GithubManager manager;

    /**
     * Constructs a new table with the given model and a reference to a detail panel to show extra details about the entity.
     *
     * @param model The table model to apply to this table.
     * @param detailPanel The panel on which to display details of the selected entity.
     * @param manager The GithubManager object used to interact with Github.
     */
    public StudentTeamsTable(AbstractEntityModel model, DetailPanel detailPanel, GithubManager manager) {
        super(model, detailPanel);
        this.manager = manager;
        this.setupTable();
    }

    @Override
    protected JPopupMenu getPopupMenu() {

        JPopupMenu menu = new JPopupMenu("menu");

        // Item for generating the repository of a given team.
        JMenuItem generateRepositoryItem = new EntityMenuItem("Generate Repository", EntitySelectionType.SINGLE | EntitySelectionType.MULTIPLE);
        generateRepositoryItem.addActionListener(new GenerateRepositoryListener(this, this.manager));
        menu.add(generateRepositoryItem);

        // Item for assigning a TA team to a student team.
        JMenuItem assignTATeamItem = new EntityMenuItem("Assign to TA Team", EntitySelectionType.SINGLE | EntitySelectionType.MULTIPLE);
        assignTATeamItem.addActionListener(actionEvent -> {
            System.out.println("Assign to TA team");
        });
        menu.add(assignTATeamItem);

        // Item for archiving the repository of a student team.
        JMenuItem archiveRepositoryItem = new EntityMenuItem("Archive Repository", EntitySelectionType.SINGLE | EntitySelectionType.MULTIPLE);
        archiveRepositoryItem.addActionListener(new ArchiveRepositoryListener(this, this.manager));
        menu.add(archiveRepositoryItem);

        // Item for deleting a repository. TODO: Remove this for production version! Only archiving is allowed.
        JMenuItem deleteRepositoryItem = new EntityMenuItem("Delete Repository", EntitySelectionType.SINGLE | EntitySelectionType.MULTIPLE);
        deleteRepositoryItem.addActionListener(new DeleteRepositoryListener(this, this.manager));
        menu.add(deleteRepositoryItem);

        // Item for removing a team.
        JMenuItem removeTeamItem = new EntityMenuItem("Remove Team", EntitySelectionType.SINGLE | EntitySelectionType.MULTIPLE);
        removeTeamItem.addActionListener(new RemoveTeamListener(this, this.manager));
        menu.add(removeTeamItem);

        return menu;
    }
}
