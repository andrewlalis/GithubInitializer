package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.database.DbHelper;
import nl.andrewlalis.model.database.DbUtil;
import nl.andrewlalis.ui.view.components.DetailPanel;
import nl.andrewlalis.ui.view.table_models.StudentTableModel;

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

        overviewPanel.add(new JLabel("Overview", SwingConstants.CENTER), BorderLayout.NORTH);

        // The real container for all the data views.
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Students", this.buildStudentsTablePanel());
        tabbedPane.addTab("Student Teams", this.buildStudentTeamsTablePanel());
        tabbedPane.addTab("Teaching Assistants", this.buildTAsTablePanel());

        overviewPanel.add(tabbedPane, BorderLayout.CENTER);

        return overviewPanel;
    }

    /**
     * @return A JPanel to be put into a tab for display of a list of students.
     */
    private JPanel buildStudentsTablePanel() {
        // Initialize the model, table, and a surrounding scroll pane.
        this.studentsModel = new StudentTableModel(DbHelper.getStudents());

        JPanel surroundingPanel = new JPanel(new BorderLayout());

        JTable table = new JTable(this.studentsModel);
        table.setFillsViewportHeight(true);
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            detailPanel.setDetailableEntity(studentsModel.getStudentAt(table.getSelectedRow()));
        });
        JScrollPane scrollPane = new JScrollPane(table);

        surroundingPanel.add(scrollPane, BorderLayout.CENTER);
        return surroundingPanel;
    }

    private JPanel buildStudentTeamsTablePanel() {
        return new JPanel();
    }

    private JPanel buildTAsTablePanel() {
        return new JPanel();
    }

    /**
     * Updates all models in the management view in accordance with the database.
     */
    public void updateModels() {
        this.studentsModel.setStudentsList(DbHelper.getStudents());

    }
}
