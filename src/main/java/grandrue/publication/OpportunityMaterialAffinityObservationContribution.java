package grandrue.publication;

import grandrue.application.MerchantScope;
import mainstreet.surface.BoundedProjectionRead;
import mainstreet.surface.EstablishedObservationContribution;
import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ObservationContributionKind;
import mainstreet.surface.ObservationRequestBinding;
import mainstreet.surface.ProjectionMaterialFragment;
import mainstreet.surface.ProjectionMaterialSourceAffinity;
import mainstreet.surface.ProjectionSourceEvidence;
import mainstreet.surface.ProjectionSourceDependencyReference;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

record OpportunityMaterialAffinityEntry(
        ExposureCandidateObservation candidate,
        ProjectionSourceDependencyReference sourceReference,
        String expectedPublishedRevisionIdentity
) {
    OpportunityMaterialAffinityEntry {
        Objects.requireNonNull(candidate, "candidate");
        OpportunityPublicExposureReferences.requireOpportunityIdentity(candidate);
        Objects.requireNonNull(sourceReference, "sourceReference");
        if (!OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE.equals(
                sourceReference
        )) {
            throw new IllegalArgumentException(
                    "Opportunity material affinity requires published-material source"
            );
        }
        if (expectedPublishedRevisionIdentity == null
                || expectedPublishedRevisionIdentity.isBlank()) {
            throw new IllegalArgumentException(
                    "Expected published revision identity must not be blank"
            );
        }
    }
}

final class OpportunityMaterialAffinityEvidence {

    private final ObservationRequestBinding requestBinding;
    private final MerchantScope merchantScope;
    private final Set<OpportunityMaterialAffinityEntry> entries;

    private OpportunityMaterialAffinityEvidence(
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope,
            Set<OpportunityMaterialAffinityEntry> entries
    ) {
        this.requestBinding = Objects.requireNonNull(requestBinding, "requestBinding");
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        this.entries = Set.copyOf(Objects.requireNonNull(entries, "entries"));
    }

    static OpportunityMaterialAffinityEvidence from(BoundedProjectionRead read) {
        Objects.requireNonNull(read, "read");
        if (!OpportunityPublicRepresentationProjectionReferences.PROJECTION_CONTRACT.equals(
                read.contractIdentity()
        ) || !OpportunityPublicRepresentationProjectionReferences.READ_USE.equals(
                read.readUseIdentity()
        )) {
            throw new IllegalArgumentException(
                    "Opportunity material affinity requires the exact P5 bounded read"
            );
        }

        Set<OpportunityMaterialAffinityEntry> entries = new LinkedHashSet<>();
        for (ProjectionMaterialFragment fragment : read.fragments()) {
            ExposureCandidateObservation candidate = fragment.candidateObservation();
            OpportunityPublicExposureReferences.requireOpportunityIdentity(candidate);

            List<ProjectionMaterialSourceAffinity> affinities = fragment.sourceAffinities()
                    .stream()
                    .filter(affinity -> OpportunityPublicRepresentationProjectionReferences
                            .PUBLISHED_MATERIAL_SOURCE.equals(affinity.sourceReference()))
                    .toList();
            if (affinities.size() != 1) {
                throw new IllegalArgumentException(
                        "Opportunity fragment requires exactly one published-material affinity"
                );
            }
            ProjectionMaterialSourceAffinity affinity = affinities.getFirst();
            String expectedProgress = affinity.observedProgressIdentifier();

            List<ProjectionSourceEvidence> sourceEvidence = read.sourceEvidence().stream()
                    .filter(evidence -> affinity.sourceReference().equals(
                            evidence.sourceReference()
                    ))
                    .toList();
            if (sourceEvidence.size() != 1
                    || !sourceEvidence.getFirst().isCurrent()
                    || !java.util.Optional.of(expectedProgress).equals(
                            sourceEvidence.getFirst().observedProgressIdentifier()
                    )
                    || !java.util.Optional.of(expectedProgress).equals(
                            sourceEvidence.getFirst().requiredCurrentProgressIdentifier()
                    )) {
                throw new IllegalArgumentException(
                        "Opportunity bounded material and source evidence must share exact progress"
                );
            }

            OpportunityMaterialAffinityEntry entry = new OpportunityMaterialAffinityEntry(
                    candidate,
                    affinity.sourceReference(),
                    expectedProgress
            );
            if (!entries.add(entry)) {
                throw new IllegalArgumentException(
                        "Duplicate Opportunity material-affinity entry"
                );
            }
        }

        return new OpportunityMaterialAffinityEvidence(
                read.requestBinding(),
                read.merchantScope(),
                entries
        );
    }

    ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    MerchantScope merchantScope() {
        return merchantScope;
    }

    Set<OpportunityMaterialAffinityEntry> entries() {
        return entries;
    }
}

final class OpportunityMaterialAffinityObservationContribution
        implements EstablishedObservationContribution {

    private final ObservationRequestBinding requestBinding;
    private final MerchantScope merchantScope;
    private final Set<OpportunityMaterialAffinityEntry> entries;

    OpportunityMaterialAffinityObservationContribution(
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope,
            Set<OpportunityMaterialAffinityEntry> entries
    ) {
        this.requestBinding = Objects.requireNonNull(requestBinding, "requestBinding");
        this.merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        this.entries = Set.copyOf(Objects.requireNonNull(entries, "entries"));
    }

    @Override
    public ObservationContributionKind kind() {
        return OpportunityMaterialAffinityObservationContributionRegistration.kind();
    }

    @Override
    public ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    @Override
    public MerchantScope merchantScope() {
        return merchantScope;
    }

    Set<OpportunityMaterialAffinityEntry> entries() {
        return entries;
    }
}
