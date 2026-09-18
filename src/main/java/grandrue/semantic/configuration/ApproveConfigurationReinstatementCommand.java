package grandrue.semantic.configuration;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;

/**
 * Command to approve reinstatement of one exact superseded immutable
 * Configuration Revision against one exact current Reinstatement Basis
 * Activation and fresh basis-affined evidence.
 *
 * Authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §§7 and 14 — Reinstatement Approval; Retry and
 * Idempotency.
 */
public record ApproveConfigurationReinstatementCommand(
        String logicalApprovalRequestIdentifier,
        MerchantScope merchantScope,
        String configurationRevisionIdentifier,
        String reinstatementBasisActivationRequestIdentifier,
        String validationEvidenceIdentifier,
        String impactReviewEvidenceIdentifier,
        String approvingPrincipalIdentifier,
        Instant approvedAt
) {

    public ApproveConfigurationReinstatementCommand {
        requireIdentifier(
                logicalApprovalRequestIdentifier,
                "logicalApprovalRequestIdentifier"
        );

        Objects.requireNonNull(
                merchantScope,
                "merchantScope"
        );

        requireIdentifier(
                configurationRevisionIdentifier,
                "configurationRevisionIdentifier"
        );

        requireIdentifier(
                reinstatementBasisActivationRequestIdentifier,
                "reinstatementBasisActivationRequestIdentifier"
        );

        requireIdentifier(
                validationEvidenceIdentifier,
                "validationEvidenceIdentifier"
        );

        requireIdentifier(
                impactReviewEvidenceIdentifier,
                "impactReviewEvidenceIdentifier"
        );

        requireIdentifier(
                approvingPrincipalIdentifier,
                "approvingPrincipalIdentifier"
        );

        Objects.requireNonNull(
                approvedAt,
                "approvedAt"
        );
    }

    private static void requireIdentifier(
            String value,
            String field
    ) {
        Objects.requireNonNull(
                value,
                field
        );

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    field + " must not be blank"
            );
        }
    }
}
