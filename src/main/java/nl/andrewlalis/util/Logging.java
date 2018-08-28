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

        outputFile = new FileHandler("log/latest.log");
        formatter = new SimpleFormatter();

        outputFile.setFormatter(formatter);
        outputFile.setLevel(Level.FINEST);

        if (verbose) {
            Handler systemOut = new ConsoleHandler();
            systemOut.setLevel(Level.ALL);

            logger.addHandler(systemOut);
        }
        logger.addHandler(outputFile);
        logger.setLevel(Level.ALL);

        Logger.getLogger("").setLevel(Level.OFF);

    }

}
