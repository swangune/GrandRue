package grandrue.commercial;

import grandrue.application.TrustedPlatformExecutionContext;

import java.util.Objects;

/**
 * Trusted admission for the exact approved initial standard Commercial
 * catalogue.
 *
 * <p>MS-PROT-056 v1.9 §§7–8 requires current explicit platform publication
 * authority and exact manifest approval affinity as independent predicates.
 * MS-PROT-056 v1.10 supplies the exact approved
 * {@code standard-commercial-catalogue@1} content.</p>
 *
 * <p>This class does not publish the catalogue and does not establish the
 * platform authorisation source. It composes an injected current-authority
 * decision with exact approved-content validation.</p>
 */
public final class InitialStandardCommercialCataloguePublicationAdmission
        implements CommercialCataloguePublicationAdmission {

    private final CommercialCataloguePublicationAuthorisationAuthority
            publicationAuthorisation;

    public InitialStandardCommercialCataloguePublicationAdmission(
            CommercialCataloguePublicationAuthorisationAuthority
                    publicationAuthorisation
    ) {
        this.publicationAuthorisation = Objects.requireNonNull(
                publicationAuthorisation,
                "publicationAuthorisation"
        );
    }

    @Override
    public void requirePublicationAuthority(
            TrustedPlatformExecutionContext context
    ) {
        Objects.requireNonNull(context, "context");
        if (!publicationAuthorisation.isAuthorised(context)) {
            throw new CataloguePublicationException(
                    CataloguePublicationException.Reason.AUTHORISATION_REJECTED,
                    "Current platform catalogue-publication authority is absent"
            );
        }
    }

    @Override
    public void requireApprovedManifest(CommercialCatalogueManifest manifest) {
        Objects.requireNonNull(manifest, "manifest");
        CommercialCatalogueManifest approved =
                InitialStandardCommercialCatalogue.manifest();
        if (!approved.equals(manifest)) {
            throw new CataloguePublicationException(
                    CataloguePublicationException.Reason.VALIDATION_REJECTED,
                    "Catalogue manifest does not match the exact approved initial manifest"
            );
        }
    }
}
