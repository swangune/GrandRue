package mainstreet.api;

import mainstreet.protection.ProtectionTarget;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Static production command-contract responsibilities governed by
 * MS-PROT-035 v1.1. References point to accepted owner/transport contracts;
 * this definition does not execute, authorise or route the command.
 */
public record ApiCommandContractDefinition(
        ApiContractRegistration registration,
        Set<String> eligiblePrincipalClassReferences,
        String transportInputContractReference,
        Optional<String> logicalRetryMechanismReference,
        Optional<String> optimisticConcurrencyPreconditionReference,
        String completionModeReference,
        String safeResultRepresentationReference,
        String safeRejectionRepresentationReference,
        Optional<ProtectionTarget> protectionTarget,
        String dataProtectionClassificationReference
) {
    public ApiCommandContractDefinition {
        Objects.requireNonNull(registration, "registration");
        if (registration.kind() != ApiContractKind.COMMAND) {
            throw new IllegalArgumentException(
                    "API command definition requires a COMMAND registration"
            );
        }
        eligiblePrincipalClassReferences = Set.copyOf(Objects.requireNonNull(
                eligiblePrincipalClassReferences,
                "eligiblePrincipalClassReferences"
        ));
        if (eligiblePrincipalClassReferences.isEmpty()) {
            throw new IllegalArgumentException(
                    "API command requires at least one eligible principal class"
            );
        }
        eligiblePrincipalClassReferences.forEach(value ->
                requireReference(value, "Eligible principal class reference"));
        requireReference(
                transportInputContractReference,
                "Transport input contract reference"
        );
        logicalRetryMechanismReference = requireOptionalReference(
                logicalRetryMechanismReference,
                "Logical retry mechanism reference"
        );
        optimisticConcurrencyPreconditionReference = requireOptionalReference(
                optimisticConcurrencyPreconditionReference,
                "Optimistic-concurrency precondition reference"
        );
        requireReference(completionModeReference, "Completion mode reference");
        requireReference(
                safeResultRepresentationReference,
                "Safe result representation reference"
        );
        requireReference(
                safeRejectionRepresentationReference,
                "Safe rejection representation reference"
        );
        protectionTarget = Objects.requireNonNull(
                protectionTarget,
                "protectionTarget"
        );
        requireReference(
                dataProtectionClassificationReference,
                "Data-protection classification reference"
        );
    }

    private static Optional<String> requireOptionalReference(
            Optional<String> value,
            String label
    ) {
        Objects.requireNonNull(value, label);
        value.ifPresent(reference -> requireReference(reference, label));
        return value;
    }

    private static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
