package nl.andrewlalis.ui.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CommandFieldKeyListener implements KeyListener {

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            System.out.println("Enter pressed.");
        }
    }
}
