package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.semantic.configuration.ConfigurationReleaseActivation;
import grandrue.semantic.executable.ActiveOperationResolver;
import grandrue.semantic.executable.ApplicableOperation;
import grandrue.semantic.executable.ExecutableConditionalRequirementDefinition;
import grandrue.semantic.executable.ExecutableMerchantModel;
import grandrue.semantic.executable.ExecutableObjectCreationEffect;
import grandrue.semantic.executable.ExecutableOperationalObjectDefinition;
import grandrue.semantic.executable.ExecutableOperationalObjectTypeIdentity;
import grandrue.semantic.executable.ExecutableOperationDefinition;
import grandrue.semantic.executable.ExecutableRequirementDefinition;
import grandrue.testing.TestConfigurationReleases;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OperationRequirementEvaluationTest {

    private static final MerchantScope SCOPE = new MerchantScope("merchant-a");
    private static final ExecutableOperationalObjectTypeIdentity ORDER_TYPE =
            new ExecutableOperationalObjectTypeIdentity("fulfilment", "order");

    @Test
    void unsatisfied_applicable_requirement_rejects_before_the_handler() {
        AtomicBoolean invoked = new AtomicBoolean();
        ScopedOperationDispatcher<FulfilmentCommand> dispatcher = dispatcher(
                deliveryRequirements(),
                fulfilmentEvaluator(),
                (command, context) -> {
                    invoked.set(true);
                    return fulfilment(context);
                }
        );

        UnsatisfiedOperationRequirementsException failure = assertThrows(
                UnsatisfiedOperationRequirementsException.class,
                () -> dispatcher.dispatch(
                        trustedContext(),
                        "order.confirm",
                        new FulfilmentCommand(true, null)
                )
        );

        assertEquals(Set.of("delivery.address"), failure.requirementIdentifiers());
        assertFalse(invoked.get());
    }

    @Test
    void irrelevant_conditional_requirement_is_not_fabricated() {
        AtomicBoolean invoked = new AtomicBoolean();
        ScopedOperationDispatcher<FulfilmentCommand> dispatcher = dispatcher(
                deliveryRequirements(),
                fulfilmentEvaluator(),
                (command, context) -> {
                    invoked.set(true);
                    return fulfilment(context);
                }
        );

        dispatcher.dispatch(
                trustedContext(),
                "order.confirm",
                new FulfilmentCommand(false, null)
        );
        assertTrue(invoked.get());
    }

    @Test
    void evaluation_cannot_invent_an_undeclared_requirement() {
        ApplicableOperation operation = applicableOperation(deliveryRequirements());
        assertThrows(
                IllegalArgumentException.class,
                () -> new RequirementEvaluation(
                        operation,
                        Set.of("payment.card"),
                        Set.of()
                )
        );
    }

    @Test
    void evaluation_cannot_omit_an_unconditional_requirement() {
        ApplicableOperation operation = applicableOperation(
                new ExecutableOperationDefinition(
                        "order.confirm",
                        List.of(new ExecutableObjectCreationEffect(
                                ORDER_TYPE,
                                "confirmed"
                        )),
                        List.of(new ExecutableRequirementDefinition(
                                "customer.contact"
                        )),
                        List.of(),
                        Set.of("order.confirmed"),
                        "order.confirm"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new RequirementEvaluation(operation, Set.of(), Set.of())
        );
    }

    @Test
    void satisfaction_cannot_be_reported_for_an_irrelevant_requirement() {
        ApplicableOperation operation = applicableOperation(deliveryRequirements());
        assertThrows(
                IllegalArgumentException.class,
                () -> new RequirementEvaluation(
                        operation,
                        Set.of(),
                        Set.of("delivery.address")
                )
        );
    }

    @Test
    void dispatcher_without_a_requirement_evaluator_fails_closed() {
        AtomicBoolean invoked = new AtomicBoolean();
        ConfigurationReleaseActivation activation = activation(deliveryRequirements());
        ScopedOperationDispatcher<FulfilmentCommand> dispatcher =
                new ScopedOperationDispatcher<>(
                        new ActiveOperationResolver(activation),
                        List.of(privilegeGuard()),
                        (command, context) -> {
                            invoked.set(true);
                            return fulfilment(context);
                        }
                );

        assertThrows(
                IllegalStateException.class,
                () -> dispatcher.dispatch(
                        trustedContext(),
                        "order.confirm",
                        new FulfilmentCommand(false, null)
                )
        );
        assertFalse(invoked.get());
    }

    private static OperationRequirementEvaluator<FulfilmentCommand>
            fulfilmentEvaluator() {
        return (command, context) -> {
            Set<String> applicable = command.delivery()
                    ? Set.of("delivery.address")
                    : Set.of();
            Set<String> satisfied = command.delivery()
                    && command.deliveryAddress() != null
                    && !command.deliveryAddress().isBlank()
                    ? Set.of("delivery.address")
                    : Set.of();
            return new RequirementEvaluation(
                    context.applicableOperation(),
                    applicable,
                    satisfied
            );
        };
    }

    private static ScopedOperationDispatcher<FulfilmentCommand> dispatcher(
            ExecutableOperationDefinition operation,
            OperationRequirementEvaluator<FulfilmentCommand> evaluator,
            CapabilityOperationHandler<FulfilmentCommand> handler
    ) {
        ConfigurationReleaseActivation activation = activation(operation);
        return new ScopedOperationDispatcher<>(
                new ActiveOperationResolver(activation),
                List.of(privilegeGuard()),
                evaluator,
                handler
        );
    }

    private static OperationExecutionGuard privilegeGuard() {
        return new PrivilegeOperationExecutionGuard(
                (scope, principal, privilege) ->
                        scope.equals(SCOPE)
                                && principal.identifier().equals("staff-1")
                                && privilege.identifier().equals("order.confirm")
        );
    }

    private static TrustedExecutionContext trustedContext() {
        return new TrustedExecutionContext(
                SCOPE,
                authorizedPrincipal(),
                Optional.empty()
        );
    }

    private static OperationFulfilment fulfilment(OperationExecutionContext context) {
        return OperationFulfilment.conformingTo(
                context.applicableOperation(),
                context.applicableOperation().operation().effects(),
                Set.of("order.confirmed")
        );
    }

    private static ApplicableOperation applicableOperation(
            ExecutableOperationDefinition operation
    ) {
        return new ActiveOperationResolver(activation(operation)).resolve(
                SCOPE,
                operation.identifier()
        );
    }

    private static ConfigurationReleaseActivation activation(
            ExecutableOperationDefinition operation
    ) {
        TestConfigurationReleases releases = new TestConfigurationReleases();
        releases.activate(new ExecutableMerchantModel(
                SCOPE.merchantIdentifier(),
                "configuration-1",
                1,
                "semantic-registry-1.0",
                Set.of("fulfilment"),
                List.of(new ExecutableOperationalObjectDefinition(
                        "fulfilment",
                        "order",
                        Set.of("confirmed"),
                        "confirmed"
                )),
                List.of(operation)
        ));
        return releases.activation();
    }

    private static ExecutableOperationDefinition deliveryRequirements() {
        return new ExecutableOperationDefinition(
                "order.confirm",
                List.of(new ExecutableObjectCreationEffect(
                        ORDER_TYPE,
                        "confirmed"
                )),
                List.of(),
                List.of(new ExecutableConditionalRequirementDefinition(
                        "delivery.address",
                        "fulfilment.delivery"
                )),
                Set.of("order.confirmed"),
                "order.confirm"
        );
    }

    private static ExecutionPrincipal authorizedPrincipal() {
        return new ExecutionPrincipal("staff-1");
    }

    private record FulfilmentCommand(boolean delivery, String deliveryAddress) {
    }
}
