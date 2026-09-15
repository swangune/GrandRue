package mainstreet.runtime;

import mainstreet.semantic.executable.ApplicableOperation;
import mainstreet.semantic.executable.ExecutableConditionalRequirementDefinition;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import mainstreet.semantic.executable.ExecutableRequirementDefinition;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable observational result of evaluating one captured operation's
 * requirements. Applicability is semantic truth; satisfaction is evidence
 * supplied by the current command context.
 */
public final class RequirementEvaluation {

    private final ApplicableOperation applicableOperation;
    private final Set<String> applicableRequirementIdentifiers;
    private final Set<String> satisfiedRequirementIdentifiers;
    private final Set<String> unsatisfiedRequirementIdentifiers;

    public RequirementEvaluation(
            ApplicableOperation applicableOperation,
            Set<String> applicableRequirementIdentifiers,
            Set<String> satisfiedRequirementIdentifiers
    ) {
        this.applicableOperation = Objects.requireNonNull(
                applicableOperation,
                "applicableOperation"
        );
        this.applicableRequirementIdentifiers = Set.copyOf(
                Objects.requireNonNull(
                        applicableRequirementIdentifiers,
                        "applicableRequirementIdentifiers"
                )
        );
        this.satisfiedRequirementIdentifiers = Set.copyOf(
                Objects.requireNonNull(
                        satisfiedRequirementIdentifiers,
                        "satisfiedRequirementIdentifiers"
                )
        );

        ExecutableOperationDefinition operation =
                applicableOperation.operation();
        Set<String> declared = declaredRequirementIdentifiers(operation);
        if (!declared.containsAll(this.applicableRequirementIdentifiers)) {
            throw new IllegalArgumentException(
                    "Requirement evaluation contains an undeclared requirement"
            );
        }
        Set<String> unconditional = unconditionalRequirementIdentifiers(
                operation
        );
        if (!this.applicableRequirementIdentifiers.containsAll(
                unconditional
        )) {
            throw new IllegalArgumentException(
                    "Requirement evaluation omitted an unconditional requirement"
            );
        }
        if (!this.applicableRequirementIdentifiers.containsAll(
                this.satisfiedRequirementIdentifiers
        )) {
            throw new IllegalArgumentException(
                    "A satisfied requirement must be applicable"
            );
        }

        Set<String> unsatisfied = new HashSet<>(
                this.applicableRequirementIdentifiers
        );
        unsatisfied.removeAll(this.satisfiedRequirementIdentifiers);
        this.unsatisfiedRequirementIdentifiers = Set.copyOf(unsatisfied);
    }

    public ApplicableOperation applicableOperation() {
        return applicableOperation;
    }

    public Set<String> applicableRequirementIdentifiers() {
        return applicableRequirementIdentifiers;
    }

    public Set<String> satisfiedRequirementIdentifiers() {
        return satisfiedRequirementIdentifiers;
    }

    public Set<String> unsatisfiedRequirementIdentifiers() {
        return unsatisfiedRequirementIdentifiers;
    }

    public boolean isSatisfied() {
        return unsatisfiedRequirementIdentifiers.isEmpty();
    }

    private static Set<String> declaredRequirementIdentifiers(
            ExecutableOperationDefinition operation
    ) {
        Set<String> identifiers = unconditionalRequirementIdentifiers(
                operation
        );
        operation.conditionalRequirements().stream()
                .map(ExecutableConditionalRequirementDefinition::identifier)
                .forEach(identifiers::add);
        return identifiers;
    }

    private static Set<String> unconditionalRequirementIdentifiers(
            ExecutableOperationDefinition operation
    ) {
        Set<String> identifiers = new HashSet<>();
        operation.unconditionalRequirements().stream()
                .map(ExecutableRequirementDefinition::identifier)
                .forEach(identifiers::add);
        return identifiers;
    }
}
