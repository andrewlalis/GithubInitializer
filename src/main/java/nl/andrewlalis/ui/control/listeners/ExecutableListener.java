package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.ui.control.command.CommandExecutor;
import nl.andrewlalis.ui.view.InitializerApp;

import java.awt.event.ActionListener;

/**
 * An action listener which is pre-set to execute an executable once an action is performed.
 * Since these are used for the user interface, an instance of the application is passed, for the purpose of providing
 * a parent component for many popups, and to have access to input fields.
 */
public abstract class ExecutableListener implements ActionListener {

    /**
     * The executor, with some registered commands that will be executed by listeners which extend this one.
     */
    protected CommandExecutor executor;

    /**
     * An instance of the UI application.
     */
    protected InitializerApp app;

    public ExecutableListener(CommandExecutor executor, InitializerApp app) {
        this.executor = executor;
        this.app = app;
    }
}
