package grandrue.surface;

import grandrue.api.ApiBoundedCollectionContract;
import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiSurfaceClass;
import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiContinuationAffinityValidatorTest {

    private static final ApiContractIdentity QUERY =
            new ApiContractIdentity("ordering", "merchant-order-list");
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";

    @Test
    void accepts_only_exact_query_scope_audience_and_release_affinity() {
        ApiBoundedCollectionContract contract = contract();
        ApiAudienceObservationContext context = publicContext(
                QUERY,
                MERCHANT,
                RELEASE
        );
        ApiContinuationAffinityCandidate candidate = candidate(
                contract,
                MERCHANT,
                SurfaceAudience.PUBLIC,
                RELEASE
        );

        assertTrue(new ApiContinuationAffinityValidator()
                .validate(contract, context, candidate)
                .isEmpty());
    }

    @Test
    void rejects_cross_query_or_changed_bounded_query_semantics() {
        ApiBoundedCollectionContract contract = contract();
        ApiContinuationAffinityValidator validator =
                new ApiContinuationAffinityValidator();
        ApiAudienceObservationContext context = publicContext(
                QUERY,
                MERCHANT,
                RELEASE
        );

        assertQuerySemanticsFailure(
                validator,
                contract,
                context,
                new ApiContinuationAffinityCandidate(
                        new ApiContractIdentity(
                                "ordering",
                                "different-query"
                        ),
                        contract.maximumPageSize(),
                        contract.stableOrderingBasisReference(),
                        contract.permittedFilterReferences(),
                        contract.continuationSemanticsReference(),
                        contract.scopeQueryAffinityRuleReference(),
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                )
        );

        assertQuerySemanticsFailure(
                validator,
                contract,
                context,
                new ApiContinuationAffinityCandidate(
                        QUERY,
                        contract.maximumPageSize(),
                        "ordering/different-stable-order",
                        contract.permittedFilterReferences(),
                        contract.continuationSemanticsReference(),
                        contract.scopeQueryAffinityRuleReference(),
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                )
        );

        assertQuerySemanticsFailure(
                validator,
                contract,
                context,
                new ApiContinuationAffinityCandidate(
                        QUERY,
                        50,
                        contract.stableOrderingBasisReference(),
                        contract.permittedFilterReferences(),
                        contract.continuationSemanticsReference(),
                        contract.scopeQueryAffinityRuleReference(),
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                )
        );

        assertQuerySemanticsFailure(
                validator,
                contract,
                context,
                new ApiContinuationAffinityCandidate(
                        QUERY,
                        contract.maximumPageSize(),
                        contract.stableOrderingBasisReference(),
                        Set.of("ordering/status"),
                        contract.continuationSemanticsReference(),
                        contract.scopeQueryAffinityRuleReference(),
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                )
        );

        assertQuerySemanticsFailure(
                validator,
                contract,
                context,
                new ApiContinuationAffinityCandidate(
                        QUERY,
                        contract.maximumPageSize(),
                        contract.stableOrderingBasisReference(),
                        contract.permittedFilterReferences(),
                        "ordering/different-continuation",
                        contract.scopeQueryAffinityRuleReference(),
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                )
        );

        assertQuerySemanticsFailure(
                validator,
                contract,
                context,
                new ApiContinuationAffinityCandidate(
                        QUERY,
                        contract.maximumPageSize(),
                        contract.stableOrderingBasisReference(),
                        contract.permittedFilterReferences(),
                        contract.continuationSemanticsReference(),
                        "ordering/different-affinity-rule",
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                )
        );
    }

    @Test
    void rejects_cross_merchant_cross_audience_and_release_replay() {
        ApiBoundedCollectionContract contract = contract();
        ApiAudienceObservationContext context = publicContext(
                QUERY,
                MERCHANT,
                RELEASE
        );
        ApiContinuationAffinityValidator validator =
                new ApiContinuationAffinityValidator();

        assertFailure(
                validator,
                contract,
                context,
                candidate(
                        contract,
                        new MerchantScope("merchant-b"),
                        SurfaceAudience.PUBLIC,
                        RELEASE
                ),
                ApiContinuationAffinityValidationFailure.MERCHANT_SCOPE_MISMATCH
        );
        assertFailure(
                validator,
                contract,
                context,
                candidate(
                        contract,
                        MERCHANT,
                        SurfaceAudience.CUSTOMER,
                        RELEASE
                ),
                ApiContinuationAffinityValidationFailure.AUDIENCE_MISMATCH
        );
        assertFailure(
                validator,
                contract,
                context,
                candidate(
                        contract,
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        "semantic-registry-2.0"
                ),
                ApiContinuationAffinityValidationFailure
                        .SEMANTIC_RELEASE_MISMATCH
        );
    }

    @Test
    void rejects_current_context_contract_or_provenance_laundering() {
        ApiBoundedCollectionContract contract = contract();
        ApiContinuationAffinityValidator validator =
                new ApiContinuationAffinityValidator();

        ApiAudienceObservationContext differentQueryContext = publicContext(
                new ApiContractIdentity("ordering", "different-query"),
                MERCHANT,
                RELEASE
        );
        assertFailure(
                validator,
                contract,
                differentQueryContext,
                candidate(
                        contract,
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                ),
                ApiContinuationAffinityValidationFailure
                        .CURRENT_QUERY_CONTEXT_MISMATCH
        );

        ApiAudienceObservationContext contractLaundering = apiContext(
                QUERY,
                ApiSurfaceClass.PUBLIC,
                new ApiContractIdentity("ordering", "different-query"),
                ApiSurfaceClass.PUBLIC,
                MERCHANT,
                RELEASE,
                new PublicObservationSubject()
        );
        assertFailure(
                validator,
                contract,
                contractLaundering,
                candidate(
                        contract,
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                ),
                ApiContinuationAffinityValidationFailure
                        .CURRENT_QUERY_CONTEXT_MISMATCH
        );

        ApiAudienceObservationContext surfaceLaundering = apiContext(
                QUERY,
                ApiSurfaceClass.PUBLIC,
                QUERY,
                ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                MERCHANT,
                RELEASE,
                new PublicObservationSubject()
        );
        assertFailure(
                validator,
                contract,
                surfaceLaundering,
                candidate(
                        contract,
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                ),
                ApiContinuationAffinityValidationFailure
                        .CURRENT_QUERY_CONTEXT_MISMATCH
        );
    }

    @Test
    void rejects_surface_and_subject_audience_laundering() {
        ApiBoundedCollectionContract contract = contract();
        ApiAudienceObservationContext syntheticCustomerSurface = apiContext(
                QUERY,
                ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                QUERY,
                ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                MERCHANT,
                RELEASE,
                new PublicObservationSubject()
        );

        assertFailure(
                new ApiContinuationAffinityValidator(),
                contract,
                syntheticCustomerSurface,
                candidate(
                        contract,
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                ),
                ApiContinuationAffinityValidationFailure.AUDIENCE_MISMATCH
        );
    }

    @Test
    void candidate_is_immutable_and_rejects_malformed_affinity_material() {
        Set<String> mutableFilters = new HashSet<>(
                Set.of("ordering/status", "ordering/created-range")
        );
        ApiContinuationAffinityCandidate candidate =
                new ApiContinuationAffinityCandidate(
                        QUERY,
                        100,
                        "ordering/stable-order",
                        mutableFilters,
                        "ordering/keyset-continuation",
                        "ordering/query-audience-affinity",
                        MERCHANT,
                        SurfaceAudience.PUBLIC,
                        RELEASE
                );

        mutableFilters.clear();
        assertEquals(
                Set.of("ordering/status", "ordering/created-range"),
                candidate.permittedFilterReferences()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> candidate.permittedFilterReferences().clear()
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> malformedCandidate(0, "stable", Set.of(),
                        "continuation", "affinity", RELEASE)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> malformedCandidate(1, " ", Set.of(),
                        "continuation", "affinity", RELEASE)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> malformedCandidate(1, "stable", Set.of(" "),
                        "continuation", "affinity", RELEASE)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> malformedCandidate(1, "stable", Set.of(),
                        " ", "affinity", RELEASE)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> malformedCandidate(1, "stable", Set.of(),
                        "continuation", " ", RELEASE)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> malformedCandidate(1, "stable", Set.of(),
                        "continuation", "affinity", " ")
        );
    }

    @Test
    void continuation_affinity_does_not_absorb_request_binding_or_transport_codec() {
        Set<Class<?>> candidateFieldTypes = Arrays.stream(
                        ApiContinuationAffinityCandidate.class.getDeclaredFields()
                )
                .map(Field::getType)
                .collect(Collectors.toUnmodifiableSet());
        Set<Class<?>> validatorFieldTypes = Arrays.stream(
                        ApiContinuationAffinityValidator.class.getDeclaredFields()
                )
                .map(Field::getType)
                .collect(Collectors.toUnmodifiableSet());
        Set<Class<?>> validatorParameterTypes = Arrays.stream(
                        ApiContinuationAffinityValidator.class.getDeclaredMethods()
                )
                .flatMap(method -> Arrays.stream(method.getParameterTypes()))
                .collect(Collectors.toUnmodifiableSet());

        assertFalse(candidateFieldTypes.contains(ObservationRequestBinding.class));
        assertFalse(candidateFieldTypes.contains(BoundedProjectionReadBinding.class));
        assertFalse(candidateFieldTypes.contains(java.util.Map.class));
        assertFalse(validatorFieldTypes.contains(ObservationRequestBinding.class));
        assertFalse(validatorFieldTypes.contains(BoundedProjectionReadBinding.class));
        assertFalse(validatorParameterTypes.contains(ObservationRequestBinding.class));
        assertFalse(validatorParameterTypes.contains(BoundedProjectionReadBinding.class));
        assertFalse(Serializable.class.isAssignableFrom(
                ApiContinuationAffinityCandidate.class
        ));

        assertEquals(
                Set.of("validate"),
                Arrays.stream(ApiContinuationAffinityValidator.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ApiContinuationAffinityValidator.class)
                        .map(Method::getName)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    private static ApiBoundedCollectionContract contract() {
        return new ApiBoundedCollectionContract(
                QUERY,
                100,
                "ordering/created-at-descending-then-order-id",
                Set.of("ordering/status", "ordering/created-range"),
                "ordering/keyset-continuation",
                "ordering/merchant-query-and-audience-affinity"
        );
    }

    private static ApiContinuationAffinityCandidate candidate(
            ApiBoundedCollectionContract contract,
            MerchantScope merchantScope,
            SurfaceAudience audience,
            String semanticRegistryReleaseIdentifier
    ) {
        return new ApiContinuationAffinityCandidate(
                contract.queryContractIdentity(),
                contract.maximumPageSize(),
                contract.stableOrderingBasisReference(),
                contract.permittedFilterReferences(),
                contract.continuationSemanticsReference(),
                contract.scopeQueryAffinityRuleReference(),
                merchantScope,
                audience,
                semanticRegistryReleaseIdentifier
        );
    }

    private static ApiContinuationAffinityCandidate malformedCandidate(
            int maximumPageSize,
            String stableOrder,
            Set<String> filters,
            String continuation,
            String affinity,
            String release
    ) {
        return new ApiContinuationAffinityCandidate(
                QUERY,
                maximumPageSize,
                stableOrder,
                filters,
                continuation,
                affinity,
                MERCHANT,
                SurfaceAudience.PUBLIC,
                release
        );
    }

    private static ApiAudienceObservationContext publicContext(
            ApiContractIdentity query,
            MerchantScope scope,
            String semanticRegistryReleaseIdentifier
    ) {
        return apiContext(
                query,
                ApiSurfaceClass.PUBLIC,
                query,
                ApiSurfaceClass.PUBLIC,
                scope,
                semanticRegistryReleaseIdentifier,
                new PublicObservationSubject()
        );
    }

    private static ApiAudienceObservationContext apiContext(
            ApiContractIdentity exposedContract,
            ApiSurfaceClass exposedSurface,
            ApiContractIdentity provenanceContract,
            ApiSurfaceClass provenanceSurface,
            MerchantScope scope,
            String semanticRegistryReleaseIdentifier,
            ObservationSubject subject
    ) {
        EstablishedObservationRequest request =
                new DefaultEstablishedObservationRequest(
                        TestReleases.activeRelease(
                                scope.merchantIdentifier(),
                                semanticRegistryReleaseIdentifier
                        ),
                        scope,
                        semanticRegistryReleaseIdentifier,
                        Optional.of(new ApiObservationRequestProvenance(
                                provenanceContract,
                                provenanceSurface,
                                "surface/public-route"
                        ))
                );
        AudienceObservationContext context =
                new DefaultAudienceObservationContext(request, subject);
        return new DefaultApiAudienceObservationContext(
                exposedContract,
                exposedSurface,
                context
        );
    }

    private static void assertQuerySemanticsFailure(
            ApiContinuationAffinityValidator validator,
            ApiBoundedCollectionContract contract,
            ApiAudienceObservationContext context,
            ApiContinuationAffinityCandidate candidate
    ) {
        assertFailure(
                validator,
                contract,
                context,
                candidate,
                ApiContinuationAffinityValidationFailure
                        .QUERY_SEMANTICS_MISMATCH
        );
    }

    private static void assertFailure(
            ApiContinuationAffinityValidator validator,
            ApiBoundedCollectionContract contract,
            ApiAudienceObservationContext context,
            ApiContinuationAffinityCandidate candidate,
            ApiContinuationAffinityValidationFailure expected
    ) {
        assertEquals(
                Optional.of(expected),
                validator.validate(contract, context, candidate)
        );
    }
}
