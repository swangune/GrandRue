package grandrue.runtime;

import grandrue.semantic.executable.ApplicableOperation;
import grandrue.semantic.executable.ExecutableOperationEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable semantic evidence reported by a capability-owned operation
 * handler. It retains the exact captured model provenance and permits only
 * effects and committed event identities declared by that operation.
 *
 * <p>This evidence does not perform or prove persistence by itself. The
 * capability handler must produce it within the consistency and ownership
 * boundaries required by the authoritative mutation.</p>
 */
public final class OperationFulfilment {

    private final ApplicableOperation applicableOperation;
    private final List<ExecutableOperationEffect> effects;
    private final Set<String> committedEventIdentifiers;

    private OperationFulfilment(
            ApplicableOperation applicableOperation,
            List<ExecutableOperationEffect> effects,
            Set<String> committedEventIdentifiers
    ) {
        this.applicableOperation = applicableOperation;
        this.effects = effects;
        this.committedEventIdentifiers = committedEventIdentifiers;
    }

    /**
     * Creates outcome evidence after verifying that the handler has not
     * extended the captured operation with undeclared semantics.
     */
    public static OperationFulfilment conformingTo(
            ApplicableOperation applicableOperation,
            List<ExecutableOperationEffect> effects,
            Set<String> committedEventIdentifiers
    ) {
        Objects.requireNonNull(
                applicableOperation,
                "applicableOperation"
        );
        List<ExecutableOperationEffect> copiedEffects = List.copyOf(
                Objects.requireNonNull(effects, "effects")
        );
        Set<String> copiedEvents = Set.copyOf(
                Objects.requireNonNull(
                        committedEventIdentifiers,
                        "committedEventIdentifiers"
                )
        );
        if (!containsDeclaredEffectOccurrences(
                applicableOperation.operation().effects(),
                copiedEffects
        )) {
            throw new IllegalArgumentException(
                    "Operation fulfilment contains an undeclared effect"
            );
        }
        if (!applicableOperation.operation().eventIdentifiers()
                .containsAll(copiedEvents)) {
            throw new IllegalArgumentException(
                    "Operation fulfilment contains an undeclared event"
            );
        }
        return new OperationFulfilment(
                applicableOperation,
                copiedEffects,
                copiedEvents
        );
    }

    private static boolean containsDeclaredEffectOccurrences(
            List<ExecutableOperationEffect> declaredEffects,
            List<ExecutableOperationEffect> reportedEffects
    ) {
        List<ExecutableOperationEffect> remaining =
                new ArrayList<>(declaredEffects);
        for (ExecutableOperationEffect effect : reportedEffects) {
            if (!remaining.remove(effect)) {
                return false;
            }
        }
        return true;
    }

    public ApplicableOperation applicableOperation() {
        return applicableOperation;
    }

    public List<ExecutableOperationEffect> effects() {
        return effects;
    }

    public Set<String> committedEventIdentifiers() {
        return committedEventIdentifiers;
    }
}
