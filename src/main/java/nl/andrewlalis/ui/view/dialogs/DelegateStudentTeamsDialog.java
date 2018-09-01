package nl.andrewlalis.ui.view.dialogs;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.TATeam;
import nl.andrewlalis.ui.view.InitializerApp;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This dialog shows a simple list of all teaching assistant teams, with a JSpinner next to the name, so that a user can
 * set how many student teams each teaching assistant team would like to manage.
 */
public class DelegateStudentTeamsDialog extends JDialog {

    /**
     * The manager used to generate repositories.
     */
    private GithubManager manager;

    /**
     * Indicates how many student teams do not have an assigned team.
     */
    private NumberIndicatorField unmatchedStudentsCounter;

    /**
     * A list of JSpinner objects linked to specific team members.
     */
    private List<TaTeamSpinner> teamSpinners;

    /**
     * A number indicating the total number of student teams.
     */
    private final int totalStudentTeamsCount;

    public DelegateStudentTeamsDialog(InitializerApp parentApp, GithubManager manager) {
        super(parentApp, "Delegate Student Teams", true);
        this.manager = manager;
        this.teamSpinners = new ArrayList<>();
        this.totalStudentTeamsCount = InitializerApp.organization.getStudentTeams().size();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.initUI();
    }

    /**
     * Begins showing the dialog.
     */
    public void begin() {
        this.setVisible(true);
    }

    /**
     * Prepares the user interface components.
     */
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(this.generateTopPanel(), BorderLayout.NORTH);
        mainPanel.add(this.generateSpinnersPanel(), BorderLayout.CENTER);

        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    /**
     * @return A JPanel containing the top-most counter of how many student teams are unassigned, and a small label.
     */
    private JPanel generateTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Unassigned teams: "), BorderLayout.CENTER);
        this.unmatchedStudentsCounter = new NumberIndicatorField(this.totalStudentTeamsCount);
        panel.add(this.unmatchedStudentsCounter, BorderLayout.EAST);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JPanel generateSpinnersPanel() {
        JPanel spinnersPanel = new JPanel();
        spinnersPanel.setLayout(new BoxLayout(spinnersPanel, BoxLayout.PAGE_AXIS));
        List<TATeam> taTeams = this.manager.getTeams();
        for (TATeam team : taTeams) {
            spinnersPanel.add(this.generateTeamSpinnerPanel(team));
        }
        return spinnersPanel;
    }

    private JPanel generateTeamSpinnerPanel(TATeam team) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel teamLabel = new JLabel(team.getName());
        teamLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(teamLabel, BorderLayout.WEST);
        TaTeamSpinner spinner = new TaTeamSpinner(team, this.totalStudentTeamsCount);
        spinner.addChangeListener(changeEvent -> {
            JSpinner s = (JSpinner) changeEvent.getSource();
            int studentTeamsMatched = 0;
            for (TaTeamSpinner teamSpinner : this.teamSpinners) {
                studentTeamsMatched += (int)teamSpinner.getValue();
            }
            if (this.totalStudentTeamsCount - studentTeamsMatched < 0) {
                s.setValue((int)s.getValue() + 1); // TODO: FIX! Causes stack overflow.
            } else {
                this.unmatchedStudentsCounter.setValue(this.totalStudentTeamsCount - studentTeamsMatched);
            }
        });
        this.teamSpinners.add(spinner);
        panel.add(spinner, BorderLayout.EAST);
        return panel;
    }

    /**
     * Gets a map containing an integer value for every TA team, which represents the number of student teams they will
     * be responsible for.
     * @return A map of TATeams and integer values.
     */
    public Map<TATeam, Integer> getResult() {
        Map<TATeam, Integer> results = new HashMap<>();
        for (TaTeamSpinner spinner : this.teamSpinners) {
            results.put(spinner.getTeam(), (int) spinner.getValue());
        }
        return results;
    }
}
