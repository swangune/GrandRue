package grandrue.infrastructure.security.webauthn;

import org.springframework.security.web.webauthn.api.PublicKeyCredentialRpEntity;
import org.springframework.security.web.webauthn.management.PublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.security.web.webauthn.management.Webauthn4JRelyingPartyOperations;

import java.util.Objects;

/**
 * Constructs Spring WebAuthn relying-party operations under ADR-015 policy.
 *
 * <p>This factory configures cryptographic ceremony policy only. It does not
 * establish Spring session, SecurityContext, Merchant Scope or business authority.</p>
 */
public final class SpringWebAuthnRelyingPartyOperationsFactory {

    private SpringWebAuthnRelyingPartyOperationsFactory() {
    }

    public static Webauthn4JRelyingPartyOperations create(
            PublicKeyCredentialUserEntityRepository userEntities,
            UserCredentialRepository userCredentials,
            PrivilegedWebAuthnRelyingPartyPolicy policy
    ) {
        Objects.requireNonNull(userEntities, "userEntities");
        Objects.requireNonNull(userCredentials, "userCredentials");
        Objects.requireNonNull(policy, "policy");

        PublicKeyCredentialRpEntity relyingParty = PublicKeyCredentialRpEntity.builder()
                .id(policy.relyingPartyIdentifier())
                .name(policy.relyingPartyDisplayName())
                .build();

        Webauthn4JRelyingPartyOperations operations = new Webauthn4JRelyingPartyOperations(
                userEntities,
                userCredentials,
                relyingParty,
                policy.allowedOrigins()
        );
        operations.setCustomizeRequestOptions(builder -> builder
                .rpId(policy.relyingPartyIdentifier())
                .userVerification(policy.userVerificationRequirement()));
        operations.setCustomizeCreationOptions(builder -> builder
                .authenticatorSelection(policy.registrationAuthenticatorSelection()));
        return operations;
    }
}
