package nl.andrewlalis.model.database;

import nl.andrewlalis.util.FileUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Contains some methods which make database actions much easier.
 */
public class Utils {

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(Utils.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    /**
     * Gets an ordered list of prepared statements from a file which contains multiple statements separated by a
     * semicolon. This method separates those statements into their own strings, and prepares them individually to be
     * executed later.
     * @param filename The name of the file which contains the statements.
     * @param connection The connection to a database; used to prepare statements.
     * @return An ordered list of prepared statements which are based on the contents of the file provided.
     */
    public static List<PreparedStatement> prepareStatementsFromFile(String filename, Connection connection) {
        String string = FileUtils.readStringFromFile(filename);
        if (string == null || string.isEmpty()) {
            return new ArrayList<>();
        }
        String[] splits = string.split(";");
        List<PreparedStatement> statements = new ArrayList<>();
        for (String split : splits) {
            if (split.trim().length() > 1) {
                try {
                    statements.add(connection.prepareStatement(split));
                } catch (SQLException e) {
                    logger.severe("SQLException while preparing a statement:\n" + split + "\nError Code: " + e.getErrorCode());
                }
            }
        }
        return statements;
    }

}
