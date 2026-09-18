package grandrue.workforce;

import mainstreet.application.MerchantScope;
import grandrue.application.TrustedDeviceApplicationContext;
import mainstreet.runtime.ExecutionPrincipal;

import java.time.Instant;
import java.util.Optional;

/**
 * Commit/query port for Merchant Operational Device Authorisation.
 *
 * <p>The command methods must revalidate current Merchant Controller authority
 * at the authoritative commit boundary. Implementations must fail closed when
 * that authority cannot be proven.</p>
 */
public interface MerchantOperationalDeviceAuthorisationStore
        extends MerchantOperationalDeviceAuthority {

    MerchantOperationalDeviceAuthorisation establishIfCurrentController(
            String logicalRequestIdentity,
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            TrustedDeviceApplicationContext deviceContext,
            String authorisationIdentifier,
            Instant authorisedAt
    );

    boolean revokeIfCurrentController(
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            String authorisationIdentifier,
            Instant revokedAt
    );

    Optional<MerchantOperationalDeviceAuthorisation> find(
            String authorisationIdentifier
    );

    @Override
    boolean isActive(
            MerchantScope merchantScope,
            TrustedDeviceApplicationContext deviceContext
    );
}
