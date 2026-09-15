package mainstreet.api;

/**
 * Transport/execution outcome classes for an API command.
 *
 * <p>These classifications report the command boundary defined by its owning
 * Operation Contract. They are not business lifecycle states.</p>
 */
public enum ApiCommandOutcomeClass {
    COMPLETED,
    ACCEPTED_PENDING,
    REJECTED,
    OUTCOME_UNCERTAIN
}
