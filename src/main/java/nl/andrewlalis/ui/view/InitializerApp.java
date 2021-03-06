package nl.andrewlalis.ui.view;

import nl.andrewlalis.command.CommandExecutor;
import nl.andrewlalis.model.Organization;
import nl.andrewlalis.ui.control.OutputTextHandler;
import nl.andrewlalis.ui.control.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the main user interface element that is referenced in main to construct the graphic interface.
 */
public class InitializerApp extends JFrame {

    /**
     * The window title.
     */
    private static final String FRAME_TITLE = "Github Initializer";

    /**
     * A default size of the window on startup.
     */
    private static final Dimension SIZE = new Dimension(1000, 600);

    /**
     * The pane on which general purpose program output is written.
     */
    private OutputTextPane outputTextPane;

    private JTextField organizationField = new JTextField();
    private JTextField accessTokenField = new JTextField();

    private JCheckBox privateCheckbox = new JCheckBox("Private");

    /**
     * The executor responsible for performing meaningful actions.
     */
    private CommandExecutor executor;

    /**
     * The organization object, which contains all important state information.
     */
    private Organization organization;

    /**
     * Constructs a new instance of the main application frame, with both an executor, and organization model.
     *
     * @param executor The command executor, which is passed to any action listeners, so that buttons in this interface
     *                 may execute commands in the same way that the command line can.
     */
    public InitializerApp(CommandExecutor executor) {
        this.executor = executor;
        this.organization = new Organization();

        // UI initialization.
        ImageIcon icon = new ImageIcon(getClass().getResource("/image/icon.png"));
        this.setIconImage(icon.getImage());
        this.initFrame();
    }

    /**
     * Begins showing the application
     */
    public void begin() {
        this.setVisible(true);
    }

    /**
     * Initializes the handler which passes logging information to the text pane for display.
     */
    private void initLoggingHandler() {
        Logger logger = Logger.getGlobal();
        OutputTextHandler handler = new OutputTextHandler(this.outputTextPane);
        handler.setLevel(Level.FINE);
        logger.addHandler(handler);
    }

    /**
     * Initializes the frame before display should begin.
     */
    private void initFrame() {
        this.setTitle(FRAME_TITLE);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setPreferredSize(SIZE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(this.initCommandPanel(), BorderLayout.CENTER);
        mainPanel.add(this.initRepoPanel(), BorderLayout.WEST);
        mainPanel.add(this.initGithubManagerPanel(), BorderLayout.EAST);

        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        this.initLoggingHandler();
    }

    /**
     * @return A JPanel containing input for all fields needed to connect to github, plus some commonly used buttons
     * which perform actions, as shortcuts for command actions.
     */
    private JPanel initGithubManagerPanel() {
        JPanel githubManagerPanel = new JPanel(new BorderLayout());

        // Information input (org name, key, etc.)
        JPanel infoInputPanel = new JPanel();
        infoInputPanel.setLayout(new BoxLayout(infoInputPanel, BoxLayout.PAGE_AXIS));

        infoInputPanel.add(generateTextFieldPanel("Organization Name", this.organizationField));
        this.organizationField.setText("InitializerTesting");
        infoInputPanel.add(generateTextFieldPanel("Access Token", this.accessTokenField));
        this.accessTokenField.setText("haha get your own");
        infoInputPanel.add(this.privateCheckbox);
        this.privateCheckbox.disable();

        githubManagerPanel.add(infoInputPanel, BorderLayout.NORTH);

        // Common actions panel.
        JPanel commonActionsPanel = new JPanel();
        commonActionsPanel.setLayout(new BoxLayout(commonActionsPanel, BoxLayout.PAGE_AXIS));

        commonActionsPanel.add(this.generateButtonPanel("Archive All", new ArchiveAllListener(this.executor, this)));
        commonActionsPanel.add(this.generateButtonPanel("Read Students File", new ReadStudentsFileListener(this.executor, this)));
        commonActionsPanel.add(this.generateButtonPanel("Delegate Student Teams", new DelegateStudentTeamsListener(this.executor, this)));
        commonActionsPanel.add(this.generateButtonPanel("Generate Assignments Repo", new GenerateAssignmentsRepoListener(this.executor, this)));

        commonActionsPanel.add(this.generateButtonPanel("Delete Repos", new DeleteReposListener(this.executor, this)));

        // Extra panel to push buttons to the top.
        JPanel buttonAlignmentPanel = new JPanel(new BorderLayout());
        buttonAlignmentPanel.add(commonActionsPanel, BorderLayout.NORTH);

        githubManagerPanel.add(buttonAlignmentPanel, BorderLayout.CENTER);

        return githubManagerPanel;
    }

    /**
     * @return A JPanel containing the command prompt field and output text pane.
     */
    private JPanel initCommandPanel() {
        JPanel commandPanel = new JPanel(new BorderLayout());
        // Output text pane.
        this.outputTextPane = new OutputTextPane();
        JScrollPane scrollPane = new JScrollPane(this.outputTextPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Text enter field and label.
        JPanel textEnterPanel = new JPanel(new BorderLayout());
        textEnterPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        textEnterPanel.add(new JLabel("Command:"), BorderLayout.WEST);
        JTextField commandField = new JTextField();
        commandField.addKeyListener(new CommandFieldKeyListener(this.executor));
        textEnterPanel.add(commandField, BorderLayout.CENTER);
        // Top Label
        JLabel commandPanelLabel = new JLabel("Program output", SwingConstants.CENTER);

        commandPanel.add(scrollPane, BorderLayout.CENTER);
        commandPanel.add(textEnterPanel, BorderLayout.SOUTH);
        commandPanel.add(commandPanelLabel, BorderLayout.NORTH);
        return commandPanel;
    }

    /**
     * @return A JPanel containing a list of repositories.
     */
    private JPanel initRepoPanel() {
        JPanel repoPanel = new JPanel();
        repoPanel.setLayout(new BoxLayout(repoPanel, BoxLayout.PAGE_AXIS));
        repoPanel.add(new JLabel("Repositories"));
        repoPanel.add(new JList());
        return repoPanel;
    }

    /**
     * Generates a text input field panel.
     * @param labelText The text for the label above the panel.
     * @param textField A reference to the text field that is used in the panel.
     * @return A JPanel containing the label and text field.
     */
    private JPanel generateTextFieldPanel(String labelText, JTextField textField) {
        JPanel newPanel = new JPanel(new BorderLayout());
        newPanel.add(new JLabel(labelText), BorderLayout.NORTH);
        newPanel.add(textField);
        newPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
        return newPanel;
    }

    /**
     * Generates a button with an attached action listener.
     * @param buttonText The text to display on the button.
     * @param listener The listener to attach to the button.
     * @return A BorderLayout JPanel which contains the button in the CENTER location.
     */
    private JPanel generateButtonPanel(String buttonText, ActionListener listener) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton(buttonText);
        button.addActionListener(listener);
        panel.add(button, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Gets the organization name entered in the relevant field.
     * @return The organization name the user has entered.
     */
    public String getOrganizationName() {
        return this.organizationField.getText().trim();
    }

    /**
     * Gets the oauth access token entered in the relevant field.
     * @return The access token the user has entered.
     */
    public String getAccessToken() {
        return this.accessTokenField.getText().trim();
    }

    /**
     * Gets whether or not the 'private' checkbox is selected.
     * @return True if the user wishes for repositories to be made private, or false otherwise.
     */
    public boolean isPrivateChecked() {
        return this.privateCheckbox.isSelected();
    }

    public Organization getOrganization() {
        return this.organization;
    }

    public void setAccessToken(String accessToken) {
        this.accessTokenField.setText(accessToken);
    }

}
