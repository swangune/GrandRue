package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.semantic.executable.ActiveOperationResolver;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.ExecutableObjectCreationEffect;
import grandrue.semantic.executable.ExecutableOperationalObjectDefinition;
import grandrue.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import grandrue.semantic.executable.ExecutableOperationDefinition;
import grandrue.semantic.execution.ExecutableSupportAdmission;
import grandrue.semantic.execution.ExecutableSupportManifest;
import grandrue.semantic.execution.ExecutableSupportRegistry;
import grandrue.semantic.execution.ExecutableSupportRequirement;
import grandrue.semantic.execution.SemanticExecutionContractReference;
import grandrue.semantic.execution.UnsupportedExecutableSupportException;
import grandrue.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExecutableSupportOperationExecutionGuardTest {

    private static final MerchantScope SCOPE = new MerchantScope("merchant-a");
    private static final ExecutableOperationalObjectTypeIdentity BOOKING =
            new ExecutableOperationalObjectTypeIdentity("booking", "booking");

    @Test
    void supported_exact_contract_reaches_the_capability_handler() {
        Fixture fixture = fixture("semantic-registry-1.0");
        AtomicBoolean invoked = new AtomicBoolean();
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                dispatcher(
                        fixture,
                        supportGuard(
                                "semantic-registry-1.0",
                                "booking.create"
                        ),
                        (command, context) -> {
                            invoked.set(true);
                            return fulfilment(context);
                        }
                );

        var fulfilment = dispatcher.dispatch(
                trustedContext(),
                "booking.create",
                new CreateBooking("booking-1")
        );

        assertTrue(invoked.get());
        assertSame(fixture.model(), fulfilment.applicableOperation().model());
    }

    @Test
    void matching_operation_identifier_on_an_unproven_release_never_reaches_handler() {
        Fixture fixture = fixture("semantic-registry-2.0");
        AtomicBoolean invoked = new AtomicBoolean();
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                dispatcher(
                        fixture,
                        supportGuard(
                                "semantic-registry-1.0",
                                "booking.create"
                        ),
                        (command, context) -> {
                            invoked.set(true);
                            return fulfilment(context);
                        }
                );

        assertThrows(
                UnsupportedExecutableSupportException.class,
                () -> dispatcher.dispatch(
                        trustedContext(),
                        "booking.create",
                        new CreateBooking("booking-1")
                )
        );
        assertFalse(invoked.get());
    }

    @Test
    void requirement_from_another_semantic_release_is_rejected_before_registry_admission() {
        Fixture fixture = fixture("semantic-registry-1.0");
        AtomicBoolean invoked = new AtomicBoolean();
        ExecutableSupportRegistry registry = new ExecutableSupportRegistry(
                List.of(new ExecutableSupportManifest(
                        "booking-handler",
                        Set.of(reference(
                                "semantic-registry-2.0",
                                "booking.create"
                        ))
                ))
        );
        ExecutableSupportAdmission admission = new ExecutableSupportAdmission(
                registry,
                "booking-handler",
                operation -> new ExecutableSupportRequirement(reference(
                        "semantic-registry-2.0",
                        operation.operation().identifier()
                ))
        );
        ScopedOperationDispatcher<CreateBooking> dispatcher = dispatcher(
                fixture,
                new ExecutableSupportOperationExecutionGuard(admission),
                (command, context) -> {
                    invoked.set(true);
                    return fulfilment(context);
                }
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> dispatcher.dispatch(
                        trustedContext(),
                        "booking.create",
                        new CreateBooking("booking-1")
                )
        );
        assertFalse(invoked.get());
    }

    @Test
    void compatible_activation_during_an_earlier_guard_preserves_the_captured_support_snapshot() {
        Fixture fixture = fixture("semantic-registry-1.0");
        ExecutableMerchantModel replacement = model(
                2,
                "semantic-registry-1.0"
        );
        OperationExecutionGuard activateReplacement =
                (scope, principal, operation) ->
                        fixture.releases().activate(replacement);
        AtomicBoolean invoked = new AtomicBoolean();
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                new ScopedOperationDispatcher<>(
                        fixture.resolver(),
                        List.of(
                                privilegeGuard(),
                                activateReplacement,
                                supportGuard(
                                        "semantic-registry-1.0",
                                        "booking.create"
                                )
                        ),
                        (command, context) -> {
                            invoked.set(true);
                            return fulfilment(context);
                        }
                );

        var fulfilment = dispatcher.dispatch(
                trustedContext(),
                "booking.create",
                new CreateBooking("booking-1")
        );

        assertTrue(invoked.get());
        assertSame(fixture.model(), fulfilment.applicableOperation().model());
        assertSame(
                replacement,
                fixture.releases().activation().current("merchant-a")
                        .orElseThrow().release().executableModel()
        );
    }

    private static ScopedOperationDispatcher<CreateBooking> dispatcher(
            Fixture fixture,
            OperationExecutionGuard supportGuard,
            CapabilityOperationHandler<CreateBooking> handler
    ) {
        return new ScopedOperationDispatcher<>(
                fixture.resolver(),
                List.of(privilegeGuard(), supportGuard),
                handler
        );
    }

    private static OperationExecutionGuard supportGuard(
            String supportedRelease,
            String contractIdentifier
    ) {
        SemanticExecutionContractReference supported = reference(
                supportedRelease,
                contractIdentifier
        );
        ExecutableSupportRegistry registry = new ExecutableSupportRegistry(
                List.of(new ExecutableSupportManifest(
                        "booking-handler",
                        Set.of(supported)
                ))
        );
        ExecutableSupportAdmission admission = new ExecutableSupportAdmission(
                registry,
                "booking-handler",
                operation -> new ExecutableSupportRequirement(reference(
                        operation.model().semanticRegistryVersion(),
                        operation.operation().identifier()
                ))
        );
        return new ExecutableSupportOperationExecutionGuard(admission);
    }

    private static SemanticExecutionContractReference reference(
            String releaseIdentifier,
            String contractIdentifier
    ) {
        return new SemanticExecutionContractReference(
                releaseIdentifier,
                contractIdentifier
        );
    }

    private static OperationExecutionGuard privilegeGuard() {
        return new PrivilegeOperationExecutionGuard(
                (scope, principal, privilege) ->
                        scope.equals(SCOPE)
                                && principal.identifier().equals("staff-1")
                                && privilege.identifier().equals("booking.create")
        );
    }

    private static OperationFulfilment fulfilment(
            OperationExecutionContext context
    ) {
        return OperationFulfilment.conformingTo(
                context.applicableOperation(),
                context.applicableOperation().operation().effects(),
                Set.of("booking.created")
        );
    }

    private static TrustedExecutionContext trustedContext() {
        return new TrustedExecutionContext(
                SCOPE,
                new ExecutionPrincipal("staff-1"),
                Optional.empty()
        );
    }

    private static Fixture fixture(String semanticRegistryVersion) {
        TestConfigurationReleases releases = new TestConfigurationReleases();
        ExecutableMerchantModel model = model(1, semanticRegistryVersion);
        releases.activate(model);
        return new Fixture(
                model,
                releases,
                new ActiveOperationResolver(releases.activation())
        );
    }

    private static ExecutableMerchantModel model(
            long version,
            String semanticRegistryVersion
    ) {
        ExecutableOperationDefinition operation =
                new ExecutableOperationDefinition(
                        "booking.create",
                        List.of(new ExecutableObjectCreationEffect(
                                BOOKING,
                                "requested"
                        )),
                        Set.of("booking.created"),
                        "booking.create"
                );
        return new ExecutableMerchantModel(
                SCOPE.merchantIdentifier(),
                "configuration-" + version,
                version,
                semanticRegistryVersion,
                Set.of("booking"),
                List.of(new ExecutableOperationalObjectDefinition(
                        "booking",
                        "booking",
                        Set.of("requested"),
                        "requested"
                )),
                List.of(operation)
        );
    }

    private record Fixture(
            ExecutableMerchantModel model,
            TestConfigurationReleases releases,
            ActiveOperationResolver resolver
    ) {
    }

    private record CreateBooking(String bookingIdentifier) {
    }
}
