package mainstreet.surface;

import mainstreet.application.MerchantScope;

import java.util.Objects;

/**
 * Exact resolved-context request presented to one capability-owned Public
 * Interaction participation source. The request carries no Exposure decision,
 * availability state or execution authority.
 */
public record PublicInteractionParticipationRequest(
        MerchantScope merchantScope,
        String semanticRegistryReleaseIdentifier,
        String resolvedModelIdentifier,
        long resolvedModelVersion,
        StaticSurfaceContribution contribution
) {
    public PublicInteractionParticipationRequest {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        requireIdentifier(resolvedModelIdentifier, "Resolved model identifier");
        Objects.requireNonNull(contribution, "contribution");
        if (contribution.kind() != SurfaceContributionKind.PUBLIC_INTERACTION) {
            throw new IllegalArgumentException(
                    "Participation request requires a PUBLIC_INTERACTION contribution"
            );
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
