package grandrue.protection;

/**
 * Policy-declared behaviour when authoritative protection state cannot be
 * established. There is deliberately no platform-wide fail-open/fail-closed
 * default.
 */
public enum ProtectionStateFailureBehaviour {
    ADMIT,
    REJECT
}
