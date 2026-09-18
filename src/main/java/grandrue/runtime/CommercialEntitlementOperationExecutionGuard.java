package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.commercial.CommercialEntitlementAuthority;
import grandrue.commercial.CommercialEntitlementException;
import grandrue.commercial.CommercialEntitlementIdentity;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.semantic.executable.ApplicableOperation;

import java.util.Map;
import java.util.Objects;

/**
 * Applies commercial entitlement at the runtime access boundary after semantic
 * applicability has already been established by operation resolution.
 *
 * <p>The guarded operation remains part of the merchant's executable semantic
 * model when entitlement is absent. This guard therefore controls protected
 * use only; it does not activate, remove or recompile merchant semantics.</p>
 */
public final class CommercialEntitlementOperationExecutionGuard
        implements OperationExecutionGuard {

    private final CommercialEntitlementAuthority entitlementAuthority;
    private final Map<String, CommercialEntitlementIdentity>
            entitlementByOperationIdentifier;

    public CommercialEntitlementOperationExecutionGuard(
            CommercialEntitlementAuthority entitlementAuthority,
            Map<String, CommercialEntitlementIdentity>
                    entitlementByOperationIdentifier
    ) {
        this.entitlementAuthority = Objects.requireNonNull(
                entitlementAuthority,
                "entitlementAuthority"
        );
        Objects.requireNonNull(
                entitlementByOperationIdentifier,
                "entitlementByOperationIdentifier"
        );
        entitlementByOperationIdentifier.forEach((operationIdentifier, entitlement) -> {
            if (operationIdentifier == null || operationIdentifier.isBlank()) {
                throw new IllegalArgumentException(
                        "Commercially gated operation identifier must not be blank"
                );
            }
            Objects.requireNonNull(entitlement, "commercial entitlement identity");
        });
        this.entitlementByOperationIdentifier = Map.copyOf(
                entitlementByOperationIdentifier
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

        CommercialEntitlementIdentity requiredEntitlement =
                entitlementByOperationIdentifier.get(
                        applicableOperation.operation().identifier()
                );
        if (requiredEntitlement == null) {
            return;
        }

        if (!entitlementAuthority.isEntitled(
                merchantScope,
                requiredEntitlement
        )) {
            throw new CommercialEntitlementException(
                    merchantScope.merchantIdentifier(),
                    requiredEntitlement
            );
        }
    }
}
