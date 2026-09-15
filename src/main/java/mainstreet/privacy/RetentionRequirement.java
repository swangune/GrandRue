package mainstreet.privacy;

import mainstreet.application.MerchantScope;

import java.util.Objects;
import java.util.Set;

/**
 * Explicit retention-policy definition for one bounded data scope.
 *
 * <p>This value identifies why retention exists and how it terminates; it does
 * not invent a universal duration or execute disposition itself.</p>
 */
public record RetentionRequirement(
        String requirementIdentity,
        MerchantScope merchantScope,
        String affectedDataScopeIdentifier,
        DataUsePurpose purpose,
        String authoritySource,
        String startTriggerIdentifier,
        String endOrReviewConditionIdentifier,
        Set<DataUsePurpose> permittedUseWhileRetained,
        DataDisposition terminalDisposition
) {
    public RetentionRequirement {
        requireIdentifier(requirementIdentity, "requirementIdentity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(affectedDataScopeIdentifier, "affectedDataScopeIdentifier");
        Objects.requireNonNull(purpose, "purpose");
        requireIdentifier(authoritySource, "authoritySource");
        requireIdentifier(startTriggerIdentifier, "startTriggerIdentifier");
        requireIdentifier(endOrReviewConditionIdentifier, "endOrReviewConditionIdentifier");
        permittedUseWhileRetained = Set.copyOf(
                Objects.requireNonNull(
                        permittedUseWhileRetained,
                        "permittedUseWhileRetained"
                )
        );
        Objects.requireNonNull(terminalDisposition, "terminalDisposition");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
