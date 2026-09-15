package mainstreet.surface;

import mainstreet.fulfilment.FulfilmentRoleIdentity;

import java.util.Objects;
import java.util.Optional;

/**
 * Static declaration of the one fulfilment-role dependency whose current
 * readiness may affect an ACTION contribution. It does not contain live
 * provider state.
 */
public record SurfaceInteractionAvailabilityContract(
        Optional<FulfilmentRoleIdentity> requiredFulfilmentRole
) {
    public SurfaceInteractionAvailabilityContract {
        requiredFulfilmentRole = Objects.requireNonNull(
                requiredFulfilmentRole,
                "requiredFulfilmentRole"
        );
    }

    public static SurfaceInteractionAvailabilityContract independent() {
        return new SurfaceInteractionAvailabilityContract(Optional.empty());
    }

    public static SurfaceInteractionAvailabilityContract requiresFulfilmentRole(
            FulfilmentRoleIdentity roleIdentity
    ) {
        return new SurfaceInteractionAvailabilityContract(
                Optional.of(Objects.requireNonNull(roleIdentity, "roleIdentity"))
        );
    }

    public boolean providerReadinessDependent() {
        return requiredFulfilmentRole.isPresent();
    }
}
