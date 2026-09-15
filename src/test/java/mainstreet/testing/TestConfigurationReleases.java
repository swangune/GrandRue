package mainstreet.testing;

import mainstreet.semantic.configuration.ActiveRelease;
import mainstreet.semantic.configuration.ConfigurationActivationRequest;
import mainstreet.semantic.configuration.ConfigurationActivationStatus;
import mainstreet.semantic.configuration.ConfigurationPublication;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.configuration.ConfigurationRevisionApproval;
import mainstreet.semantic.configuration.InMemoryConfigurationPublication;
import mainstreet.semantic.configuration.InMemoryConfigurationReleaseActivation;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.executable.ExecutableMerchantModel;

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
