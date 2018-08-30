package nl.andrewlalis.model.error;

/**
 * Represents the different levels of severity for errors.
 */
public enum Severity {
    CRITICAL,       // Anything which happens and could have serious side-effects or massive implications system-wide.
    HIGH,           // Errors which should be at the top of priority to sort out after a procedure is executed.
    MEDIUM,         // Medium errors are, as the name implies, of a medium priority.
    LOW,            // Low severity errors are the smallest errors that should be processed soon to avoid issues.
    MINOR           // Minor errors are so insignificant that they may not even require user intervention.
}
