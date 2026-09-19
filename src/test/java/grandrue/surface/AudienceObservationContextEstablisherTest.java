package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.SessionRecord;
import grandrue.runtime.SessionRecordStore;
import grandrue.runtime.TrustedExecutionContext;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudienceObservationContextEstablisherTest {

    private static final Instant AUTHENTICATED_AT =
            Instant.parse("2026-09-01T08:20:00Z");
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");

    @Test
    void establishes_public_context_with_public_subject_and_no_execution_context() {
        EstablishedObservationRequest request = request(MERCHANT);
        AudienceObservationContextEstablisher establisher =
                new AudienceObservationContextEstablisher(
                        new TestSessionRecordStore()
                );

        AudienceObservationContextEstablishmentResult result =
                establisher.establishPublic(request);

        AudienceObservationContext context = result.context().orElseThrow();
        assertTrue(result.failure().isEmpty());
        assertSame(
                request,
                AudienceObservationContextDetails.request(context)
        );
        assertEquals(
                SurfaceAudience.PUBLIC,
                AudienceObservationContextDetails.audience(context)
        );
        assertTrue(
                AudienceObservationContextDetails.executionContext(context)
                        .isEmpty()
        );
    }

    @Test
    void establishes_customer_and_merchant_subjects_from_exact_bound_authentication() {
        TestSessionRecordStore sessions = new TestSessionRecordStore();
        sessions.add(session(
                "session-customer",
                "customer-1",
                AUTHENTICATED_AT
        ));
        sessions.add(session(
                "session-merchant",
                "merchant-principal-1",
                AUTHENTICATED_AT.plusSeconds(1)
        ));
        AudienceObservationContextEstablisher establisher =
                new AudienceObservationContextEstablisher(sessions);
        EstablishedObservationRequest request = request(MERCHANT);

        TrustedExecutionContext customer = execution(
                MERCHANT,
                "customer-1",
                "session-customer",
                AUTHENTICATED_AT
        );
        AudienceObservationContext customerContext =
                establisher.establishCustomerPrincipal(request, customer)
                        .context()
                        .orElseThrow();
        assertEquals(
                SurfaceAudience.CUSTOMER,
                AudienceObservationContextDetails.audience(customerContext)
        );
        assertSame(
                customer,
                AudienceObservationContextDetails
                        .executionContext(customerContext)
                        .orElseThrow()
        );

        TrustedExecutionContext merchant = execution(
                MERCHANT,
                "merchant-principal-1",
                "session-merchant",
                AUTHENTICATED_AT.plusSeconds(1)
        );
        AudienceObservationContext merchantContext =
                establisher.establishMerchantInteractive(request, merchant)
                        .context()
                        .orElseThrow();
        assertEquals(
                SurfaceAudience.MERCHANT,
                AudienceObservationContextDetails.audience(merchantContext)
        );
        assertSame(
                merchant,
                AudienceObservationContextDetails
                        .executionContext(merchantContext)
                        .orElseThrow()
        );
    }

    @Test
    void rejects_subject_scope_mismatch_before_authentication_binding() {
        TestSessionRecordStore sessions = new TestSessionRecordStore();
        AudienceObservationContextEstablisher establisher =
                new AudienceObservationContextEstablisher(sessions);
        TrustedExecutionContext otherMerchant = execution(
                new MerchantScope("merchant-b"),
                "customer-1",
                "session-1",
                AUTHENTICATED_AT
        );

        assertFailure(
                establisher.establishCustomerPrincipal(
                        request(MERCHANT),
                        otherMerchant
                ),
                AudienceObservationContextEstablishmentFailure
                        .SUBJECT_SCOPE_MISMATCH
        );
        assertEquals(0, sessions.lookups);
    }

    @Test
    void rejects_missing_authentication_and_principal_mismatch() {
        TestSessionRecordStore sessions = new TestSessionRecordStore();
        AudienceObservationContextEstablisher establisher =
                new AudienceObservationContextEstablisher(sessions);
        EstablishedObservationRequest request = request(MERCHANT);

        TrustedExecutionContext unauthenticated = new TrustedExecutionContext(
                MERCHANT,
                new ExecutionPrincipal("guest"),
                Optional.empty()
        );
        assertFailure(
                establisher.establishCustomerPrincipal(
                        request,
                        unauthenticated
                ),
                AudienceObservationContextEstablishmentFailure
                        .AUTHENTICATION_REQUIRED
        );

        TrustedExecutionContext mismatched = new TrustedExecutionContext(
                MERCHANT,
                new ExecutionPrincipal("customer-1"),
                Optional.of(new AuthenticationProvenance(
                        "session-1",
                        "customer-2",
                        AUTHENTICATED_AT
                ))
        );
        assertFailure(
                establisher.establishCustomerPrincipal(request, mismatched),
                AudienceObservationContextEstablishmentFailure
                        .AUTHENTICATION_PRINCIPAL_MISMATCH
        );
        assertEquals(0, sessions.lookups);
    }

    @Test
    void rejects_missing_or_identity_mismatched_current_session_record() {
        TestSessionRecordStore sessions = new TestSessionRecordStore();
        AudienceObservationContextEstablisher establisher =
                new AudienceObservationContextEstablisher(sessions);
        EstablishedObservationRequest request = request(MERCHANT);
        TrustedExecutionContext execution = execution(
                MERCHANT,
                "customer-1",
                "session-1",
                AUTHENTICATED_AT
        );

        assertFailure(
                establisher.establishCustomerPrincipal(request, execution),
                AudienceObservationContextEstablishmentFailure
                        .AUTHENTICATION_SESSION_UNRESOLVED
        );

        sessions.add(session(
                "session-1",
                "customer-2",
                AUTHENTICATED_AT
        ));
        assertFailure(
                establisher.establishCustomerPrincipal(request, execution),
                AudienceObservationContextEstablishmentFailure
                        .AUTHENTICATION_SESSION_IDENTITY_MISMATCH
        );
    }

    @Test
    void rejects_session_establishment_time_mismatch() {
        TestSessionRecordStore sessions = new TestSessionRecordStore();
        sessions.add(session(
                "session-1",
                "customer-1",
                AUTHENTICATED_AT.minusSeconds(1)
        ));
        AudienceObservationContextEstablisher establisher =
                new AudienceObservationContextEstablisher(sessions);

        assertFailure(
                establisher.establishMerchantInteractive(
                        request(MERCHANT),
                        execution(
                                MERCHANT,
                                "customer-1",
                                "session-1",
                                AUTHENTICATED_AT
                        )
                ),
                AudienceObservationContextEstablishmentFailure
                        .AUTHENTICATION_SESSION_ESTABLISHMENT_MISMATCH
        );
    }

    @Test
    void authoritative_context_types_have_no_public_constructors_or_serialization() {
        assertTrue(Arrays.stream(AudienceObservationContext.class
                        .getPermittedSubclasses())
                .flatMap(type -> Arrays.stream(type.getDeclaredConstructors()))
                .noneMatch(constructor -> Modifier.isPublic(
                        constructor.getModifiers()
                )));
        assertFalse(Serializable.class.isAssignableFrom(
                AudienceObservationContext.class
        ));
        assertFalse(Serializable.class.isAssignableFrom(
                ObservationSubject.class
        ));
        assertTrue(Arrays.stream(AudienceObservationContext.class
                        .getPermittedSubclasses())
                .noneMatch(type -> Modifier.isPublic(type.getModifiers())));
    }

    private static EstablishedObservationRequest request(MerchantScope scope) {
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
            String principal,
            String session,
            Instant authenticatedAt
    ) {
        return new TrustedExecutionContext(
                scope,
                new ExecutionPrincipal(principal),
                Optional.of(new AuthenticationProvenance(
                        session,
                        principal,
                        authenticatedAt
                ))
        );
    }

    private static SessionRecord session(
            String session,
            String identity,
            Instant establishedAt
    ) {
        return new SessionRecord(
                session,
                identity,
                "credential-verifier-" + session,
                establishedAt,
                "assurance",
                "passkey",
                establishedAt.plusSeconds(3600),
                establishedAt,
                "generation-1",
                Optional.empty(),
                Optional.empty()
        );
    }

    private static void assertFailure(
            AudienceObservationContextEstablishmentResult result,
            AudienceObservationContextEstablishmentFailure expected
    ) {
        assertEquals(Optional.of(expected), result.failure());
        assertTrue(result.context().isEmpty());
    }

    private static final class TestSessionRecordStore
            implements SessionRecordStore {

        private final Map<String, SessionRecord> sessions = new HashMap<>();
        private int lookups;

        void add(SessionRecord session) {
            sessions.put(session.sessionIdentity(), session);
        }

        @Override
        public SessionRecord create(SessionRecord candidate) {
            sessions.put(candidate.sessionIdentity(), candidate);
            return candidate;
        }

        @Override
        public Optional<SessionRecord> sessionByIdentity(
                String sessionIdentity
        ) {
            lookups++;
            return Optional.ofNullable(sessions.get(sessionIdentity));
        }

        @Override
        public Optional<SessionRecord> sessionByCredentialVerifier(
                String credentialVerifier
        ) {
            return sessions.values().stream()
                    .filter(session -> session.credentialVerifier()
                            .equals(credentialVerifier))
                    .findFirst();
        }

        @Override
        public SessionRecord revoke(
                String sessionIdentity,
                Instant revokedAt,
                String reason
        ) {
            SessionRecord revoked = sessions.get(sessionIdentity)
                    .revoke(revokedAt, reason);
            sessions.put(sessionIdentity, revoked);
            return revoked;
        }

        @Override
        public void revokeAllForIdentity(
                String identityReference,
                Instant revokedAt,
                String reason
        ) {
            sessions.replaceAll((identifier, session) ->
                    session.identityReference().equals(identityReference)
                            && !session.revoked()
                            ? session.revoke(revokedAt, reason)
                            : session
            );
        }
    }
}
