package mainstreet.api;

import mainstreet.protection.ProtectionTarget;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiCommandContractDefinitionTest {

    @Test
    void retains_static_command_contract_responsibilities_without_transport_routing() {
        ProtectionTarget target = new ProtectionTarget("public-order-create");
        ApiCommandContractDefinition definition = new ApiCommandContractDefinition(
                registration(ApiContractKind.COMMAND),
                Set.of("anonymous-public-principal"),
                "public-order-create-input",
                Optional.of("ordering-client-logical-retry"),
                Optional.of("ordering-expected-catalogue-revision"),
                "ordering-owner-synchronous-completion",
                "public-order-create-result",
                "public-order-create-rejection",
                Optional.of(target),
                "customer-order-input-protected-data"
        );

        assertEquals(target, definition.protectionTarget().orElseThrow());
        assertEquals(
                Set.of("anonymous-public-principal"),
                definition.eligiblePrincipalClassReferences()
        );
        assertEquals(
                "ordering-client-logical-retry",
                definition.logicalRetryMechanismReference().orElseThrow()
        );
        assertTrue(definition.registration().identity().contractIdentifier()
                .equals("public-order-create"));
    }

    @Test
    void rejects_non_command_registration() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiCommandContractDefinition(
                        registration(ApiContractKind.QUERY),
                        Set.of("anonymous-public-principal"),
                        "input",
                        Optional.empty(),
                        Optional.empty(),
                        "completion",
                        "result",
                        "rejection",
                        Optional.empty(),
                        "classification"
                )
        );
    }

    @Test
    void requires_principal_input_completion_result_rejection_and_data_contracts() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiCommandContractDefinition(
                        registration(ApiContractKind.COMMAND),
                        Set.of(),
                        "input",
                        Optional.empty(),
                        Optional.empty(),
                        "completion",
                        "result",
                        "rejection",
                        Optional.empty(),
                        "classification"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiCommandContractDefinition(
                        registration(ApiContractKind.COMMAND),
                        Set.of("principal"),
                        " ",
                        Optional.empty(),
                        Optional.empty(),
                        "completion",
                        "result",
                        "rejection",
                        Optional.empty(),
                        "classification"
                )
        );
    }

    @Test
    void optional_retry_concurrency_and_protection_contracts_are_explicit_and_immutable() {
        ApiCommandContractDefinition definition = new ApiCommandContractDefinition(
                registration(ApiContractKind.COMMAND),
                Set.of("merchant-controller", "authorised-staff"),
                "input",
                Optional.empty(),
                Optional.empty(),
                "completion",
                "result",
                "rejection",
                Optional.empty(),
                "classification"
        );

        assertTrue(definition.logicalRetryMechanismReference().isEmpty());
        assertTrue(definition.optimisticConcurrencyPreconditionReference().isEmpty());
        assertTrue(definition.protectionTarget().isEmpty());
        assertThrows(
                UnsupportedOperationException.class,
                () -> definition.eligiblePrincipalClassReferences().clear()
        );
    }

    private static ApiContractRegistration registration(ApiContractKind kind) {
        return new ApiContractRegistration(
                new ApiContractIdentity("ordering", "public-order-create"),
                ApiSurfaceClass.PUBLIC,
                kind,
                new ApiOwnerContractReference("ordering", "order-create"),
                "trusted-public-merchant-scope"
        );
    }
}
