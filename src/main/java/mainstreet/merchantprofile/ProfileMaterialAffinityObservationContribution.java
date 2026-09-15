package mainstreet.merchantprofile;

import mainstreet.application.MerchantScope;
import mainstreet.surface.EstablishedObservationContribution;
import mainstreet.surface.ObservationContributionKind;
import mainstreet.surface.ObservationRequestBinding;

import java.util.Objects;
import java.util.Set;

/** Profile-owned concrete Material Affinity Observation Contribution. */
final class ProfileMaterialAffinityObservationContribution
        implements EstablishedObservationContribution {

    private final ObservationRequestBinding requestBinding;
    private final MerchantScope merchantScope;
    private final Set<ProfileMaterialAffinityEntry> entries;

    ProfileMaterialAffinityObservationContribution(
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

    @Override
    public ObservationContributionKind kind() {
        return ProfileMaterialAffinityObservationContributionRegistration.kind();
    }

    @Override
    public ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    @Override
    public MerchantScope merchantScope() {
        return merchantScope;
    }

    Set<ProfileMaterialAffinityEntry> entries() {
        return entries;
    }
}
