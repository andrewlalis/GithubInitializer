package nl.andrewlalis.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents one student's github information.
 */
@Entity(name = "Student")
@Table(name="students")
public class Student extends Person {

    private static final Logger logger = Logger.getLogger(Student.class.getName());
    static {
        logger.setParent(Logger.getGlobal());
    }

    /**
     * A list of partners that the student has said that they would like to be partners with.
     */
    @ManyToMany
    @JoinTable(
            name = "student_preferred_partners",
            joinColumns = { @JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "preferred_partner_id")}
    )
    private List<Student> preferredPartners;

    /**
     * The team that this student is assigned to.
     */
    @ManyToOne
    @JoinColumn(name = "team_id")
    private StudentTeam team;

    /**
     * Constructs an empty student object.
     */
    public Student() {
        this.preferredPartners = new ArrayList<>();
    }

    /**
     * Constructs a student similarly to a Person, but with an extra preferredPartners list.
     * @param number The student's S-Number.
     * @param name The student's name.
     * @param emailAddress The student's email address.
     * @param githubUsername The student's github username.
     * @param preferredPartners A list of this student's preferred partners, as a list of integers representing the
     * other students' numbers.
     */
    public Student(int number, String name, String emailAddress, String githubUsername, List<Student> preferredPartners) {
        super(number, name, emailAddress, githubUsername);
        this.preferredPartners = preferredPartners;
    }

    public List<Student> getPreferredPartners() {
        return this.preferredPartners;
    }

    public void setPreferredPartners(List<Student> preferredPartners) {
        this.preferredPartners = preferredPartners;
    }

    public void addPreferredPartner(Student student) {
        this.preferredPartners.add(student);
    }

    /**
     * Returns a student's preferred team, including himself.
     * @return A team with unknown number, comprised of this student's preferred partners.
     */
    public StudentTeam getPreferredTeam() {
        StudentTeam t = new StudentTeam();
        for (Student partner : this.getPreferredPartners()) {
            t.addMember(partner);
        }
        t.addMember(this);
        return t;
    }

    /**
     * Assigns this student to the given team, from the student's perspective.
     * @param team The team to set as the assigned team.
     */
    public void assignToTeam(StudentTeam team) {
        this.team = team;
    }

    /**
     * @return The team that this student is assigned to. May return null if the student is unassigned.
     */
    public StudentTeam getAssignedTeam() {
        return this.team;
    }
}
