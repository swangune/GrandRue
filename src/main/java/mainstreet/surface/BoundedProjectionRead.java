package mainstreet.surface;

import grandrue.application.MerchantScope;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * One immutable request-bounded Projection read containing typed owner material,
 * the exact source evidence supplied to P2, and the trusted
 * request/release/contract/read-use affinity that produced it.
 */
public final class BoundedProjectionRead {

    private final ObservationRequestBinding requestBinding;
    private final MerchantScope merchantScope;
    private final String semanticRegistryReleaseIdentifier;
    private final ProjectionContractIdentity contractIdentity;
    private final ProjectionReadUseIdentity readUseIdentity;
    private final BoundedProjectionReadBinding binding;
    private final Set<ProjectionSourceEvidence> sourceEvidence;
    private final List<ProjectionMaterialFragment> fragments;

    public BoundedProjectionRead(
            EstablishedObservationRequest request,
            ProjectionContractIdentity contractIdentity,
            ProjectionReadUseIdentity readUseIdentity,
            List<? extends ProjectionMaterialFragment> fragments
    ) {
        this(
                request,
                contractIdentity,
                readUseIdentity,
                Set.of(),
                fragments
        );
    }

    public BoundedProjectionRead(
            EstablishedObservationRequest request,
            ProjectionContractIdentity contractIdentity,
            ProjectionReadUseIdentity readUseIdentity,
            Set<ProjectionSourceEvidence> sourceEvidence,
            List<? extends ProjectionMaterialFragment> fragments
    ) {
        Objects.requireNonNull(request, "request");
        this.contractIdentity = Objects.requireNonNull(
                contractIdentity,
                "contractIdentity"
        );
        this.readUseIdentity = Objects.requireNonNull(
                readUseIdentity,
                "readUseIdentity"
        );
        if (!contractIdentity.ownerIdentifier().equals(
                readUseIdentity.ownerIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Projection read use owner must match Projection Contract owner"
            );
        }

        this.sourceEvidence = immutableEvidence(sourceEvidence);
        List<ProjectionMaterialFragment> copiedFragments =
                List.copyOf(Objects.requireNonNull(fragments, "fragments"));
        validateFragments(copiedFragments);

        this.requestBinding = EstablishedObservationRequestDetails.requestBinding(
                request
        );
        this.merchantScope = EstablishedObservationRequestDetails.merchantScope(
                request
        );
        this.semanticRegistryReleaseIdentifier =
                EstablishedObservationRequestDetails
                        .semanticRegistryReleaseIdentifier(request);
        this.binding = BoundedProjectionReadBindings.issue();
        this.fragments = copiedFragments;
    }

    public ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    public MerchantScope merchantScope() {
        return merchantScope;
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public ProjectionContractIdentity contractIdentity() {
        return contractIdentity;
    }

    public ProjectionReadUseIdentity readUseIdentity() {
        return readUseIdentity;
    }

    public BoundedProjectionReadBinding binding() {
        return binding;
    }

    public Set<ProjectionSourceEvidence> sourceEvidence() {
        return sourceEvidence;
    }

    public List<ProjectionMaterialFragment> fragments() {
        return fragments;
    }

    private static Set<ProjectionSourceEvidence> immutableEvidence(
            Set<ProjectionSourceEvidence> evidence
    ) {
        Set<ProjectionSourceEvidence> copied = Set.copyOf(
                Objects.requireNonNull(evidence, "sourceEvidence")
        );
        Set<ProjectionSourceDependencyReference> sourceReferences =
                new HashSet<>();
        for (ProjectionSourceEvidence value : copied) {
            Objects.requireNonNull(value, "source evidence");
            if (!sourceReferences.add(value.sourceReference())) {
                throw new IllegalArgumentException(
                        "Duplicate bounded-read source evidence: "
                                + value.sourceReference()
                );
            }
        }
        return copied;
    }

    private static void validateFragments(
            List<ProjectionMaterialFragment> fragments
    ) {
        Set<ExposureCandidateObservation> candidateIdentities = new HashSet<>();
        for (ProjectionMaterialFragment fragment : fragments) {
            Objects.requireNonNull(fragment, "fragment");
            ExposureCandidateObservation candidate = Objects.requireNonNull(
                    fragment.candidateObservation(),
                    "fragment.candidateObservation"
            );
            Set<ProjectionMaterialSourceAffinity> affinities =
                    Objects.requireNonNull(
                            fragment.sourceAffinities(),
                            "fragment.sourceAffinities"
                    );
            if (affinities.stream().anyMatch(Objects::isNull)) {
                throw new IllegalArgumentException(
                        "Fragment source affinities must not contain null values"
                );
            }
            if (!candidateIdentities.add(candidate)) {
                throw new IllegalArgumentException(
                        "Duplicate Projection material fragment candidate identity"
                );
            }
        }
    }
}
