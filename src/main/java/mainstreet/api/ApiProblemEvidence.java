package mainstreet.api;

/**
 * Marker for accepted owner evidence presented to an API problem mapping.
 *
 * <p>Implementing this interface does not itself make evidence trusted or
 * externally safe. The exact registered owner mapping remains authoritative.
 * Java exceptions are explicitly ineligible.</p>
 */
public interface ApiProblemEvidence {
}
