package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.runtime.AuthenticationProvenance;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.runtime.SessionRecord;
import grandrue.runtime.SessionRecordStore;
import grandrue.runtime.TrustedExecutionContext;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerContextualObservationSubjectTest {

    private static final Instant AUTHENTICATED_AT =
            Instant.parse("2026-09-01T09:15:00Z");
    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");
    private static final ContextualAccessProofKind KIND =
            new ContextualAccessProofKind("booking", "guest-access");

    @Test
    void default_empty_registry_cannot_enable_a_contextual_subject() {
        EstablishedObservationRequest request = request(MERCHANT);
        TrustedExecutionContext guest = guest(MERCHANT, "guest-1");
        EstablishedContextualAccessProof proof =
                proof(request, MERCHANT, "guest-1");

        AudienceObservationContextEstablishmentResult result =
                new AudienceObservationContextEstablisher(
                        new FixedSessionStore(Optional.empty())
                ).establishCustomerContextual(request, guest, proof);

        assertFailure(
                result,
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_OWNER_OR_KIND_MISMATCH
        );
    }

    @Test
    void establishes_guest_and_authenticated_contextual_subjects_from_exact_binding() {
        EstablishedObservationRequest guestRequest = request(MERCHANT);
        TrustedExecutionContext guest = guest(MERCHANT, "guest-1");
        EstablishedContextualAccessProof guestProof =
                proof(guestRequest, MERCHANT, "guest-1");
        AudienceObservationContextEstablisher guestEstablisher =
                establisher(Optional.empty());

        AudienceObservationContext guestContext = guestEstablisher
                .establishCustomerContextual(
                        guestRequest,
                        guest,
                        guestProof
                )
                .context()
                .orElseThrow();

        assertEquals(
                SurfaceAudience.CUSTOMER,
                AudienceObservationContextDetails.audience(guestContext)
        );
        assertSame(
                guestProof,
                AudienceObservationContextDetails
                        .contextualAccessProof(guestContext)
                        .orElseThrow()
        );
        assertSame(
                guest,
                AudienceObservationContextDetails
                        .executionContext(guestContext)
                        .orElseThrow()
        );

        EstablishedObservationRequest authenticatedRequest =
                request(MERCHANT);
        TrustedExecutionContext authenticated = authenticated(
                MERCHANT,
                "customer-1"
        );
        EstablishedContextualAccessProof authenticatedProof =
                proof(authenticatedRequest, MERCHANT, "customer-1");
        AudienceObservationContext authenticatedContext =
                establisher(Optional.of(session(
                        "session-customer-1",
                        "customer-1",
                        AUTHENTICATED_AT
                )))
                        .establishCustomerContextual(
                                authenticatedRequest,
                                authenticated,
                                authenticatedProof
                        )
                        .context()
                        .orElseThrow();

        assertSame(
                authenticatedProof,
                AudienceObservationContextDetails
                        .contextualAccessProof(authenticatedContext)
                        .orElseThrow()
        );
    }

    @Test
    void rejects_request_scope_and_principal_mismatch() {
        EstablishedObservationRequest request = request(MERCHANT);
        TrustedExecutionContext guest = guest(MERCHANT, "guest-1");
        AudienceObservationContextEstablisher establisher =
                establisher(Optional.empty());

        assertFailure(
                establisher.establishCustomerContextual(
                        request,
                        guest,
                        proof(request(MERCHANT), MERCHANT, "guest-1")
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_REQUEST_MISMATCH
        );
        assertFailure(
                establisher.establishCustomerContextual(
                        request,
                        guest,
                        proof(
                                request,
                                new MerchantScope("merchant-b"),
                                "guest-1"
                        )
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_SCOPE_MISMATCH
        );
        assertFailure(
                establisher.establishCustomerContextual(
                        request,
                        guest,
                        proof(request, MERCHANT, "guest-2")
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_PRINCIPAL_MISMATCH
        );
    }

    @Test
    void rejects_blank_stable_access_binding() {
        EstablishedObservationRequest request = request(MERCHANT);
        EstablishedContextualAccessProof blankBinding =
                new TestContextualAccessProof(
                        KIND,
                        EstablishedObservationRequestDetails
                                .requestBinding(request),
                        MERCHANT,
                        "guest-1",
                        " "
                );

        assertFailure(
                establisher(Optional.empty())
                        .establishCustomerContextual(
                                request,
                                guest(MERCHANT, "guest-1"),
                                blankBinding
                        ),
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_ACCESS_BINDING_INVALID
        );
    }

    @Test
    void rejects_unregistered_owner_kind_and_wrong_exact_class() {
        EstablishedObservationRequest request = request(MERCHANT);
        TrustedExecutionContext guest = guest(MERCHANT, "guest-1");

        EstablishedContextualAccessProof wrongKind =
                new TestContextualAccessProof(
                        new ContextualAccessProofKind("ordering", "guest-access"),
                        EstablishedObservationRequestDetails
                                .requestBinding(request),
                        MERCHANT,
                        "guest-1",
                        "safe-binding-1"
                );
        assertFailure(
                establisher(Optional.empty())
                        .establishCustomerContextual(
                                request,
                                guest,
                                wrongKind
                        ),
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_OWNER_OR_KIND_MISMATCH
        );

        EstablishedContextualAccessProof wrongClass =
                new OtherTestContextualAccessProof(
                        KIND,
                        EstablishedObservationRequestDetails
                                .requestBinding(request),
                        MERCHANT,
                        "guest-1",
                        "safe-binding-1"
                );
        assertFailure(
                establisher(Optional.empty())
                        .establishCustomerContextual(
                                request,
                                guest,
                                wrongClass
                        ),
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_CLASS_MISMATCH
        );
    }

    @Test
    void authenticated_contextual_subject_requires_principal_and_session_binding() {
        EstablishedObservationRequest request = request(MERCHANT);
        TrustedExecutionContext mismatchedPrincipal =
                new TrustedExecutionContext(
                        MERCHANT,
                        new ExecutionPrincipal("customer-1"),
                        Optional.of(new AuthenticationProvenance(
                                "session-customer-1",
                                "customer-2",
                                AUTHENTICATED_AT
                        ))
                );
        assertFailure(
                establisher(Optional.empty())
                        .establishCustomerContextual(
                                request,
                                mismatchedPrincipal,
                                proof(request, MERCHANT, "customer-1")
                        ),
                AudienceObservationContextEstablishmentFailure
                        .CONTEXTUAL_PROOF_PRINCIPAL_MISMATCH
        );

        assertFailure(
                establisher(Optional.empty())
                        .establishCustomerContextual(
                                request,
                                authenticated(MERCHANT, "customer-1"),
                                proof(request, MERCHANT, "customer-1")
                        ),
                AudienceObservationContextEstablishmentFailure
                        .AUTHENTICATION_SESSION_UNRESOLVED
        );
    }

    @Test
    void proof_binding_registry_is_empty_capable_exact_and_immutable() {
        ContextualAccessProofRuntimeBinding binding =
                new ContextualAccessProofRuntimeBinding(
                        KIND,
                        TestContextualAccessProof.class
                );
        ContextualAccessProofRuntimeBindingSnapshot empty =
                new ContextualAccessProofRuntimeBindingSnapshot(List.of());
        ContextualAccessProofRuntimeBindingSnapshot snapshot =
                new ContextualAccessProofRuntimeBindingSnapshot(
                        List.of(binding)
                );

        assertTrue(empty.bindings().isEmpty());
        assertEquals(Optional.of(binding), snapshot.binding(KIND));
        assertTrue(snapshot.matches(KIND, TestContextualAccessProof.class));
        assertFalse(snapshot.matches(KIND, OtherTestContextualAccessProof.class));
        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.bindings().clear()
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> new ContextualAccessProofRuntimeBindingSnapshot(
                        List.of(binding, binding)
                )
        );
    }

    private static AudienceObservationContextEstablisher establisher(
            Optional<SessionRecord> session
    ) {
        return new AudienceObservationContextEstablisher(
                new FixedSessionStore(session),
                new ContextualAccessProofRuntimeBindingSnapshot(List.of(
                        new ContextualAccessProofRuntimeBinding(
                                KIND,
                                TestContextualAccessProof.class
                        )
                ))
        );
    }

    private static EstablishedContextualAccessProof proof(
            EstablishedObservationRequest request,
            MerchantScope scope,
            String principal
    ) {
        return new TestContextualAccessProof(
                KIND,
                EstablishedObservationRequestDetails.requestBinding(request),
                scope,
                principal,
                "safe-binding-1"
        );
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

    private static TrustedExecutionContext guest(
            MerchantScope scope,
            String principal
    ) {
        return new TrustedExecutionContext(
                scope,
                new ExecutionPrincipal(principal),
                Optional.empty()
        );
    }

    private static TrustedExecutionContext authenticated(
            MerchantScope scope,
            String principal
    ) {
        return new TrustedExecutionContext(
                scope,
                new ExecutionPrincipal(principal),
                Optional.of(new AuthenticationProvenance(
                        "session-" + principal,
                        principal,
                        AUTHENTICATED_AT
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
                "verifier-" + session,
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
        assertTrue(result.context().isEmpty());
        assertEquals(Optional.of(expected), result.failure());
    }

    private record FixedSessionStore(Optional<SessionRecord> result)
            implements SessionRecordStore {

        @Override
        public SessionRecord create(SessionRecord candidate) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<SessionRecord> sessionByIdentity(
                String sessionIdentity
        ) {
            return result;
        }

        @Override
        public Optional<SessionRecord> sessionByCredentialVerifier(
                String credentialVerifier
        ) {
            return Optional.empty();
        }

        @Override
        public SessionRecord revoke(
                String sessionIdentity,
                Instant revokedAt,
                String reason
        ) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void revokeAllForIdentity(
                String identityReference,
                Instant revokedAt,
                String reason
        ) {
            throw new UnsupportedOperationException();
        }
    }
}

final class TestContextualAccessProof implements EstablishedContextualAccessProof {

    private final ContextualAccessProofKind kind;
    private final ObservationRequestBinding requestBinding;
    private final MerchantScope merchantScope;
    private final String principalIdentifier;
    private final String accessBinding;

    TestContextualAccessProof(
            ContextualAccessProofKind kind,
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope,
            String principalIdentifier,
            String accessBinding
    ) {
        this.kind = kind;
        this.requestBinding = requestBinding;
        this.merchantScope = merchantScope;
        this.principalIdentifier = principalIdentifier;
        this.accessBinding = accessBinding;
    }

    @Override
    public ContextualAccessProofKind kind() {
        return kind;
    }

    @Override
    public ObservationRequestBinding requestBinding() {
        return requestBinding;
    }

    @Override
    public MerchantScope merchantScope() {
        return merchantScope;
    }

    @Override
    public String principalIdentifier() {
        return principalIdentifier;
    }

    @Override
    public String accessBinding() {
        return accessBinding;
    }
}

final class OtherTestContextualAccessProof
        implements EstablishedContextualAccessProof {

    private final TestContextualAccessProof delegate;

    OtherTestContextualAccessProof(
            ContextualAccessProofKind kind,
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope,
            String principalIdentifier,
            String accessBinding
    ) {
        delegate = new TestContextualAccessProof(
                kind,
                requestBinding,
                merchantScope,
                principalIdentifier,
                accessBinding
        );
    }

    @Override
    public ContextualAccessProofKind kind() {
        return delegate.kind();
    }

    @Override
    public ObservationRequestBinding requestBinding() {
        return delegate.requestBinding();
    }

    @Override
    public MerchantScope merchantScope() {
        return delegate.merchantScope();
    }

    @Override
    public String principalIdentifier() {
        return delegate.principalIdentifier();
    }

    @Override
    public String accessBinding() {
        return delegate.accessBinding();
    }
}
