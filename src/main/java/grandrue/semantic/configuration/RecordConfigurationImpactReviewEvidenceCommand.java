package grandrue.semantic.configuration;

import java.util.Objects;
import java.util.Optional;

/**
 * Trusted internal request to retain one completed exact impact analysis.
 *
 * Authority: composite MS-PROT-040 through v1.8.
 */
public record RecordConfigurationImpactReviewEvidenceCommand(
        String impactReviewEvidenceIdentifier,
        ConfigurationImpactAnalysisResult impactAnalysisResult,
        Optional<String> reinstatementBasisActivationRequestIdentifier
) {

    public RecordConfigurationImpactReviewEvidenceCommand {
        if (impactReviewEvidenceIdentifier == null
                || impactReviewEvidenceIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Impact-review evidence identifier must not be blank"
            );
        }
        Objects.requireNonNull(
                impactAnalysisResult,
                "impactAnalysisResult"
        );
        reinstatementBasisActivationRequestIdentifier =
                Objects.requireNonNull(
                        reinstatementBasisActivationRequestIdentifier,
                        "reinstatementBasisActivationRequestIdentifier"
                );
        reinstatementBasisActivationRequestIdentifier.ifPresent(identifier -> {
            if (identifier.isBlank()) {
                throw new IllegalArgumentException(
                        "Reinstatement Basis Activation identifier "
                                + "must not be blank"
                );
            }
        });
    }

    public RecordConfigurationImpactReviewEvidenceCommand(
            String impactReviewEvidenceIdentifier,
            ConfigurationImpactAnalysisResult impactAnalysisResult
    ) {
        this(
                impactReviewEvidenceIdentifier,
                impactAnalysisResult,
                Optional.empty()
        );
    }
}
