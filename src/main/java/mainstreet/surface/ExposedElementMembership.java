package mainstreet.surface;

import java.util.Objects;
import java.util.Optional;

/** Immutable positive Exposure membership identity for one bounded resolution. */
public record ExposedElementMembership(
        ExposableElementReference elementReference,
        Optional<ExposureCandidateInstanceReference> memberInstanceReference
) {
    public ExposedElementMembership {
        elementReference = Objects.requireNonNull(
                elementReference,
                "elementReference"
        );
        memberInstanceReference = Objects.requireNonNull(
                memberInstanceReference,
                "memberInstanceReference"
        );
        if (memberInstanceReference.isPresent()) {
            ExposureCandidateInstanceReference instance =
                    memberInstanceReference.orElseThrow();
            if (!elementReference.ownerIdentifier().equals(instance.ownerIdentifier())) {
                throw new IllegalArgumentException(
                        "Exposure member instance owner must match element owner"
                );
            }
        }
    }
}
