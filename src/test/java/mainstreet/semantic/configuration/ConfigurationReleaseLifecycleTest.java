package mainstreet.semantic.configuration;

import mainstreet.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationReleaseLifecycleTest {

    private static final Instant GENERATED_AT =
            Instant.parse("2026-08-21T12:00:00Z");
    private static final String PRINCIPAL = "merchant-controller";

    @Test
    void release_rejects_a_model_compiled_from_different_source_provenance() {
        MerchantConfiguration source = configuration(1, Optional.empty());
        ExecutableMerchantModel wrongModel = model(
                "merchant-a",
                "configuration-2",
                2
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ConfigurationRelease(
                        "release-1",
                        source,
                        wrongModel,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void release_rejects_a_model_missing_a_selected_capability() {
        MerchantConfiguration source = configuration(1, Optional.empty());
        ExecutableMerchantModel incomplete = new ExecutableMerchantModel(
                source.merchantIdentifier(),
                source.configurationIdentifier(),
                source.version(),
                source.semanticRegistryVersion(),
                Set.of(),
                List.of(),
                List.of()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ConfigurationRelease(
                        "release-1",
                        source,
                        incomplete,
                        "mainstreet-compiler-1",
                        GENERATED_AT
                )
        );
    }

    @Test
    void staging_failure_and_activation_rejection_leave_active_release_unchanged() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();
        ConfigurationReleaseActivation activation = activation(publication);
        ConfigurationRelease first = release(1, Optional.empty());
        ConfigurationRelease second = release(
                2,
                Optional.of("configuration-1")
        );

        publication.publish(first);
        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(request(
                        "activate-1",
                        first,
                        Optional.empty()
                )).status()
        );
        ActiveRelease captured = activation.current("merchant-a")
                .orElseThrow();

        publication.publish(second);

        assertSame(
                first,
                activation.current("merchant-a").orElseThrow().release()
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> publication.publish(release(
                        4,
                        Optional.of("configuration-3")
                ))
        );
        assertEquals(
                ConfigurationActivationStatus.VALIDATION_REJECTION,
                activation.activate(new ConfigurationActivationRequest(
                        "activate-missing",
                        "release-missing",
                        Optional.of("configuration-1"),
                        PRINCIPAL
                )).status()
        );
        assertSame(
                first,
                activation.current("merchant-a").orElseThrow().release()
        );
        assertSame(first, captured.release());
    }

    @Test
    void ordinary_forward_change_requires_new_revision_based_on_current_revision() {
        ConfigurationPublication publication =
                new InMemoryConfigurationPublication();
        ConfigurationReleaseActivation activation = activation(publication);
        ConfigurationRelease first = release(1, Optional.empty());
        ConfigurationRelease second = release(
                2,
                Optional.of("configuration-1")
        );
        publication.publish(first);
        publication.publish(second);

        activation.activate(request("activate-1", first, Optional.empty()));
        activation.activate(request(
                "activate-2",
                second,
                Optional.of("configuration-1")
        ));

        assertSame(
                second,
                activation.current("merchant-a").orElseThrow().release()
        );
        assertEquals(
                ConfigurationActivationStatus.ACTIVATION_CONFLICT,
                activation.activate(request(
                        "reactivate-old-release",
                        first,
                        Optional.of("configuration-2")
                )).status()
        );
        assertSame(
                second,
                activation.current("merchant-a").orElseThrow().release()
        );

        ConfigurationRelease third = release(
                3,
                Optional.of("configuration-2")
        );
        publication.publish(third);
        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(request(
                        "activate-forward-revision",
                        third,
                        Optional.of("configuration-2")
                )).status()
        );

        ActiveRelease active = activation.current("merchant-a")
                .orElseThrow();
        assertEquals("release-3", active.releaseIdentifier());
        assertSame(third, active.release());
        assertSame(
                third,
                publication.latest("merchant-a").orElseThrow()
        );
    }

    private static ConfigurationReleaseActivation activation(
            ConfigurationPublication publication
    ) {
        return new InMemoryConfigurationReleaseActivation(
                publication,
                (merchant, revision) -> Optional.of(
                        new ConfigurationRevisionApproval(
                                merchant,
                                revision,
                                PRINCIPAL,
                                GENERATED_AT
                        )
                ),
                (principal, merchant, revision) -> true,
                Clock.fixed(GENERATED_AT, ZoneOffset.UTC)
        );
    }

    private static ConfigurationActivationRequest request(
            String requestIdentifier,
            ConfigurationRelease release,
            Optional<String> expectedCurrent
    ) {
        return new ConfigurationActivationRequest(
                requestIdentifier,
                release.releaseIdentifier(),
                expectedCurrent,
                PRINCIPAL
        );
    }

    private static ConfigurationRelease release(
            long version,
            Optional<String> baseConfigurationIdentifier
    ) {
        MerchantConfiguration source = configuration(
                version,
                baseConfigurationIdentifier
        );
        return new ConfigurationRelease(
                "release-" + version,
                source,
                model(
                        source.merchantIdentifier(),
                        source.configurationIdentifier(),
                        source.version()
                ),
                "mainstreet-compiler-1",
                GENERATED_AT
        );
    }

    private static MerchantConfiguration configuration(
            long version,
            Optional<String> baseConfigurationIdentifier
    ) {
        return new MerchantConfiguration(
                "merchant-a",
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                Set.of(),
                baseConfigurationIdentifier
        );
    }

    private static ExecutableMerchantModel model(
            String merchantIdentifier,
            String configurationIdentifier,
            long version
    ) {
        return new ExecutableMerchantModel(
                merchantIdentifier,
                configurationIdentifier,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(),
                List.of()
        );
    }
}
