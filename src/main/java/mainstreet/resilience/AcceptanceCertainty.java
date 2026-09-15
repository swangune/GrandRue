package mainstreet.resilience;

/** Whether authoritative durable acceptance of one logical command is known. */
public enum AcceptanceCertainty {
    KNOWN_NOT_ACCEPTED,
    DURABLY_ACCEPTED,
    UNCERTAIN
}
