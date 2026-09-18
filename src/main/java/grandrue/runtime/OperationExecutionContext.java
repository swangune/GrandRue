package grandrue.runtime;

import grandrue.application.MerchantScope;
import mainstreet.semantic.executable.ApplicableOperation;

import java.util.Objects;

/**
 * Coherent execution-time context propagated to a capability-owned handler.
 * It binds an identity-only execution principal and explicit merchant scope to
 * the exact executable-model snapshot from which the operation was resolved.
 *
 * <p>The context carries trusted-boundary results; it is not by itself an
 * authorisation decision. The handler still performs final validation inside
 * the consistency boundary protecting its mutation.</p>
 */
public record OperationExecutionContext(
        MerchantScope merchantScope,
        ExecutionPrincipal principal,
        ApplicableOperation applicableOperation
) {

    public OperationExecutionContext {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(principal, "principal");
        Objects.requireNonNull(
                applicableOperation,
                "applicableOperation"
        );
        if (!merchantScope.merchantIdentifier().equals(
                applicableOperation.model().merchantIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Applicable operation belongs to another merchant scope"
            );
        }
    }
}
