package mainstreet.credential;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * One append-only security/use policy state for a credential generation.
 * Business policy is not encoded here; callers supply only already-authorised
 * technical use classifications.
 */
public record CredentialGenerationPolicy(
        String policyIdentity,
        String generationIdentity,
        long sequence,
        CredentialGenerationState state,
        Set<CredentialTechnicalUse> permittedUses,
        Instant effectiveAt,
        Optional<String> evidenceReference
) {
    public CredentialGenerationPolicy {
        require(policyIdentity, "policyIdentity");
        require(generationIdentity, "generationIdentity");
        if (sequence < 1) {
            throw new IllegalArgumentException("Credential policy sequence must be positive");
        }
        Objects.requireNonNull(state, "state");
        permittedUses = Set.copyOf(
                Objects.requireNonNull(permittedUses, "permittedUses")
        );
        Objects.requireNonNull(effectiveAt, "effectiveAt");
        evidenceReference = Objects.requireNonNull(evidenceReference, "evidenceReference");
        evidenceReference.ifPresent(value -> require(value, "evidenceReference"));

        if (state.terminal() && !permittedUses.isEmpty()) {
            throw new IllegalArgumentException(
                    "Terminal credential generation states cannot permit credential use"
            );
        }
        if (state == CredentialGenerationState.RETIRING
                && permittedUses.contains(CredentialTechnicalUse.NEW_EXECUTION)) {
            throw new IllegalArgumentException(
                    "Retiring credential generations cannot be used for new execution"
            );
        }
        if (!state.terminal() && permittedUses.isEmpty()) {
            throw new IllegalArgumentException(
                    "Non-terminal credential generation state requires at least one permitted technical use"
            );
        }
    }

    public boolean permits(CredentialTechnicalUse use) {
        return permittedUses.contains(Objects.requireNonNull(use, "use"));
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
