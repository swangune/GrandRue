package grandrue.runtime;

import grandrue.application.MerchantScope;
import mainstreet.semantic.executable.ApplicableOperation;
import mainstreet.semantic.execution.ExecutableSupportAdmission;
import mainstreet.runtime.ExecutionPrincipal;

import java.util.Objects;

/**
 * ADR-012 runtime guard that rejects a configuration-dependent invocation
 * unless the concrete implementation path has proven support for the exact
 * captured semantic execution requirement.
 */
public final class ExecutableSupportOperationExecutionGuard
        implements OperationExecutionGuard {

    private final ExecutableSupportAdmission supportAdmission;

    public ExecutableSupportOperationExecutionGuard(
            ExecutableSupportAdmission supportAdmission
    ) {
        this.supportAdmission = Objects.requireNonNull(
                supportAdmission,
                "supportAdmission"
        );
    }

    @Override
    public void validate(
            MerchantScope merchantScope,
            ExecutionPrincipal principal,
            ApplicableOperation operation
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(principal, "principal");
        Objects.requireNonNull(operation, "operation");
        supportAdmission.requireSupport(operation);
    }
}
