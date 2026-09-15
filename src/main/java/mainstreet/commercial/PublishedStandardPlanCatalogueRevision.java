package mainstreet.commercial;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Read projection of a retained publication's plan snapshots and predecessor.
 * This value is not manifest approval or permission to publish a catalogue.
 * MS-PROT-056 v1.9,
 * designs/MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment.md,
 * §§8–10, 13.
 */
public record PublishedStandardPlanCatalogueRevision(
        StandardPlanCatalogueRevision revision,
        Instant publishedAt,
        Optional<String> predecessorCatalogueRevisionIdentifier
) {
    public PublishedStandardPlanCatalogueRevision {
        Objects.requireNonNull(revision, "revision");
        Objects.requireNonNull(publishedAt, "publishedAt");
        Objects.requireNonNull(predecessorCatalogueRevisionIdentifier,
                "predecessorCatalogueRevisionIdentifier");
        predecessorCatalogueRevisionIdentifier.ifPresent(identifier -> {
            if (identifier.isBlank()) {
                throw new IllegalArgumentException("Predecessor identity must not be blank");
            }
        });
    }
}
