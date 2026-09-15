package mainstreet.semantic.capability;

import mainstreet.semantic.Operation;

import java.util.Objects;

/**
 * Declares a semantic relationship between two independently owned operations.
 *
 * This model records composition only; it does not execute either operation.
 */
public final class OperationComposition {

    private final Operation trigger;
    private final Operation consequence;

    public OperationComposition(Operation trigger, Operation consequence) {
        this.trigger = Objects.requireNonNull(trigger, "trigger must not be null");
        this.consequence = Objects.requireNonNull(consequence, "consequence must not be null");
    }

    public Operation trigger() {
        return trigger;
    }

    public Operation consequence() {
        return consequence;
    }
}
