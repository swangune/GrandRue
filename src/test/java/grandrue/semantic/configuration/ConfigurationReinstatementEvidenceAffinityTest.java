package grandrue.semantic.configuration;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Semantic boundary tests for optional exact reinstatement evidence affinity.
 *
 * <p>Authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §§5–6 and 16 — Reinstatement Decision Affinity,
 * Fresh Evidence Rule, and Persistence Semantics.</p>
 */
class ConfigurationReinstatementEvidenceAffinityTest {

    private static final Instant NOW =
            Instant.parse("2026-09-12T15:00:00Z");

    @Test
    void ordinary_evidence_preserves_absent_reinstatement_basis() {
        ConfigurationValidationEvidence validation =
                validation(Optional.empty());

        ConfigurationImpactReviewEvidence impact =
                impact(Optional.empty());

        assertTrue(
                validation.reinstatementBasisActivationRequestIdentifier()
                        .isEmpty()
        );

        assertTrue(
                impact.reinstatementBasisActivationRequestIdentifier()
                        .isEmpty()
        );
    }

    @Test
    void validation_evidence_rejects_blank_reinstatement_basis() {
        assertThrows(
                IllegalArgumentException.class,
                () -> validation(Optional.of(" "))
        );
    }

    @Test
    void impact_evidence_rejects_blank_reinstatement_basis() {
        assertThrows(
                IllegalArgumentException.class,
                () -> impact(Optional.of(" "))
        );
    }

    private static ConfigurationValidationEvidence validation(
            Optional<String> reinstatementBasis
    ) {
        return new ConfigurationValidationEvidence(
                "validation-1",
                "merchant-a",
                "configuration-1",
                "semantic-release-1",
                "package-1",
                ConfigurationValidationOutcome.SUCCEEDED,
                "compiler-1",
                NOW.minusSeconds(60),
                NOW,
                reinstatementBasis
        );
    }

    private static ConfigurationImpactReviewEvidence impact(
            Optional<String> reinstatementBasis
    ) {
        return new ConfigurationImpactReviewEvidence(
                "impact-1",
                "merchant-a",
                "configuration-1",
                "semantic-release-1",
                "validation-1",
                "package-1",
                List.of("No material business-facing change"),
                List.of(),
                NOW,
                reinstatementBasis
        );
    }
}
