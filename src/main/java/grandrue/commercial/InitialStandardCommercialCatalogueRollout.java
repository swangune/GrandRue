package grandrue.commercial;

import grandrue.application.TrustedPlatformExecutionContext;

import java.util.Objects;
import java.util.Optional;

/**
 * Explicit rollout boundary for the approved initial standard Commercial
 * catalogue.
 *
 * <p>Publication remains a separately authorised Commercial operation. This
 * component does not auto-publish at application startup and does not mutate
 * Merchant Account state. It also supplies the external readiness predicate
 * required before an ordinary new-account delivery path is enabled.</p>
 */
public final class InitialStandardCommercialCatalogueRollout
        implements OrdinaryMerchantAccountPathReadiness {

    private final CommercialCatalogueStore catalogueStore;

    public InitialStandardCommercialCatalogueRollout(
            CommercialCatalogueStore catalogueStore
    ) {
        this.catalogueStore = Objects.requireNonNull(
                catalogueStore,
                "catalogueStore"
        );
    }

    /**
     * Publish the exact accepted first generation with explicit NO_PREDECESSOR.
     * Current publication authority and exact approved-content affinity remain
     * enforced by the store's required publication admission.
     */
    public CommercialCataloguePublication publishInitial(
            String requestIdentifier,
            TrustedPlatformExecutionContext context
    ) {
        Objects.requireNonNull(context, "context");
        return catalogueStore.publish(
                new CommercialCataloguePublicationRequest(
                        requestIdentifier,
                        InitialStandardCommercialCatalogue.manifest(),
                        Optional.empty()
                ),
                context
        );
    }

    @Override
    public void requireReady() {
        requireOrdinaryMerchantAccountPathReady();
    }

    public void requireOrdinaryMerchantAccountPathReady() {
        CommercialCatalogueManifest approved =
                InitialStandardCommercialCatalogue.manifest();
        String catalogueIdentifier =
                approved.revision().catalogueRevisionIdentifier();

        CommercialCataloguePublication publication = catalogueStore
                .exactGeneration(catalogueIdentifier)
                .orElseThrow(() -> new CatalogueResolutionException(
                        CatalogueResolutionException.Reason.NOT_ESTABLISHED,
                        "Initial standard Commercial catalogue is not published"
                ));

        if (publication.predecessor().isPresent()
                || !publication.manifest().equals(approved)) {
            throw new CatalogueResolutionException(
                    CatalogueResolutionException.Reason.INTEGRITY_FAILURE,
                    "Published initial standard Commercial catalogue does not match approved first-generation evidence"
            );
        }
    }
}
