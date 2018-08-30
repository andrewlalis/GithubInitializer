package nl.andrewlalis.model.error;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents an error that occurs, but does not stop the application while running. Different from an exception in that
 * errors are meant to be added to a list and processed at a later time, and not hinder the operations in the present.
 */
public class Error {

    /**
     * The time at which this error occurred.
     */
    private long timestamp;

    /**
     * The severity of the error.
     */
    private Severity severity;

    /**
     * A custom error message generated at the time of the error.
     */
    private String message;

    /**
     * Constructs a new Error with a severity and message.
     * @param severity The severity of the message.
     * @param message The custom generated error message for this error.
     */
    public Error(Severity severity, String message) {
        this.timestamp = System.currentTimeMillis();
        this.severity = severity;
        this.message = message;
    }

    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = df.format(new Date(this.timestamp));
        return dateString + ' ' + this.severity.toString() + ": " + this.message;
    }
}
