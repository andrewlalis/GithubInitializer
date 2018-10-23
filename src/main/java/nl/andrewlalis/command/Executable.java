package nl.andrewlalis.command;

/**
 * Classes which implement this interface tell that they may be 'executed', either via command-line, or through the use
 * of user interface actions.
 */
public interface Executable {

    /**
     * Runs this Executable's main functionality.
     *
     * @param args The list of arguments supplied to the executable.
     * @return True if successful, false if an error occurred.
     */
    boolean execute(String[] args);

}
