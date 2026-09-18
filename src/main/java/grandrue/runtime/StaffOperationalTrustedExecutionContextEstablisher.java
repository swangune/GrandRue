package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.application.TrustedDeviceApplicationContext;
import grandrue.workforce.MerchantMembershipAuthority;
import grandrue.workforce.MerchantOperationalDeviceAuthority;
import mainstreet.runtime.AuthenticationException;
import mainstreet.runtime.AuthenticationFailureCategory;
import mainstreet.runtime.AuthenticationProvenance;
import mainstreet.runtime.ScopedExecutionPrincipalResolver;
import mainstreet.runtime.SessionCredentialResolver;
import mainstreet.runtime.SessionTrustedExecutionContextEstablisher;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Objects;
import java.util.Optional;

/**
 * Establishes the trusted context required for staff merchant-operational
 * access. Opaque session authentication, current Merchant Membership and
 * current Merchant Operational Device Authorisation remain independent
 * prerequisites.
 *
 * <p>Role/privilege authority is deliberately not resolved here; it remains a
 * downstream MS-PROT-062 Actor Authorisation decision.</p>
 */
public final class StaffOperationalTrustedExecutionContextEstablisher {

    private final SessionTrustedExecutionContextEstablisher sessionEstablisher;
    private final MerchantMembershipAuthority membershipAuthority;
    private final MerchantOperationalDeviceAuthority deviceAuthority;

    public StaffOperationalTrustedExecutionContextEstablisher(
            SessionCredentialResolver sessionCredentialResolver,
            MerchantMembershipAuthority membershipAuthority,
            MerchantOperationalDeviceAuthority deviceAuthority,
            ScopedExecutionPrincipalResolver principalResolver
    ) {
        this.sessionEstablisher = new SessionTrustedExecutionContextEstablisher(
                Objects.requireNonNull(
                        sessionCredentialResolver,
                        "sessionCredentialResolver"
                ),
                Objects.requireNonNull(principalResolver, "principalResolver")
        );
        this.membershipAuthority = Objects.requireNonNull(
                membershipAuthority,
                "membershipAuthority"
        );
        this.deviceAuthority = Objects.requireNonNull(
                deviceAuthority,
                "deviceAuthority"
        );
    }

    public TrustedExecutionContext establish(
            MerchantScope trustedMerchantScope,
            String presentedSessionCredential,
            TrustedDeviceApplicationContext trustedDeviceApplicationContext
    ) {
        Objects.requireNonNull(trustedMerchantScope, "trustedMerchantScope");
        Objects.requireNonNull(
                trustedDeviceApplicationContext,
                "trustedDeviceApplicationContext"
        );

        TrustedExecutionContext sessionContext = sessionEstablisher.establish(
                trustedMerchantScope,
                presentedSessionCredential
        );
        AuthenticationProvenance authentication = sessionContext.authentication()
                .orElseThrow(() -> new AuthenticationException(
                        AuthenticationFailureCategory.PRINCIPAL_ESTABLISHMENT_FAILED,
                        "Staff operational context requires authentication provenance"
                ));

        if (!membershipAuthority.isActive(
                trustedMerchantScope,
                authentication.identityIdentifier()
        )) {
            throw new AuthenticationException(
                    AuthenticationFailureCategory.PRINCIPAL_ESTABLISHMENT_FAILED,
                    "Active merchant membership could not be established"
            );
        }
        if (!deviceAuthority.isActive(
                trustedMerchantScope,
                trustedDeviceApplicationContext
        )) {
            throw new AuthenticationException(
                    AuthenticationFailureCategory.PRINCIPAL_ESTABLISHMENT_FAILED,
                    "Active merchant operational device context could not be established"
            );
        }

        return new TrustedExecutionContext(
                trustedMerchantScope,
                sessionContext.principal(),
                sessionContext.authentication(),
                Optional.of(trustedDeviceApplicationContext)
        );
    }
}
