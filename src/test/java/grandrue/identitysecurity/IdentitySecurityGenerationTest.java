package grandrue.identitysecurity;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentitySecurityGenerationTest {

    private static final Instant ESTABLISHED_AT =
            Instant.parse("2026-08-28T20:00:00Z");

    @Test
    void generation_is_identity_security_evidence_without_merchant_authority() {
        IdentitySecurityGeneration generation = new IdentitySecurityGeneration(
                "identity-42",
                "generation-1",
                1,
                ESTABLISHED_AT,
                Optional.empty()
        );

        assertEquals("identity-42", generation.identityReference());
        assertEquals("generation-1", generation.generationReference());
        assertEquals(1, generation.version());
        assertEquals(ESTABLISHED_AT, generation.establishedAt());
        assertEquals(Optional.empty(), generation.lastRotatedAt());
        assertEquals(5, IdentitySecurityGeneration.class.getRecordComponents().length);
    }

    @Test
    void rotation_command_requires_distinct_expected_and_replacement_generations() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new IdentitySecurityRotationCommand(
                        "identity-42",
                        "generation-1",
                        "generation-1",
                        IdentitySecurityRotationReason.AUTHENTICATOR_RESET,
                        ESTABLISHED_AT.plusSeconds(60),
                        "principal-7",
                        "correlation-8",
                        "audit-9",
                        Optional.of("privileged-browser")
                )
        );
    }

    @Test
    void generation_version_and_rotation_time_are_ordered() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new IdentitySecurityGeneration(
                        "identity-42",
                        "generation-1",
                        0,
                        ESTABLISHED_AT,
                        Optional.empty()
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new IdentitySecurityGeneration(
                        "identity-42",
                        "generation-2",
                        2,
                        ESTABLISHED_AT,
                        Optional.of(ESTABLISHED_AT.minusSeconds(1))
                )
        );
    }
}
