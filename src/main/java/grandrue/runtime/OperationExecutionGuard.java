package grandrue.runtime;

import grandrue.application.MerchantScope;
import mainstreet.semantic.executable.ApplicableOperation;

/**
 * Validates one authority concern before an applicable operation may mutate
 * authoritative state. Implementations are guards, not permission grants:
 * every required guard and concurrency-sensitive invariant must be
 * revalidated in the operation's consistency boundary.
 */
@FunctionalInterface
public interface OperationExecutionGuard {

    /**
     * Rejects execution when the principal does not satisfy this guard in the
     * explicit merchant scope of the attempted operation.
     */
    void validate(
            MerchantScope merchantScope,
            ExecutionPrincipal principal,
            ApplicableOperation operation
    );

    /**
     * Explicit permissive guard for isolated application/persistence tests.
     * Production composition must supply the applicable current-authority
     * guards and must not use this helper as an authentication substitute.
     */
    static OperationExecutionGuard allowAll() {
        return (merchantScope, principal, operation) -> {
            // Explicitly permissive by construction.
        };
    }
}
