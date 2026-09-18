package mainstreet.commercial;

import grandrue.commercial.FreePlanRevisionAuthority;
import grandrue.application.TrustedPlatformExecutionContext;
import java.time.Instant;
import java.util.Optional;

/**
 * Commercial's atomic publication and authoritative historical-read boundary.
 * MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §8 — Publication operation; §9 — Initial effective start and succession;
 * §10 — Historical resolution; §12 — Idempotency, concurrency and failure.
 */
public interface CommercialCatalogueStore extends FreePlanRevisionAuthority {
    CommercialCataloguePublication publish(CommercialCataloguePublicationRequest request, TrustedPlatformExecutionContext context);
    Optional<CommercialCataloguePublication> exactGeneration(String catalogueIdentifier);
    CommercialCataloguePublication effectiveAt(Instant instant);

    @Override
    default StandardPlanRevision effectiveFreePlanRevisionAt(Instant instant) {
        return effectiveAt(instant).manifest().revision().freePlan();
    }
}
