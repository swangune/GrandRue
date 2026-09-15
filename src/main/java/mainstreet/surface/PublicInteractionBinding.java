package mainstreet.surface;

import mainstreet.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * Audience-safe internal projection of one already-authoritative subject-
 * interaction participation fact. It is not transport DTO or mutation authority.
 */
public record PublicInteractionBinding(
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
    public PublicInteractionBinding {
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

    static PublicInteractionBinding from(
            PublicInteractionParticipationFact fact
    ) {
        Objects.requireNonNull(fact, "fact");
        return new PublicInteractionBinding(
                fact.merchantScope(),
                fact.semanticRegistryReleaseIdentifier(),
                fact.resolvedModelIdentifier(),
                fact.resolvedModelVersion(),
                fact.contributionIdentity(),
                fact.operationReference(),
                fact.subject(),
                fact.participationSourceIdentity(),
                fact.participationRoleReference()
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
