package mainstreet.semantic.execution;

import mainstreet.semantic.executable.ApplicableOperation;

import java.util.Objects;

/**
 * Binds one executable implementation path to contract-scoped ADR-012 support
 * evidence for a captured operation snapshot.
 *
 * <p>This component proves implementation compatibility only. It does not
 * establish semantic applicability, actor authority, commercial entitlement,
 * provider readiness or operational eligibility.</p>
 */
public final class ExecutableSupportAdmission {

    private final ExecutableSupportRegistry supportRegistry;
    private final String implementationPathIdentifier;
    private final ExecutableSupportRequirementResolver requirementResolver;

    public ExecutableSupportAdmission(
            ExecutableSupportRegistry supportRegistry,
            String implementationPathIdentifier,
            ExecutableSupportRequirementResolver requirementResolver
    ) {
        this.supportRegistry = Objects.requireNonNull(
                supportRegistry,
                "supportRegistry"
        );
        if (implementationPathIdentifier == null
                || implementationPathIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Implementation path identifier must not be blank"
            );
        }
        this.implementationPathIdentifier = implementationPathIdentifier;
        this.requirementResolver = Objects.requireNonNull(
                requirementResolver,
                "requirementResolver"
        );
    }

    /**
     * Requires the selected path to cover the complete exact support
     * requirement for the supplied captured operation.
     */
    public ExecutableSupportRequirement requireSupport(
            ApplicableOperation operation
    ) {
        Objects.requireNonNull(operation, "operation");
        ExecutableSupportRequirement requirement = Objects.requireNonNull(
                requirementResolver.resolve(operation),
                "executable support requirement"
        );
        requireSemanticReleaseAffinity(operation, requirement);
        supportRegistry.requirePath(
                implementationPathIdentifier,
                requirement
        );
        return requirement;
    }

    private static void requireSemanticReleaseAffinity(
            ApplicableOperation operation,
            ExecutableSupportRequirement requirement
    ) {
        String expectedRelease = operation.model().semanticRegistryVersion();
        for (SemanticExecutionContractReference contract
                : requirement.requiredContracts()) {
            if (!expectedRelease.equals(
                    contract.semanticRegistryReleaseIdentifier()
            )) {
                throw new IllegalArgumentException(
                        "Executable support requirement does not match the "
                                + "captured semantic registry release"
                );
            }
        }
    }
}
