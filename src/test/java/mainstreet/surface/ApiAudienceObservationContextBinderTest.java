package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiContractKind;
import mainstreet.api.ApiContractRegistration;
import mainstreet.api.ApiContractRegistrySnapshot;
import mainstreet.api.ApiOwnerContractReference;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.application.MerchantScope;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.TrustedExecutionContext;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiAudienceObservationContextBinderTest {

    private static final ApiContractIdentity CONTRACT =
            new ApiContractIdentity("booking", "availability-query");
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");

    @Test
    void binds_exact_public_query_without_public_internal_unwrap() {
        ApiContractRegistration registration = registration(
                ApiSurfaceClass.PUBLIC,
                ApiContractKind.QUERY,
                "public-route"
        );
        AudienceObservationContext context = new DefaultAudienceObservationContext(
                request(
                        MERCHANT,
                        registration.identity(),
                        registration.surface(),
                        registration.scopeEstablishmentRuleReference()
                ),
                new PublicObservationSubject()
        );

        ApiAudienceObservationContextBindingResult result =
                binder(registration).bind(context);

        ApiAudienceObservationContext bound =
                result.context().orElseThrow();
        assertTrue(result.failure().isEmpty());
        assertEquals(CONTRACT, bound.contractIdentity());
        assertEquals(ApiSurfaceClass.PUBLIC, bound.surface());
        assertSame(
                context,
                ApiAudienceObservationContextDetails.context(bound)
        );

        assertEquals(
                Set.of("contractIdentity", "surface"),
                Arrays.stream(ApiAudienceObservationContext.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ApiAudienceObservationContext.class)
                        .map(Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertFalse(Serializable.class.isAssignableFrom(
                ApiAudienceObservationContext.class
        ));
        assertTrue(Arrays.stream(
                        ApiAudienceObservationContext.class
                                .getPermittedSubclasses()
                )
                .flatMap(type -> Arrays.stream(type.getDeclaredConstructors()))
                .noneMatch(constructor -> Modifier.isPublic(
                        constructor.getModifiers()
                )));
    }

    @Test
    void binds_only_the_subject_permitted_by_each_api_surface() {
        ApiContractRegistration customerRegistration = registration(
                ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                ApiContractKind.QUERY,
                "customer-session"
        );
        TrustedExecutionContext customerExecution =
                execution(MERCHANT, "customer-1");
        AudienceObservationContext customer = new DefaultAudienceObservationContext(
                request(
                        MERCHANT,
                        CONTRACT,
                        ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                        "customer-session"
                ),
                new CustomerPrincipalObservationSubject(customerExecution)
        );
        assertTrue(binder(customerRegistration).bind(customer)
                .context().isPresent());

        ApiContractRegistration merchantRegistration = registration(
                ApiSurfaceClass.MERCHANT_OPERATIONAL,
                ApiContractKind.QUERY,
                "merchant-session"
        );
        AudienceObservationContext merchant = new DefaultAudienceObservationContext(
                request(
                        MERCHANT,
                        CONTRACT,
                        ApiSurfaceClass.MERCHANT_OPERATIONAL,
                        "merchant-session"
                ),
                new MerchantInteractiveObservationSubject(
                        execution(MERCHANT, "merchant-1")
                )
        );
        assertTrue(binder(merchantRegistration).bind(merchant)
                .context().isPresent());

        assertFailure(
                binder(customerRegistration).bind(
                        new DefaultAudienceObservationContext(
                                AudienceObservationContextDetails.request(customer),
                                new PublicObservationSubject()
                        )
                ),
                ApiAudienceObservationContextBindingFailure
                        .API_AUDIENCE_MISMATCH
        );
    }

    @Test
    void rejects_internally_established_context_and_absent_or_nonquery_contract() {
        AudienceObservationContext internal =
                new DefaultAudienceObservationContext(
                        internalRequest(MERCHANT),
                        new PublicObservationSubject()
                );
        assertFailure(
                binder().bind(internal),
                ApiAudienceObservationContextBindingFailure
                        .API_PROVENANCE_MISSING
        );

        AudienceObservationContext apiContext =
                new DefaultAudienceObservationContext(
                        request(
                                MERCHANT,
                                CONTRACT,
                                ApiSurfaceClass.PUBLIC,
                                "public-route"
                        ),
                        new PublicObservationSubject()
                );
        assertFailure(
                binder().bind(apiContext),
                ApiAudienceObservationContextBindingFailure
                        .API_CONTRACT_UNREGISTERED
        );
        assertFailure(
                binder(registration(
                        ApiSurfaceClass.PUBLIC,
                        ApiContractKind.COMMAND,
                        "public-route"
                )).bind(apiContext),
                ApiAudienceObservationContextBindingFailure
                        .API_CONTRACT_NOT_QUERY
        );
    }

    @Test
    void rejects_surface_scope_rule_and_subject_scope_drift() {
        AudienceObservationContext apiContext =
                new DefaultAudienceObservationContext(
                        request(
                                MERCHANT,
                                CONTRACT,
                                ApiSurfaceClass.PUBLIC,
                                "public-route"
                        ),
                        new PublicObservationSubject()
                );

        assertFailure(
                binder(registration(
                        ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                        ApiContractKind.QUERY,
                        "public-route"
                )).bind(apiContext),
                ApiAudienceObservationContextBindingFailure
                        .API_SURFACE_MISMATCH
        );
        assertFailure(
                binder(registration(
                        ApiSurfaceClass.PUBLIC,
                        ApiContractKind.QUERY,
                        "different-rule"
                )).bind(apiContext),
                ApiAudienceObservationContextBindingFailure
                        .API_PROVENANCE_MISMATCH
        );

        ApiContractRegistration customerRegistration = registration(
                ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                ApiContractKind.QUERY,
                "customer-session"
        );
        AudienceObservationContext scopeDrift =
                new DefaultAudienceObservationContext(
                        request(
                                MERCHANT,
                                CONTRACT,
                                ApiSurfaceClass.CUSTOMER_CONTEXTUAL,
                                "customer-session"
                        ),
                        new CustomerPrincipalObservationSubject(
                                execution(
                                        new MerchantScope("merchant-b"),
                                        "customer-1"
                                )
                        )
                );
        assertFailure(
                binder(customerRegistration).bind(scopeDrift),
                ApiAudienceObservationContextBindingFailure
                        .API_SCOPE_MISMATCH
        );
    }

    private static ApiAudienceObservationContextBinder binder(
            ApiContractRegistration... registrations
    ) {
        return new ApiAudienceObservationContextBinder(
                new ApiContractRegistrySnapshot(Set.of(registrations))
        );
    }

    private static ApiContractRegistration registration(
            ApiSurfaceClass surface,
            ApiContractKind kind,
            String scopeRule
    ) {
        return new ApiContractRegistration(
                CONTRACT,
                surface,
                kind,
                new ApiOwnerContractReference("booking", "booking-read"),
                scopeRule
        );
    }

    private static EstablishedObservationRequest request(
            MerchantScope scope,
            ApiContractIdentity contract,
            ApiSurfaceClass surface,
            String scopeRule
    ) {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        scope.merchantIdentifier(),
                        "semantic-registry-1.0"
                ),
                scope,
                "semantic-registry-1.0",
                Optional.of(new ApiObservationRequestProvenance(
                        contract,
                        surface,
                        scopeRule
                ))
        );
    }

    private static EstablishedObservationRequest internalRequest(
            MerchantScope scope
    ) {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        scope.merchantIdentifier(),
                        "semantic-registry-1.0"
                ),
                scope,
                "semantic-registry-1.0",
                Optional.empty()
        );
    }

    private static TrustedExecutionContext execution(
            MerchantScope scope,
            String principal
    ) {
        return new TrustedExecutionContext(
                scope,
                new ExecutionPrincipal(principal),
                Optional.empty()
        );
    }

    private static void assertFailure(
            ApiAudienceObservationContextBindingResult result,
            ApiAudienceObservationContextBindingFailure expected
    ) {
        assertTrue(result.context().isEmpty());
        assertEquals(Optional.of(expected), result.failure());
    }
}
