package mainstreet.identitysecurity;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Attributable optimistic command for one Identity security-generation
 * rotation.
 */
public record IdentitySecurityRotationCommand(
        String identityReference,
        String expectedGenerationReference,
        String replacementGenerationReference,
        IdentitySecurityRotationReason reason,
        Instant occurredAt,
        String principalReference,
        String correlationIdentifier,
        String auditIdentity,
        Optional<String> originIdentifier
) {

    public IdentitySecurityRotationCommand {
        require(identityReference, "identityReference");
        require(expectedGenerationReference, "expectedGenerationReference");
        require(replacementGenerationReference, "replacementGenerationReference");
        Objects.requireNonNull(reason, "reason");
        Objects.requireNonNull(occurredAt, "occurredAt");
        require(principalReference, "principalReference");
        require(correlationIdentifier, "correlationIdentifier");
        require(auditIdentity, "auditIdentity");
        originIdentifier = Objects.requireNonNull(
                originIdentifier,
                "originIdentifier"
        );
        originIdentifier.ifPresent(value -> require(value, "originIdentifier"));

        if (expectedGenerationReference.equals(replacementGenerationReference)) {
            throw new IllegalArgumentException(
                    "Replacement Identity security generation must differ"
                            + " from the expected current generation"
            );
        }
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
