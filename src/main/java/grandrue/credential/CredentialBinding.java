package grandrue.credential;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Stable non-secret security reference for one exact provider connection or
 * platform responsibility. The binding identity is never a secret-store
 * locator and never defines provider/business semantics.
 */
public record CredentialBinding(
        String bindingIdentity,
        CredentialBindingScope scope,
        Optional<MerchantScope> merchantScope,
        String responsibilityReference,
        String externalContextReference,
        String technicalPurposeIdentifier,
        Instant establishedAt
) {
    public CredentialBinding {
        require(bindingIdentity, "bindingIdentity");
        Objects.requireNonNull(scope, "scope");
        merchantScope = Objects.requireNonNull(merchantScope, "merchantScope");
        require(responsibilityReference, "responsibilityReference");
        require(externalContextReference, "externalContextReference");
        require(technicalPurposeIdentifier, "technicalPurposeIdentifier");
        Objects.requireNonNull(establishedAt, "establishedAt");

        if (scope == CredentialBindingScope.MERCHANT && merchantScope.isEmpty()) {
            throw new IllegalArgumentException(
                    "MERCHANT credential binding requires Merchant Scope"
            );
        }
        if (scope == CredentialBindingScope.PLATFORM && merchantScope.isPresent()) {
            throw new IllegalArgumentException(
                    "PLATFORM credential binding must not fabricate Merchant Scope"
            );
        }
        if (bindingIdentity.equals(externalContextReference)) {
            throw new IllegalArgumentException(
                    "Credential binding identity must remain distinct from external context identity"
            );
        }
    }

    private static void require(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
