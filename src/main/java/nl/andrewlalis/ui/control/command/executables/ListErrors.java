package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.model.error.Error;
import nl.andrewlalis.ui.control.command.Executable;
import nl.andrewlalis.ui.view.InitializerApp;

import java.util.logging.Logger;

/**
 * This executable lists all errors that have occurred so far in the runtime of the program, and have not been resolved.
 */
public class ListErrors implements Executable {

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(ListErrors.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    @Override
    public boolean execute(String[] args) {
        StringBuilder sb = new StringBuilder("Runtime Errors:\n");
        for (Error error : InitializerApp.organization.getErrors()) {
            sb.append(error);
        }
        logger.info(sb.toString());
        return true;
    }

}
