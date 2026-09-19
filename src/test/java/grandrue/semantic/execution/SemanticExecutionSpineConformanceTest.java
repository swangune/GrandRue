package grandrue.semantic.execution;

import grandrue.application.MerchantScope;
import grandrue.runtime.CapabilityOperationHandler;
import grandrue.runtime.ExecutableSupportOperationExecutionGuard;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.OperationFulfilment;
import grandrue.runtime.ScopedOperationDispatcher;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.compiler.ConfigurationCompiler;
import grandrue.semantic.configuration.ConfigurationActivationRequest;
import grandrue.semantic.configuration.ConfigurationActivationStatus;
import grandrue.semantic.configuration.ConfigurationPackageResolver;
import grandrue.semantic.configuration.ConfigurationRelease;
import grandrue.semantic.configuration.ConfigurationRevisionApproval;
import grandrue.semantic.configuration.InMemoryConfigurationPublication;
import grandrue.semantic.configuration.InMemoryConfigurationReleaseActivation;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.configuration.ResolvedConfigurationPackage;
import grandrue.semantic.executable.ActiveOperationResolver;
import grandrue.semantic.registry.InMemorySemanticRegistry;
import grandrue.semantic.registry.OwnedOperationDefinition;
import grandrue.semantic.registry.OwnedOperationalObjectDefinition;
import grandrue.semantic.registry.RegisteredCapability;
import grandrue.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end IMP-03 proof that one registered semantic operation is compiled,
 * packaged, activated, resolved with exact provenance, admitted against
 * ADR-012 executable-support evidence and executed by a capability-owned
 * handler without introducing a parallel semantic path.
 */
class SemanticExecutionSpineConformanceTest {

    private static final String MERCHANT = "merchant-001";
    private static final String PRINCIPAL = "merchant-controller-001";
    private static final String SEMANTIC_RELEASE = "semantic-registry-1.0";
    private static final String OPERATION = "booking.create";
    private static final Instant NOW = Instant.parse("2026-08-28T09:00:00Z");

    @Test
    void registered_operation_executes_through_the_exact_compiled_and_supported_context() {
        InMemorySemanticRegistry semanticRegistry = semanticRegistry();
        MerchantConfiguration configuration = new MerchantConfiguration(
                MERCHANT,
                "configuration-001",
                1,
                SEMANTIC_RELEASE,
                Set.of("booking")
        );
        ResolvedConfigurationPackage resolvedPackage =
                new ConfigurationPackageResolver(
                        new ConfigurationCompiler(semanticRegistry)
                ).resolve(
                        configuration,
                        "mainstreet-compiler-1",
                        NOW.minusSeconds(60)
                );

        ConfigurationRelease release = new ConfigurationRelease(
                "configuration-release-001",
                configuration,
                resolvedPackage
        );
        InMemoryConfigurationPublication publication =
                new InMemoryConfigurationPublication();
        publication.publish(release);
        InMemoryConfigurationReleaseActivation activation =
                new InMemoryConfigurationReleaseActivation(
                        publication,
                        (merchant, revision) -> Optional.of(
                                new ConfigurationRevisionApproval(
                                        merchant,
                                        revision,
                                        PRINCIPAL,
                                        NOW.minusSeconds(30)
                                )
                        ),
                        (principal, merchant, revision) -> true,
                        Clock.fixed(NOW, ZoneOffset.UTC)
                );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(new ConfigurationActivationRequest(
                        "activate-configuration-001",
                        release.releaseIdentifier(),
                        Optional.empty(),
                        PRINCIPAL
                )).status()
        );

        ExecutableSupportRegistry supportRegistry =
                new ExecutableSupportRegistry(List.of(
                        new ExecutableSupportManifest(
                                "booking-handler",
                                Set.of(new SemanticExecutionContractReference(
                                        SEMANTIC_RELEASE,
                                        OPERATION
                                ))
                        )
                ));
        ExecutableSupportAdmission supportAdmission =
                new ExecutableSupportAdmission(
                        supportRegistry,
                        "booking-handler",
                        operation -> new ExecutableSupportRequirement(
                                new SemanticExecutionContractReference(
                                        operation.model().semanticRegistryVersion(),
                                        operation.operation().identifier()
                                )
                        )
                );
        AtomicBoolean invoked = new AtomicBoolean();
        CapabilityOperationHandler<CreateBooking> handler =
                (command, executionContext) -> {
                    invoked.set(true);
                    return OperationFulfilment.conformingTo(
                            executionContext.applicableOperation(),
                            executionContext.applicableOperation()
                                    .operation()
                                    .effects(),
                            Set.of("BookingCreated")
                    );
                };
        ScopedOperationDispatcher<CreateBooking> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(activation),
                        List.of(
                                (scope, principal, operation) -> { },
                                new ExecutableSupportOperationExecutionGuard(
                                        supportAdmission
                                )
                        ),
                        handler
                );

        OperationFulfilment fulfilment = dispatcher.dispatch(
                new TrustedExecutionContext(
                        new MerchantScope(MERCHANT),
                        new ExecutionPrincipal("staff-001"),
                        Optional.empty()
                ),
                OPERATION,
                new CreateBooking("booking-001")
        );

        assertTrue(invoked.get());
        assertSame(
                resolvedPackage.executableSemanticModel(),
                fulfilment.applicableOperation().model()
        );
        assertEquals(
                release.releaseIdentifier(),
                fulfilment.applicableOperation().releaseIdentifier()
        );
        assertEquals(
                SEMANTIC_RELEASE,
                fulfilment.applicableOperation()
                        .model()
                        .semanticRegistryVersion()
        );
        assertEquals(
                OPERATION,
                fulfilment.applicableOperation().operation().identifier()
        );
    }

    private static InMemorySemanticRegistry semanticRegistry() {
        InMemorySemanticRegistry registry = new InMemorySemanticRegistry();
        registry.publish(new SemanticRegistrySnapshot(
                SEMANTIC_RELEASE,
                Set.of(new RegisteredCapability(
                        "booking",
                        List.of(new OwnedOperationalObjectDefinition(
                                "booking",
                                Set.of("requested"),
                                "requested"
                        )),
                        List.of(OwnedOperationDefinition.creation(
                                OPERATION,
                                "booking",
                                "requested",
                                "BookingCreated",
                                "booking.create"
                        ))
                ))
        ));
        return registry;
    }

    private record CreateBooking(String bookingIdentifier) {
    }
}
