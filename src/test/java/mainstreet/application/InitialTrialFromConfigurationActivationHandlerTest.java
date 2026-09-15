package mainstreet.application;

import mainstreet.commercial.InitialFullExperienceTrial;
import mainstreet.commercial.InitialFullExperienceTrialEstablisher;
import mainstreet.commercial.InitialFullExperienceTrialStore;
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
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitialTrialFromConfigurationActivationHandlerTest {

    private static final Instant FIRST_ACTIVATED_AT =
            Instant.parse("2026-08-23T15:45:00Z");
    private static final String PRINCIPAL = "merchant-controller";

    @Test
    void first_committed_activation_establishes_trial_from_original_commit_fact() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release(1, Optional.empty());
        publication.publish(first);
        ConfigurationReleaseActivation activation = activation(publication);

        var activationResult = activation.activate(new ConfigurationActivationRequest(
                "activation-request-1",
                first.releaseIdentifier(),
                Optional.empty(),
                PRINCIPAL
        ));
        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activationResult.status()
        );

        AtomicReference<InitialFullExperienceTrial> stored = new AtomicReference<>();
        InitialFullExperienceTrialStore store = candidate -> {
            stored.compareAndSet(null, candidate);
            return stored.get();
        };
        InitialFullExperienceTrialEstablisher establisher =
                new InitialFullExperienceTrialEstablisher(
                        new ConfigurationBackedFirstActivationAuthority(activation),
                        store
                );
        InitialTrialFromConfigurationActivationHandler handler =
                new InitialTrialFromConfigurationActivationHandler(
                        establisher,
                        committed -> "initial-trial-" + committed.merchantIdentifier()
                );

        InitialFullExperienceTrial trial = handler.handle(
                activationResult.activation().orElseThrow()
        ).orElseThrow();

        assertEquals("initial-trial-merchant-a", trial.trialIdentity());
        assertEquals("merchant-a", trial.merchantScope().merchantIdentifier());
        assertEquals(
                "configuration-1",
                trial.originConfigurationRevisionIdentifier()
        );
        assertEquals(
                "activation-request-1",
                trial.originatingFirstActivationIdentity()
        );
        assertEquals(FIRST_ACTIVATED_AT, trial.startsAt());
        assertSame(trial, stored.get());
    }

    @Test
    void duplicate_delivery_resolves_existing_trial_and_replacement_activation_is_ignored() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release(1, Optional.empty());
        ConfigurationRelease second = release(
                2,
                Optional.of("configuration-1")
        );
        publication.publish(first);
        publication.publish(second);
        ConfigurationReleaseActivation activation = activation(publication);

        var firstResult = activation.activate(new ConfigurationActivationRequest(
                "activation-request-1",
                first.releaseIdentifier(),
                Optional.empty(),
                PRINCIPAL
        ));
        var secondResult = activation.activate(new ConfigurationActivationRequest(
                "activation-request-2",
                second.releaseIdentifier(),
                Optional.of("configuration-1"),
                PRINCIPAL
        ));

        AtomicReference<InitialFullExperienceTrial> stored = new AtomicReference<>();
        InitialFullExperienceTrialStore store = candidate -> {
            stored.compareAndSet(null, candidate);
            return stored.get();
        };
        InitialTrialFromConfigurationActivationHandler handler =
                new InitialTrialFromConfigurationActivationHandler(
                        new InitialFullExperienceTrialEstablisher(
                                new ConfigurationBackedFirstActivationAuthority(activation),
                                store
                        ),
                        committed -> "initial-trial-" + committed.merchantIdentifier()
                );

        InitialFullExperienceTrial firstDelivery = handler.handle(
                firstResult.activation().orElseThrow()
        ).orElseThrow();
        InitialFullExperienceTrial duplicateDelivery = handler.handle(
                firstResult.activation().orElseThrow()
        ).orElseThrow();

        assertSame(firstDelivery, duplicateDelivery);
        assertTrue(handler.handle(
                secondResult.activation().orElseThrow()
        ).isEmpty());
        assertSame(firstDelivery, stored.get());
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
                                FIRST_ACTIVATED_AT.minusSeconds(60)
                        )
                ),
                (principal, merchant, revision) -> true,
                Clock.fixed(FIRST_ACTIVATED_AT, ZoneOffset.UTC)
        );
    }

    private static ConfigurationRelease release(
            long version,
            Optional<String> baseConfigurationIdentifier
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                "merchant-a",
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                Set.of(),
                baseConfigurationIdentifier
        );
        return new ConfigurationRelease(
                "release-" + version,
                configuration,
                model(version),
                "mainstreet-compiler-1",
                FIRST_ACTIVATED_AT.minusSeconds(120)
        );
    }

    private static ExecutableMerchantModel model(long version) {
        return new ExecutableMerchantModel(
                "merchant-a",
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(),
                List.of()
        );
    }
}
