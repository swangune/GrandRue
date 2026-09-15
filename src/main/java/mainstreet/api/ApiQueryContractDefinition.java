package mainstreet.api;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Static production query-contract responsibilities governed by MS-PROT-035
 * v1.1. The definition records accepted query/projection, observation,
 * serviceability, Exposure, bound and representation contracts without
 * executing the query or introducing transport mechanics.
 */
public record ApiQueryContractDefinition(
        ApiContractRegistration registration,
        String audienceObservationContextReference,
        Optional<String> relationshipRequirementReference,
        String projectionServiceabilityRequirementReference,
        Set<String> exposureContractReferences,
        String filterSortContractReference,
        String paginationBoundRuleReference,
        String safeResponseRepresentationReference,
        String unavailableRepresentationReference
) {
    public ApiQueryContractDefinition {
        Objects.requireNonNull(registration, "registration");
        if (registration.kind() != ApiContractKind.QUERY) {
            throw new IllegalArgumentException(
                    "API query definition requires a QUERY registration"
            );
        }
        requireReference(
                audienceObservationContextReference,
                "Audience Observation Context reference"
        );
        relationshipRequirementReference = requireOptionalReference(
                relationshipRequirementReference,
                "Relationship requirement reference"
        );
        requireReference(
                projectionServiceabilityRequirementReference,
                "Projection Serviceability requirement reference"
        );
        exposureContractReferences = Set.copyOf(Objects.requireNonNull(
                exposureContractReferences,
                "exposureContractReferences"
        ));
        if (exposureContractReferences.isEmpty()) {
            throw new IllegalArgumentException(
                    "API query requires at least one Exposure contract reference"
            );
        }
        exposureContractReferences.forEach(reference ->
                requireReference(reference, "Exposure contract reference"));
        requireReference(filterSortContractReference, "Filter/sort contract reference");
        requireReference(paginationBoundRuleReference, "Pagination/bound rule reference");
        requireReference(
                safeResponseRepresentationReference,
                "Safe response representation reference"
        );
        requireReference(
                unavailableRepresentationReference,
                "Unavailable representation reference"
        );
    }

    private static Optional<String> requireOptionalReference(
            Optional<String> value,
            String label
    ) {
        Objects.requireNonNull(value, label);
        value.ifPresent(reference -> requireReference(reference, label));
        return value;
    }

    private static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
