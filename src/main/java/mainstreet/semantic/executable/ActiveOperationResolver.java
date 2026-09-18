package mainstreet.semantic.executable;

import grandrue.application.MerchantScope;
import mainstreet.semantic.configuration.ActiveRelease;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;

import java.util.Objects;

/**
 * Resolves operation applicability from one captured active model snapshot.
 * Runtime must still perform contextual authority and authoritative-state
 * validation before mutation.
 */
public final class ActiveOperationResolver {

    private final ConfigurationReleaseActivation activation;

    public ActiveOperationResolver(
            ConfigurationReleaseActivation activation
    ) {
        this.activation = Objects.requireNonNull(activation);
    }

    /**
     * Resolves an operation and preserves the exact active model from which it
     * was obtained, even if a newer model is activated later.
     */
    public ApplicableOperation resolve(
            MerchantScope merchantScope,
            String operationIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireIdentifier(operationIdentifier, "Operation identifier");
        String merchantIdentifier = merchantScope.merchantIdentifier();
        ActiveRelease activeRelease = activation.current(merchantIdentifier)
                .orElseThrow(() -> new IllegalStateException(
                        "Merchant has no active configuration release: "
                                + merchantIdentifier
                ));
        ExecutableMerchantModel model = activeRelease.release()
                .executableModel();
        ExecutableOperationDefinition operation = model.operation(
                        operationIdentifier
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "Operation is not applicable to the active model: "
                                + operationIdentifier
                ));
        return new ApplicableOperation(activeRelease, operation);
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
