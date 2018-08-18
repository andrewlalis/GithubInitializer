package nl.andrewlalis.model.database;

import nl.andrewlalis.model.Person;
import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.TeachingAssistant;

import java.sql.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class abstracts many of the functions needed for interaction with the application's SQLite database.
 */
public class Database {

    private static final int PERSON_TYPE_STUDENT = 0;
    private static final int PERSON_TYPE_TA = 1;

    private static final int TEAM_TYPE_STUDENT = 0;
    private static final int TEAM_TYPE_TA = 1;
    private static final int TEAM_TYPE_TA_ALL = 2;

    private static final int TEAM_NONE = 0;
    private static final int TEAM_TA_ALL = 1;

    private static final int ERROR_TYPE_TEAM = 0;
    private static final int ERROR_TYPE_PERSON = 1;
    private static final int ERROR_TYPE_SYSTEM = 2;

    /**
     * The connection needed for all queries.
     */
    private Connection connection;

    /**
     * The logger for outputting debug info.
     */
    private static final Logger logger = Logger.getLogger(Database.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    public Database(String databaseFilename) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFilename);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the database from the table_init.sql script, which defines the table schema and starting data.
     * @return True, if successful, false if not.
     */
    public boolean initialize() {
        List<PreparedStatement> statements = Utils.prepareStatementsFromFile("/sql/table_init.sql", this.connection);
        for (PreparedStatement statement : statements) {
            try {
                statement.execute();
            } catch (SQLException e) {
                logger.severe("SQLException while executing prepared statement: " + statement.toString() + ". Code: " + e.getErrorCode());
                return false;
            }
        }
        logger.fine("Database initialized.");
        return true;
    }

    /**
     * Stores a person in the database.
     * @param person The person object to store.
     * @param personType The type of person to store, using a constant defined above.
     * @return True if successful, false otherwise.
     */
    private boolean storePerson(Person person, int personType) {
        try {
            String sql = "INSERT INTO persons (id, name, email_address, github_username, person_type_id) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, person.getNumber());
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getEmailAddress());
            stmt.setString(4, person.getGithubUsername());
            stmt.setInt(5, personType);
            return stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Stores a teaching assistant without a team.
     * @param ta The teaching assistant to store.
     * @return True if successful, false otherwise.
     */
    public boolean storeTeachingAssistant(TeachingAssistant ta) {
        return this.storeTeachingAssistant(ta, TEAM_NONE);
    }

    /**
     * Stores a teaching assistant in the database.
     * @param ta The teaching assistant to store.
     * @param teamId The teaching assistant's team id.
     * @return True if successful, false otherwise.
     */
    public boolean storeTeachingAssistant(TeachingAssistant ta, int teamId) {
        if (!storePerson(ta, PERSON_TYPE_TA)) {
            return false;
        }
        try {
            String sql = "INSERT INTO teaching_assistants (person_id, team_id) VALUES (?, ?);";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, ta.getNumber());
            stmt.setInt(2, teamId);
            return stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Stores a student without a team.
     * @param student The student to store.
     * @return True if successful, false otherwise.
     */
    public boolean storeStudent(Student student) {
        return this.storeStudent(student, TEAM_NONE);
    }

    /**
     * Stores a student in the database.
     * @param student The student to store.
     * @param teamId The team id for the team the student is in.
     * @return True if the operation was successful, false otherwise.
     */
    public boolean storeStudent(Student student, int teamId) {
        if (!storePerson(student, PERSON_TYPE_STUDENT)) {
            return false;
        }
        try {
            String sql = "INSERT INTO students (person_id, team_id, chose_partner) VALUES (?, ?, ?);";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, student.getNumber());
            stmt.setInt(2, teamId);
            stmt.setInt(3, student.getPreferredPartners().size() > 0 ? 1 : 0);
            return stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
