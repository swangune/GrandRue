package grandrue.runtime;

import mainstreet.runtime.RequirementEvaluation;

/**
 * Capability-owned, typed command evaluator for operation requirements. It
 * observes a command and its captured execution context but must not mutate
 * authoritative state.
 *
 * @param <C> capability command type
 */
@FunctionalInterface
public interface OperationRequirementEvaluator<C> {

    /**
     * Determines which declared requirements apply and which are satisfied by
     * the supplied command context.
     */
    RequirementEvaluation evaluate(
            C command,
            OperationExecutionContext context
    );
}
