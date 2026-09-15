package mainstreet.semantic.configuration;

import java.time.Instant;
import java.util.Objects;

/**
 * Append-only exact approval fact for one immutable non-initial
 * Configuration Revision.
 *
 * Authority: approved MS-PROT-040 v1.6.
 */
public record NonInitialConfigurationRevisionApproval(
        String logicalApprovalRequestIdentifier,
        String merchantIdentifier,
        String configurationRevisionIdentifier,
        String semanticRegistryReleaseIdentifier,
        String validationEvidenceIdentifier,
        String impactReviewEvidenceIdentifier,
        String resolvedPackageEvidenceIdentifier,
        String approvingPrincipalIdentifier,
        String controllerRelationshipIdentifier,
        Instant approvedAt
) {

    public NonInitialConfigurationRevisionApproval {
        require(logicalApprovalRequestIdentifier);
        require(merchantIdentifier);
        require(configurationRevisionIdentifier);
        require(semanticRegistryReleaseIdentifier);
        require(validationEvidenceIdentifier);
        require(impactReviewEvidenceIdentifier);
        require(resolvedPackageEvidenceIdentifier);
        require(approvingPrincipalIdentifier);
        require(controllerRelationshipIdentifier);
        Objects.requireNonNull(approvedAt, "approvedAt");
    }

    private static void require(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Approval identity must not be blank"
            );
        }
    }
}