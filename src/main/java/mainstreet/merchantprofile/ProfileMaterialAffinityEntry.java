package mainstreet.merchantprofile;

import mainstreet.surface.ExposableElementReference;
import mainstreet.surface.ExposureCandidateInstanceReference;
import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ProjectionSourceDependencyReference;

import java.util.Objects;

/** One Profile-owned candidate/source/progress expectation for BR4. */
record ProfileMaterialAffinityEntry(
        ExposureCandidateObservation candidateObservation,
        ProjectionSourceDependencyReference sourceReference,
        String expectedProgressIdentifier
) {

    private static final ExposableElementReference CONTACT_ELEMENT =
            new ExposableElementReference("profile", "public-contact-point");
    private static final ExposableElementReference LOCATION_ELEMENT =
            new ExposableElementReference("profile", "public-merchant-location");
    private static final ProjectionSourceDependencyReference CONTACT_SOURCE =
            new ProjectionSourceDependencyReference("profile", "contact-points");
    private static final ProjectionSourceDependencyReference LOCATION_SOURCE =
            new ProjectionSourceDependencyReference("profile", "merchant-locations");

    ProfileMaterialAffinityEntry {
        Objects.requireNonNull(candidateObservation, "candidateObservation");
        Objects.requireNonNull(sourceReference, "sourceReference");
        if (expectedProgressIdentifier == null
                || expectedProgressIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Expected material progress identifier must not be blank"
            );
        }

        ExposableElementReference element = candidateObservation.elementReference();
        ExposureCandidateInstanceReference instance =
                candidateObservation.instanceReference().orElseThrow(
                        () -> new IllegalArgumentException(
                                "Profile material-affinity candidate must be instance-qualified"
                        )
                );

        if (CONTACT_ELEMENT.equals(element)) {
            requireInstance(instance, "contact-point");
            requireSource(sourceReference, CONTACT_SOURCE);
        } else if (LOCATION_ELEMENT.equals(element)) {
            requireInstance(instance, "merchant-location");
            requireSource(sourceReference, LOCATION_SOURCE);
        } else {
            throw new IllegalArgumentException(
                    "Candidate is not an initial Profile material-affinity element"
            );
        }
    }

    private static void requireInstance(
            ExposureCandidateInstanceReference instance,
            String expectedKind
    ) {
        if (!"profile".equals(instance.ownerIdentifier())
                || !expectedKind.equals(instance.instanceKindIdentifier())) {
            throw new IllegalArgumentException(
                    "Profile material-affinity candidate instance kind mismatch"
            );
        }
    }

    private static void requireSource(
            ProjectionSourceDependencyReference actual,
            ProjectionSourceDependencyReference expected
    ) {
        if (!expected.equals(actual)) {
            throw new IllegalArgumentException(
                    "Profile material-affinity candidate source mismatch"
            );
        }
    }
}
