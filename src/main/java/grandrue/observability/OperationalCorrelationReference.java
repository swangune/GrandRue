package grandrue.observability;

/**
 * Stable reference kinds that operational evidence may safely correlate without
 * taking ownership of the referenced responsibility.
 */
public enum OperationalCorrelationReference {
    MERCHANT_SCOPE,
    DOMAIN_EVENT_IDENTITY,
    EVENT_REACTION_IDENTITY,
    DURABLE_WORK_INSTRUCTION_IDENTITY,
    WORK_ATTEMPT_IDENTITY
}
