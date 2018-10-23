package nl.andrewlalis.model.database;

import org.hibernate.Session;

/**
 * Defines a series of operations done within a transaction.
 */
public interface TransactionContent {

    /**
     * Performs a transaction. Implement this method by updating, inserting, or deleting entities.
     * @param session The session in which operations are being done.
     */
    void doTransaction(Session session);

}
