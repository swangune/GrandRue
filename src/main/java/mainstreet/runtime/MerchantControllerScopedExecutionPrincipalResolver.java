package mainstreet.runtime;

import mainstreet.application.MerchantScope;
import mainstreet.merchantaccount.MerchantControllerRelationship;
import mainstreet.merchantaccount.MerchantControllerRelationshipAuthority;

import java.util.Objects;
import java.util.Optional;

/**
 * Establishes a contextual execution principal only when the authenticated
 * Identity is the current active Controller for the already-resolved Merchant
 * Scope.
 *
 * <p>The resulting principal carries attribution only. Controller relationship
 * and operation privileges remain separate current authorities.</p>
 */
public final class MerchantControllerScopedExecutionPrincipalResolver
        implements ScopedExecutionPrincipalResolver {

    private final MerchantControllerRelationshipAuthority controllerAuthority;

    public MerchantControllerScopedExecutionPrincipalResolver(
            MerchantControllerRelationshipAuthority controllerAuthority
    ) {
        this.controllerAuthority = Objects.requireNonNull(
                controllerAuthority,
                "controllerAuthority"
        );
    }

    @Override
    public ExecutionPrincipal resolve(
            MerchantScope merchantScope,
            String authenticatedIdentityIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(authenticatedIdentityIdentifier);

        Optional<MerchantControllerRelationship> current =
                controllerAuthority.activeController(merchantScope);
        if (current.isEmpty()) {
            return null;
        }

        MerchantControllerRelationship relationship = current.orElseThrow();
        if (!relationship.merchantScope().equals(merchantScope)
                || !relationship.identityIdentifier().equals(
                        authenticatedIdentityIdentifier
                )) {
            return null;
        }

        return new ExecutionPrincipal(authenticatedIdentityIdentifier);
    }

    private static void requireIdentifier(String identityIdentifier) {
        if (identityIdentifier == null || identityIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Authenticated identity identifier must not be blank"
            );
        }
    }
}
