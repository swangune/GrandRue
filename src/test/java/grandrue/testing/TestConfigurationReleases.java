package grandrue.testing;

import grandrue.semantic.configuration.ActiveRelease;
import grandrue.semantic.configuration.ConfigurationActivationRequest;
import grandrue.semantic.configuration.ConfigurationActivationStatus;
import grandrue.semantic.configuration.ConfigurationPublication;
import grandrue.semantic.configuration.ConfigurationRelease;
import grandrue.semantic.configuration.ConfigurationReleaseActivation;
import grandrue.semantic.configuration.ConfigurationRevisionApproval;
import grandrue.semantic.configuration.InMemoryConfigurationPublication;
import grandrue.semantic.configuration.InMemoryConfigurationReleaseActivation;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.executable.ExecutableMerchantModel;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * Test fixture that stages real immutable releases before activating models.
 */
public final class TestConfigurationReleases {

    private static final Instant GENERATED_AT = Instant.EPOCH;
    private static final String TEST_PRINCIPAL = "test-principal";

    private final ConfigurationPublication publication =
            new InMemoryConfigurationPublication();
    private final ConfigurationReleaseActivation activation =
            new InMemoryConfigurationReleaseActivation(
                    publication,
                    (merchantIdentifier, configurationRevisionIdentifier) ->
                            Optional.of(new ConfigurationRevisionApproval(
                                    merchantIdentifier,
                                    configurationRevisionIdentifier,
                                    TEST_PRINCIPAL,
                                    GENERATED_AT
                            )),
                    (principal, merchant, revision) -> true,
                    Clock.fixed(GENERATED_AT, ZoneOffset.UTC)
            );

    public ConfigurationReleaseActivation activation() {
        return activation;
    }

    public void activate(ExecutableMerchantModel model) {
        Optional<String> expectedCurrent = current(model.merchantIdentifier())
                .map(active -> active.release().configurationIdentifier());
        ConfigurationRelease release = release(model, expectedCurrent);
        if (publication.release(release.releaseIdentifier()).isEmpty()) {
            publication.publish(release);
        }
        var result = activation.activate(new ConfigurationActivationRequest(
                "test-activation-" + release.releaseIdentifier(),
                release.releaseIdentifier(),
                expectedCurrent,
                TEST_PRINCIPAL
        ));
        if (result.status() != ConfigurationActivationStatus.SUCCESS) {
            throw new IllegalStateException(
                    "Test configuration activation failed: " + result.status()
            );
        }
    }

    public Optional<ActiveRelease> current(String merchantIdentifier) {
        return activation.current(merchantIdentifier);
    }

    private static ConfigurationRelease release(
            ExecutableMerchantModel model,
            Optional<String> baseConfigurationIdentifier
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                model.merchantIdentifier(),
                model.modelIdentifier(),
                model.version(),
                model.semanticRegistryVersion(),
                model.capabilityIdentifiers(),
                java.util.Set.of(),
                baseConfigurationIdentifier
        );
        return new ConfigurationRelease(
                "test-release-" + model.merchantIdentifier()
                        + "-" + model.modelIdentifier(),
                configuration,
                model,
                "test-compiler",
                GENERATED_AT
        );
    }
}
