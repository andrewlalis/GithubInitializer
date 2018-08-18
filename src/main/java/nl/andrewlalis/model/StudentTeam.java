package nl.andrewlalis.model;

import java.util.Arrays;

/**
 * Represents one or more students' collective information.
 */
public class StudentTeam extends Team{

    public StudentTeam() {
        super(-1);
    }

    /**
     * Gets a list of students, casted from the original Person[].
     * @return An array of Students.
     */
    public Student[] getStudents() {
        return Arrays.copyOf(this.getMembers(), this.memberCount(), Student[].class);
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
        if (this.memberCount() == teamSize) {
            for (Student studentA : this.getStudents()) {
                for (Student studentB : this.getStudents()) {
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
        for (Student s : (Student[]) this.getMembers()) {
            sb.append('_').append(s.getNumber());
        }
        return sb.toString();
    }
}
