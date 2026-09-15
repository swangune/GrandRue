package mainstreet.semantic.configuration;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable Configuration-owned approval for one exact reinstatement
 * decision.
 *
 * <p>The Reinstatement Basis Activation is causal authority. Revision
 * identity, Controller Relationship identity and timestamp recency are not
 * substitutes for this affinity.</p>
 *
 * Authority: MS-PROT-040 v1.8,
 * {@code designs/MS-PROT-040 v1.8 — Configuration Reinstatement Decision
 * Affinity Amendment.md}, §7 — Reinstatement Approval.
 */
public record ConfigurationReinstatementApproval(
        String logicalApprovalRequestIdentifier,
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String reinstatementBasisActivationRequestIdentifier,
        String validationEvidenceIdentifier,
        String impactReviewEvidenceIdentifier,
        String resolvedPackageEvidenceIdentifier,
        String approvingPrincipalIdentifier,
        String controllerRelationshipIdentifier,
        Instant approvedAt
) {

    public ConfigurationReinstatementApproval {
        requireIdentifier(
                logicalApprovalRequestIdentifier,
                "logicalApprovalRequestIdentifier"
        );

        requireIdentifier(
                merchantIdentifier,
                "merchantIdentifier"
        );

        requireIdentifier(
                configurationRevisionIdentifier,
                "configurationRevisionIdentifier"
        );

        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "semanticRegistryReleaseIdentifier"
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
                resolvedPackageEvidenceIdentifier,
                "resolvedPackageEvidenceIdentifier"
        );

        requireIdentifier(
                approvingPrincipalIdentifier,
                "approvingPrincipalIdentifier"
        );

        requireIdentifier(
                controllerRelationshipIdentifier,
                "controllerRelationshipIdentifier"
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
