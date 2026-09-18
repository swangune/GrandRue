package mainstreet.publication;

import grandrue.application.MerchantScope;
import mainstreet.surface.ProjectionMaterialSourceAffinity;
import mainstreet.surface.ProjectionSourceAvailability;
import mainstreet.surface.ProjectionSourceCompleteness;
import mainstreet.surface.ProjectionSourceEvidence;
import mainstreet.surface.ProjectionSourceRevocationState;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Publication-owned P5 adapter from authoritative published Opportunity material to one coherent
 * typed bounded-projection observation.
 */
public final class AuthorityBackedOpportunityPublicRepresentationProjectionReadPort
        implements OpportunityPublicRepresentationProjectionReadPort {

    private final OpportunityPublicationStateAuthority stateAuthority;

    public AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(
            OpportunityPublicationStateAuthority stateAuthority
    ) {
        this.stateAuthority = Objects.requireNonNull(stateAuthority, "stateAuthority");
    }

    @Override
    public OpportunityPublicRepresentationProjectionObservation observe(
            MerchantScope merchantScope,
            String opportunityIdentity,
            Instant observedAt
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");
        Objects.requireNonNull(observedAt, "observedAt");

        Optional<OpportunityPublicationState> initialState = stateAuthority.current(
                merchantScope,
                opportunityIdentity
        ).filter(state -> state.lifecycle() == PublicationLifecycle.PUBLISHED);
        if (initialState.isEmpty()) {
            return missingObservation(merchantScope, opportunityIdentity, observedAt);
        }

        String publishedRevisionIdentity = initialState.orElseThrow()
                .publishedRevisionIdentity()
                .orElseThrow(() -> new IllegalStateException(
                        "PUBLISHED Opportunity is missing published revision evidence"
                ));
        Optional<OpportunityPublicationMaterialRevision> revision = stateAuthority.revision(
                merchantScope,
                opportunityIdentity,
                publishedRevisionIdentity
        ).map(material -> requireExactAffinity(
                merchantScope,
                opportunityIdentity,
                publishedRevisionIdentity,
                material
        ));
        if (revision.isEmpty()
                || !stillPublishedAt(
                        merchantScope,
                        opportunityIdentity,
                        publishedRevisionIdentity
                )) {
            return missingObservation(merchantScope, opportunityIdentity, observedAt);
        }

        OpportunityPublicationMaterialRevision material = revision.orElseThrow();
        ProjectionMaterialSourceAffinity sourceAffinity = new ProjectionMaterialSourceAffinity(
                OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE,
                publishedRevisionIdentity
        );
        OpportunityPublicRepresentationProjectionFragment fragment =
                new OpportunityPublicRepresentationProjectionFragment(
                        OpportunityPublicExposureReferences.candidate(opportunityIdentity),
                        Set.of(sourceAffinity),
                        representation(material)
                );

        return new OpportunityPublicRepresentationProjectionObservation(
                merchantScope,
                Optional.of(fragment),
                new ProjectionSourceEvidence(
                        OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE,
                        evidenceIdentifier(
                                merchantScope,
                                opportunityIdentity,
                                publishedRevisionIdentity
                        ),
                        Optional.of(publishedRevisionIdentity),
                        Optional.of(publishedRevisionIdentity),
                        observedAt,
                        ProjectionSourceAvailability.AVAILABLE,
                        ProjectionSourceCompleteness.COMPLETE,
                        ProjectionSourceRevocationState.NOT_APPLICABLE
                )
        );
    }

    private boolean stillPublishedAt(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String publishedRevisionIdentity
    ) {
        return stateAuthority.current(merchantScope, opportunityIdentity)
                .filter(state -> state.lifecycle() == PublicationLifecycle.PUBLISHED)
                .flatMap(OpportunityPublicationState::publishedRevisionIdentity)
                .filter(publishedRevisionIdentity::equals)
                .isPresent();
    }

    private static OpportunityPublicationMaterialRevision requireExactAffinity(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String publishedRevisionIdentity,
            OpportunityPublicationMaterialRevision revision
    ) {
        Objects.requireNonNull(revision, "revision");
        if (!merchantScope.equals(revision.merchantScope())
                || !opportunityIdentity.equals(revision.opportunityIdentity())
                || !publishedRevisionIdentity.equals(revision.revisionIdentity())) {
            throw new IllegalStateException(
                    "Publication authority returned material with mismatched published affinity"
            );
        }
        return revision;
    }

    private static OpportunityPublicRepresentation representation(
            OpportunityPublicationMaterialRevision revision
    ) {
        return new OpportunityPublicRepresentation(
                revision.title(),
                revision.description(),
                revision.eligibilityInformation(),
                revision.externalProviderName(),
                revision.sourceName(),
                revision.externalLinks(),
                revision.applicationsOpen(),
                revision.applicationDeadline()
        );
    }

    private static OpportunityPublicRepresentationProjectionObservation missingObservation(
            MerchantScope merchantScope,
            String opportunityIdentity,
            Instant observedAt
    ) {
        return new OpportunityPublicRepresentationProjectionObservation(
                merchantScope,
                Optional.empty(),
                new ProjectionSourceEvidence(
                        OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE,
                        "publication-opportunity-"
                                + merchantScope.merchantIdentifier()
                                + "-"
                                + opportunityIdentity
                                + "-published-material-absent-"
                                + observedAt,
                        Optional.empty(),
                        Optional.empty(),
                        observedAt,
                        ProjectionSourceAvailability.UNAVAILABLE,
                        ProjectionSourceCompleteness.MISSING,
                        ProjectionSourceRevocationState.NOT_APPLICABLE
                )
        );
    }

    private static String evidenceIdentifier(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String publishedRevisionIdentity
    ) {
        return "publication-opportunity-"
                + merchantScope.merchantIdentifier()
                + "-"
                + opportunityIdentity
                + "-published-revision-"
                + publishedRevisionIdentity;
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
