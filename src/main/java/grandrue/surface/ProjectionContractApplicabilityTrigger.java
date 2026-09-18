package grandrue.surface;

/** Closed representation of MS-PROT-027 v1.3 applicability triggers A-G. */
public enum ProjectionContractApplicabilityTrigger {
    PERSISTED_OR_CACHED,
    ASYNCHRONOUS_UPDATE,
    REGISTERED_DEPENDENCY,
    SOURCE_OUTAGE_SERVING,
    DELIBERATE_KNOWN_LAG,
    DIVERGENT_MULTI_SOURCE,
    REVOCATION_SENSITIVE
}
