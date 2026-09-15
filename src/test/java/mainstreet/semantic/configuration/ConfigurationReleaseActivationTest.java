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
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfigurationReleaseActivationTest {

    private static final Instant ACTIVATED_AT =
            Instant.parse("2026-08-23T15:30:00Z");
    private static final String PRINCIPAL = "merchant-controller-1";

    @Test
    void first_activation_commits_exact_revision_and_activation_fact() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        publication.publish(first);
        ConfigurationReleaseActivation activation = allowedActivation(publication);

        var result = activation.activate(request(
                "activate-1",
                first,
                Optional.empty()
        ));

        assertEquals(ConfigurationActivationStatus.SUCCESS, result.status());
        ConfigurationActivation fact = result.activation().orElseThrow();
        assertEquals("configuration-1", fact.configurationRevisionIdentifier());
        assertEquals(ACTIVATED_AT, fact.activatedAt());
        assertTrue(fact.replacedConfigurationRevisionIdentifier().isEmpty());
        assertSame(
                first,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void concurrent_first_activation_candidate_loses_after_another_commits() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        ConfigurationRelease competing = release("merchant-a", 2, Optional.empty());
        publication.publish(first);
        publication.publish(competing);
        ConfigurationReleaseActivation activation = allowedActivation(publication);

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(request(
                        "activate-first",
                        first,
                        Optional.empty()
                )).status()
        );
        assertEquals(
                ConfigurationActivationStatus.ACTIVATION_CONFLICT,
                activation.activate(request(
                        "activate-competing-first",
                        competing,
                        Optional.empty()
                )).status()
        );
        assertSame(
                first,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void replacement_requires_both_expected_current_and_candidate_base_to_match() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        ConfigurationRelease second = release(
                "merchant-a",
                2,
                Optional.of("configuration-1")
        );
        ConfigurationRelease stale = release(
                "merchant-a",
                3,
                Optional.of("configuration-1")
        );
        publication.publish(first);
        publication.publish(second);
        publication.publish(stale);
        ConfigurationReleaseActivation activation = allowedActivation(publication);

        activation.activate(request("activate-1", first, Optional.empty()));
        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                activation.activate(request(
                        "activate-2",
                        second,
                        Optional.of("configuration-1")
                )).status()
        );
        assertEquals(
                ConfigurationActivationStatus.ACTIVATION_CONFLICT,
                activation.activate(request(
                        "activate-stale",
                        stale,
                        Optional.of("configuration-2")
                )).status()
        );
        assertSame(
                second,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void approval_and_authorisation_are_independent_activation_preconditions() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        publication.publish(first);

        ConfigurationReleaseActivation noApproval = new InMemoryConfigurationReleaseActivation(
                publication,
                (merchant, revision) -> Optional.empty(),
                (principal, merchant, revision) -> true,
                fixedClock()
        );
        assertEquals(
                ConfigurationActivationStatus.APPROVAL_REQUIRED,
                noApproval.activate(request("no-approval", first, Optional.empty())).status()
        );

        ConfigurationReleaseActivation unauthorized = new InMemoryConfigurationReleaseActivation(
                publication,
                approvalAuthority(),
                (principal, merchant, revision) -> false,
                fixedClock()
        );
        assertEquals(
                ConfigurationActivationStatus.AUTHORISATION_REJECTION,
                unauthorized.activate(request("unauthorized", first, Optional.empty())).status()
        );
    }

    @Test
    void historical_activation_request_is_not_replay_after_pointer_moves() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        ConfigurationRelease second = release(
                "merchant-a",
                2,
                Optional.of("configuration-1")
        );
        publication.publish(first);
        publication.publish(second);
        ConfigurationReleaseActivation activation = allowedActivation(publication);
        ConfigurationActivationRequest firstRequest = request(
                "activate-1",
                first,
                Optional.empty()
        );

        ConfigurationActivation original = activation.activate(firstRequest)
                .activation().orElseThrow();
        activation.activate(request(
                "activate-2",
                second,
                Optional.of("configuration-1")
        ));
        assertEquals(
                ConfigurationActivationStatus.ACTIVATION_CONFLICT,
                activation.activate(firstRequest).status()
        );
        assertEquals(
                Optional.of(original),
                activation.committedActivation("activate-1")
        );
        assertSame(
                second,
                activation.current("merchant-a").orElseThrow().release()
        );
    }

    @Test
    void committed_request_identity_cannot_be_reused_for_different_intent() {
        ConfigurationPublication publication = new InMemoryConfigurationPublication();
        ConfigurationRelease first = release("merchant-a", 1, Optional.empty());
        ConfigurationRelease second = release(
                "merchant-a",
                2,
                Optional.of("configuration-1")
        );
        publication.publish(first);
        publication.publish(second);
        ConfigurationReleaseActivation activation = allowedActivation(publication);

        activation.activate(request("same-request", first, Optional.empty()));

        assertThrows(
                IllegalArgumentException.class,
                () -> activation.activate(request(
                        "same-request",
                        second,
                        Optional.of("configuration-1")
                ))
        );
    }

    private static ConfigurationReleaseActivation allowedActivation(
            ConfigurationPublication publication
    ) {
        return new InMemoryConfigurationReleaseActivation(
                publication,
                approvalAuthority(),
                (principal, merchant, revision) -> true,
                fixedClock()
        );
    }

    private static ConfigurationRevisionApprovalAuthority approvalAuthority() {
        return (merchant, revision) -> Optional.of(
                new ConfigurationRevisionApproval(
                        merchant,
                        revision,
                        PRINCIPAL,
                        ACTIVATED_AT.minusSeconds(60)
                )
        );
    }

    private static Clock fixedClock() {
        return Clock.fixed(ACTIVATED_AT, ZoneOffset.UTC);
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
            String merchantIdentifier,
            long version,
            Optional<String> baseConfigurationIdentifier
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                merchantIdentifier,
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                Set.of(),
                baseConfigurationIdentifier
        );
        return new ConfigurationRelease(
                "release-" + merchantIdentifier + "-" + version,
                configuration,
                model(merchantIdentifier, version),
                "mainstreet-compiler-1",
                ACTIVATED_AT.minusSeconds(120)
        );
    }

    private static ExecutableMerchantModel model(
            String merchantIdentifier,
            long version
    ) {
        return new ExecutableMerchantModel(
                merchantIdentifier,
                "configuration-" + version,
                version,
                "semantic-registry-1.0",
                Set.of("booking"),
                List.of(),
                List.of()
        );
    }
}
