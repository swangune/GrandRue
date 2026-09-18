package grandrue.surface;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable semantic-release snapshot of explicitly registered Public
 * Interaction participation sources. An empty registry is a valid fail-closed
 * production state.
 */
public record PublicInteractionParticipationSourceRegistrySnapshot(
        String semanticRegistryReleaseIdentifier,
        Set<PublicInteractionParticipationSourceRegistration> registrations
) {
    public PublicInteractionParticipationSourceRegistrySnapshot {
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        registrations = Set.copyOf(
                Objects.requireNonNull(registrations, "registrations")
        );
        Set<PublicInteractionParticipationSourceIdentity> identities =
                new HashSet<>();
        for (PublicInteractionParticipationSourceRegistration registration
                : registrations) {
            Objects.requireNonNull(registration, "registration");
            if (!identities.add(registration.identity())) {
                throw new IllegalArgumentException(
                        "Duplicate Public Interaction participation source identity: "
                                + registration.identity()
                );
            }
        }
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
