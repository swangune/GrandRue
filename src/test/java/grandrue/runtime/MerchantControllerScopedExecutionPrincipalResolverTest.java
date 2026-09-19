package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.merchantaccount.MerchantControllerRelationship;
import grandrue.merchantaccount.MerchantControllerRelationshipAuthority;
import grandrue.merchantaccount.MerchantControllerRelationshipLifecycle;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MerchantControllerScopedExecutionPrincipalResolverTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");

    @Test
    void authenticated_identity_establishes_principal_only_when_it_is_the_current_controller() {
        MerchantControllerRelationshipAuthority authority = scope -> Optional.of(
                controller("controller-rel-1", "identity-123")
        );
        MerchantControllerScopedExecutionPrincipalResolver resolver =
                new MerchantControllerScopedExecutionPrincipalResolver(authority);

        ExecutionPrincipal principal = resolver.resolve(MERCHANT, "identity-123");

        assertEquals(new ExecutionPrincipal("identity-123"), principal);
        assertNull(resolver.resolve(MERCHANT, "identity-other"));
    }

    @Test
    void absent_current_controller_fails_principal_establishment_without_manufacturing_authority() {
        MerchantControllerScopedExecutionPrincipalResolver resolver =
                new MerchantControllerScopedExecutionPrincipalResolver(
                        scope -> Optional.empty()
                );

        assertNull(resolver.resolve(MERCHANT, "identity-123"));
    }

    @Test
    void controller_transfer_is_observed_from_current_authority_on_each_resolution() {
        AtomicReference<MerchantControllerRelationship> current =
                new AtomicReference<>(controller("controller-rel-1", "identity-old"));
        MerchantControllerScopedExecutionPrincipalResolver resolver =
                new MerchantControllerScopedExecutionPrincipalResolver(
                        scope -> Optional.ofNullable(current.get())
                );

        assertEquals(
                new ExecutionPrincipal("identity-old"),
                resolver.resolve(MERCHANT, "identity-old")
        );

        current.set(controller("controller-rel-2", "identity-new"));

        assertNull(resolver.resolve(MERCHANT, "identity-old"));
        assertEquals(
                new ExecutionPrincipal("identity-new"),
                resolver.resolve(MERCHANT, "identity-new")
        );
    }

    private static MerchantControllerRelationship controller(
            String relationshipIdentity,
            String identity
    ) {
        return new MerchantControllerRelationship(
                relationshipIdentity,
                MERCHANT,
                identity,
                MerchantControllerRelationshipLifecycle.ACTIVE
        );
    }
}
