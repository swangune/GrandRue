package mainstreet.commercial;

import mainstreet.application.TrustedPlatformExecutionContext;

/**
 * Required trusted admission boundary; deliberately has no permissive default.
 * Implementations must reject with the appropriate CataloguePublicationException.
 * MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §7 — Publication authority; §8 — Publication operation; §12 — Idempotency, concurrency and failure.
 */
public interface CommercialCataloguePublicationAdmission {
    /** Establish current explicit platform publication authority, including on retries. */
    void requirePublicationAuthority(TrustedPlatformExecutionContext context);

    /**
     * Resolve exact approved-content provenance, target contracts, supporting
     * classifications and accepted allocation conformance before a new publication.
     * Structural manifest construction or a nonblank approval reference is insufficient.
     * This is a validation boundary, not a foreign-owner mutation operation.
     */
    void requireApprovedManifest(CommercialCatalogueManifest manifest);
}
