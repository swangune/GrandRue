package grandrue.privacy;

/** Accepted conceptual data end-of-life outcomes. */
public enum DataDisposition {
    DELETE,
    ANONYMISE,
    REDACT,
    RESTRICT_USE,
    RETAIN_MINIMAL_EVIDENCE,
    RETAIN_WITH_EXPLICIT_JUSTIFICATION
}
