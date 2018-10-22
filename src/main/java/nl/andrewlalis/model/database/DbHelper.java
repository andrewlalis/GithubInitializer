package nl.andrewlalis.model.database;

import nl.andrewlalis.model.Student;
import nl.andrewlalis.model.StudentTeam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * This class will contain some static methods to help in the retrieval of commonly used information.
 */
public class DbHelper {

    /**
     * Gets a list of students in the database.
     * @return A list of students.
     */
    public static List<Student> getStudents() {
        SessionFactory sessionFactory = DbUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
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

}
