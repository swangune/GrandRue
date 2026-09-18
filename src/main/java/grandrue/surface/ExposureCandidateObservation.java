package grandrue.surface;

import java.util.Objects;
import java.util.Optional;

/**
 * One already-legitimate candidate submitted to bounded Exposure evaluation.
 * It carries identity only, never business payload or projection value.
 */
public record ExposureCandidateObservation(
        ExposableElementReference elementReference,
        Optional<ExposureCandidateInstanceReference> instanceReference
) {
    public ExposureCandidateObservation {
        Objects.requireNonNull(elementReference, "elementReference");
        Objects.requireNonNull(instanceReference, "instanceReference");
        instanceReference.ifPresent(instance -> {
            if (!elementReference.ownerIdentifier().equals(
                    instance.ownerIdentifier()
            )) {
                throw new IllegalArgumentException(
                        "Candidate instance owner must match exposable element owner"
                );
            }
        });
    }
}
