package grandrue.semantic.executable;

import java.util.Objects;
import java.util.Optional;

/**
 * Immutable effective policy value and its resolution provenance for one
 * registered capability owner.
 */
public record ExecutablePolicyValue(
        String ownerCapabilityIdentifier,
        String policyIdentifier,
        PolicyResolutionStatus status,
        Optional<String> value
) {

    public ExecutablePolicyValue {
        requireIdentifier(
                ownerCapabilityIdentifier,
                "Policy owner capability identifier"
        );
        requireIdentifier(policyIdentifier, "Policy identifier");
        Objects.requireNonNull(status, "status");
        value = Objects.requireNonNull(value, "value");
        if (status == PolicyResolutionStatus.NOT_APPLICABLE
                && value.isPresent()) {
            throw new IllegalArgumentException(
                    "An inapplicable policy must not have an effective value"
            );
        }
        if (status != PolicyResolutionStatus.NOT_APPLICABLE) {
            requireIdentifier(
                    value.orElseThrow(() -> new IllegalArgumentException(
                            "An applicable policy requires an effective value"
                    )),
                    "Effective policy value"
            );
        }
    }

    public static ExecutablePolicyValue notApplicable(
            String ownerCapabilityIdentifier,
            String policyIdentifier
    ) {
        return new ExecutablePolicyValue(
                ownerCapabilityIdentifier,
                policyIdentifier,
                PolicyResolutionStatus.NOT_APPLICABLE,
                Optional.empty()
        );
    }

    public static ExecutablePolicyValue defaulted(
            String ownerCapabilityIdentifier,
            String policyIdentifier,
            String value
    ) {
        return new ExecutablePolicyValue(
                ownerCapabilityIdentifier,
                policyIdentifier,
                PolicyResolutionStatus.DEFAULTED,
                Optional.of(value)
        );
    }

    public static ExecutablePolicyValue explicitlySelected(
            String ownerCapabilityIdentifier,
            String policyIdentifier,
            String value
    ) {
        return new ExecutablePolicyValue(
                ownerCapabilityIdentifier,
                policyIdentifier,
                PolicyResolutionStatus.EXPLICITLY_SELECTED,
                Optional.of(value)
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
