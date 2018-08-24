package nl.andrewlalis.ui.view;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.control.listeners.CommandFieldKeyListener;
import nl.andrewlalis.ui.control.OutputTextHandler;

import javax.swing.*;
import java.awt.*;
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
    private static final Dimension SIZE = new Dimension(800, 600);

    /**
     * The pane on which general purpose program output is written.
     */
    private OutputTextPane outputTextPane;

    /**
     * The executor responsible for performing meaningful actions.
     */
    private CommandExecutor executor;

    public InitializerApp(CommandExecutor executor) {
        this.executor = executor;

        // UI initialization.
        this.initFrame();
    }

    /**
     * Begins showing the application
     */
    public void begin() {
        this.pack();
        this.setVisible(true);
    }

    /**
     * Initializes the handler which passes logging information to the text pane for display.
     */
    private void initLoggingHandler() {
        Logger logger = Logger.getGlobal();
        logger.addHandler(new OutputTextHandler(this.outputTextPane));
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

        this.setContentPane(mainPanel);

        this.initLoggingHandler();
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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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

}
