package grandrue.semantic.executable;

/**
 * Provenance of a resolved policy value. Absence through inapplicability is
 * deliberately distinct from a registered default or explicit selection.
 */
public enum PolicyResolutionStatus {
    NOT_APPLICABLE,
    DEFAULTED,
    EXPLICITLY_SELECTED
}
