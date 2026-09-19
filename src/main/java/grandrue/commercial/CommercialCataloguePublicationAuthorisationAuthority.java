package grandrue.commercial;

import grandrue.application.TrustedPlatformExecutionContext;

/**
 * Current platform authorisation predicate for standard Commercial catalogue
 * publication.
 *
 * <p>This is an implementation port for the explicit platform-authorisation
 * requirement already owned by MS-PROT-056 v1.9 §7. Trusted platform
 * attribution alone is not permission.</p>
 */
@FunctionalInterface
public interface CommercialCataloguePublicationAuthorisationAuthority {

    /**
     * Return whether this exact trusted platform execution context is currently
     * authorised to publish the standard Commercial catalogue.
     */
    boolean isAuthorised(TrustedPlatformExecutionContext context);
}
