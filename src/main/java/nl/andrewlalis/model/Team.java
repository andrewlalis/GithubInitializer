package nl.andrewlalis.model;

import nl.andrewlalis.model.database.BaseEntity;
import nl.andrewlalis.util.Pair;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An abstract Team object from which both Teaching Assistant and Student teams can be built. A Team consists of a list
 * of members, and a unique identification number.
 */
@Entity(name = "Team")
@Table(name = "teams")
public abstract class Team extends BaseEntity {

    /**
     * An identification number unique to this team alone.
     */
    @Column(name = "number")
    protected int number;

    /**
     * A list of members of this team.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "team_members",
            joinColumns = {@JoinColumn(name = "team_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")}
    )
    private List<Person> members;

    /**
     * Constructs this team with the given number.
     * @param number The number to assign to this team.
     */
    public Team(int number) {
        this.number = number;
        this.members = new ArrayList<>();
    }

    /**
     * Constructs an empty team with a default id of -1.
     */
    protected Team() {
        this(-1);
    }

    /**
     * @param newId The new number number to assign to this team.
     */
    public void setNumber(int newId) {
        this.number = newId;
    }

    /**
     * @return This team's number number.
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Adds a new person to this team, only if they do not exist in this team yet.
     * @param newMember The new member to add.
     */
    public void addMember(Person newMember) {
        for (Person person : this.members) {
            if (person.equals(newMember)) {
                return;
            }
        }
        this.members.add(newMember);
    }

    /**
     * Removes a person from this team.
     * @param person The person to remove.
     */
    public void removeMember(Person person) {
        this.members.remove(person);
    }

    /**
     * Checks if this team contains the given person.
     * @param person The person to check for.
     * @return True if the person is a member of this team, false otherwise.
     */
    public boolean containsMember(Person person) {
        for (Person p : this.members) {
            if (p.equals(person)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the team to be comprised of only the members given in the array.
     * @param people The people which will make up the members of this team.
     */
    public void setMembers(Person[] people) {
        this.members = new ArrayList<>(Arrays.asList(people));
    }

    /**
     * Gets a list of people in this team.
     * @return A list of people in this team.
     */
    public Person[] getMembers() {
        Person[] people = new Person[this.memberCount()];
        this.members.toArray(people);
        return people;
    }

    /**
     * Gets the number of people in this team.
     * @return The number of people in this team.
     */
    public int memberCount() {
        return this.members.size();
    }

    /**
     * Determines if another team has the same members as this team.
     * @param team The team to compare to this team.
     * @return True if the other team has all the same members as this team.
     */
    public boolean hasSameMembers(Team team) {
        if (this.memberCount() == team.memberCount()) {
            for (Person person : this.members) {
                if (!team.containsMember(person)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if an object is equal to this team. First checks if the other object is a Team, and then if it has the
     * same number and team size. If both of those conditions are met, then it will check that all team members are the
     * same.
     * @param obj The object to check for equality.
     * @return True if the two objects represent the same team, or false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Team) {
            Team team = (Team) obj;
            return team.getNumber() == this.getNumber() && this.hasSameMembers(team);
        }
        return false;
    }

    /**
     * @return A String containing a line for each member in the team.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Team of ").append(this.memberCount()).append(" members:\tID: ").append(this.number).append('\n');
        for (Person person : this.members) {
            sb.append(person.toString()).append('\n');
        }
        return sb.toString();
    }

    @Override
    public String getDetailName() {
        return String.valueOf(this.getNumber());
    }

    @Override
    public List<Pair<String, String>> getDetailPairs() {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("Number", this.getDetailName()));

        for (int i = 0; i < this.memberCount(); i++) {
            pairs.add(new Pair<>("Member " + (i + 1), this.members.get(i).getDetailName()));
        }

        return pairs;
    }
}
