package mainstreet.surface;

import mainstreet.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Positive capability-owned subject-interaction participation fact. It carries
 * semantic identity and provenance only; no availability or execution grant.
 */
public record PublicInteractionParticipationFact(
        MerchantScope merchantScope,
        String semanticRegistryReleaseIdentifier,
        String resolvedModelIdentifier,
        long resolvedModelVersion,
        SurfaceContributionIdentity contributionIdentity,
        String operationReference,
        ExposureCandidateObservation subject,
        PublicInteractionParticipationSourceIdentity participationSourceIdentity,
        Optional<String> participationRoleReference
) {
    public PublicInteractionParticipationFact {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        requireIdentifier(resolvedModelIdentifier, "Resolved model identifier");
        Objects.requireNonNull(contributionIdentity, "contributionIdentity");
        requireIdentifier(operationReference, "Operation reference");
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(
                participationSourceIdentity,
                "participationSourceIdentity"
        );
        participationRoleReference = Objects.requireNonNull(
                participationRoleReference,
                "participationRoleReference"
        );
        participationRoleReference.ifPresent(value ->
                requireIdentifier(value, "Participation role reference"));
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
