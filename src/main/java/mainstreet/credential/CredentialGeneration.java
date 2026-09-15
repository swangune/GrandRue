package mainstreet.credential;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable non-secret metadata for one physical credential generation.
 * protectedMaterialReference is an opaque security-boundary reference, not the
 * credential value and not a business identity.
 */
public record CredentialGeneration(
        String generationIdentity,
        String bindingIdentity,
        String protectedMaterialReference,
        Instant registeredAt
) {
    public CredentialGeneration {
        require(generationIdentity, "generationIdentity");
        require(bindingIdentity, "bindingIdentity");
        require(protectedMaterialReference, "protectedMaterialReference");
        Objects.requireNonNull(registeredAt, "registeredAt");
        if (generationIdentity.equals(bindingIdentity)
                || generationIdentity.equals(protectedMaterialReference)
                || bindingIdentity.equals(protectedMaterialReference)) {
            throw new IllegalArgumentException(
                    "Credential generation, binding and protected material references must remain distinct"
            );
        }
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
