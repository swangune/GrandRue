package mainstreet.semantic.configuration;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Trusted application request to resolve, validate, assess and retain
 * business-facing impact evidence for one exact Configuration Revision.
 *
 * Authority:
 * designs/MS-PROT-040 v1.1 —
 * Configuration Revision, Resolved Package & Atomic Activation Amendment,
 * §§3–5;
 * designs/MS-PROT-040 v1.3 —
 * Configuration Validation, Impact Review & Approval Evidence Amendment,
 * §§9–11;
 * designs/MS-PROT-040 v1.8 —
 * Configuration Reinstatement Decision Affinity Amendment, §§5–6.
 */
public record ProduceConfigurationImpactReviewCommand(
        MerchantScope merchantScope,
        String configurationRevisionIdentifier,
        String compilerIdentifier,
        Instant packageGeneratedAt,
        String validationEvidenceIdentifier,
        String resolvedPackageEvidenceIdentifier,
        Instant validationEvidenceProducedAt,
        String impactReviewEvidenceIdentifier,
        Instant impactAnalysisCompletedAt,
        Optional<String> reinstatementBasisActivationRequestIdentifier
) {

    public ProduceConfigurationImpactReviewCommand {
        Objects.requireNonNull(merchantScope, "merchantScope");

        requireIdentifier(
                configurationRevisionIdentifier,
                "Configuration Revision identifier"
        );
        requireIdentifier(
                compilerIdentifier,
                "Compiler identifier"
        );
        Objects.requireNonNull(
                packageGeneratedAt,
                "packageGeneratedAt"
        );

        requireIdentifier(
                validationEvidenceIdentifier,
                "Validation evidence identifier"
        );
        requireIdentifier(
                resolvedPackageEvidenceIdentifier,
                "Resolved package evidence identifier"
        );
        Objects.requireNonNull(
                validationEvidenceProducedAt,
                "validationEvidenceProducedAt"
        );

        requireIdentifier(
                impactReviewEvidenceIdentifier,
                "Impact-review evidence identifier"
        );
        Objects.requireNonNull(
                impactAnalysisCompletedAt,
                "impactAnalysisCompletedAt"
        );

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

    public ProduceConfigurationImpactReviewCommand(
            MerchantScope merchantScope,
            String configurationRevisionIdentifier,
            String compilerIdentifier,
            Instant packageGeneratedAt,
            String validationEvidenceIdentifier,
            String resolvedPackageEvidenceIdentifier,
            Instant validationEvidenceProducedAt,
            String impactReviewEvidenceIdentifier,
            Instant impactAnalysisCompletedAt
    ) {
        this(
                merchantScope,
                configurationRevisionIdentifier,
                compilerIdentifier,
                packageGeneratedAt,
                validationEvidenceIdentifier,
                resolvedPackageEvidenceIdentifier,
                validationEvidenceProducedAt,
                impactReviewEvidenceIdentifier,
                impactAnalysisCompletedAt,
                Optional.empty()
        );
    }

    private static void requireIdentifier(
            String value,
            String label
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    label + " must not be blank"
            );
        }
    }
}
