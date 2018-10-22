package nl.andrewlalis.model.database;

import nl.andrewlalis.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

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

}
