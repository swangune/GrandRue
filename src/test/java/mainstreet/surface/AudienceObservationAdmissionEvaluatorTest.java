package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.merchantaccount.MerchantControllerRelationship;
import mainstreet.merchantaccount.MerchantControllerRelationshipAuthority;
import mainstreet.merchantaccount.MerchantControllerRelationshipLifecycle;
import mainstreet.runtime.AuthenticationProvenance;
import mainstreet.runtime.AuthenticationSessionCurrentness;
import mainstreet.runtime.AuthenticationSessionCurrentnessAuthority;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.runtime.TrustedExecutionContext;
import mainstreet.workforce.MerchantMembershipAuthority;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudienceObservationAdmissionEvaluatorTest {

    private static final Instant NOW =
            Instant.parse("2026-09-01T09:00:00Z");
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");

    @Test
    void admits_public_once_from_current_platform_protection_only() {
        AtomicInteger authenticationCalls = new AtomicInteger();
        AtomicInteger controllerCalls = new AtomicInteger();
        AtomicInteger membershipCalls = new AtomicInteger();
        AtomicInteger protectionCalls = new AtomicInteger();
        AudienceObservationAdmissionEvaluator evaluator = evaluator(
                provenance -> {
                    authenticationCalls.incrementAndGet();
                    return AuthenticationSessionCurrentness.CURRENT;
                },
                scope -> {
                    controllerCalls.incrementAndGet();
                    return Optional.empty();
                },
                (scope, identity) -> {
                    membershipCalls.incrementAndGet();
                    return false;
                },
                (context, binding, evaluatedAt) -> {
                    protectionCalls.incrementAndGet();
                    assertEquals(NOW, evaluatedAt);
                    return AudienceObservationPlatformProtection.SATISFIED;
                }
        );

        AudienceObservationAdmissionResult result =
                evaluator.evaluate(publicContext());

        assertTrue(result.admitted());
        assertTrue(result.failure().isEmpty());
        assertEquals(NOW, result.evaluatedAt());
        assertEquals(0, authenticationCalls.get());
        assertEquals(0, controllerCalls.get());
        assertEquals(0, membershipCalls.get());
        assertEquals(1, protectionCalls.get());

        AudienceObservationAdmissionResult second =
                evaluator.evaluate(publicContext());
        assertNotSame(result.invocationBinding(), second.invocationBinding());
    }

    @Test
    void admits_customer_only_when_authentication_is_current() {
        AtomicInteger currentnessCalls = new AtomicInteger();
        AudienceObservationAdmissionEvaluator evaluator = evaluator(
                provenance -> {
                    currentnessCalls.incrementAndGet();
                    return AuthenticationSessionCurrentness.CURRENT;
                },
                scope -> Optional.empty(),
                (scope, identity) -> false,
                satisfiedProtection()
        );

        AudienceObservationAdmissionResult result =
                evaluator.evaluate(customerContext("customer-1"));

        assertTrue(result.admitted());
        assertEquals(1, currentnessCalls.get());
    }

    @Test
    void distinguishes_not_current_and_unresolved_authentication() {
        assertFailure(
                evaluator(
                        provenance -> AuthenticationSessionCurrentness.NOT_CURRENT,
                        scope -> Optional.empty(),
                        (scope, identity) -> false,
                        satisfiedProtection()
                ).evaluate(customerContext("customer-1")),
                AudienceObservationAdmissionFailure.AUTHENTICATION_NOT_CURRENT
        );
        assertFailure(
                evaluator(
                        provenance -> AuthenticationSessionCurrentness.UNRESOLVED,
                        scope -> Optional.empty(),
                        (scope, identity) -> false,
                        satisfiedProtection()
                ).evaluate(customerContext("customer-1")),
                AudienceObservationAdmissionFailure
                        .AUTHENTICATION_CURRENTNESS_UNRESOLVED
        );
    }

    @Test
    void merchant_requires_matching_current_controller_or_active_membership() {
        MerchantControllerRelationship matching =
                new MerchantControllerRelationship(
                        "controller-1",
                        MERCHANT,
                        "merchant-principal-1",
                        MerchantControllerRelationshipLifecycle.ACTIVE
                );
        AtomicInteger membershipCalls = new AtomicInteger();

        AudienceObservationAdmissionResult controllerResult = evaluator(
                currentAuthentication(),
                scope -> Optional.of(matching),
                (scope, identity) -> {
                    membershipCalls.incrementAndGet();
                    return false;
                },
                satisfiedProtection()
        ).evaluate(merchantContext("merchant-principal-1"));
        assertTrue(controllerResult.admitted());
        assertEquals(0, membershipCalls.get());

        AudienceObservationAdmissionResult membershipResult = evaluator(
                currentAuthentication(),
                scope -> Optional.empty(),
                (scope, identity) -> scope.equals(MERCHANT)
                        && identity.equals("merchant-principal-1"),
                satisfiedProtection()
        ).evaluate(merchantContext("merchant-principal-1"));
        assertTrue(membershipResult.admitted());

        assertFailure(
                evaluator(
                        currentAuthentication(),
                        scope -> Optional.empty(),
                        (scope, identity) -> false,
                        satisfiedProtection()
                ).evaluate(merchantContext("merchant-principal-1")),
                AudienceObservationAdmissionFailure
                        .MERCHANT_ASSOCIATION_UNSATISFIED
        );
    }

    @Test
    void merchant_authority_failure_is_unresolved_and_fails_closed() {
        assertFailure(
                evaluator(
                        currentAuthentication(),
                        scope -> {
                            throw new IllegalStateException("unavailable");
                        },
                        (scope, identity) -> false,
                        satisfiedProtection()
                ).evaluate(merchantContext("merchant-principal-1")),
                AudienceObservationAdmissionFailure
                        .MERCHANT_ASSOCIATION_UNRESOLVED
        );
        assertFailure(
                evaluator(
                        currentAuthentication(),
                        scope -> Optional.empty(),
                        (scope, identity) -> {
                            throw new IllegalStateException("unavailable");
                        },
                        satisfiedProtection()
                ).evaluate(merchantContext("merchant-principal-1")),
                AudienceObservationAdmissionFailure
                        .MERCHANT_ASSOCIATION_UNRESOLVED
        );
    }

    @Test
    void distinguishes_unsatisfied_and_unresolved_platform_protection() {
        assertFailure(
                evaluator(
                        currentAuthentication(),
                        scope -> Optional.empty(),
                        (scope, identity) -> false,
                        (context, binding, evaluatedAt) ->
                                AudienceObservationPlatformProtection.UNSATISFIED
                ).evaluate(publicContext()),
                AudienceObservationAdmissionFailure
                        .PLATFORM_PROTECTION_UNSATISFIED
        );
        assertFailure(
                evaluator(
                        currentAuthentication(),
                        scope -> Optional.empty(),
                        (scope, identity) -> false,
                        (context, binding, evaluatedAt) ->
                                AudienceObservationPlatformProtection.UNRESOLVED
                ).evaluate(publicContext()),
                AudienceObservationAdmissionFailure
                        .PLATFORM_PROTECTION_UNRESOLVED
        );
        assertFailure(
                evaluator(
                        currentAuthentication(),
                        scope -> Optional.empty(),
                        (scope, identity) -> false,
                        (context, binding, evaluatedAt) -> {
                            throw new IllegalStateException("unavailable");
                        }
                ).evaluate(publicContext()),
                AudienceObservationAdmissionFailure
                        .PLATFORM_PROTECTION_UNRESOLVED
        );
    }

    @Test
    void admission_result_and_binding_are_closed_nonserializable_boundaries() {
        assertTrue(AudienceObservationAdmissionResult.class.isSealed());
        assertTrue(AudienceObservationInvocationBinding.class.isSealed());
        assertFalse(Serializable.class.isAssignableFrom(
                AudienceObservationAdmissionResult.class
        ));
        assertFalse(Serializable.class.isAssignableFrom(
                AudienceObservationInvocationBinding.class
        ));
        assertTrue(Arrays.stream(
                        AudienceObservationInvocationBinding.class
                                .getPermittedSubclasses()
                )
                .flatMap(type -> Arrays.stream(type.getDeclaredConstructors()))
                .noneMatch(constructor -> Modifier.isPublic(
                        constructor.getModifiers()
                )));
    }

    private static AudienceObservationAdmissionEvaluator evaluator(
            AuthenticationSessionCurrentnessAuthority currentness,
            MerchantControllerRelationshipAuthority controllers,
            MerchantMembershipAuthority memberships,
            AudienceObservationPlatformProtectionAuthority protection
    ) {
        return new AudienceObservationAdmissionEvaluator(
                currentness,
                controllers,
                memberships,
                protection,
                Clock.fixed(NOW, ZoneOffset.UTC)
        );
    }

    private static AuthenticationSessionCurrentnessAuthority
    currentAuthentication() {
        return provenance -> AuthenticationSessionCurrentness.CURRENT;
    }

    private static AudienceObservationPlatformProtectionAuthority
    satisfiedProtection() {
        return (context, binding, evaluatedAt) ->
                AudienceObservationPlatformProtection.SATISFIED;
    }

    private static AudienceObservationContext publicContext() {
        return new DefaultAudienceObservationContext(
                request(),
                new PublicObservationSubject()
        );
    }

    private static AudienceObservationContext customerContext(
            String principal
    ) {
        return new DefaultAudienceObservationContext(
                request(),
                new CustomerPrincipalObservationSubject(execution(principal))
        );
    }

    private static AudienceObservationContext merchantContext(
            String principal
    ) {
        return new DefaultAudienceObservationContext(
                request(),
                new MerchantInteractiveObservationSubject(execution(principal))
        );
    }

    private static TrustedExecutionContext execution(String principal) {
        return new TrustedExecutionContext(
                MERCHANT,
                new ExecutionPrincipal(principal),
                Optional.of(new AuthenticationProvenance(
                        "session-" + principal,
                        principal,
                        NOW.minusSeconds(60)
                ))
        );
    }

    private static EstablishedObservationRequest request() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        MERCHANT.merchantIdentifier(),
                        "semantic-registry-1.0"
                ),
                MERCHANT,
                "semantic-registry-1.0",
                Optional.empty()
        );
    }

    private static void assertFailure(
            AudienceObservationAdmissionResult result,
            AudienceObservationAdmissionFailure expected
    ) {
        assertFalse(result.admitted());
        assertEquals(Optional.of(expected), result.failure());
        assertEquals(NOW, result.evaluatedAt());
    }
}
