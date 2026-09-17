package grandrue.background;

/** Result classifications accepted by MS-PROT-065 for one work attempt. */
public enum BackgroundWorkResultClassification {
    RETRY_SAFE,
    RECONCILIATION_REQUIRED,
    TERMINAL_FAILURE,
    MANUAL_INTERVENTION_REQUIRED,
    NO_LONGER_APPLICABLE,
    SUCCESS
}
