package grandrue.fulfilment;

import java.util.Objects;
import java.util.Set;

/** Technical responsibilities one registered provider claims for one role. */
public record ProviderFulfilmentSupport(
        FulfilmentRoleIdentity roleIdentity,
        Set<String> supportedObligations
) {
    public ProviderFulfilmentSupport {
        Objects.requireNonNull(roleIdentity, "roleIdentity");
        supportedObligations = Set.copyOf(
                Objects.requireNonNull(
                        supportedObligations,
                        "supportedObligations"
                )
        );
        supportedObligations.forEach(value ->
                requireIdentifier(value, "Supported fulfilment obligation"));
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
