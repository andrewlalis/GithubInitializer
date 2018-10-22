package nl.andrewlalis.ui.view;

import nl.andrewlalis.git_api.GithubManager;
import nl.andrewlalis.model.database.DbHelper;
import nl.andrewlalis.model.database.DbUtil;
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

    public ManagementView(GithubManager githubManager) {
        super(
                "Course Management",
                false,
                DISPOSE_ON_CLOSE,
                null,
                githubManager
        );
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        // Dispose of all parents when this window closes.
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                for (AbstractView parent : getParentViews()) {
                    parent.dispose();
                }
                DbUtil.tearDown();
            }
        });
    }

    @Override
    protected JPanel buildContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout());

        contentPane.add(this.buildCommandPanel(), BorderLayout.WEST);
        contentPane.add(this.buildDetailPanel(), BorderLayout.EAST);
        contentPane.add(this.buildOverviewPanel(), BorderLayout.CENTER);

        return contentPane;
    }

    /**
     * @return A JPanel for the command prompt interface.
     */
    private JPanel buildCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());

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
     * @return A JPanel for the entity details section.
     */
    private JPanel buildDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout());

        detailPanel.add(new JLabel("Details", SwingConstants.CENTER), BorderLayout.NORTH);

        return detailPanel;
    }

    /**
     * @return Builds the overview panel, containing a listing of entities.
     */
    private JPanel buildOverviewPanel() {
        JPanel overviewPanel = new JPanel(new BorderLayout());

        overviewPanel.add(new JLabel("Overview"), BorderLayout.NORTH);

        // The real container for all the data views.
        JTabbedPane tabbedPane = new JTabbedPane();

        this.studentsModel = new StudentTableModel(DbHelper.getStudents());
        JTable studentsTable = new JTable(this.studentsModel);
        JScrollPane studentsScrollPane = new JScrollPane(studentsTable);
        tabbedPane.addTab("Students", studentsScrollPane);

        overviewPanel.add(tabbedPane, BorderLayout.CENTER);

        return overviewPanel;
    }

    /**
     * Updates all models in the management view in accordance with the database.
     */
    public void updateModels() {
        this.studentsModel.setStudentsList(DbHelper.getStudents());

    }
}
