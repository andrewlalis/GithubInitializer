package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.control.command.Executable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An action listener which is pre-set to execute an executable once an action is performed.
 */
public abstract class ExecutableListener implements ActionListener {

    protected CommandExecutor executor;

    public ExecutableListener(CommandExecutor executor) {
        this.executor = executor;
    }
}
