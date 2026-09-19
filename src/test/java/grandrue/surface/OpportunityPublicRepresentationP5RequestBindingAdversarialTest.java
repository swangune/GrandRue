package grandrue.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiSurfaceClass;
import grandrue.application.MerchantScope;
import grandrue.publication.AuthorityBackedOpportunityPublicRepresentationProjectionReadPort;
import grandrue.publication.OpportunityMaterialAffinityObservationContributionRegistration;
import grandrue.publication.OpportunityPublicationHistoryEntry;
import grandrue.publication.OpportunityPublicationMaterialRevision;
import grandrue.publication.OpportunityPublicationState;
import grandrue.publication.OpportunityPublicationStateAuthority;
import grandrue.publication.PublicationLifecycle;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpportunityPublicRepresentationP5RequestBindingAdversarialTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final String OPPORTUNITY = "opportunity-1";
    private static final ApiContractIdentity API_CONTRACT =
            new ApiContractIdentity("publication", "public-opportunity-query");

    @Test
    void material_affinity_constructor_cannot_be_rebound_to_another_observation_request() {
        EstablishedObservationRequest firstRequest = request("route-one");
        EstablishedObservationRequest secondRequest = request("route-two");
        BoundedProjectionRead read =
                new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(
                        stableAuthority()
                ).observe(
                        MERCHANT,
                        OPPORTUNITY,
                        Instant.parse("2026-09-05T12:00:00Z")
                ).toBoundedRead(firstRequest);

        IllegalStateException failure = assertThrows(
                IllegalStateException.class,
                () -> new ObservationContributionConstructionBoundary().construct(
                        secondRequest,
                        OpportunityMaterialAffinityObservationContributionRegistration
                                .constructor(read)
                )
        );

        assertTrue(failure.getMessage().contains(
                "Capability Observation Contribution construction failed"
        ));
    }

    @Test
    void bounded_public_material_cannot_be_attached_to_another_merchant_request() {
        EstablishedObservationRequest otherMerchantRequest =
                new DefaultEstablishedObservationRequest(
                        TestReleases.activeRelease("merchant-b", RELEASE),
                        new MerchantScope("merchant-b"),
                        RELEASE,
                        Optional.of(new ApiObservationRequestProvenance(
                                API_CONTRACT,
                                ApiSurfaceClass.PUBLIC,
                                "public-opportunity-route"
                        ))
                );

        var observation = new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(
                stableAuthority()
        ).observe(
                MERCHANT,
                OPPORTUNITY,
                Instant.parse("2026-09-05T12:00:00Z")
        );

        assertThrows(
                IllegalStateException.class,
                () -> observation.toBoundedRead(otherMerchantRequest)
        );
    }

    private static EstablishedObservationRequest request(String routeIdentity) {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(MERCHANT.merchantIdentifier(), RELEASE),
                MERCHANT,
                RELEASE,
                Optional.of(new ApiObservationRequestProvenance(
                        API_CONTRACT,
                        ApiSurfaceClass.PUBLIC,
                        routeIdentity
                ))
        );
    }

    private static OpportunityPublicationStateAuthority stableAuthority() {
        OpportunityPublicationMaterialRevision material =
                new OpportunityPublicationMaterialRevision(
                        MERCHANT,
                        OPPORTUNITY,
                        "revision-1",
                        "Published title",
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        List.of(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty()
                );
        OpportunityPublicationState state = new OpportunityPublicationState(
                MERCHANT,
                OPPORTUNITY,
                "revision-1",
                PublicationLifecycle.PUBLISHED,
                Optional.of("revision-1")
        );

        return new OpportunityPublicationStateAuthority() {
            @Override
            public OpportunityPublicationState establish(
                    OpportunityPublicationState initialState,
                    OpportunityPublicationMaterialRevision initialRevision
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public OpportunityPublicationState compareAndSet(
                    OpportunityPublicationState expectedState,
                    OpportunityPublicationState nextState,
                    Optional<OpportunityPublicationMaterialRevision> newRevision
            ) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<OpportunityPublicationState> current(
                    MerchantScope merchantScope,
                    String opportunityIdentity
            ) {
                return MERCHANT.equals(merchantScope) && OPPORTUNITY.equals(opportunityIdentity)
                        ? Optional.of(state)
                        : Optional.empty();
            }

            @Override
            public Optional<OpportunityPublicationMaterialRevision> revision(
                    MerchantScope merchantScope,
                    String opportunityIdentity,
                    String revisionIdentity
            ) {
                return MERCHANT.equals(merchantScope)
                        && OPPORTUNITY.equals(opportunityIdentity)
                        && "revision-1".equals(revisionIdentity)
                        ? Optional.of(material)
                        : Optional.empty();
            }

            @Override
            public List<OpportunityPublicationHistoryEntry> publicationHistory(
                    MerchantScope merchantScope,
                    String opportunityIdentity
            ) {
                return List.of();
            }
        };
    }
}
