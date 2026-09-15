package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiContractKind;
import mainstreet.api.ApiContractRegistration;
import mainstreet.api.ApiContractRegistrySnapshot;
import mainstreet.api.ApiOwnerContractReference;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.api.ApiTransportScope;
import mainstreet.api.ApiTransportScopeAuthorityRegistrySnapshot;
import mainstreet.api.ApiTransportScopeEstablishmentAuthority;
import mainstreet.api.ApiTransportScopeEvidence;
import mainstreet.api.MerchantApiTransportScope;
import mainstreet.api.PlatformApiTransportScope;
import mainstreet.application.MerchantScope;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.semantic.configuration.ActiveRelease;
import mainstreet.semantic.configuration.ConfigurationActivation;
import mainstreet.semantic.configuration.ConfigurationActivationRequest;
import mainstreet.semantic.configuration.ConfigurationActivationResult;
import mainstreet.semantic.configuration.ConfigurationRelease;
import mainstreet.semantic.configuration.ConfigurationReleaseActivation;
import mainstreet.semantic.configuration.MerchantConfiguration;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import mainstreet.semantic.registry.SemanticRegistrySnapshot;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObservationRequestEstablisherTest {

    private static final String SEMANTIC_RELEASE = "semantic-registry-1.0";
    private static final Instant GENERATED_AT =
            Instant.parse("2026-09-01T08:10:00Z");

    @Test
    void establishes_api_request_from_registered_query_scope_and_current_release() {
        MerchantScope scope = new MerchantScope("merchant-a");
        ActiveRelease activeRelease = activeRelease("merchant-a", SEMANTIC_RELEASE);
        TestScopeAuthority scopeAuthority = new TestScopeAuthority(
                "trusted-public-storefront-route",
                ApiSurfaceClass.PUBLIC,
                new MerchantApiTransportScope(scope)
        );
        ApiContractRegistration registration = registration(
                ApiContractKind.QUERY,
                ApiSurfaceClass.PUBLIC,
                scopeAuthority.ruleReference()
        );
        ObservationRequestEstablisher establisher = establisher(
                Set.of(registration),
                List.of(scopeAuthority),
                new FixedActivation(activeRelease),
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE
        );

        ObservationRequestEstablishmentResult result = establisher.establishForApi(
                registration.identity(),
                new RouteEvidence("shop.example")
        );

        EstablishedObservationRequest request =
                result.establishedRequest().orElseThrow();
        assertTrue(result.failure().isEmpty());
        assertSame(
                activeRelease,
                EstablishedObservationRequestDetails.activeRelease(request)
        );
        assertEquals(
                scope,
                EstablishedObservationRequestDetails.merchantScope(request)
        );
        assertEquals(
                SEMANTIC_RELEASE,
                EstablishedObservationRequestDetails
                        .semanticRegistryReleaseIdentifier(request)
        );
        assertEquals(
                Optional.of(registration.identity()),
                EstablishedObservationRequestDetails.apiContractIdentity(request)
        );
        assertEquals(
                Optional.of(ApiSurfaceClass.PUBLIC),
                EstablishedObservationRequestDetails.apiSurface(request)
        );
        assertEquals(1, scopeAuthority.calls);
    }

    @Test
    void rejects_unregistered_and_non_query_contracts_before_scope_establishment() {
        TestScopeAuthority authority = new TestScopeAuthority(
                "merchant-session",
                ApiSurfaceClass.MERCHANT_OPERATIONAL,
                new MerchantApiTransportScope(new MerchantScope("merchant-a"))
        );
        ApiContractRegistration command = registration(
                ApiContractKind.COMMAND,
                ApiSurfaceClass.MERCHANT_OPERATIONAL,
                authority.ruleReference()
        );
        ObservationRequestEstablisher establisher = establisher(
                Set.of(command),
                List.of(authority),
                new FixedActivation(activeRelease("merchant-a", SEMANTIC_RELEASE)),
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE
        );

        assertFailure(
                establisher.establishForApi(
                        new ApiContractIdentity("missing", "query"),
                        new RouteEvidence("merchant.example")
                ),
                ObservationRequestEstablishmentFailure.API_CONTRACT_UNREGISTERED
        );
        assertFailure(
                establisher.establishForApi(
                        command.identity(),
                        new RouteEvidence("merchant.example")
                ),
                ObservationRequestEstablishmentFailure.API_CONTRACT_NOT_QUERY
        );
        assertEquals(0, authority.calls);
    }

    @Test
    void rejects_non_merchant_scope_and_scope_authority_failure() {
        TestScopeAuthority platformAuthority = new TestScopeAuthority(
                "integration-scope",
                ApiSurfaceClass.INTEGRATION_INGRESS,
                new PlatformApiTransportScope("integration")
        );
        ApiContractRegistration integrationQuery = registration(
                ApiContractKind.QUERY,
                ApiSurfaceClass.INTEGRATION_INGRESS,
                platformAuthority.ruleReference()
        );
        ObservationRequestEstablisher platformEstablisher = establisher(
                Set.of(integrationQuery),
                List.of(platformAuthority),
                new FixedActivation(activeRelease("merchant-a", SEMANTIC_RELEASE)),
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE
        );

        assertFailure(
                platformEstablisher.establishForApi(
                        integrationQuery.identity(),
                        new RouteEvidence("integration.example")
                ),
                ObservationRequestEstablishmentFailure.API_SCOPE_NOT_MERCHANT
        );

        TestScopeAuthority throwingAuthority = new TestScopeAuthority(
                "public-route",
                ApiSurfaceClass.PUBLIC,
                new MerchantApiTransportScope(new MerchantScope("merchant-a"))
        );
        throwingAuthority.failure = new IllegalStateException("scope unavailable");
        ApiContractRegistration publicQuery = registration(
                ApiContractKind.QUERY,
                ApiSurfaceClass.PUBLIC,
                throwingAuthority.ruleReference()
        );
        ObservationRequestEstablisher failingEstablisher = establisher(
                Set.of(publicQuery),
                List.of(throwingAuthority),
                new FixedActivation(activeRelease("merchant-a", SEMANTIC_RELEASE)),
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE
        );

        assertFailure(
                failingEstablisher.establishForApi(
                        publicQuery.identity(),
                        new RouteEvidence("shop.example")
                ),
                ObservationRequestEstablishmentFailure
                        .API_SCOPE_ESTABLISHMENT_FAILED
        );
    }

    @Test
    void rejects_unresolved_or_scope_mismatched_active_configuration() {
        MerchantScope scope = new MerchantScope("merchant-a");
        TestScopeAuthority authority = new TestScopeAuthority(
                "public-route",
                ApiSurfaceClass.PUBLIC,
                new MerchantApiTransportScope(scope)
        );
        ApiContractRegistration query = registration(
                ApiContractKind.QUERY,
                ApiSurfaceClass.PUBLIC,
                authority.ruleReference()
        );

        ObservationRequestEstablisher unresolved = establisher(
                Set.of(query),
                List.of(authority),
                new EmptyActivation(),
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE
        );
        assertFailure(
                unresolved.establishForApi(
                        query.identity(),
                        new RouteEvidence("shop.example")
                ),
                ObservationRequestEstablishmentFailure
                        .ACTIVE_CONFIGURATION_UNRESOLVED
        );

        ObservationRequestEstablisher mismatched = establisher(
                Set.of(query),
                List.of(authority),
                new FixedActivation(activeRelease("merchant-b", SEMANTIC_RELEASE)),
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE
        );
        assertFailure(
                mismatched.establishForApi(
                        query.identity(),
                        new RouteEvidence("shop.example")
                ),
                ObservationRequestEstablishmentFailure
                        .ACTIVE_CONFIGURATION_SCOPE_MISMATCH
        );
    }

    @Test
    void rejects_every_exact_release_mismatch_independently() {
        MerchantScope scope = new MerchantScope("merchant-a");
        TestScopeAuthority authority = new TestScopeAuthority(
                "public-route",
                ApiSurfaceClass.PUBLIC,
                new MerchantApiTransportScope(scope)
        );
        ApiContractRegistration query = registration(
                ApiContractKind.QUERY,
                ApiSurfaceClass.PUBLIC,
                authority.ruleReference()
        );
        ConfigurationReleaseActivation activation =
                new FixedActivation(activeRelease("merchant-a", SEMANTIC_RELEASE));

        assertFailure(
                establisher(
                        Set.of(query),
                        List.of(authority),
                        activation,
                        "semantic-registry-other",
                        SEMANTIC_RELEASE,
                        SEMANTIC_RELEASE
                ).establishForApi(query.identity(), new RouteEvidence("shop.example")),
                ObservationRequestEstablishmentFailure
                        .SEMANTIC_RELEASE_NOT_MATERIALISED
        );
        assertFailure(
                establisher(
                        Set.of(query),
                        List.of(authority),
                        activation,
                        SEMANTIC_RELEASE,
                        "semantic-registry-other",
                        SEMANTIC_RELEASE
                ).establishForApi(query.identity(), new RouteEvidence("shop.example")),
                ObservationRequestEstablishmentFailure
                        .PROJECTION_REGISTRY_RELEASE_MISMATCH
        );
        assertFailure(
                establisher(
                        Set.of(query),
                        List.of(authority),
                        activation,
                        SEMANTIC_RELEASE,
                        SEMANTIC_RELEASE,
                        "semantic-registry-other"
                ).establishForApi(query.identity(), new RouteEvidence("shop.example")),
                ObservationRequestEstablishmentFailure
                        .EXPOSURE_REGISTRY_RELEASE_MISMATCH
        );
    }

    @Test
    void establishes_internal_request_only_from_trusted_execution_scope() {
        MerchantScope scope = new MerchantScope("merchant-a");
        ActiveRelease activeRelease = activeRelease("merchant-a", SEMANTIC_RELEASE);
        ObservationRequestEstablisher establisher = establisher(
                Set.of(),
                List.of(),
                new FixedActivation(activeRelease),
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE,
                SEMANTIC_RELEASE
        );
        TrustedExecutionContext executionContext = new TrustedExecutionContext(
                scope,
                new ExecutionPrincipal("guest-context-principal"),
                Optional.empty()
        );

        ObservationRequestEstablishmentResult result =
                establisher.establishForExecution(executionContext);

        EstablishedObservationRequest request =
                result.establishedRequest().orElseThrow();
        assertEquals(
                scope,
                EstablishedObservationRequestDetails.merchantScope(request)
        );
        assertSame(
                activeRelease,
                EstablishedObservationRequestDetails.activeRelease(request)
        );
        assertTrue(
                EstablishedObservationRequestDetails
                        .apiContractIdentity(request)
                        .isEmpty()
        );
    }

    @Test
    void public_api_exposes_no_scope_or_active_release_authority_parameter() {
        assertTrue(Arrays.stream(ObservationRequestEstablisher.class.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getDeclaringClass()
                        == ObservationRequestEstablisher.class)
                .flatMap(method -> Arrays.stream(method.getParameterTypes()))
                .noneMatch(type -> type == MerchantScope.class
                        || type == ActiveRelease.class
                        || type == ConfigurationRelease.class));

        assertFalse(java.io.Serializable.class.isAssignableFrom(
                EstablishedObservationRequest.class
        ));
        assertFalse(java.io.Serializable.class.isAssignableFrom(
                ObservationRequestBinding.class
        ));
    }

    private static ObservationRequestEstablisher establisher(
            Set<ApiContractRegistration> registrations,
            List<ApiTransportScopeEstablishmentAuthority> scopeAuthorities,
            ConfigurationReleaseActivation activation,
            String servingRelease,
            String projectionRelease,
            String exposureRelease
    ) {
        return new ObservationRequestEstablisher(
                new ApiContractRegistrySnapshot(registrations),
                new ApiTransportScopeAuthorityRegistrySnapshot(scopeAuthorities),
                activation,
                new SemanticRegistrySnapshot(servingRelease, Set.of()),
                new ProjectionContractRegistrySnapshot(projectionRelease, Set.of()),
                new ExposureElementContractRegistrySnapshot(exposureRelease, Set.of())
        );
    }

    private static ApiContractRegistration registration(
            ApiContractKind kind,
            ApiSurfaceClass surface,
            String scopeRule
    ) {
        return new ApiContractRegistration(
                new ApiContractIdentity("test-owner", "test-query"),
                surface,
                kind,
                new ApiOwnerContractReference("test-owner", "test-read"),
                scopeRule
        );
    }

    private static ActiveRelease activeRelease(
            String merchantIdentifier,
            String semanticRelease
    ) {
        MerchantConfiguration configuration = new MerchantConfiguration(
                merchantIdentifier,
                "configuration-1",
                1,
                semanticRelease,
                Set.of(),
                Set.of(),
                Optional.empty()
        );
        ExecutableMerchantModel model = new ExecutableMerchantModel(
                merchantIdentifier,
                "configuration-1",
                1,
                semanticRelease,
                Set.of(),
                List.of(),
                List.of()
        );
        return new ActiveRelease(new ConfigurationRelease(
                "release-" + merchantIdentifier,
                configuration,
                model,
                "test-compiler",
                GENERATED_AT
        ));
    }

    private static void assertFailure(
            ObservationRequestEstablishmentResult result,
            ObservationRequestEstablishmentFailure expected
    ) {
        assertEquals(Optional.of(expected), result.failure());
        assertTrue(result.establishedRequest().isEmpty());
    }

    private record RouteEvidence(String route)
            implements ApiTransportScopeEvidence {
    }

    private static final class TestScopeAuthority
            implements ApiTransportScopeEstablishmentAuthority {

        private final String ruleReference;
        private final ApiSurfaceClass surface;
        private final ApiTransportScope result;
        private RuntimeException failure;
        private int calls;

        private TestScopeAuthority(
                String ruleReference,
                ApiSurfaceClass surface,
                ApiTransportScope result
        ) {
            this.ruleReference = ruleReference;
            this.surface = surface;
            this.result = result;
        }

        @Override
        public String ruleReference() {
            return ruleReference;
        }

        @Override
        public ApiSurfaceClass surface() {
            return surface;
        }

        @Override
        public Class<? extends ApiTransportScopeEvidence> evidenceType() {
            return RouteEvidence.class;
        }

        @Override
        public ApiTransportScope establish(ApiTransportScopeEvidence evidence) {
            calls++;
            if (failure != null) {
                throw failure;
            }
            return result;
        }
    }

    private record FixedActivation(ActiveRelease activeRelease)
            implements ConfigurationReleaseActivation {

        @Override
        public ConfigurationActivationResult activate(
                ConfigurationActivationRequest request
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<ActiveRelease> current(String merchantIdentifier) {
            return Optional.of(activeRelease);
        }

        @Override
        public Optional<ConfigurationActivation> committedActivation(
                String activationRequestIdentifier
        ) {
            return Optional.empty();
        }
    }

    private static final class EmptyActivation
            implements ConfigurationReleaseActivation {

        @Override
        public ConfigurationActivationResult activate(
                ConfigurationActivationRequest request
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<ActiveRelease> current(String merchantIdentifier) {
            return Optional.empty();
        }

        @Override
        public Optional<ConfigurationActivation> committedActivation(
                String activationRequestIdentifier
        ) {
            return Optional.empty();
        }
    }
}
