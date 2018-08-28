package nl.andrewlalis.model.database;

import nl.andrewlalis.model.*;

import java.sql.*;
import java.util.ArrayList;
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

    private static final int TEAM_NONE = 1000000;
    private static final int TEAM_TA_ALL = 1000001;

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
     * Initializes the database from the table_init.sql script, which defines the table schema.
     * Then, inserts some constant starter data from /sql/insert/types.sql.
     * @return True if successful, false if not.
     */
    public boolean initialize() {
        List<PreparedStatement> tableStatements = Utils.prepareStatementsFromFile("/sql/table_init.sql", this.connection);
        for (PreparedStatement statement : tableStatements) {
            try {
                statement.execute();
            } catch (SQLException e) {
                logger.severe("SQLException while executing prepared statement:\n" + statement.toString() + "\nCode: " + e.getErrorCode());
                return false;
            }
        }
        logger.fine("Database tables initialized.");
        List<PreparedStatement> insertStatements = Utils.prepareStatementsFromFile("/sql/insert/types.sql", this.connection);
        for (PreparedStatement statement : insertStatements) {
            try {
                statement.execute();
            } catch (SQLException e) {
                logger.severe("SQLException while inserting into table:\n" + statement.toString() + "\nCode: " + e.getErrorCode());
                return false;
            }
        }
        logger.fine("Initial types inserted.");
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
            logger.finest("Storing person: " + person);
            String sql = "INSERT INTO persons (id, name, email_address, github_username, person_type_id) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, person.getNumber());
            stmt.setString(2, person.getName());
            stmt.setString(3, person.getEmailAddress());
            stmt.setString(4, person.getGithubUsername());
            stmt.setInt(5, personType);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            logger.severe("SQLException while inserting Person: " + person + '\n' + e.getMessage());
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
            stmt.execute();
            return true;
        } catch (SQLException e) {
            logger.severe("SQL Exception while inserting TeachingAssistant.\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Stores a team in the database.
     * @param team The team to store.
     * @param type The type of team that this is.
     * @return True if successful, false otherwise.
     */
    public boolean storeTeam(Team team, int type) {
        try {
            String sql = "INSERT INTO teams (id, team_type_id) VALUES (?, ?);";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, team.getId());
            stmt.setInt(2, type);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            logger.severe("SQLException while inserting team: " + team + '\n' + e.getMessage());
            return false;
        }
    }

    /**
     * Stores a list of student teams in the database.
     * @param teams The list of teams to store.
     * @return True if successful, or false if an error occurred.
     */
    public boolean storeStudentTeams(List<StudentTeam> teams) {
        for (StudentTeam team : teams) {
            if (!this.storeTeam(team, TEAM_TYPE_STUDENT)) {
                return false;
            }
            for (Student student : team.getStudents()) {
                if (!this.storeStudent(student, team.getId())) {
                    return false;
                }
            }
        }
        return true;
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
        logger.finest("Storing student: " + student);
        if (!storePerson(student, PERSON_TYPE_STUDENT)) {
            return false;
        }
        try {
            String sql = "INSERT INTO students (person_id, team_id, chose_partner) VALUES (?, ?, ?);";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, student.getNumber());
            stmt.setInt(2, teamId);
            stmt.setInt(3, student.getPreferredPartners().size() > 0 ? 1 : 0);
            stmt.execute();
            // Storing partners.
            String sqlPartner = "INSERT INTO student_preferred_partners (student_id, partner_id) VALUES (?, ?);";
            PreparedStatement stmtPartner = this.connection.prepareStatement(sqlPartner);
            for (int partnerId : student.getPreferredPartners()) {
                stmtPartner.setInt(1, student.getNumber());
                stmtPartner.setInt(2, partnerId);
                stmtPartner.execute();
            }
            return true;
        } catch (SQLException e) {
            logger.severe("SQL Exception while inserting Student into database.\n" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of preferred partners that each student has set.
     * @param studentId The student id to search by.
     * @return A list of student id's for all students that the given student wishes to be their partner.
     */
    private List<Integer> retrievePreferredPartners(int studentId) {
        try {
            logger.finest("Retrieving preferred partners of student: " + studentId);
            String sql = "SELECT partner_id FROM student_preferred_partners WHERE student_id=?;";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, studentId);
            ResultSet results = stmt.executeQuery();
            List<Integer> partners = new ArrayList<>();
            while (results.next()) {
                partners.add(results.getInt(1));
            }
            return partners;
        } catch (SQLException e) {
            logger.severe("SQL Exception while retrieving preferred partners of student: " + studentId + '\n' + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a student by their id.
     * @param id The id of the student (student number)
     * @return The student corresponding to this number, or null if it could not be found.
     */
    public Student retrieveStudent(int id) {
        try {
            String sql = "SELECT * FROM persons WHERE id=?";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet result = stmt.executeQuery();
            return new Student(id, result.getString("name"), result.getString("email_address"), result.getString("github_username"), this.retrievePreferredPartners(id));
        } catch (SQLException e) {
            logger.severe("SQL Exception while retrieving Student.\n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
