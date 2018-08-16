package nl.andrewlalis.model;

import org.kohsuke.github.GHTeam;

public class TeachingAssistant extends Person {

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
