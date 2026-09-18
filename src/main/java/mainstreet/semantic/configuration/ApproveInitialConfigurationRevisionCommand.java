package mainstreet.semantic.configuration;

import grandrue.application.MerchantScope;
import java.time.Instant;
import java.util.Objects;

/** Exact current-Controller intent to approve one ordinary first revision. */
public record ApproveInitialConfigurationRevisionCommand(
        String logicalApprovalRequestIdentifier,
        MerchantScope merchantScope,
        String configurationRevisionIdentifier,
        String validationEvidenceIdentifier,
        String impactReviewEvidenceIdentifier,
        String approvingPrincipalIdentifier,
        Instant approvedAt
) {
    public ApproveInitialConfigurationRevisionCommand {
        require(logicalApprovalRequestIdentifier, "Approval request identifier");
        Objects.requireNonNull(merchantScope, "merchantScope");
        require(configurationRevisionIdentifier, "Configuration revision identifier");
        require(validationEvidenceIdentifier, "Validation evidence identifier");
        require(impactReviewEvidenceIdentifier, "Impact-review evidence identifier");
        require(approvingPrincipalIdentifier, "Approving principal identifier");
        Objects.requireNonNull(approvedAt, "approvedAt");
    }
    private static void require(String value, String label) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(label + " must not be blank");
    }
}
