package nl.andrewlalis.ui.control.listeners;

import nl.andrewlalis.command.CommandExecutor;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * This Key Listener listens for when the ENTER key is pressed in the command-line text field, and executes the command
 * when that is the case.
 */
public class CommandFieldKeyListener implements KeyListener {

    /**
     * This is responsible for parsing and running entered commands.
     */
    private CommandExecutor executor;

    public CommandFieldKeyListener(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField inputField = (JTextField) keyEvent.getComponent();
            this.executor.executeString(inputField.getText());
            inputField.setText(null);
        }
    }
}
