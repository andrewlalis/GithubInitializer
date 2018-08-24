package nl.andrewlalis.ui.control.command;

import java.util.HashMap;
import java.util.Map;
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

    public CommandExecutor() {
        this.commands = new HashMap<>();
    }

    /**
     * Adds a new command to the list of commands which this executor can handle.
     * @param commandName The name that the command will be found by.
     * @param executable The executable command that is bound to the given name.
     */
    public void registerCommand(String commandName, Executable executable) {
        this.commands.put(commandName, executable);
    }

    /**
     * Attempts to execute a command string, or show an error message if an invalid command or argument was entered.
     * @param commandString The String command and any arguments that go with it.
     */
    public void executeString(String commandString) {
        String[] words = commandString.trim().toLowerCase().split(" ");
        if (words.length < 1) {
            logger.warning("No command supplied.");
            return;
        }
        String commandName = words[0];
        String[] args = new String[words.length - 1];
        if (words.length > 1) {
            System.arraycopy(words, 1, args, 0, words.length - 1);
        }
        if (this.commands.containsKey(commandName)) {
            this.commands.get(commandName).execute(args);
            logger.info(commandString);
        } else {
            logger.warning(commandName + " is not a valid command.");
        }
    }

}
