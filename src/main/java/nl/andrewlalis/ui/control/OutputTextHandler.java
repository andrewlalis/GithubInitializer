package nl.andrewlalis.ui.control;

import nl.andrewlalis.ui.view.OutputTextPane;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
        String sourceLocationString = logRecord.getSourceClassName() + "::" + logRecord.getSourceMethodName();
        this.outputPane.printStyled(dateString + ' ', "gray_italics");
        this.outputPane.printStyled(logRecord.getLevel().getName() + ": ", "bold");
        this.outputPane.printStyled(sourceLocationString + "\n\t", "bold");
        this.outputPane.printStyled(logRecord.getMessage() + '\n', "smaller");
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
