package grandrue.application;

import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.TrustedExecutionContext;
import grandrue.semantic.configuration.ConfigurationActivation;
import grandrue.semantic.configuration.ConfigurationActivationRequest;
import grandrue.semantic.configuration.ConfigurationActivationResult;
import grandrue.semantic.configuration.ConfigurationActivationStatus;
import grandrue.semantic.configuration.ConfigurationPublication;
import grandrue.semantic.configuration.ConfigurationRelease;
import grandrue.semantic.configuration.ConfigurationReleaseActivation;
import grandrue.semantic.configuration.MerchantConfiguration;
import grandrue.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfigurationActivationApplicationServiceTest {

    private static final Instant NOW =
            Instant.parse("2026-09-11T19:00:00Z");

    @Test
    void exact_authenticated_context_delegates_activation() {
        ConfigurationPublication publication =
                mock(ConfigurationPublication.class);

        ConfigurationReleaseActivation activation =
                mock(ConfigurationReleaseActivation.class);

        ConfigurationRelease release = release();
        ConfigurationActivationRequest request = request();

        when(
                activation.committedActivation(
                        request.activationRequestIdentifier()
                )
        ).thenReturn(Optional.empty());

        when(
                publication.release(
                        request.releaseIdentifier()
                )
        ).thenReturn(Optional.of(release));

        ConfigurationActivationResult expected =
                ConfigurationActivationResult.rejected(
                        ConfigurationActivationStatus.APPROVAL_REQUIRED
                );

        when(
                activation.activate(request)
        ).thenReturn(expected);

        ConfigurationActivationApplicationService service =
                new ConfigurationActivationApplicationService(
                        publication,
                        activation
                );

        ConfigurationActivationResult result =
                service.activate(
                        request,
                        context(
                                "merchant-a",
                                "controller-a",
                                "controller-a",
                                true
                        )
                );

        assertSame(expected, result);

        verify(activation).activate(request);
    }

    @Test
    void unauthenticated_context_cannot_reach_activation_mutation_boundary() {
        ConfigurationPublication publication =
                mock(ConfigurationPublication.class);

        ConfigurationReleaseActivation activation =
                mock(ConfigurationReleaseActivation.class);

        ConfigurationActivationRequest request = request();

        when(
                activation.committedActivation(
                        request.activationRequestIdentifier()
                )
        ).thenReturn(Optional.empty());

        when(
                publication.release(
                        request.releaseIdentifier()
                )
        ).thenReturn(Optional.of(release()));

        ConfigurationActivationApplicationService service =
                new ConfigurationActivationApplicationService(
                        publication,
                        activation
                );

        ConfigurationActivationResult result =
                service.activate(
                        request,
                        context(
                                "merchant-a",
                                "controller-a",
                                "controller-a",
                                false
                        )
                );

        assertEquals(
                ConfigurationActivationStatus.AUTHORISATION_REJECTION,
                result.status()
        );

        verify(activation, never()).activate(any());
    }

    @Test
    void trusted_merchant_scope_must_match_candidate_merchant() {
        ConfigurationPublication publication =
                mock(ConfigurationPublication.class);

        ConfigurationReleaseActivation activation =
                mock(ConfigurationReleaseActivation.class);

        ConfigurationActivationRequest request = request();

        when(
                activation.committedActivation(
                        request.activationRequestIdentifier()
                )
        ).thenReturn(Optional.empty());

        when(
                publication.release(
                        request.releaseIdentifier()
                )
        ).thenReturn(Optional.of(release()));

        ConfigurationActivationApplicationService service =
                new ConfigurationActivationApplicationService(
                        publication,
                        activation
                );

        ConfigurationActivationResult result =
                service.activate(
                        request,
                        context(
                                "merchant-other",
                                "controller-a",
                                "controller-a",
                                true
                        )
                );

        assertEquals(
                ConfigurationActivationStatus.AUTHORISATION_REJECTION,
                result.status()
        );

        verify(activation, never()).activate(any());
    }

    @Test
    void trusted_principal_must_match_activation_request_principal() {
        ConfigurationPublication publication =
                mock(ConfigurationPublication.class);

        ConfigurationReleaseActivation activation =
                mock(ConfigurationReleaseActivation.class);

        ConfigurationActivationRequest request = request();

        when(
                activation.committedActivation(
                        request.activationRequestIdentifier()
                )
        ).thenReturn(Optional.empty());

        when(
                publication.release(
                        request.releaseIdentifier()
                )
        ).thenReturn(Optional.of(release()));

        ConfigurationActivationApplicationService service =
                new ConfigurationActivationApplicationService(
                        publication,
                        activation
                );

        ConfigurationActivationResult result =
                service.activate(
                        request,
                        context(
                                "merchant-a",
                                "controller-b",
                                "controller-b",
                                true
                        )
                );

        assertEquals(
                ConfigurationActivationStatus.AUTHORISATION_REJECTION,
                result.status()
        );

        verify(activation, never()).activate(any());
    }

    @Test
    void authentication_identity_must_match_trusted_execution_principal() {
        ConfigurationPublication publication =
                mock(ConfigurationPublication.class);

        ConfigurationReleaseActivation activation =
                mock(ConfigurationReleaseActivation.class);

        ConfigurationActivationRequest request = request();

        when(
                activation.committedActivation(
                        request.activationRequestIdentifier()
                )
        ).thenReturn(Optional.empty());

        when(
                publication.release(
                        request.releaseIdentifier()
                )
        ).thenReturn(Optional.of(release()));

        ConfigurationActivationApplicationService service =
                new ConfigurationActivationApplicationService(
                        publication,
                        activation
                );

        ConfigurationActivationResult result =
                service.activate(
                        request,
                        context(
                                "merchant-a",
                                "controller-a",
                                "different-identity",
                                true
                        )
                );

        assertEquals(
                ConfigurationActivationStatus.AUTHORISATION_REJECTION,
                result.status()
        );

        verify(activation, never()).activate(any());
    }

    @Test
    void committed_replay_requires_trusted_original_principal_but_not_current_controller_recheck() {
        ConfigurationPublication publication =
                mock(ConfigurationPublication.class);

        ConfigurationReleaseActivation activation =
                mock(ConfigurationReleaseActivation.class);

        ConfigurationActivationRequest request = request();

        ConfigurationActivation committed =
                new ConfigurationActivation(
                        request.activationRequestIdentifier(),
                        "merchant-a",
                        "configuration-2",
                        request.releaseIdentifier(),
                        NOW.minusSeconds(60),
                        Optional.of("configuration-1")
                );

        when(
                activation.committedActivation(
                        request.activationRequestIdentifier()
                )
        ).thenReturn(Optional.of(committed));

        when(
                activation.activate(request)
        ).thenReturn(
                ConfigurationActivationResult.success(committed)
        );

        ConfigurationActivationApplicationService service =
                new ConfigurationActivationApplicationService(
                        publication,
                        activation
                );

        ConfigurationActivationResult result =
                service.activate(
                        request,
                        context(
                                "merchant-a",
                                "controller-a",
                                "controller-a",
                                true
                        )
                );

        assertEquals(
                ConfigurationActivationStatus.SUCCESS,
                result.status()
        );

        assertEquals(
                committed,
                result.activation().orElseThrow()
        );

        /*
         * Historical replay does not require the original publication
         * envelope to remain available and does not re-run current business
         * authority. JooqConfigurationReleaseActivation validates exact
         * request-intent affinity against the committed activation.
         */
        verify(publication, never()).release(any());
        verify(activation).activate(request);
    }

    @Test
    void committed_replay_cannot_be_claimed_by_different_authenticated_principal() {
        ConfigurationPublication publication =
                mock(ConfigurationPublication.class);

        ConfigurationReleaseActivation activation =
                mock(ConfigurationReleaseActivation.class);

        ConfigurationActivationRequest request = request();

        ConfigurationActivation committed =
                new ConfigurationActivation(
                        request.activationRequestIdentifier(),
                        "merchant-a",
                        "configuration-2",
                        request.releaseIdentifier(),
                        NOW.minusSeconds(60),
                        Optional.of("configuration-1")
                );

        when(
                activation.committedActivation(
                        request.activationRequestIdentifier()
                )
        ).thenReturn(Optional.of(committed));

        ConfigurationActivationApplicationService service =
                new ConfigurationActivationApplicationService(
                        publication,
                        activation
                );

        ConfigurationActivationResult result =
                service.activate(
                        request,
                        context(
                                "merchant-a",
                                "controller-b",
                                "controller-b",
                                true
                        )
                );

        assertEquals(
                ConfigurationActivationStatus.AUTHORISATION_REJECTION,
                result.status()
        );

        verify(activation, never()).activate(any());
    }

    private static ConfigurationActivationRequest request() {
        return new ConfigurationActivationRequest(
                "activate-2",
                "published-configuration-2",
                Optional.of("configuration-1"),
                "controller-a"
        );
    }

    private static ConfigurationRelease release() {
        MerchantConfiguration configuration =
                new MerchantConfiguration(
                        "merchant-a",
                        "configuration-2",
                        2,
                        "semantic-release-1",
                        Set.of(),
                        Set.of(),
                        Optional.of("configuration-1")
                );

        ExecutableMerchantModel executable =
                new ExecutableMerchantModel(
                        "merchant-a",
                        "configuration-2",
                        2,
                        "semantic-release-1",
                        Set.of(),
                        List.of(),
                        List.of()
                );

        return new ConfigurationRelease(
                "published-configuration-2",
                configuration,
                executable,
                "compiler-1",
                NOW.minusSeconds(120)
        );
    }

    private static TrustedExecutionContext context(
            String merchantIdentifier,
            String principalIdentifier,
            String authenticatedIdentityIdentifier,
            boolean authenticated
    ) {
        return new TrustedExecutionContext(
                new MerchantScope(merchantIdentifier),
                new ExecutionPrincipal(principalIdentifier),
                authenticated
                        ? Optional.of(
                                new AuthenticationProvenance(
                                        "session-1",
                                        authenticatedIdentityIdentifier,
                                        NOW.minusSeconds(30)
                                )
                        )
                        : Optional.empty()
        );
    }
}