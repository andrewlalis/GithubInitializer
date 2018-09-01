package nl.andrewlalis.ui.view.dialogs;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.ui.view.InitializerApp;
import nl.andrewlalis.ui.view.list_models.TATeamListCellRenderer;
import nl.andrewlalis.ui.view.list_models.TATeamListModel;
import nl.andrewlalis.ui.view.list_models.TeachingAssistantListCellRenderer;
import nl.andrewlalis.ui.view.list_models.TeachingAssistantsListModel;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a pop-up dialog that appears when the user wants to manipulate teams in the organization. With
 * this dialog, it will be possible to do the following:
 *  - View all members of the organization.
 *  - View all teams in the organization.
 *  - Create new teams from a selection of members.
 *  - Invite new members to the organization.
 */
public class DefineTaTeamsDialog extends JDialog {

    private static final Dimension LIST_SIZE = new Dimension(200, -1);

    /**
     * The manager used to manipulate the organization.
     */
    private GithubManager manager;

    public DefineTaTeamsDialog(InitializerApp parentApp, GithubManager manager) {
        super(parentApp, "Define TA Teams", true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.manager = manager;

        this.initUI();
    }

    /**
     * Sets the dialog as visible.
     */
    public void begin() {
        this.setVisible(true);
    }

    /**
     * Constructs all UI components.
     */
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JLabel("Hello world", SwingConstants.CENTER), BorderLayout.NORTH);

        mainPanel.add(this.initMembersPanel(), BorderLayout.WEST);
        mainPanel.add(this.initTeamsPanel(), BorderLayout.EAST);

        this.setContentPane(mainPanel);
        this.setPreferredSize(new Dimension(600, 800));
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * @return A JPanel containing the list of teams in the organization.
     */
    private JPanel initTeamsPanel() {
        JPanel teamsPanel = new JPanel(new BorderLayout());
        teamsPanel.add(new JLabel("Teams", SwingConstants.CENTER), BorderLayout.NORTH);

        ListModel model = new TATeamListModel(this.manager.getTeams());
        JList teamsList = new JList(model);
        teamsList.setCellRenderer(new TATeamListCellRenderer());
        teamsList.setPreferredSize(LIST_SIZE);
        teamsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane teamsListScrollPane = new JScrollPane(teamsList);
        teamsListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        teamsListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        teamsPanel.add(teamsListScrollPane, BorderLayout.CENTER);

        return teamsPanel;
    }

    /**
     * @return A JPanel containing the list of members of the organization.
     */
    private JPanel initMembersPanel() {
        JPanel membersPanel = new JPanel(new BorderLayout());
        membersPanel.add(new JLabel("Members", SwingConstants.CENTER), BorderLayout.NORTH);

        ListModel model = new TeachingAssistantsListModel(this.manager.getMembers());
        JList membersList = new JList();
        membersList.setModel(model);
        membersList.setCellRenderer(new TeachingAssistantListCellRenderer());
        membersList.setPreferredSize(LIST_SIZE);
        membersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane membersListScrollPane = new JScrollPane(membersList);
        membersListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        membersListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        membersPanel.add(membersListScrollPane, BorderLayout.CENTER);

        return membersPanel;
    }
}
