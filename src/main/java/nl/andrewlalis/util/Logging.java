package nl.andrewlalis.util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Responsible for creating logs to standard output and writing to files.
 */
public class Logging {

    private static FileHandler outputFile;
    private static SimpleFormatter formatter;

    public static void setup(boolean verbose) throws IOException {
        Logger logger = Logger.getGlobal();

        Handler[] handlers = logger.getHandlers();
        for (Handler h : handlers) {
            logger.removeHandler(h);
        }
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        if (verbose) {
            handler.setLevel(Level.FINEST);
        } else {
            handler.setLevel(Level.INFO);
        }

        logger.addHandler(handler);

        outputFile = new FileHandler("log/latest.txt");
        formatter = new SimpleFormatter();

        outputFile.setFormatter(formatter);
        outputFile.setLevel(Level.FINEST);
        logger.addHandler(outputFile);

    }

}
