package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.SessionRecordStore;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AudienceObservationContributionEstablishmentTest {

    private static final MerchantScope MERCHANT =
            new MerchantScope("merchant-a");
    private static final ObservationContributionKind KIND =
            new ObservationContributionKind("booking", "context");
    private static final ObservationContributionKind OTHER_KIND =
            new ObservationContributionKind("ordering", "context");

    @Test
    void establishes_valid_exact_release_bound_contributions_immutably() {
        EstablishedObservationRequest request = request(MERCHANT);
        EstablishedObservationContribution contribution =
                contribution(KIND, request, MERCHANT);
        AudienceObservationContextEstablisher establisher = establisher(
                definition(
                        KIND,
                        Set.of(SurfaceAudience.PUBLIC)
                ),
                new ObservationContributionRuntimeBinding(
                        KIND,
                        TestObservationContribution.class
                )
        );

        AudienceObservationContextEstablishmentResult result =
                establisher.establishPublic(
                        request,
                        List.of(contribution)
                );

        AudienceObservationContext context = result.context().orElseThrow();
        assertTrue(result.failure().isEmpty());
        assertEquals(
                Set.of(contribution),
                AudienceObservationContextDetails.contributions(context)
        );
        assertSame(
                contribution,
                AudienceObservationContextDetails.contributions(context)
                        .iterator()
                        .next()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> AudienceObservationContextDetails
                        .contributions(context)
                        .clear()
        );
    }

    @Test
    void permits_zero_contributions_without_manufacturing_registry_authority() {
        AudienceObservationContextEstablishmentResult result =
                new AudienceObservationContextEstablisher(emptySessions())
                        .establishPublic(request(MERCHANT));

        assertTrue(result.failure().isEmpty());
        assertTrue(AudienceObservationContextDetails.contributions(
                result.context().orElseThrow()
        ).isEmpty());
    }

    @Test
    void rejects_absent_or_wrong_release_definition_snapshot() {
        EstablishedObservationRequest request = request(MERCHANT);
        EstablishedObservationContribution contribution =
                contribution(KIND, request, MERCHANT);

        assertFailure(
                new AudienceObservationContextEstablisher(emptySessions())
                        .establishPublic(
                                request,
                                List.of(contribution)
                        ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_DEFINITION_MISMATCH
        );

        AudienceObservationContextEstablisher wrongRelease =
                new AudienceObservationContextEstablisher(
                        emptySessions(),
                        new ContextualAccessProofRuntimeBindingSnapshot(
                                List.of()
                        ),
                        new ObservationContributionDefinitionRegistrySnapshot(
                                "semantic-registry-2.0",
                                List.of(definition(
                                        KIND,
                                        Set.of(SurfaceAudience.PUBLIC)
                                ))
                        ),
                        new ObservationContributionRuntimeBindingSnapshot(
                                List.of(new ObservationContributionRuntimeBinding(
                                        KIND,
                                        TestObservationContribution.class
                                ))
                        )
                );
        assertFailure(
                wrongRelease.establishPublic(
                        request,
                        List.of(contribution)
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_DEFINITION_MISMATCH
        );
    }

    @Test
    void rejects_unregistered_definition_or_wrong_exact_runtime_class() {
        EstablishedObservationRequest request = request(MERCHANT);

        assertFailure(
                establisher(
                        definition(
                                KIND,
                                Set.of(SurfaceAudience.PUBLIC)
                        ),
                        new ObservationContributionRuntimeBinding(
                                KIND,
                                TestObservationContribution.class
                        )
                ).establishPublic(
                        request,
                        List.of(contribution(OTHER_KIND, request, MERCHANT))
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_DEFINITION_MISMATCH
        );

        assertFailure(
                establisher(
                        definition(
                                KIND,
                                Set.of(SurfaceAudience.PUBLIC)
                        ),
                        new ObservationContributionRuntimeBinding(
                                KIND,
                                TestObservationContribution.class
                        )
                ).establishPublic(
                        request,
                        List.of(new OtherObservationContribution(
                                KIND,
                                EstablishedObservationRequestDetails
                                        .requestBinding(request),
                                MERCHANT
                        ))
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_RUNTIME_BINDING_MISMATCH
        );
    }

    @Test
    void rejects_audience_request_scope_and_duplicate_mismatch() {
        EstablishedObservationRequest request = request(MERCHANT);
        AudienceObservationContextEstablisher customerOnly = establisher(
                definition(
                        KIND,
                        Set.of(SurfaceAudience.CUSTOMER)
                ),
                new ObservationContributionRuntimeBinding(
                        KIND,
                        TestObservationContribution.class
                )
        );

        assertFailure(
                customerOnly.establishPublic(
                        request,
                        List.of(contribution(KIND, request, MERCHANT))
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_AUDIENCE_MISMATCH
        );

        AudienceObservationContextEstablisher publicEstablisher = establisher(
                definition(
                        KIND,
                        Set.of(SurfaceAudience.PUBLIC)
                ),
                new ObservationContributionRuntimeBinding(
                        KIND,
                        TestObservationContribution.class
                )
        );
        assertFailure(
                publicEstablisher.establishPublic(
                        request,
                        List.of(contribution(
                                KIND,
                                request(MERCHANT),
                                MERCHANT
                        ))
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_REQUEST_MISMATCH
        );
        assertFailure(
                publicEstablisher.establishPublic(
                        request,
                        List.of(contribution(
                                KIND,
                                request,
                                new MerchantScope("merchant-b")
                        ))
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_SCOPE_MISMATCH
        );

        EstablishedObservationContribution first =
                contribution(KIND, request, MERCHANT);
        EstablishedObservationContribution second =
                contribution(KIND, request, MERCHANT);
        assertFailure(
                publicEstablisher.establishPublic(
                        request,
                        List.of(first, second)
                ),
                AudienceObservationContextEstablishmentFailure
                        .CONTRIBUTION_DUPLICATE
        );
    }

    private static AudienceObservationContextEstablisher establisher(
            ObservationContributionDefinition definition,
            ObservationContributionRuntimeBinding binding
    ) {
        return new AudienceObservationContextEstablisher(
                emptySessions(),
                new ContextualAccessProofRuntimeBindingSnapshot(List.of()),
                new ObservationContributionDefinitionRegistrySnapshot(
                        "semantic-registry-1.0",
                        List.of(definition)
                ),
                new ObservationContributionRuntimeBindingSnapshot(
                        List.of(binding)
                )
        );
    }

    private static ObservationContributionDefinition definition(
            ObservationContributionKind kind,
            Set<SurfaceAudience> audiences
    ) {
        return new ObservationContributionDefinition(
                kind,
                audiences,
                ObservationContributionCardinality.SINGLE
        );
    }

    private static EstablishedObservationContribution contribution(
            ObservationContributionKind kind,
            EstablishedObservationRequest request,
            MerchantScope scope
    ) {
        return new TestObservationContribution(
                kind,
                EstablishedObservationRequestDetails.requestBinding(request),
                scope
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

    private static SessionRecordStore emptySessions() {
        return new SessionRecordStore() {
            @Override
            public mainstreet.runtime.SessionRecord create(
                    mainstreet.runtime.SessionRecord candidate
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<mainstreet.runtime.SessionRecord> sessionByIdentity(
                    String sessionIdentity
            ) {
                return Optional.empty();
            }

            @Override
            public Optional<mainstreet.runtime.SessionRecord>
            sessionByCredentialVerifier(String credentialVerifier) {
                return Optional.empty();
            }

            @Override
            public mainstreet.runtime.SessionRecord revoke(
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
        };
    }

    private static void assertFailure(
            AudienceObservationContextEstablishmentResult result,
            AudienceObservationContextEstablishmentFailure expected
    ) {
        assertTrue(result.context().isEmpty());
        assertEquals(Optional.of(expected), result.failure());
    }
}

final class TestObservationContribution
        implements EstablishedObservationContribution {

    private final ObservationContributionKind kind;
    private final ObservationRequestBinding requestBinding;
    private final MerchantScope merchantScope;

    TestObservationContribution(
            ObservationContributionKind kind,
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope
    ) {
        this.kind = kind;
        this.requestBinding = requestBinding;
        this.merchantScope = merchantScope;
    }

    @Override
    public ObservationContributionKind kind() {
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
}

final class OtherObservationContribution
        implements EstablishedObservationContribution {

    private final TestObservationContribution delegate;

    OtherObservationContribution(
            ObservationContributionKind kind,
            ObservationRequestBinding requestBinding,
            MerchantScope merchantScope
    ) {
        delegate = new TestObservationContribution(
                kind,
                requestBinding,
                merchantScope
        );
    }

    @Override
    public ObservationContributionKind kind() {
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
}
