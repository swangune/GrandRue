package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.runtime.OperationRequirementEvaluator;
import grandrue.runtime.UnsatisfiedOperationRequirementsException;
import mainstreet.semantic.executable.ActiveOperationResolver;
import mainstreet.semantic.executable.ApplicableOperation;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Resolves one operation from one merchant model snapshot, applies generic
 * execution guards, and dispatches the typed command to its capability-owned
 * handler. It performs no authoritative mutation itself.
 *
 * <p>The dispatcher accepts only a Trusted Execution Context already
 * established by the applicable security/application boundary. The trusted
 * context is still not business authority: configured guards consult current
 * authority and the capability-owned handler performs final security-sensitive
 * and concurrency-sensitive validation inside its consistency boundary.</p>
 */
public final class ScopedOperationDispatcher<C> {

    private final ActiveOperationResolver operationResolver;
    private final List<OperationExecutionGuard> guards;
    private final OperationRequirementEvaluator<C> requirementEvaluator;
    private final CapabilityOperationHandler<C> handler;

    public ScopedOperationDispatcher(
            ActiveOperationResolver operationResolver,
            List<OperationExecutionGuard> guards,
            CapabilityOperationHandler<C> handler
    ) {
        this(
                operationResolver,
                guards,
                ScopedOperationDispatcher::evaluateNoDeclaredRequirements,
                handler
        );
    }

    public ScopedOperationDispatcher(
            ActiveOperationResolver operationResolver,
            List<OperationExecutionGuard> guards,
            OperationRequirementEvaluator<C> requirementEvaluator,
            CapabilityOperationHandler<C> handler
    ) {
        this.operationResolver = Objects.requireNonNull(operationResolver);
        this.guards = List.copyOf(Objects.requireNonNull(guards));
        if (this.guards.isEmpty()) {
            throw new IllegalArgumentException(
                    "Scoped operation dispatch requires an execution guard"
            );
        }
        this.requirementEvaluator = Objects.requireNonNull(
                requirementEvaluator
        );
        this.handler = Objects.requireNonNull(handler);
    }

    /**
     * Dispatches a command only after trusted context establishment,
     * scope-bound applicability and every configured generic guard have
     * succeeded.
     */
    public OperationFulfilment dispatch(
            TrustedExecutionContext trustedContext,
            String operationIdentifier,
            C command
    ) {
        Objects.requireNonNull(trustedContext, "trustedContext");
        Objects.requireNonNull(command, "command");
        MerchantScope merchantScope = trustedContext.merchantScope();
        ExecutionPrincipal principal = trustedContext.principal();
        ApplicableOperation operation = operationResolver.resolve(
                merchantScope,
                operationIdentifier
        );
        for (OperationExecutionGuard guard : guards) {
            guard.validate(merchantScope, principal, operation);
        }
        OperationExecutionContext context = new OperationExecutionContext(
                merchantScope,
                principal,
                operation
        );
        RequirementEvaluation requirementEvaluation = Objects.requireNonNull(
                requirementEvaluator.evaluate(command, context),
                "requirement evaluation"
        );
        if (requirementEvaluation.applicableOperation() != operation) {
            throw new IllegalStateException(
                    "Requirement evaluation belongs to another operation snapshot"
            );
        }
        if (!requirementEvaluation.isSatisfied()) {
            throw new UnsatisfiedOperationRequirementsException(
                    requirementEvaluation
                            .unsatisfiedRequirementIdentifiers()
            );
        }
        OperationFulfilment fulfilment = Objects.requireNonNull(
                handler.fulfill(command, context),
                "handler fulfilment"
        );
        if (fulfilment.applicableOperation() != operation) {
            throw new IllegalStateException(
                    "Handler reported fulfilment for another operation snapshot"
            );
        }
        return fulfilment;
    }

    private static <C> RequirementEvaluation evaluateNoDeclaredRequirements(
            C command,
            OperationExecutionContext context
    ) {
        var operation = context.applicableOperation().operation();
        if (!operation.unconditionalRequirements().isEmpty()
                || !operation.conditionalRequirements().isEmpty()) {
            throw new IllegalStateException(
                    "An operation with requirements needs an evaluator"
            );
        }
        return new RequirementEvaluation(
                context.applicableOperation(),
                Set.of(),
                Set.of()
        );
    }
}
