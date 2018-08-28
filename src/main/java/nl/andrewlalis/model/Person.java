package nl.andrewlalis.model;

/**
 * A generic object that students, teaching assistants, and professors can extend from. This covers all the basic
 * functionality that applies to anyone in the system.
 */
public abstract class Person  {

    /**
     * The unique identification number for this person. (P- or S-Number)
     */
    protected int number;

    /**
     * The person's first and last name.
     */
    protected String name;

    /**
     * The person's email address.
     */
    protected String emailAddress;

    /**
     * The person's github username.
     */
    protected String githubUsername;

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

    public String getName(){
        return this.name;
    }

    public String getEmailAddress(){
        return this.emailAddress;
    }

    public String getGithubUsername(){
        return this.githubUsername;
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
        return p.getNumber() == this.getNumber()
                || p.getEmailAddress().equals(this.getEmailAddress())
                || p.getGithubUsername().equals(this.getGithubUsername())
                || p.getName().equalsIgnoreCase(this.getName());
    }

    /**
     * Represents the person as a basic comma-separated string object.
     * @return A comma-separated String object.
     */
    @Override
    public String toString() {
        return this.getName() + ", " + this.getNumber() + ", " + this.getEmailAddress() + ", " + this.getGithubUsername();
    }
}
