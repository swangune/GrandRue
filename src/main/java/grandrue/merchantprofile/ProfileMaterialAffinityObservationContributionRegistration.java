package grandrue.merchantprofile;

import grandrue.surface.BoundedProjectionRead;
import grandrue.surface.ObservationContributionCardinality;
import grandrue.surface.ObservationContributionConstructor;
import grandrue.surface.ObservationContributionDefinition;
import grandrue.surface.ObservationContributionKind;
import grandrue.surface.ObservationContributionRuntimeBinding;
import grandrue.surface.SurfaceAudience;

import java.util.Objects;
import java.util.Set;

/** Exact-release registration and owner construction seam for Profile BR4 evidence. */
public final class ProfileMaterialAffinityObservationContributionRegistration {

    private static final ObservationContributionKind KIND =
            new ObservationContributionKind("profile", "material-affinity");

    private ProfileMaterialAffinityObservationContributionRegistration() {
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
                ProfileMaterialAffinityObservationContribution.class
        );
    }

    /**
     * Public owner seam for application orchestration. The Profile evidence is
     * derived from the exact bounded read before E3 supplies request affinity.
     */
    public static ObservationContributionConstructor constructor(
            BoundedProjectionRead read
    ) {
        return constructor(ProfileMaterialAffinityEvidence.from(
                Objects.requireNonNull(read, "read")
        ));
    }

    static ObservationContributionConstructor constructor(
            ProfileMaterialAffinityEvidence evidence
    ) {
        Objects.requireNonNull(evidence, "evidence");
        return (requestBinding, merchantScope) -> {
            if (evidence.requestBinding() != requestBinding) {
                throw new IllegalStateException(
                        "Profile material-affinity evidence belongs to a different Observation Request"
                );
            }
            if (!evidence.merchantScope().equals(merchantScope)) {
                throw new IllegalStateException(
                        "Profile material-affinity evidence belongs to a different Merchant Scope"
                );
            }
            return new ProfileMaterialAffinityObservationContribution(
                    requestBinding,
                    merchantScope,
                    evidence.entries()
            );
        };
    }
}
