package mainstreet.publication;

import mainstreet.surface.BoundedProjectionRead;
import mainstreet.surface.ObservationContributionCardinality;
import mainstreet.surface.ObservationContributionConstructor;
import mainstreet.surface.ObservationContributionDefinition;
import mainstreet.surface.ObservationContributionKind;
import mainstreet.surface.ObservationContributionRuntimeBinding;
import mainstreet.surface.SurfaceAudience;

import java.util.Objects;
import java.util.Set;

/** Exact-release registration and request-bound construction seam for P5 material affinity. */
public final class OpportunityMaterialAffinityObservationContributionRegistration {

    private static final ObservationContributionKind KIND =
            new ObservationContributionKind("publication", "material-affinity");

    private OpportunityMaterialAffinityObservationContributionRegistration() {
    }

    public static ObservationContributionKind kind() {
        return KIND;
    }

    public static ObservationContributionDefinition definition() {
        return new ObservationContributionDefinition(
                KIND,
                Set.of(SurfaceAudience.PUBLIC),
                ObservationContributionCardinality.SINGLE
        );
    }

    public static ObservationContributionRuntimeBinding runtimeBinding() {
        return new ObservationContributionRuntimeBinding(
                KIND,
                OpportunityMaterialAffinityObservationContribution.class
        );
    }

    public static ObservationContributionConstructor constructor(
            BoundedProjectionRead read
    ) {
        OpportunityMaterialAffinityEvidence evidence =
                OpportunityMaterialAffinityEvidence.from(
                        Objects.requireNonNull(read, "read")
                );
        return (requestBinding, merchantScope) -> {
            if (evidence.requestBinding() != requestBinding) {
                throw new IllegalStateException(
                        "Opportunity material-affinity evidence belongs to a different Observation Request"
                );
            }
            if (!evidence.merchantScope().equals(merchantScope)) {
                throw new IllegalStateException(
                        "Opportunity material-affinity evidence belongs to a different Merchant Scope"
                );
            }
            return new OpportunityMaterialAffinityObservationContribution(
                    requestBinding,
                    merchantScope,
                    evidence.entries()
            );
        };
    }
}
