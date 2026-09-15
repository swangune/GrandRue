package mainstreet.semantic.registry;

import java.util.Objects;
import java.util.Set;

/**
 * Platform-owned bounded variability belonging to its containing registered
 * capability. Merchant configuration may select only one of the declared
 * values and cannot use a policy to create its owner.
 */
public record OwnedPolicyDefinition(
        String identifier,
        String defaultValue,
        Set<String> allowedValues
) {

    public OwnedPolicyDefinition {
        requireIdentifier(identifier, "Policy identifier");
        requireIdentifier(defaultValue, "Default policy value");
        allowedValues = Set.copyOf(Objects.requireNonNull(allowedValues));
        for (String allowedValue : allowedValues) {
            requireIdentifier(allowedValue, "Allowed policy value");
        }
        if (!allowedValues.contains(defaultValue)) {
            throw new IllegalArgumentException(
                    "Default policy value must be allowed"
            );
        }
    }

    public boolean allows(String value) {
        return value != null && allowedValues.contains(value);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
