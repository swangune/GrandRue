package grandrue.commercial;

import mainstreet.commercial.CommercialAccessTarget;
import grandrue.commercial.CommercialCatalogueManifest;

import java.util.Objects;
import java.util.Optional;

/** MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment, §8 — Publication operation. */
public record CommercialCataloguePublicationRequest(
        String requestIdentifier,
        CommercialCatalogueManifest manifest,
        Optional<String> expectedPredecessor
) {
    public CommercialCataloguePublicationRequest {
        CommercialAccessTarget.requireText(requestIdentifier, "requestIdentifier");
        Objects.requireNonNull(manifest, "manifest");
        Objects.requireNonNull(expectedPredecessor, "expectedPredecessor");
        expectedPredecessor.ifPresent(value -> CommercialAccessTarget.requireText(value, "expectedPredecessor"));
    }
}
