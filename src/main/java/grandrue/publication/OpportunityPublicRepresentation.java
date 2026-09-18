package grandrue.publication;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Bounded audience-facing Opportunity material for one request-scoped public projection.
 *
 * <p>This value intentionally excludes Publication revision identity, schema/provenance internals
 * and publishFrom/publishUntil Exposure-window facts. Those remain owner-only evidence.</p>
 */
public record OpportunityPublicRepresentation(
        String title,
        Optional<String> description,
        Optional<String> eligibilityInformation,
        Optional<String> externalProviderName,
        Optional<String> sourceName,
        List<OpportunityExternalLink> externalLinks,
        Optional<OpportunityTemporalBoundary> applicationsOpen,
        Optional<OpportunityTemporalBoundary> applicationDeadline
) {
    public OpportunityPublicRepresentation {
        requireText(title, "title");
        description = Objects.requireNonNull(description, "description");
        eligibilityInformation = Objects.requireNonNull(
                eligibilityInformation,
                "eligibilityInformation"
        );
        externalProviderName = Objects.requireNonNull(
                externalProviderName,
                "externalProviderName"
        );
        sourceName = Objects.requireNonNull(sourceName, "sourceName");
        externalLinks = List.copyOf(Objects.requireNonNull(externalLinks, "externalLinks"));
        externalLinks.forEach(link -> Objects.requireNonNull(link, "externalLink"));
        applicationsOpen = Objects.requireNonNull(applicationsOpen, "applicationsOpen");
        applicationDeadline = Objects.requireNonNull(
                applicationDeadline,
                "applicationDeadline"
        );
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
