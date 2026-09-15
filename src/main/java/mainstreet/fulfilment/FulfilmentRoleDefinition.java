package mainstreet.fulfilment;

import java.util.Objects;
import java.util.Set;

/**
 * Platform-owned definition of one stable fulfilment responsibility.
 *
 * <p>The owner kind distinguishes a capability-private role from an explicitly
 * registered platform-service/infrastructure role that may be required by
 * other capabilities. Registration alone never makes a role applicable.</p>
 */
public record FulfilmentRoleDefinition(
        FulfilmentRoleIdentity identity,
        FulfilmentRoleOwnerKind ownerKind,
        Set<String> obligations,
        String authorityBoundaryContractReference,
        String evidenceContractReference,
        String failureContractReference
) {
    public FulfilmentRoleDefinition {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(ownerKind, "ownerKind");
        obligations = Set.copyOf(Objects.requireNonNull(obligations, "obligations"));
        if (obligations.isEmpty()) {
            throw new IllegalArgumentException(
                    "A fulfilment role requires at least one declared obligation"
            );
        }
        obligations.forEach(value -> requireIdentifier(value, "Fulfilment obligation"));
        requireIdentifier(
                authorityBoundaryContractReference,
                "Authority-boundary contract reference"
        );
        requireIdentifier(evidenceContractReference, "Evidence contract reference");
        requireIdentifier(failureContractReference, "Failure contract reference");
    }

    /** Existing capability-owned role form retained for source compatibility. */
    public FulfilmentRoleDefinition(
            FulfilmentRoleIdentity identity,
            Set<String> obligations,
            String authorityBoundaryContractReference,
            String evidenceContractReference,
            String failureContractReference
    ) {
        this(
                identity,
                FulfilmentRoleOwnerKind.CAPABILITY,
                obligations,
                authorityBoundaryContractReference,
                evidenceContractReference,
                failureContractReference
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
