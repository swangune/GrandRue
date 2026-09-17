package grandrue.commercial;

import mainstreet.application.MerchantScope;

import java.time.Instant;

/**
 * Commercial-side port for verifying that supplied trial provenance refers to
 * the Merchant Account's first successfully committed Configuration Revision
 * activation.
 *
 * <p>The implementation must query/resolve Configuration authority; this port
 * does not copy or own Configuration state.</p>
 */
@FunctionalInterface
public interface FirstConfigurationActivationAuthority {

    boolean isFirstCommittedActivation(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier,
            String activationIdentity,
            Instant activatedAt
    );
}
