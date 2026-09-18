package grandrue.runtime;

import grandrue.application.MerchantScope;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.OperationExecutionGuard;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.executable.ApplicableOperation;

import java.util.Objects;

/**
 * Enforces the privilege requirement compiled into an applicable operation by
 * consulting current actor-authorisation authority for the explicit Merchant
 * Scope.
 *
 * <p>The execution principal carries identity only. This guard does not cache
 * or embed privileges on the principal, and privilege alone is not universal
 * contextual authorisation.</p>
 */
public final class PrivilegeOperationExecutionGuard
        implements OperationExecutionGuard {

    private final ActorAuthorisationAuthority authorisationAuthority;

    public PrivilegeOperationExecutionGuard(
            ActorAuthorisationAuthority authorisationAuthority
    ) {
        this.authorisationAuthority = Objects.requireNonNull(
                authorisationAuthority,
                "authorisationAuthority"
        );
    }

    @Override
    public void validate(
            MerchantScope merchantScope,
            ExecutionPrincipal principal,
            ApplicableOperation applicableOperation
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(principal, "principal");
        Objects.requireNonNull(applicableOperation, "applicableOperation");
        if (!merchantScope.merchantIdentifier().equals(
                applicableOperation.model().merchantIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Applicable operation belongs to another merchant scope"
            );
        }

        Privilege required = new Privilege(
                applicableOperation.operation()
                        .requiredPrivilegeIdentifier()
        );
        if (!authorisationAuthority.isAuthorised(
                merchantScope,
                principal,
                required
        )) {
            throw new AuthorizationException(
                    "Principal lacks privilege in merchant scope: "
                            + required.identifier()
            );
        }
    }
}
