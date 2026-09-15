package mainstreet.credential;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CredentialGenerationPolicyTest {

    private static final Instant T0 = Instant.parse("2026-08-24T15:00:00Z");

    @Test
    void usable_generation_may_be_bounded_to_explicit_technical_uses() {
        CredentialGenerationPolicy policy = new CredentialGenerationPolicy(
                "policy-1",
                "generation-1",
                1,
                CredentialGenerationState.USABLE,
                Set.of(
                        CredentialTechnicalUse.NEW_EXECUTION,
                        CredentialTechnicalUse.VERIFICATION
                ),
                T0,
                Optional.empty()
        );

        assertTrue(policy.permits(CredentialTechnicalUse.NEW_EXECUTION));
        assertTrue(policy.permits(CredentialTechnicalUse.VERIFICATION));
        assertFalse(policy.permits(CredentialTechnicalUse.EXISTING_OBLIGATION));
    }

    @Test
    void retiring_generation_cannot_authorise_new_execution() {
        assertThrows(IllegalArgumentException.class, () -> new CredentialGenerationPolicy(
                "policy-1",
                "generation-1",
                2,
                CredentialGenerationState.RETIRING,
                Set.of(CredentialTechnicalUse.NEW_EXECUTION),
                T0,
                Optional.empty()
        ));
    }

    @Test
    void terminal_generation_cannot_authorise_any_technical_use() {
        for (CredentialGenerationState state : Set.of(
                CredentialGenerationState.EXPIRED,
                CredentialGenerationState.REVOKED,
                CredentialGenerationState.COMPROMISED
        )) {
            assertThrows(IllegalArgumentException.class, () -> new CredentialGenerationPolicy(
                    "policy-" + state.name(),
                    "generation-1",
                    2,
                    state,
                    Set.of(CredentialTechnicalUse.VERIFICATION),
                    T0,
                    Optional.empty()
            ));
        }
    }

    @Test
    void terminal_generation_may_preserve_security_evidence_without_use_permission() {
        CredentialGenerationPolicy revoked = new CredentialGenerationPolicy(
                "policy-revoked",
                "generation-1",
                2,
                CredentialGenerationState.REVOKED,
                Set.of(),
                T0,
                Optional.of("audit-credential-revocation-1")
        );

        assertFalse(revoked.permits(CredentialTechnicalUse.NEW_EXECUTION));
        assertFalse(revoked.permits(CredentialTechnicalUse.EXISTING_OBLIGATION));
        assertFalse(revoked.permits(CredentialTechnicalUse.VERIFICATION));
    }
}
