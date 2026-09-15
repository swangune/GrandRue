package mainstreet.publication;

import mainstreet.application.MerchantScope;

import java.time.Instant;

/** Narrow Publication-owned acquisition boundary for P5 request-scoped public material. */
@FunctionalInterface
public interface OpportunityPublicRepresentationProjectionReadPort {

    OpportunityPublicRepresentationProjectionObservation observe(
            MerchantScope merchantScope,
            String opportunityIdentity,
            Instant observedAt
    );
}
