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

        if (verbose) {
            logger.setLevel(Level.FINEST);
        } else {
            logger.setLevel(Level.INFO);
        }

        outputFile = new FileHandler("log/latest.txt");
        formatter = new SimpleFormatter();

        outputFile.setFormatter(formatter);
        logger.addHandler(outputFile);

    }

}
