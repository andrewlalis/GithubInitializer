package nl.andrewlalis.model;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents one student's github information.
 */
public class Student extends Person {

    private static final Logger logger = Logger.getLogger(Student.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    /**
     * A list of partners that the student has said that they would like to be partners with.
     */
    private List<Integer> preferredPartners;

    /**
     * Constructs a student similarly to a Person, but with an extra preferredPartners list.
     * @param number The student's S-Number.
     * @param name The student's name.
     * @param emailAddress The student's email address.
     * @param githubUsername The student's github username.
     * @param preferredPartners A list of this student's preferred partners, as a list of integers representing the
     * other students' numbers.
     */
    public Student(int number, String name, String emailAddress, String githubUsername, List<Integer> preferredPartners) {
        super(number, name, emailAddress, githubUsername);
        this.preferredPartners = preferredPartners;
    }

    public List<Integer> getPreferredPartners() {
        return this.preferredPartners;
    }

    /**
     * Using a given map of all students, returns a student's preferred team.
     * @param studentMap A mapping from student number to student for all students who have signed up.
     * @return A team with unknown id, comprised of this student's preferred partners.
     */
    public StudentTeam getPreferredTeam(Map<Integer, Student> studentMap) {
        StudentTeam t = new StudentTeam();
        for (int partnerNumber : this.getPreferredPartners()) {
            t.addMember(studentMap.get(partnerNumber));
        }
        t.addMember(this);
        return t;
    }
}
