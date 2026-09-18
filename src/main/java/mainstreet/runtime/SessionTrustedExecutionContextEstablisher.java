package mainstreet.runtime;

import grandrue.application.MerchantScope;
import grandrue.runtime.AuthenticationSession;
import grandrue.runtime.ScopedExecutionPrincipalResolver;

import java.util.Objects;
import java.util.Optional;

/**
 * Establishes trusted runtime context from a presented opaque session
 * credential and current scoped-principal resolution.
 *
 * <p>The credential is resolved through the durable server-authoritative
 * Session Record before principal establishment. The Merchant Scope supplied
 * here must already have been resolved through a trusted MS-PROT-031 boundary.
 * This class deliberately does not accept client role/privilege claims and
 * does not cache mutable relationship or authorisation state.</p>
 */
public final class SessionTrustedExecutionContextEstablisher {

    private final SessionCredentialResolver sessionCredentialResolver;
    private final ScopedExecutionPrincipalResolver principalResolver;

    public SessionTrustedExecutionContextEstablisher(
            SessionCredentialResolver sessionCredentialResolver,
            ScopedExecutionPrincipalResolver principalResolver
    ) {
        this.sessionCredentialResolver = Objects.requireNonNull(
                sessionCredentialResolver,
                "sessionCredentialResolver"
        );
        this.principalResolver = Objects.requireNonNull(
                principalResolver,
                "principalResolver"
        );
    }

    public TrustedExecutionContext establish(
            MerchantScope trustedMerchantScope,
            String presentedSessionCredential
    ) {
        Objects.requireNonNull(trustedMerchantScope, "trustedMerchantScope");

        AuthenticationSession session = sessionCredentialResolver.resolve(
                presentedSessionCredential
        );
        ExecutionPrincipal principal = principalResolver.resolve(
                trustedMerchantScope,
                session.identityIdentifier()
        );
        if (principal == null) {
            throw new AuthenticationException(
                    AuthenticationFailureCategory.PRINCIPAL_ESTABLISHMENT_FAILED,
                    "Execution principal could not be established"
            );
        }

        return new TrustedExecutionContext(
                trustedMerchantScope,
                principal,
                Optional.of(new AuthenticationProvenance(
                        session.identifier(),
                        session.identityIdentifier(),
                        session.authenticatedAt()
                ))
        );
    }
}
