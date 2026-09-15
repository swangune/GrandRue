package mainstreet.api;

/**
 * Stable client-relevant API problem categories.
 *
 * <p>Owner-qualified safe detail and delivery-protocol status mapping are
 * separate responsibilities.</p>
 */
public enum ApiProblemCategory {
    INVALID_REQUEST,
    UNAUTHENTICATED,
    NOT_AUTHORISED,
    NOT_FOUND_OR_NOT_ACCESSIBLE,
    NOT_APPLICABLE,
    NOT_ENTITLED,
    TRUST_REQUIREMENT_UNSATISFIED,
    OPERATIONALLY_INELIGIBLE,
    PROVIDER_UNAVAILABLE,
    REPRESENTATION_UNAVAILABLE,
    CONFLICT,
    IDEMPOTENCY_CONFLICT,
    RATE_LIMITED,
    OUTCOME_UNCERTAIN,
    TEMPORARILY_UNAVAILABLE,
    INTERNAL_FAILURE
}
