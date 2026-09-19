package mainstreet.runtime;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.executable.ActiveOperationResolver;
import mainstreet.semantic.executable.ApplicableOperation;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.executable.ExecutableObjectCreationEffect;
import mainstreet.semantic.executable.ExecutableOperationalObjectDefinition;
import mainstreet.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import mainstreet.semantic.executable.ExecutableOperationDefinition;
import grandrue.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegeOperationExecutionGuardTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");

    @Test
    void applicable_operation_still_requires_its_declared_privilege() {
        ApplicableOperation operation = applicableOperation("merchant-a");
        OperationExecutionGuard guard = new PrivilegeOperationExecutionGuard(
                (scope, principal, privilege) -> false
        );
        ExecutionPrincipal unauthorized = new ExecutionPrincipal("staff-1");

        assertThrows(
                AuthorizationException.class,
                () -> guard.validate(MERCHANT, unauthorized, operation)
        );
    }

    @Test
    void accepts_authority_resolved_for_the_current_scope_and_required_privilege() {
        ApplicableOperation operation = applicableOperation("merchant-a");
        OperationExecutionGuard guard = new PrivilegeOperationExecutionGuard(
                (scope, principal, privilege) ->
                        scope.equals(MERCHANT)
                                && principal.identifier().equals("staff-1")
                                && privilege.equals(new Privilege("booking.create"))
        );
        ExecutionPrincipal authorized = new ExecutionPrincipal("staff-1");

        assertDoesNotThrow(
                () -> guard.validate(MERCHANT, authorized, operation)
        );
    }

    @Test
    void forwards_explicit_scope_principal_and_required_privilege_to_authority() {
        ApplicableOperation operation = applicableOperation("merchant-a");
        AtomicReference<MerchantScope> capturedScope = new AtomicReference<>();
        AtomicReference<ExecutionPrincipal> capturedPrincipal = new AtomicReference<>();
        AtomicReference<Privilege> capturedPrivilege = new AtomicReference<>();
        ActorAuthorisationAuthority authority = (scope, principal, privilege) -> {
            capturedScope.set(scope);
            capturedPrincipal.set(principal);
            capturedPrivilege.set(privilege);
            return true;
        };
        OperationExecutionGuard guard = new PrivilegeOperationExecutionGuard(authority);
        ExecutionPrincipal principal = new ExecutionPrincipal("staff-1");

        guard.validate(MERCHANT, principal, operation);

        assertSame(MERCHANT, capturedScope.get());
        assertSame(principal, capturedPrincipal.get());
        assertEquals(new Privilege("booking.create"), capturedPrivilege.get());
    }

    @Test
    void same_principal_authority_does_not_leak_between_merchants() {
        MerchantScope merchantA = new MerchantScope("merchant-a");
        MerchantScope merchantB = new MerchantScope("merchant-b");
        ApplicableOperation operationA = applicableOperation("merchant-a");
        ApplicableOperation operationB = applicableOperation("merchant-b");
        ActorAuthorisationAuthority authority = (scope, principal, privilege) ->
                scope.equals(merchantA);
        OperationExecutionGuard guard = new PrivilegeOperationExecutionGuard(authority);
        ExecutionPrincipal principal = new ExecutionPrincipal("staff-1");

        assertDoesNotThrow(
                () -> guard.validate(merchantA, principal, operationA)
        );
        assertThrows(
                AuthorizationException.class,
                () -> guard.validate(merchantB, principal, operationB)
        );
    }

    private static ApplicableOperation applicableOperation(
            String merchantIdentifier
    ) {
        ExecutableOperationalObjectTypeIdentity bookingType =
                new ExecutableOperationalObjectTypeIdentity(
                        "booking",
                        "booking"
                );
        ExecutableOperationDefinition create = new ExecutableOperationDefinition(
                "booking.create",
                List.of(new ExecutableObjectCreationEffect(
                        bookingType,
                        "requested"
                )),
                Set.of("booking.created"),
                "booking.create"
        );
        ExecutableMerchantModel model = new ExecutableMerchantModel(
                merchantIdentifier,
                "configuration-1",
                1,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(new ExecutableOperationalObjectDefinition(
                        "booking",
                        "booking",
                        Set.of("requested"),
                        "requested"
                )),
                List.of(create)
        );
        TestConfigurationReleases releases = new TestConfigurationReleases();
        releases.activate(model);
        MerchantScope scope = new MerchantScope(merchantIdentifier);
        return new ActiveOperationResolver(releases.activation()).resolve(
                scope,
                "booking.create"
        );
    }
}
