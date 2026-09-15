package mainstreet.infrastructure.security.webauthn;

import org.junit.jupiter.api.Test;
import org.springframework.core.SpringVersion;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.web.webauthn.api.UserVerificationRequirement;
import org.springframework.security.web.webauthn.management.ImmutablePublicKeyCredentialRequestOptionsRequest;
import org.springframework.security.web.webauthn.management.MapPublicKeyCredentialUserEntityRepository;
import org.springframework.security.web.webauthn.management.MapUserCredentialRepository;
import org.springframework.security.web.webauthn.management.Webauthn4JRelyingPartyOperations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegedWebAuthnRelyingPartyPolicyTest {

    @Test
    void approved_spring_platform_security_baseline_is_active() {
        assertEquals("7.0.9", SpringVersion.getVersion());
        assertEquals("7.1.1", SpringSecurityCoreVersion.getVersion());
    }

    @Test
    void production_privileged_authentication_requires_explicit_https_origins() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new PrivilegedWebAuthnRelyingPartyPolicy(
                        "mainstreet.app",
                        "Main Street",
                        Set.of("http://auth.mainstreet.app")
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new PrivilegedWebAuthnRelyingPartyPolicy(
                        "mainstreet.app",
                        "Main Street",
                        Set.of()
                )
        );
    }

    @Test
    void only_explicit_privileged_origins_are_admitted() {
        PrivilegedWebAuthnRelyingPartyPolicy policy = new PrivilegedWebAuthnRelyingPartyPolicy(
                "mainstreet.app",
                "Main Street",
                Set.of("https://auth.mainstreet.app")
        );

        assertEquals(Set.of("https://auth.mainstreet.app"), policy.allowedOrigins());
        assertFalse(policy.allowedOrigins().contains("https://merchant.example"));
    }

    @Test
    void passwordless_controller_authentication_requires_user_verification() {
        PrivilegedWebAuthnRelyingPartyPolicy policy = new PrivilegedWebAuthnRelyingPartyPolicy(
                "mainstreet.app",
                "Main Street",
                Set.of("https://auth.mainstreet.app")
        );

        assertEquals(UserVerificationRequirement.REQUIRED, policy.userVerificationRequirement());
        assertEquals(
                UserVerificationRequirement.REQUIRED,
                policy.registrationAuthenticatorSelection().getUserVerification()
        );
    }

    @Test
    void spring_relying_party_operations_receive_exact_rp_and_required_verification_policy() {
        PrivilegedWebAuthnRelyingPartyPolicy policy = new PrivilegedWebAuthnRelyingPartyPolicy(
                "mainstreet.app",
                "Main Street",
                Set.of("https://auth.mainstreet.app")
        );

        Webauthn4JRelyingPartyOperations operations =
                SpringWebAuthnRelyingPartyOperationsFactory.create(
                        new MapPublicKeyCredentialUserEntityRepository(),
                        new MapUserCredentialRepository(),
                        policy
                );

        var requestOptions = operations.createCredentialRequestOptions(
                new ImmutablePublicKeyCredentialRequestOptionsRequest(null)
        );

        assertEquals("mainstreet.app", requestOptions.getRpId());
        assertEquals(UserVerificationRequirement.REQUIRED, requestOptions.getUserVerification());
    }

    @Test
    void origin_values_are_defensively_copied_and_are_not_expandable_after_configuration() {
        Set<String> configuredOrigins = new java.util.HashSet<>();
        configuredOrigins.add("https://auth.mainstreet.app");

        PrivilegedWebAuthnRelyingPartyPolicy policy = new PrivilegedWebAuthnRelyingPartyPolicy(
                "mainstreet.app",
                "Main Street",
                configuredOrigins
        );
        configuredOrigins.add("https://merchant.example");

        assertEquals(Set.of("https://auth.mainstreet.app"), policy.allowedOrigins());
        assertThrows(
                UnsupportedOperationException.class,
                () -> policy.allowedOrigins().add("https://merchant.example")
        );
    }
}
