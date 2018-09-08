package nl.andrewlalis.ui.control;

import nl.andrewlalis.ui.view.OutputTextPane;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * A custom handler for printing log messages to the user interface text output pane.
 */
public class OutputTextHandler extends Handler {

    /**
     * The pane to which this handler writes.
     */
    private OutputTextPane outputPane;

    public OutputTextHandler(OutputTextPane outputPane) {
        this.outputPane = outputPane;
    }

    @Override
    public void publish(LogRecord logRecord) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = df.format(new Date(logRecord.getMillis()));
        this.outputPane.printStyled(dateString + ' ', "gray_italics");
        String style = "default";
        Level level = logRecord.getLevel();
        if (level == Level.SEVERE) {
            style = "error_red";
        } else if (level == Level.FINE
        || level == Level.FINER
        || level == Level.FINEST) {
            style = "smaller";
        } else if (level == Level.WARNING) {
            style = "warning_orange";
        }
        this.outputPane.printStyled(logRecord.getMessage() + '\n', style);
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
