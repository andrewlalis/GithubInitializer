package nl.andrewlalis.model;

import javax.persistence.Entity;

/**
 * Represents an administrator in the organization, who manages student teams.
 */
@Entity(name = "TeachingAssistant")
public class TeachingAssistant extends Person {

    /**
     * Constructs an empty Teaching Assistant object.
     */
    public TeachingAssistant() {
        super();
    }

    /**
     * Constructs a Teaching Assistant from only a github username.
     * @param githubUsername The person's github username.
     */
    public TeachingAssistant(String githubUsername) {
        super(githubUsername);
    }

    /**
     * Constructs a Teaching Assistant from all the basic information needed, much like its parent, Person.
     *
     * @param number         Either an S- or P-Number without the letter prefix.
     * @param name           The first, middle (if applicable) and last name.
     * @param emailAddress   The email address. (Either university or personal.)
     * @param githubUsername The person's github username.
     */
    public TeachingAssistant(int number, String name, String emailAddress, String githubUsername) {
        super(number, name, emailAddress, githubUsername);
    }
}
