package grandrue.infrastructure.security.webauthn;

import grandrue.runtime.EstablishedHumanSession;
import grandrue.runtime.HumanSessionEstablishmentService;
import grandrue.runtime.IdentitySecurityGenerationAuthority;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialUserEntity;
import org.springframework.security.web.webauthn.authentication.WebAuthnAuthentication;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

/**
 * ADR-015 adapter from a successful Spring WebAuthn authentication to the
 * Main Street-owned opaque Session authority.
 *
 * <p>This adapter deliberately does not consume Spring GrantedAuthority,
 * SecurityContext, HttpSession or Merchant Scope. Authentication establishes
 * Identity evidence only; Main Street creates its own fresh Session Credential
 * and resolves merchant/business authority later from current sources.</p>
 */
public final class SpringWebAuthnSessionBridge {

    private final HumanSessionEstablishmentService sessionEstablishmentService;
    private final WebAuthnIdentityReferenceAuthority identityReferenceAuthority;
    private final IdentitySecurityGenerationAuthority securityGenerationAuthority;
    private final WebAuthnSessionEstablishmentPolicy sessionPolicy;
    private final Clock clock;

    public SpringWebAuthnSessionBridge(
            HumanSessionEstablishmentService sessionEstablishmentService,
            WebAuthnIdentityReferenceAuthority identityReferenceAuthority,
            IdentitySecurityGenerationAuthority securityGenerationAuthority,
            WebAuthnSessionEstablishmentPolicy sessionPolicy,
            Clock clock
    ) {
        this.sessionEstablishmentService = Objects.requireNonNull(
                sessionEstablishmentService,
                "sessionEstablishmentService"
        );
        this.identityReferenceAuthority = Objects.requireNonNull(
                identityReferenceAuthority,
                "identityReferenceAuthority"
        );
        this.securityGenerationAuthority = Objects.requireNonNull(
                securityGenerationAuthority,
                "securityGenerationAuthority"
        );
        this.sessionPolicy = Objects.requireNonNull(sessionPolicy, "sessionPolicy");
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public EstablishedHumanSession establish(WebAuthnAuthentication authentication) {
        Objects.requireNonNull(authentication, "authentication");
        if (!authentication.isAuthenticated()) {
            throw new UnverifiedWebAuthnAuthenticationException();
        }

        PublicKeyCredentialUserEntity verifiedPrincipal = authentication.getPrincipal();
        if (verifiedPrincipal == null) {
            throw new UnverifiedWebAuthnAuthenticationException();
        }

        String identityReference = requireResolved(
                identityReferenceAuthority.identityReference(verifiedPrincipal),
                "WebAuthn Identity mapping"
        );
        String securityGenerationReference = requireResolved(
                securityGenerationAuthority.currentSecurityGenerationReference(
                        identityReference
                ),
                "Identity security generation"
        );

        Instant authenticatedAt = clock.instant();

        return sessionEstablishmentService.establish(
                identityReference,
                authenticatedAt,
                sessionPolicy.authenticationAssuranceReference(),
                sessionPolicy.authenticationMethodReference(),
                authenticatedAt.plus(sessionPolicy.absoluteLifetime()),
                securityGenerationReference
        );
    }

    private static String requireResolved(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(label + " must resolve to a non-blank reference");
        }
        return value;
    }
}
