package mainstreet.semantic.configuration;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Trusted internal request to retain successful exact package evidence.
 * Validation success is supplied by the package itself, never by a boolean.
 *
 * Authority: composite MS-PROT-040 through v1.8.
 */
public record RecordConfigurationValidationEvidenceCommand(
        String validationEvidenceIdentifier,
        String resolvedPackageEvidenceIdentifier,
        ResolvedConfigurationPackage resolvedPackage,
        Instant evidenceProducedAt,
        Optional<String> reinstatementBasisActivationRequestIdentifier
) {

    public RecordConfigurationValidationEvidenceCommand {
        requireIdentifier(
                validationEvidenceIdentifier,
                "Validation evidence identifier"
        );
        requireIdentifier(
                resolvedPackageEvidenceIdentifier,
                "Resolved package evidence identifier"
        );
        Objects.requireNonNull(resolvedPackage, "resolvedPackage");
        Objects.requireNonNull(evidenceProducedAt, "evidenceProducedAt");
        reinstatementBasisActivationRequestIdentifier =
                Objects.requireNonNull(
                        reinstatementBasisActivationRequestIdentifier,
                        "reinstatementBasisActivationRequestIdentifier"
                );
        reinstatementBasisActivationRequestIdentifier.ifPresent(identifier ->
                requireIdentifier(
                        identifier,
                        "Reinstatement Basis Activation identifier"
                )
        );
    }

    public RecordConfigurationValidationEvidenceCommand(
            String validationEvidenceIdentifier,
            String resolvedPackageEvidenceIdentifier,
            ResolvedConfigurationPackage resolvedPackage,
            Instant evidenceProducedAt
    ) {
        this(
                validationEvidenceIdentifier,
                resolvedPackageEvidenceIdentifier,
                resolvedPackage,
                evidenceProducedAt,
                Optional.empty()
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
