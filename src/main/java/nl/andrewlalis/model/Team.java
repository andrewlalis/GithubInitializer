package nl.andrewlalis.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents one or more students' collective information.
 */
public class Team {

    /**
     * The list of students in this team.
     */
    private List<Student> students;

    /**
     * The team identification number.
     */
    private int id;

    public Team() {
        this.students = new ArrayList<>();
        this.id = -1;
    }

    /**
     * Determines if a student is already included in this team.
     * @param student A student.
     * @return True if the student is in this team, false otherwise.
     */
    public boolean hasStudent(Student student) {
        for (Student s : this.students) {
            if (s.equals(student)) {
                return true;
            }
        }
        return false;
    }

    public int getStudentCount() {
        return this.students.size();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Student> getStudents() {
        return this.students;
    }

    /**
     * Adds a student to this team.
     * @param student The student to add.
     * @return True if the student could be added, false otherwise.
     */
    public boolean addStudent(Student student) {
        if (!this.hasStudent(student)) {
            this.students.add(student);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if a team is valid, and ready to be added to the Github organization.
     * A team is valid if and only if:
     *      - The student count is equal to the team size.
     *      - Each student is unique.
     *      - Each student's preferred partners match all the others.
     * @param teamSize The preferred size of teams.
     * @return True if the team is valid, and false otherwise.
     */
    public boolean isValid(int teamSize) {
        if (this.getStudentCount() == teamSize) {
            List<Integer> encounteredIds = new ArrayList<>();
            for (Student studentA : this.students) {
                for (Student studentB : this.students) {
                    if (!studentA.equals(studentB) && !studentA.getPreferredPartners().contains(studentB.getNumber())) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Generates a unique name which is intended to be used for the repository name of this team.
     * @param prefix A prefix to further reduce the chances of duplicate names.
     *               It is suggested to use something like "2018_OOP"
     * @return A string comprised of the prefix, team id, and student number of each team member.
     */
    public String generateUniqueName(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append("_team_").append(this.id);
        for (Student s : this.students) {
            sb.append('_').append(s.getNumber());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Team: ");
        sb.append(this.id).append('\n');
        for (Student s : this.students) {
            sb.append('\t').append(s.toString()).append('\n');
        }
        return sb.toString();
    }

    /**
     * Determines if one team is equivalent to another. This is determined by if the two teams are comprised of the same
     * students, in any order.
     * @param o The object to compare to this team.
     * @return True if the teams contain the same students, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Team) {
            Team t = (Team) o;
            if (t.getStudentCount() != this.getStudentCount()) {
                return false;
            }
            for (Student s : this.students) {
                if (!t.hasStudent(s)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
