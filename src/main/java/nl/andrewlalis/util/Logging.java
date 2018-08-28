package nl.andrewlalis.util;

import java.io.IOException;
import java.util.logging.*;

/**
 * Responsible for creating logs to standard output and writing to files.
 */
public class Logging {

    private static FileHandler outputFile;
    private static SimpleFormatter formatter;

    public static void setup() {
        Logger logger = Logger.getGlobal();

        try {
            outputFile = new FileHandler("log/latest.log");
            formatter = new SimpleFormatter();
            outputFile.setFormatter(formatter);
            outputFile.setLevel(Level.FINEST);
            logger.addHandler(outputFile);
        } catch (IOException e) {
            logger.warning("Unable to save log to output file.");
            e.printStackTrace();
        }

        logger.setLevel(Level.ALL);
        Logger.getLogger("").setLevel(Level.OFF);

    }

}
