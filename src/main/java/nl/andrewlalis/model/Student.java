package nl.andrewlalis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents one student's github information.
 */
public class Student {

    /**
     * The student's S-number.
     */
    private int number;

    /**
     * The student's name.
     */
    private String name;

    /**
     * The student's email.
     */
    private String emailAddress;

    /**
     * The student's github username.
     */
    private String githubUsername;

    /**
     * A list of partners that the student has said that they would like to be partners with.
     */
    private List<Integer> preferredPartners;

    public Student(int number, String name, String emailAddress, String githubUsername, List<Integer> preferredPartners) {
        this.number = number;
        this.name = name;
        this.emailAddress = emailAddress;
        this.githubUsername = githubUsername;
        this.preferredPartners = preferredPartners;
    }

    @Override
    public String toString() {
        return this.number + " - " + this.name + " - " + this.emailAddress + " - " + this.githubUsername;
    }

    public int getNumber() {
        return number;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getGithubUsername() {
        return githubUsername;
    }

    public List<Integer> getPreferredPartners() {
        return preferredPartners;
    }

    /**
     * Using a given map of all students, returns a student's preferred team.
     * @param studentMap A mapping from student number to student for all students who have signed up.
     * @return A team with unknown id, comprised of this student's preferred partners.
     */
    public Team getPreferredTeam(Map<Integer, Student> studentMap) {
        Team t = new Team();
        for (int partnerNumber : this.getPreferredPartners()) {
            t.addStudent(studentMap.get(partnerNumber));
        }
        t.addStudent(this);
        return t;
    }

    @Override
    public boolean equals(Object s) {
        if (!(s instanceof Student)) {
            return false;
        }
        Student student = (Student) s;
        return student.getNumber() == this.getNumber()
                || student.getEmailAddress().equals(this.getEmailAddress())
                || student.getGithubUsername().equals(this.getGithubUsername());
    }
}
