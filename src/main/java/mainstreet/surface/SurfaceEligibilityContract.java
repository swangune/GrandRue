package mainstreet.surface;

import java.util.Objects;
import java.util.Optional;

/**
 * Initial registered contextual-eligibility contract for merchant surfaces.
 * It deliberately models only capability presence/residual-management and one
 * optional current actor privilege. It is not a generic predicate DSL.
 */
public record SurfaceEligibilityContract(
        SurfaceCapabilityEligibility capabilityEligibility,
        Optional<String> requiredPrivilegeIdentifier
) {
    public SurfaceEligibilityContract {
        Objects.requireNonNull(capabilityEligibility, "capabilityEligibility");
        requiredPrivilegeIdentifier = Objects.requireNonNull(
                requiredPrivilegeIdentifier,
                "requiredPrivilegeIdentifier"
        );
        requiredPrivilegeIdentifier.ifPresent(value ->
                requireIdentifier(value, "Required privilege identifier"));
    }

    public static SurfaceEligibilityContract activeOnly() {
        return new SurfaceEligibilityContract(
                SurfaceCapabilityEligibility.ACTIVE_ONLY,
                Optional.empty()
        );
    }

    public static SurfaceEligibilityContract activeOnly(String privilegeIdentifier) {
        return new SurfaceEligibilityContract(
                SurfaceCapabilityEligibility.ACTIVE_ONLY,
                Optional.of(requireIdentifier(
                        privilegeIdentifier,
                        "Required privilege identifier"
                ))
        );
    }

    public static SurfaceEligibilityContract activeOrResidual() {
        return new SurfaceEligibilityContract(
                SurfaceCapabilityEligibility.ACTIVE_OR_RESIDUAL,
                Optional.empty()
        );
    }

    public static SurfaceEligibilityContract activeOrResidual(
            String privilegeIdentifier
    ) {
        return new SurfaceEligibilityContract(
                SurfaceCapabilityEligibility.ACTIVE_OR_RESIDUAL,
                Optional.of(requireIdentifier(
                        privilegeIdentifier,
                        "Required privilege identifier"
                ))
        );
    }

    public boolean allowsResidualManagement() {
        return capabilityEligibility == SurfaceCapabilityEligibility.ACTIVE_OR_RESIDUAL;
    }

    private static String requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
