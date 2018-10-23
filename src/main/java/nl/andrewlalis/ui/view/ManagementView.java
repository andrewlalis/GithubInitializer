package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.database.DbHelper;
import nl.andrewlalis.model.database.DbUtil;
import nl.andrewlalis.ui.view.components.DetailPanel;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;
import nl.andrewlalis.ui.view.table_models.StudentTeamTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The view in which the user manages a course.
 */
public class ManagementView extends AbstractView {

    /**
     * The model for the students table.
     */
    private StudentTableModel studentsModel;

    /**
     * The model for the student teams table.
     */
    private StudentTeamTableModel studentTeamModel;

    /**
     * A panel which displays the details of selected entities.
     */
    private DetailPanel detailPanel;

    public ManagementView(GithubManager githubManager) {
        super(
                "Course Management",
                false,
                DISPOSE_ON_CLOSE,
                null,
                githubManager
        );
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        // Dispose of all parents when this window closes. This is unique to the management view.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                for (AbstractView parent : getParentViews()) {
                    parent.dispose();
                }
                DbUtil.tearDown(); // Shut down the database session factory once everything is done.
            }
        });
    }

    @Override
    protected JPanel buildContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.detailPanel = new DetailPanel();

        contentPane.add(this.buildCommandPanel(), BorderLayout.WEST);
        contentPane.add(this.detailPanel, BorderLayout.EAST);
        contentPane.add(this.buildOverviewPanel(), BorderLayout.CENTER);

        return contentPane;
    }

    /**
     * @return A JPanel for the command prompt interface.
     */
    private JPanel buildCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());
        commandPanel.setBorder(BorderFactory.createLoweredBevelBorder());

        commandPanel.add(new JLabel("Commands", SwingConstants.CENTER), BorderLayout.NORTH);
        commandPanel.add(new JTextArea("Command prompt area goes here."), BorderLayout.CENTER);

        // Construct the sub-panel for commands at the bottom of the panel.
        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField commandTextField = new JTextField();
        inputPanel.add(commandTextField, BorderLayout.CENTER);

        commandPanel.add(inputPanel, BorderLayout.SOUTH);

        return commandPanel;
    }

    /**
     * @return Builds the overview panel, containing a listing of entities.
     */
    private JPanel buildOverviewPanel() {
        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        overviewPanel.add(this.buildSearchPanel(), BorderLayout.NORTH);

        // The real container for all the data views.
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Students", this.buildStudentsTablePanel());
        tabbedPane.addTab("Student Teams", this.buildStudentTeamsTablePanel());
        tabbedPane.addTab("Teaching Assistants", this.buildTAsTablePanel());

        overviewPanel.add(tabbedPane, BorderLayout.CENTER);

        return overviewPanel;
    }

    /**
     * Builds a JPanel containing utilities to search the data in the various tables in the application.
     * @return A JPanel containing search functionality.
     */
    private JPanel buildSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());

        searchPanel.add(new JLabel("Search", SwingConstants.LEFT), BorderLayout.WEST);
        searchPanel.add(new JTextField(), BorderLayout.CENTER);

        return searchPanel;
    }

    /**
     * Provides a JScrollPane and JPanel to surround a table.
     * @param table The table to wrap.
     * @return The JPanel containing the table, wrapped in a JScrollPane.
     */
    private JPanel buildGenericTablePanel(JTable table) {
        JPanel surroundingPanel = new JPanel(new BorderLayout());
        surroundingPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return surroundingPanel;
    }

    /**
     * @return A JPanel to be put into a tab for display of a list of students.
     */
    private JPanel buildStudentsTablePanel() {
        // Initialize the model, table, and a surrounding scroll pane.
        this.studentsModel = new StudentTableModel(DbHelper.getStudents());

        JTable table = new JTable(this.studentsModel);
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            detailPanel.setDetailableEntity(studentsModel.getStudentAt(table.getSelectedRow()));
        });
        return this.buildGenericTablePanel(table);
    }

    /**
     * @return A JPanel to be put into a tab for display of a list of student teams.
     */
    private JPanel buildStudentTeamsTablePanel() {
        this.studentTeamModel = new StudentTeamTableModel(DbHelper.getStudentTeams());

        JTable table = new JTable(this.studentTeamModel);
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            detailPanel.setDetailableEntity(studentTeamModel.getStudentTeamAt(table.getSelectedRow()));
        });

        return this.buildGenericTablePanel(table);
    }

    private JPanel buildTAsTablePanel() {
        return new JPanel();
    }

    /**
     * Updates all models in the management view in accordance with the database.
     */
    public void updateModels() {
        this.studentsModel.setStudentsList(DbHelper.getStudents());
        this.studentTeamModel.setStudentTeamsList(DbHelper.getStudentTeams());
    }
}
