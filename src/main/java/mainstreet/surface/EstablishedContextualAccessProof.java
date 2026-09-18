package mainstreet.surface;

import grandrue.application.MerchantScope;

/**
 * Capability-owned contextual access established for one exact request.
 *
 * <p>The stable access binding is attributable non-secret evidence, not a raw
 * token, credential or guest link. Presence is not current access authority.</p>
 */
public interface EstablishedContextualAccessProof {

    ContextualAccessProofKind kind();

    ObservationRequestBinding requestBinding();

    MerchantScope merchantScope();

    String principalIdentifier();

    String accessBinding();
}
