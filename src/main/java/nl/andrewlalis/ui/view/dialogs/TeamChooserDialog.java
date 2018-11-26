package nl.andrewlalis.ui.view.dialogs;

import nl.andrewlalis.model.StudentTeam;
import nl.andrewlalis.model.Team;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * With this dialog, a user can choose from a list of teams. This works for any type of team.
 * The user should have the option to create a new team.
 */
public class TeamChooserDialog extends JDialog {

    /**
     * The combo box used in selecting a team. Will be populated by all team numbers.
     */
    private JComboBox<Integer> teamChooserBox;

    /**
     * The team which is selected.
     */
    private Team selectedTeam;

    /**
     * The model containing the list of teams.
     */
    private List<StudentTeam> teams;

    public TeamChooserDialog(Window parent, List<StudentTeam> teams) {
        super(parent, "Team Chooser", ModalityType.APPLICATION_MODAL);
        this.teams = teams;
        this.setContentPane(this.buildContentPane());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * @return The dialog's content pane, containing all user interface elements.
     */
    private JPanel buildContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());

        contentPane.add(new JLabel("Choose a team."), BorderLayout.NORTH);

        // Main selection panel.
        // Create a list of numbers to represent the teams.
        Integer[] teamNumbers = new Integer[this.teams.size()];
        for (int row = 0; row < this.teams.size(); row++) {
            teamNumbers[row] = this.teams.get(row).getNumber();
        }

        this.teamChooserBox = new JComboBox<>(teamNumbers);
        contentPane.add(this.teamChooserBox, BorderLayout.CENTER);

        // Button panel for confirming or cancelling selection.
        JPanel confirmPanel = new JPanel();
        JButton doneButton = new JButton("Done");
        // Add a small action listener to set the selected team and dispose of the dialog.
        doneButton.addActionListener(actionEvent -> {
            selectedTeam = teams.get(teamChooserBox.getSelectedIndex());
            dispose();
        });
        confirmPanel.add(doneButton);

        JButton cancelButton = new JButton("Cancel");
        // Add a small action listener to dispose of the dialog and return the null selected team.
        cancelButton.addActionListener(actionEvent -> dispose());
        confirmPanel.add(cancelButton);
        contentPane.add(confirmPanel, BorderLayout.SOUTH);

        return contentPane;
    }

    /**
     * @return The team which was selected by the user.
     */
    public Team getSelectedTeam() {
        return this.selectedTeam;
    }

}
