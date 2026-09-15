package mainstreet.fulfilment;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Registered declaration of technical fulfilment obligations required by a
 * capability under bounded deterministic applicability.
 */
public record FulfilmentRequirementDefinition(
        FulfilmentRequirementIdentity identity,
        FulfilmentRoleIdentity roleIdentity,
        Optional<String> semanticContextIdentifier,
        Set<String> obligationIdentifiers,
        FulfilmentRequirementApplicability applicability
) {
    public FulfilmentRequirementDefinition {
        Objects.requireNonNull(identity, "identity");
        Objects.requireNonNull(roleIdentity, "roleIdentity");
        semanticContextIdentifier = Objects.requireNonNull(
                semanticContextIdentifier,
                "semanticContextIdentifier"
        );
        obligationIdentifiers = Set.copyOf(Objects.requireNonNull(
                obligationIdentifiers,
                "obligationIdentifiers"
        ));
        if (obligationIdentifiers.isEmpty()) {
            throw new IllegalArgumentException(
                    "A fulfilment requirement requires at least one obligation"
            );
        }
        obligationIdentifiers.forEach(value -> requireIdentifier(
                value,
                "Fulfilment obligation identifier"
        ));
        semanticContextIdentifier.ifPresent(value -> requireIdentifier(
                value,
                "Semantic context identifier"
        ));
        Objects.requireNonNull(applicability, "applicability");
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
