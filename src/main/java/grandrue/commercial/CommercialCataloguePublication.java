package grandrue.commercial;

import mainstreet.commercial.CommercialAccessTarget;
import mainstreet.commercial.CommercialCatalogueManifest;

import grandrue.commercial.PublishedStandardPlanCatalogueRevision;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Retained committed evidence under MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment, §§8–9, 12–13. */
public record CommercialCataloguePublication(
        String requestIdentifier,
        CommercialCatalogueManifest manifest,
        Optional<String> predecessor,
        String publishingPrincipalIdentifier,
        Instant publishedAt
) {
    public CommercialCataloguePublication {
        CommercialAccessTarget.requireText(requestIdentifier, "requestIdentifier");
        Objects.requireNonNull(manifest, "manifest");
        Objects.requireNonNull(predecessor, "predecessor");
        predecessor.ifPresent(value -> CommercialAccessTarget.requireText(value, "predecessor"));
        CommercialAccessTarget.requireText(publishingPrincipalIdentifier, "publishingPrincipalIdentifier");
        Objects.requireNonNull(publishedAt, "publishedAt");
    }

    public PublishedStandardPlanCatalogueRevision planProjection() {
        return new PublishedStandardPlanCatalogueRevision(manifest.revision(), publishedAt, predecessor);
    }
}
