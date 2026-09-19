package grandrue.api;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiCallbackContractDefinitionTest {

    @Test
    void retains_static_callback_ingress_responsibilities() {
        ApiCallbackContractDefinition definition = new ApiCallbackContractDefinition(
                registration(ApiSurfaceClass.INTEGRATION_INGRESS, ApiContractKind.CALLBACK),
                "payment-execution-provider",
                "provider-signature-v2",
                "payment-attempt-and-provider-effect",
                Optional.of("historical-payment-provider-connection"),
                "provider-effect-identity",
                "payment-provider-payload-v3",
                "payments/provider-evidence",
                "durable-evidence-before-acknowledgement",
                "accepted-ingress-only",
                "safe-provider-callback-rejection"
        );

        assertEquals("payment-execution-provider", definition.providerIntegrationRoleReference());
        assertEquals("provider-effect-identity", definition.duplicateReplayIdentityReference());
        assertEquals(
                "historical-payment-provider-connection",
                definition.historicalProviderConnectionBindingContextReference().orElseThrow()
        );
    }

    @Test
    void requires_callback_registration_on_integration_ingress() {
        assertThrows(
                IllegalArgumentException.class,
                () -> definition(registration(
                        ApiSurfaceClass.INTEGRATION_INGRESS,
                        ApiContractKind.COMMAND
                ))
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> definition(registration(ApiSurfaceClass.PUBLIC, ApiContractKind.CALLBACK))
        );
    }

    @Test
    void historical_provider_connection_context_is_explicitly_optional() {
        ApiCallbackContractDefinition definition = definition(registration(
                ApiSurfaceClass.INTEGRATION_INGRESS,
                ApiContractKind.CALLBACK
        ));

        assertTrue(definition.historicalProviderConnectionBindingContextReference().isEmpty());
    }

    @Test
    void rejects_blank_callback_authority_references() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiCallbackContractDefinition(
                        registration(ApiSurfaceClass.INTEGRATION_INGRESS, ApiContractKind.CALLBACK),
                        "provider-role",
                        " ",
                        "correlation",
                        Optional.empty(),
                        "duplicate-identity",
                        "payload-validation",
                        "evidence-owner",
                        "processing-mode",
                        "acknowledgement",
                        "safe-rejection"
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ApiCallbackContractDefinition(
                        registration(ApiSurfaceClass.INTEGRATION_INGRESS, ApiContractKind.CALLBACK),
                        "provider-role",
                        "authentication",
                        "correlation",
                        Optional.of(" "),
                        "duplicate-identity",
                        "payload-validation",
                        "evidence-owner",
                        "processing-mode",
                        "acknowledgement",
                        "safe-rejection"
                )
        );
    }

    private static ApiCallbackContractDefinition definition(ApiContractRegistration registration) {
        return new ApiCallbackContractDefinition(
                registration,
                "provider-role",
                "source-authentication",
                "correlation",
                Optional.empty(),
                "duplicate-identity",
                "payload-validation",
                "evidence-owner",
                "processing-mode",
                "acknowledgement",
                "safe-rejection"
        );
    }

    private static ApiContractRegistration registration(
            ApiSurfaceClass surface,
            ApiContractKind kind
    ) {
        return new ApiContractRegistration(
                new ApiContractIdentity("payments", "provider-evidence-callback"),
                surface,
                kind,
                new ApiOwnerContractReference("payments", "provider-evidence-ingress"),
                "trusted-provider-correlation-scope"
        );
    }
}
