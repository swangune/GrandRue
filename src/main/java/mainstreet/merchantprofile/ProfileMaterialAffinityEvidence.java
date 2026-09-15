package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.surface.BoundedProjectionRead;
import mainstreet.surface.ExposableElementReference;
import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ObservationRequestBinding;
import mainstreet.surface.ProjectionMaterialFragment;
import mainstreet.surface.ProjectionMaterialSourceAffinity;
import mainstreet.surface.ProjectionSourceDependencyReference;
import mainstreet.surface.ProjectionSourceEvidence;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Profile-owned BR4 evidence derived only from one exact Bounded Projection Read.
 *
 * <p>It retains request and merchant affinity mechanically while exposing no
 * rendered Profile values and performing no owner re-query.</p>
 */
final class ProfileMaterialAffinityEvidence {

    private static final ExposableElementReference CONTACT_ELEMENT =
            new ExposableElementReference("profile", "public-contact-point");
    private static final ExposableElementReference LOCATION_ELEMENT =
            new ExposableElementReference("profile", "public-merchant-location");
    private static final ProjectionSourceDependencyReference CONTACT_SOURCE =
            new ProjectionSourceDependencyReference("profile", "contact-points");
    private static final ProjectionSourceDependencyReference LOCATION_SOURCE =
            new ProjectionSourceDependencyReference("profile", "merchant-locations");

    private final ObservationRequestBinding requestBinding;
    private final MerchantScope merchantScope;
    private final Set<ProfileMaterialAffinityEntry> entries;

    private ProfileMaterialAffinityEvidence(
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope,
            Set<ProfileMaterialAffinityEntry> entries
    ) {
        this.requestBinding = Objects.requireNonNull(
                requestBinding,
                "requestBinding"
        );
        this.merchantScope = Objects.requireNonNull(
                merchantScope,
                "merchantScope"
        );
        this.entries = Set.copyOf(Objects.requireNonNull(entries, "entries"));
    }

    static ProfileMaterialAffinityEvidence from(BoundedProjectionRead read) {
        Objects.requireNonNull(read, "read");
        List<ProfileMaterialAffinityEntry> entries = new ArrayList<>();

        for (ProjectionMaterialFragment fragment : read.fragments()) {
            ExposureCandidateObservation candidate =
                    Objects.requireNonNull(
                            fragment.candidateObservation(),
                            "fragment.candidateObservation"
                    );
            Optional<ProjectionSourceDependencyReference> expectedSource =
                    expectedSource(candidate);
            if (expectedSource.isEmpty()) {
                continue;
            }

            ProjectionSourceDependencyReference source =
                    expectedSource.orElseThrow();
            List<ProjectionMaterialSourceAffinity> matchingAffinities =
                    fragment.sourceAffinities().stream()
                            .filter(affinity -> source.equals(
                                    affinity.sourceReference()
                            ))
                            .toList();
            if (matchingAffinities.size() != 1) {
                throw new IllegalArgumentException(
                        "Profile material-affinity candidate requires exactly one expected source affinity"
                );
            }

            String progress = matchingAffinities.getFirst()
                    .observedProgressIdentifier();
            ProjectionSourceEvidence sourceEvidence = read.sourceEvidence()
                    .stream()
                    .filter(value -> source.equals(value.sourceReference()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Profile material-affinity source evidence is missing from the bounded read"
                    ));
            if (!Optional.of(progress).equals(
                    sourceEvidence.observedProgressIdentifier()
            )) {
                throw new IllegalArgumentException(
                        "Profile material affinity must match the bounded read P2 observed progress"
                );
            }

            entries.add(new ProfileMaterialAffinityEntry(
                    candidate,
                    source,
                    progress
            ));
        }

        Set<ProfileMaterialAffinityEntry> immutableEntries = Set.copyOf(entries);
        if (immutableEntries.size() != entries.size()) {
            throw new IllegalArgumentException(
                    "Duplicate Profile material-affinity entry"
            );
        }
        return new ProfileMaterialAffinityEvidence(
                read.requestBinding(),
                read.merchantScope(),
                immutableEntries
        );
    }

    ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    MerchantScope merchantScope() {
        return merchantScope;
    }

    Set<ProfileMaterialAffinityEntry> entries() {
        return entries;
    }

    private static Optional<ProjectionSourceDependencyReference> expectedSource(
            ExposureCandidateObservation candidate
    ) {
        if (CONTACT_ELEMENT.equals(candidate.elementReference())) {
            return Optional.of(CONTACT_SOURCE);
        }
        if (LOCATION_ELEMENT.equals(candidate.elementReference())) {
            return Optional.of(LOCATION_SOURCE);
        }
        return Optional.empty();
    }
}
