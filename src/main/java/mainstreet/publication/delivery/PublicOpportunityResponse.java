package mainstreet.publication.delivery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Transport-owned PUBLIC values only. No Publication revision, Projection binding, source evidence
 * or owner domain object is reachable from this DTO.
 */
public record PublicOpportunityResponse(
        String opportunityIdentity,
        String title,
        Optional<String> description,
        Optional<String> eligibilityInformation,
        Optional<String> externalProviderName,
        Optional<String> sourceName,
        List<ExternalLink> externalLinks,
        Optional<TemporalBoundary> applicationsOpen,
        Optional<TemporalBoundary> applicationDeadline
) {
    public PublicOpportunityResponse {
        Objects.requireNonNull(opportunityIdentity, "opportunityIdentity");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(eligibilityInformation, "eligibilityInformation");
        Objects.requireNonNull(externalProviderName, "externalProviderName");
        Objects.requireNonNull(sourceName, "sourceName");
        externalLinks = List.copyOf(externalLinks);
        Objects.requireNonNull(applicationsOpen, "applicationsOpen");
        Objects.requireNonNull(applicationDeadline, "applicationDeadline");
    }

    public record ExternalLink(String role, String uri, Optional<String> label) {
        public ExternalLink {
            Objects.requireNonNull(role, "role");
            Objects.requireNonNull(uri, "uri");
            Objects.requireNonNull(label, "label");
        }
    }

    /** CALENDAR_DATE carries its interpretation zone; EXACT_INSTANT carries an ISO instant. */
    public record TemporalBoundary(String kind, String value, Optional<String> zone) {
        public TemporalBoundary {
            Objects.requireNonNull(kind, "kind");
            Objects.requireNonNull(value, "value");
            Objects.requireNonNull(zone, "zone");
            if (!("CALENDAR_DATE".equals(kind) && zone.isPresent())
                    && !("EXACT_INSTANT".equals(kind) && zone.isEmpty())) {
                throw new IllegalArgumentException("Temporal boundary kind and zone disagree");
            }
        }
    }
}
