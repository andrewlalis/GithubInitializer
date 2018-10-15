package nl.andrewlalis.command.executables;

import nl.andrewlalis.command.Executable;
import nl.andrewlalis.model.error.Error;
import nl.andrewlalis.ui.view.InitializerApp;

import java.util.logging.Logger;

/**
 * This executable lists all errors that have occurred so far in the runtime of the program, and have not been resolved.
 */
public class ListErrors implements Executable {

    /**
     * A reference to the current application.
     */
    private InitializerApp app;

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(ListErrors.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    public ListErrors(InitializerApp app) {
        this.app = app;
    }

    @Override
    public boolean execute(String[] args) {
        StringBuilder sb = new StringBuilder("Runtime Errors:\n");
        if (this.app.getOrganization().getErrors().isEmpty()) {
            sb.append("None");
        }
        for (Error error : this.app.getOrganization().getErrors()) {
            sb.append(error).append('\n');
        }
        logger.info(sb.toString());
        return true;
    }

}
