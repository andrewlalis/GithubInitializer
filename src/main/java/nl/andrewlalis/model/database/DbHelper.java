package nl.andrewlalis.model.database;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * This class will contain some static methods to help in the retrieval of commonly used information.
 */
@SuppressWarnings("unchecked")
public class DbHelper {

    /**
     * Gets a list of students in the database.
     * @return A list of students.
     */
    public static List<Student> getStudents() {
        Session session = DbUtil.getSessionFactory().openSession();
        List<Student> students = (List<Student>) session.createQuery("from Student").list();
        session.close();
        return students;
    }

    /**
     * Saves a list of student teams to the database.
     */
    public static void saveStudentTeams(List<StudentTeam> teams) {
        Session session = DbUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        for (StudentTeam team : teams) {
            for (Student s : team.getStudents()) {
                session.save(s);
            }
            session.save(team);
        }

        tx.commit();
        session.close();
    }

    /**
     * Gets a list of student teams in the database.
     * @return A list of student teams.
     */
    public static List<StudentTeam> getStudentTeams() {
        Session session = DbUtil.getSessionFactory().openSession();
        List<StudentTeam> studentTeams = (List<StudentTeam>) session.createQuery("from StudentTeam").list();
        session.close();
        return studentTeams;
    }

}
