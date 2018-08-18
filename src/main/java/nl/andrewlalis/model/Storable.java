package nl.andrewlalis.model;

import java.sql.Connection;

/**
 * Defines objects which may be stored in the database, and requires that they implement methods for both storage and
 * retrieval of the objects.
 */
public interface Storable {

    /**
     * Stores the object in the database.
     * @param connection The connection to the database which can be used for preparation of and execution of queries.
     * @return True if the object is successfully stored, false if an error occurred.
     */
    boolean store(Connection connection);

}
