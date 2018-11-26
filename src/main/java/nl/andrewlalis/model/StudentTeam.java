package nl.andrewlalis.model;

import nl.andrewlalis.util.Pair;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Arrays;
import java.util.List;

/**
 * Represents one or more students' collective information.
 */
@Entity(name = "StudentTeam")
public class StudentTeam extends Team {

    /**
     * The repository belonging to this team.
     */
    @Column(name = "repository_name", unique = true)
    private String repositoryName;

    /**
     * The TATeam responsible for this student team.
     */
    @ManyToOne
    private TATeam taTeam;

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
     *      - Each student's preferred partners match all the others, or a student has no preferences.
     * @param teamSize The preferred size of teams.
     * @return True if the team is valid, and false otherwise.
     */
    public boolean isValid(int teamSize) {
        if (this.memberCount() == teamSize) {
            for (Student studentA : this.getStudents()) {
                // If the student doesn't have an preferred partners, then assume that this is valid.
                if (!studentA.getPreferredPartners().isEmpty()) {
                    for (Student studentB : this.getStudents()) {
                        if (!studentA.equals(studentB) && !studentA.getPreferredPartners().contains(studentB)) {
                            return false;
                        }
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
     * @return A string comprised of the prefix, team number, and student number of each team member.
     */
    public String generateUniqueName(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        sb.append("_team_").append(this.number);
        for (Student s : this.getStudents()) {
            sb.append('_').append(s.getNumber());
        }
        return sb.toString();
    }

    /**
     * Generates a description for the repository, based on the students' names and group number.
     * @return A description for the students' repository.
     */
    public String generateRepoDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("Group ").append(this.number).append(": ");
        for (int i = 0; i < this.memberCount(); i++) {
            sb.append(this.getStudents()[i].getName());
            if (i != this.memberCount()-1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public TATeam getTaTeam() {
        return this.taTeam;
    }

    public void setTaTeam(TATeam team) {
        this.taTeam = team;
    }

    @Override
    public String getDetailName() {
        return this.generateRepoDescription();
    }

    @Override
    public List<Pair<String, String>> getDetailPairs() {
        List<Pair<String, String>> pairs = super.getDetailPairs();
        pairs.add(new Pair<>("Repository Name", this.getRepositoryName()));
        String taTeamName = "None";
        if (this.getTaTeam() != null) {
            taTeamName = this.getTaTeam().getDetailName();
        }
        pairs.add(new Pair<>("TA Team", taTeamName));

        return pairs;
    }
}
