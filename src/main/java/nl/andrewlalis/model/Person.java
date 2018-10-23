package nl.andrewlalis.model;

import nl.andrewlalis.model.database.BaseEntity;
import nl.andrewlalis.ui.view.components.Detailable;
import nl.andrewlalis.util.Pair;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * A generic object that students, teaching assistants, and professors can extend from. This covers all the basic
 * functionality that applies to anyone in the system.
 */
@Entity(name = "Person")
@Table(name = "persons")
public abstract class Person extends BaseEntity implements Detailable {

    /**
     * The unique identification number for this person. (P- or S-Number)
     */
    @Column(name="number", nullable = false)
    protected int number;

    /**
     * The person's first and last name.
     */
    @Column(name="name", nullable = false)
    protected String name;

    /**
     * The person's email address.
     */
    @Column(name="email_address", nullable = false)
    protected String emailAddress;

    /**
     * The person's github username.
     */
    @Column(name="github_username", nullable = false)
    protected String githubUsername;

    /**
     * Constructs an empty default Person.
     */
    public Person() {
        this.number = -1;
        this.name = null;
        this.emailAddress = null;
        this.githubUsername = null;
    }

    /**
     * Constructs a Person from only a github username, which is, in some cases, enough to perform a lot of actions.
     * @param githubUsername The person's github username.
     */
    public Person(String githubUsername) {
        this.number = -1;
        this.name = null;
        this.emailAddress = null;
        this.githubUsername = githubUsername;
    }

    /**
     * Constructs a Person from all the basic information needed.
     * @param number Either an S- or P-Number without the letter prefix.
     * @param name The first, middle (if applicable) and last name.
     * @param emailAddress The email address. (Either university or personal.)
     * @param githubUsername The person's github username.
     */
    public Person(int number, String name, String emailAddress, String githubUsername){
        this.number = number;
        this.name = name;
        this.emailAddress = emailAddress;
        this.githubUsername = githubUsername;
    }

    /**
     * Accessors
     */
    public int getNumber(){
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress(){
        return this.emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGithubUsername(){
        return this.githubUsername;
    }

    public void setGithubUsername(String githubUsername) {
        this.githubUsername = githubUsername;
    }

    /**
     * Determines if two persons are the same. This is defined as:
     * Two persons are equal if at least one of their personal data points is identical. Because each of the data points
     * should be unique to this person alone, if there is any conflict, assume that they are equal.
     * @param o The object to compare to.
     * @return True if the two persons share personal data, or false if all data is unique among the two.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) {
            return false;
        }
        Person p = (Person)o;
        boolean emailSame = (p.getEmailAddress() != null && p.getEmailAddress().equals(this.getEmailAddress()));
        boolean githubSame = (p.getGithubUsername() != null && p.getGithubUsername().equals(this.getGithubUsername()));
        boolean nameSame = (p.getName() != null && p.getName().equalsIgnoreCase(this.getName()));
        return p.getNumber() == this.getNumber()
                || emailSame
                || githubSame
                || nameSame;
    }

    /**
     * Represents the person as a basic comma-separated string object.
     * @return A comma-separated String object.
     */
    @Override
    public String toString() {
        return this.getName() + ", " + this.getNumber() + ", " + this.getEmailAddress() + ", " + this.getGithubUsername();
    }

    @Override
    public String getDetailName() {
        return this.getName() + ", " + this.getNumber();
    }

    @Override
    public String getDetailDescription() {
        return null;
    }

    @Override
    public List<Pair<String, String>> getDetailPairs() {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("Name", this.getName()));
        pairs.add(new Pair<>("Number", String.valueOf(this.getNumber())));
        pairs.add(new Pair<>("Email Address", this.getEmailAddress()));
        pairs.add(new Pair<>("Github Username", this.getGithubUsername()));
        return pairs;
    }
}
