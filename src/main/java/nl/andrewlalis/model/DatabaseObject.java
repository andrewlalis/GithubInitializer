package nl.andrewlalis.model;

public abstract class DatabaseObject {

    public abstract DatabaseObject retrieve();

    public abstract boolean store();

}
