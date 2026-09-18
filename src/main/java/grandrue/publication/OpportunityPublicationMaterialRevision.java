package grandrue.publication;

import grandrue.application.MerchantScope;
import mainstreet.semantic.registry.OwnedSchemaReference;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable reconstructible material representation of one Opportunity logical revision under the
 * accepted initial Publication-owned Opportunity schema.
 */
public record OpportunityPublicationMaterialRevision(
        MerchantScope merchantScope,
        String opportunityIdentity,
        String revisionIdentity,
        String title,
        Optional<String> description,
        Optional<String> eligibilityInformation,
        Optional<String> externalProviderName,
        Optional<String> sourceName,
        List<OpportunityExternalLink> externalLinks,
        Optional<OpportunityTemporalBoundary> applicationsOpen,
        Optional<OpportunityTemporalBoundary> applicationDeadline,
        Optional<OpportunityTemporalBoundary> publishFrom,
        Optional<OpportunityTemporalBoundary> publishUntil
) {
    private static final OwnedSchemaReference SCHEMA_REFERENCE =
            new OwnedSchemaReference("opportunity", 1);

    public OpportunityPublicationMaterialRevision {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        requireText(revisionIdentity, "revisionIdentity");
        requireText(title, "title");
        Objects.requireNonNull(description, "description");
        Objects.requireNonNull(eligibilityInformation, "eligibilityInformation");
        Objects.requireNonNull(externalProviderName, "externalProviderName");
        Objects.requireNonNull(sourceName, "sourceName");
        externalLinks = List.copyOf(Objects.requireNonNull(externalLinks, "externalLinks"));
        externalLinks.forEach(link -> Objects.requireNonNull(link, "externalLink"));
        Objects.requireNonNull(applicationsOpen, "applicationsOpen");
        Objects.requireNonNull(applicationDeadline, "applicationDeadline");
        Objects.requireNonNull(publishFrom, "publishFrom");
        Objects.requireNonNull(publishUntil, "publishUntil");
    }

    /** Exact immutable schema affinity for the accepted initial Opportunity material contract. */
    public OwnedSchemaReference schemaReference() {
        return SCHEMA_REFERENCE;
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
