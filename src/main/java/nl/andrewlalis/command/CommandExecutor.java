package nl.andrewlalis.command;

import nl.andrewlalis.command.executables.ExecutableContext;

import java.util.*;
import java.util.logging.Logger;

/**
 * Manages parsing an entered string and executing a task based upon information in the command.
 */
public class CommandExecutor {

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(CommandExecutor.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    /**
     * A list of named commands which can be executed.
     */
    private Map<String, Executable> commands;

    /**
     * A list of all the executables which have failed to execute.
     */
    private List<ExecutableContext> failedExecutables;

    /**
     * A list of all executables which have been run successfully.
     */
    private List<ExecutableContext> successfulExecutables;

    public CommandExecutor() {
        this.commands = new HashMap<>();
        this.failedExecutables = new ArrayList<>();
        this.successfulExecutables = new ArrayList<>();
    }

    /**
     * Adds a new command to the list of commands which this executor can handle.
     * @param commandName The name that the command will be found by.
     * @param executable The executable command that is bound to the given name.
     */
    public void registerCommand(String commandName, Executable executable) {
        this.commands.put(commandName, executable);
        logger.fine("Registered command: " + commandName);
    }

    /**
     * Attempts to execute a command string, or show an error message if an invalid command or argument was entered.
     * @param commandString The String command and any arguments that go with it.
     */
    public void executeString(String commandString) {
        String[] words = commandString.trim().split(" ");
        if (words.length < 1) {
            logger.warning("No command supplied.");
            return;
        }
        String commandName = words[0];
        String[] args = new String[words.length - 1];
        if (words.length > 1) {
            System.arraycopy(words, 1, args, 0, words.length - 1);
        }
        this.executeCommand(commandName, args);
    }

    /**
     * Executes a command with the given name, and given arguments.
     * @param commandName The name of the command. A command must be registered using registerCommand before it can be
     *                    called here.
     * @param args The list of arguments to provide to the command as needed by the executable that was registered.
     */
    public void executeCommand(String commandName, String[] args) {
        if (this.commands.containsKey(commandName)) {
            logger.info("Command executed: " + commandName + ' ' + Arrays.toString(args));
            Executable executable = this.commands.get(commandName);
            ExecutableContext context = new ExecutableContext(executable, args);
            if (!executable.execute(args)) {
                logger.warning("Command did not execute successfully.");
                this.failedExecutables.add(context);
            } else {
                this.successfulExecutables.add(context);
            }
        } else {
            logger.warning(commandName + " is not a valid command.");
        }
    }

    /**
     * Retries all failed executables, and if successful, removes them from the queue.
     */
    public void rerunFailedExecutables() {
        this.failedExecutables.removeIf(ExecutableContext::runAgain);
    }

}
