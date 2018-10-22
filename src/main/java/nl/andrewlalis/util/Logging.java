package nl.andrewlalis.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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

        logger.setLevel(Level.FINEST);
        Logger.getLogger("").setLevel(Level.OFF);

    }

}
