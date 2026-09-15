package mainstreet.api;

import java.util.Objects;
import java.util.Optional;

/**
 * Static production callback-contract responsibilities governed by
 * MS-PROT-035 v1.1. This definition registers trusted evidence ingress; it
 * does not authenticate a request, interpret provider evidence as business
 * truth or execute provider-specific processing.
 */
public record ApiCallbackContractDefinition(
        ApiContractRegistration registration,
        String providerIntegrationRoleReference,
        String sourceAuthenticationMethodReference,
        String correlationRequirementsReference,
        Optional<String> historicalProviderConnectionBindingContextReference,
        String duplicateReplayIdentityReference,
        String payloadValidationRuleReference,
        String evidenceOwnerReference,
        String processingModeReference,
        String acknowledgementSemanticsReference,
        String safeRejectionSemanticsReference
) {
    public ApiCallbackContractDefinition {
        Objects.requireNonNull(registration, "registration");
        if (registration.kind() != ApiContractKind.CALLBACK) {
            throw new IllegalArgumentException(
                    "API callback definition requires a CALLBACK registration"
            );
        }
        if (registration.surface() != ApiSurfaceClass.INTEGRATION_INGRESS) {
            throw new IllegalArgumentException(
                    "API callback definition requires INTEGRATION_INGRESS"
            );
        }
        requireReference(
                providerIntegrationRoleReference,
                "Provider/integration role reference"
        );
        requireReference(
                sourceAuthenticationMethodReference,
                "Source-authentication method reference"
        );
        requireReference(
                correlationRequirementsReference,
                "Correlation requirements reference"
        );
        historicalProviderConnectionBindingContextReference =
                requireOptionalReference(
                        historicalProviderConnectionBindingContextReference,
                        "Historical ProviderConnection/binding context reference"
                );
        requireReference(
                duplicateReplayIdentityReference,
                "Duplicate/replay identity reference"
        );
        requireReference(
                payloadValidationRuleReference,
                "Payload-validation rule reference"
        );
        requireReference(evidenceOwnerReference, "Evidence owner reference");
        requireReference(processingModeReference, "Processing mode reference");
        requireReference(
                acknowledgementSemanticsReference,
                "Acknowledgement semantics reference"
        );
        requireReference(
                safeRejectionSemanticsReference,
                "Safe rejection semantics reference"
        );
    }

    private static Optional<String> requireOptionalReference(
            Optional<String> value,
            String label
    ) {
        Objects.requireNonNull(value, label);
        value.ifPresent(reference -> requireReference(reference, label));
        return value;
    }

    private static void requireReference(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
