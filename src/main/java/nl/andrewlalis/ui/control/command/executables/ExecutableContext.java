package nl.andrewlalis.ui.control.command.executables;

import nl.andrewlalis.ui.control.command.Executable;

/**
 * An object to record a specific executable's execution context (args given), with the ability to re-run the executable
 * if a failure occurs.
 */
public class ExecutableContext {

    /**
     * The executable object, without any contextual information.
     */
    private Executable executable;

    /**
     * A list of arguments given to the executable when it was called.
     */
    private String[] args;

    public ExecutableContext(Executable executable, String[] args) {
        this.executable = executable;
        this.args = args;
    }

    /**
     * Runs the stored executable again with the same arguments it was originally given.
     * @return True if the execution was successful, or false otherwise.
     */
    public boolean runAgain() {
        return this.executable.execute(this.args);
    }

}
