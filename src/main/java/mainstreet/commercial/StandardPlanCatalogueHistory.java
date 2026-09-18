package mainstreet.commercial;

import grandrue.commercial.CatalogueResolutionException;
import grandrue.commercial.FreePlanRevisionAuthority;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;

import static grandrue.commercial.CatalogueResolutionException.Reason.*;

/**
 * Immutable, complete publication-history read through one authoritative read instant.
 * A storage adapter must establish a consistent history/read instant and coordinate
 * that read with publication; caller-supplied data is not publication authority.
 * This snapshot cannot answer beyond its read instant or establish a catalogue.
 *
 * <p>MS-PROT-056 v1.9 — Commercial Catalogue Binding, Publication & Historical Resolution Amendment,
 * §§9–11, 13. This class implements the pure selection rules, not the storage
 * coordination or retained binding/approval evidence required by those sections.</p>
 */
public final class StandardPlanCatalogueHistory implements FreePlanRevisionAuthority {
    private final Instant observedAt;
    private final NavigableMap<Instant, PublishedStandardPlanCatalogueRevision> publications;
    private final Map<String, PublishedStandardPlanCatalogueRevision> byIdentity;

    public StandardPlanCatalogueHistory(
            Instant observedAt,
            List<PublishedStandardPlanCatalogueRevision> completeHistory) {
        this.observedAt = Objects.requireNonNull(observedAt, "observedAt");
        var byTime = new TreeMap<Instant, PublishedStandardPlanCatalogueRevision>();
        var identities = new HashMap<String, PublishedStandardPlanCatalogueRevision>();
        for (var publication : List.copyOf(completeHistory)) {
            if (publication.publishedAt().isAfter(observedAt)) {
                throw integrity("Publication is later than the authoritative read instant");
            }
            if (byTime.putIfAbsent(publication.publishedAt(), publication) != null) {
                throw integrity("Catalogue publication instants must be strictly increasing");
            }
            if (identities.putIfAbsent(publication.revision().catalogueRevisionIdentifier(), publication) != null) {
                throw integrity("Duplicate catalogue revision identity");
            }
        }
        validateChain(byTime);
        publications = Collections.unmodifiableNavigableMap(byTime);
        byIdentity = Map.copyOf(identities);
    }

    public Optional<PublishedStandardPlanCatalogueRevision> exactRevision(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Catalogue revision identity must not be blank");
        }
        return Optional.ofNullable(byIdentity.get(identifier));
    }

    public PublishedStandardPlanCatalogueRevision effectiveAt(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        if (instant.isAfter(observedAt)) {
            throw new CatalogueResolutionException(FUTURE_UNSUPPORTED,
                    "Selection exceeds the authoritative catalogue read instant");
        }
        if (publications.isEmpty()) {
            throw new CatalogueResolutionException(NOT_ESTABLISHED, "No catalogue has been published");
        }
        var selected = publications.floorEntry(instant);
        if (selected == null) {
            throw new CatalogueResolutionException(HISTORY_NOT_COVERED,
                    "Selection precedes the first catalogue publication");
        }
        return selected.getValue();
    }

    @Override
    public StandardPlanRevision effectiveFreePlanRevisionAt(Instant instant) {
        return effectiveAt(instant).revision().freePlan();
    }

    private static void validateChain(
            NavigableMap<Instant, PublishedStandardPlanCatalogueRevision> publications) {
        Optional<String> predecessor = Optional.empty();
        var retainedPlans = new HashMap<String, StandardPlanRevision>();
        for (var publication : publications.values()) {
            if (!publication.predecessorCatalogueRevisionIdentifier().equals(predecessor)) {
                throw integrity("Publication predecessor does not match complete retained history");
            }
            var revision = publication.revision();
            for (var plan : List.of(revision.freePlan(), revision.businessPlan(), revision.growthPlan())) {
                var previous = retainedPlans.putIfAbsent(plan.revisionIdentifier(), plan);
                if (previous != null && !previous.equals(plan)) {
                    throw integrity("A retained plan revision identity has conflicting content");
                }
            }
            predecessor = Optional.of(revision.catalogueRevisionIdentifier());
        }
    }

    private static CatalogueResolutionException integrity(String message) {
        return new CatalogueResolutionException(INTEGRITY_FAILURE, message);
    }
}
