package grandrue.semantic.configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Process-local reference adapter for exact semantic compatibility evidence.
 * Production durability can replace this adapter without changing the
 * activation contract.
 */
public final class InMemorySemanticCompatibilityAuthority
        implements SemanticCompatibilityAuthority {

    private final Map<TransitionKey, SemanticCompatibilityEvidence> evidenceByTransition =
            new HashMap<>();

    public synchronized void record(SemanticCompatibilityEvidence evidence) {
        Objects.requireNonNull(evidence, "evidence");
        TransitionKey key = new TransitionKey(
                evidence.merchantIdentifier(),
                evidence.sourceReleaseIdentifier(),
                evidence.targetReleaseIdentifier()
        );
        SemanticCompatibilityEvidence existing = evidenceByTransition.get(key);
        if (existing != null && !existing.equals(evidence)) {
            throw new IllegalArgumentException(
                    "Semantic compatibility transition is already bound to different evidence"
            );
        }
        evidenceByTransition.put(key, evidence);
    }

    @Override
    public synchronized Optional<SemanticCompatibilityEvidence> evidenceFor(
            String merchantIdentifier,
            String sourceReleaseIdentifier,
            String targetReleaseIdentifier
    ) {
        return Optional.ofNullable(evidenceByTransition.get(new TransitionKey(
                requireIdentifier(merchantIdentifier, "Merchant identifier"),
                requireIdentifier(sourceReleaseIdentifier, "Source release identifier"),
                requireIdentifier(targetReleaseIdentifier, "Target release identifier")
        )));
    }

    private static String requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }

    private record TransitionKey(
            String merchantIdentifier,
            String sourceReleaseIdentifier,
            String targetReleaseIdentifier
    ) {
    }
}
