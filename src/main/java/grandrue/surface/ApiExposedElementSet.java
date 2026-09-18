package grandrue.surface;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable positive-membership-only API Exposure result set. */
public sealed interface ApiExposedElementSet
        permits DefaultApiExposedElementSet {

    Set<ExposedElementMembership> members();
}

final class DefaultApiExposedElementSet implements ApiExposedElementSet {

    private final Set<ExposedElementMembership> members;
    private final Map<ExposedElementMembership, ExposureElementContractIdentity>
            contractIdentities;
    private final ObservationRequestBinding requestBinding;
    private final AudienceObservationInvocationBinding invocationBinding;
    private final String semanticRegistryReleaseIdentifier;

    DefaultApiExposedElementSet(
            Map<ExposedElementMembership, ExposureElementContractIdentity>
                    contractIdentities,
            ObservationRequestBinding requestBinding,
            AudienceObservationInvocationBinding invocationBinding,
            String semanticRegistryReleaseIdentifier
    ) {
        this.contractIdentities = Map.copyOf(Objects.requireNonNull(
                contractIdentities,
                "contractIdentities"
        ));
        this.members = Set.copyOf(this.contractIdentities.keySet());
        this.requestBinding = Objects.requireNonNull(
                requestBinding,
                "requestBinding"
        );
        this.invocationBinding = Objects.requireNonNull(
                invocationBinding,
                "invocationBinding"
        );
        if (semanticRegistryReleaseIdentifier == null
                || semanticRegistryReleaseIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Semantic registry release identifier must not be blank"
            );
        }
        this.semanticRegistryReleaseIdentifier =
                semanticRegistryReleaseIdentifier;
    }

    @Override
    public Set<ExposedElementMembership> members() {
        return members;
    }

    ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    AudienceObservationInvocationBinding invocationBinding() {
        return invocationBinding;
    }

    String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    Optional<ExposureElementContractIdentity> memberContractIdentity(
            ExposedElementMembership membership
    ) {
        Objects.requireNonNull(membership, "membership");
        return Optional.ofNullable(contractIdentities.get(membership));
    }
}
