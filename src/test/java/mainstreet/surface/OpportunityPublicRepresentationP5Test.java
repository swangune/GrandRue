package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.application.MerchantScope;
import mainstreet.publication.AuthorityBackedOpportunityPublicExposureReadPort;
import mainstreet.publication.AuthorityBackedOpportunityPublicRepresentationProjectionReadPort;
import mainstreet.publication.OpportunityExactInstantBoundary;
import mainstreet.publication.OpportunityExposureWindowRequirementEvaluator;
import mainstreet.publication.OpportunityExternalLink;
import mainstreet.publication.OpportunityExternalLinkRole;
import mainstreet.publication.OpportunityMaterialAffinityObservationContributionRegistration;
import mainstreet.publication.OpportunityPublicExposureContractPortfolio;
import mainstreet.publication.OpportunityPublicExposureReadPort;
import mainstreet.publication.OpportunityPublicExposureReferences;
import mainstreet.publication.OpportunityPublicRepresentation;
import mainstreet.publication.OpportunityPublicRepresentationProjectionFragment;
import mainstreet.publication.OpportunityPublicRepresentationProjectionReferences;
import mainstreet.publication.OpportunityPublicationHistoryEntry;
import mainstreet.publication.OpportunityPublicationMaterialRevision;
import mainstreet.publication.OpportunityPublicationState;
import mainstreet.publication.OpportunityPublicationStateAuthority;
import mainstreet.publication.PublicationLifecycle;
import org.junit.jupiter.api.Test;

import java.lang.reflect.RecordComponent;
import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpportunityPublicRepresentationP5Test {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final String OPPORTUNITY = "opportunity-1";
    private static final Instant OBSERVED_AT = Instant.parse("2026-09-05T12:00:00Z");
    private static final ApiContractIdentity API_CONTRACT =
            new ApiContractIdentity("publication", "public-opportunity-query");

    @Test
    void bounded_read_uses_exact_published_revision_and_contains_only_public_material() {
        MutableAuthority authority = authorityWithUnpublishedCurrentEdit();
        EstablishedObservationRequest request = request();
        BoundedProjectionRead read =
                new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(authority)
                        .observe(MERCHANT, OPPORTUNITY, OBSERVED_AT)
                        .toBoundedRead(request);

        OpportunityPublicRepresentationProjectionFragment fragment = assertInstanceOf(
                OpportunityPublicRepresentationProjectionFragment.class,
                read.fragments().getFirst()
        );
        assertEquals(OpportunityPublicExposureReferences.candidate(OPPORTUNITY),
                fragment.candidateObservation());
        assertEquals(Set.of(new ProjectionMaterialSourceAffinity(
                        OpportunityPublicRepresentationProjectionReferences.PUBLISHED_MATERIAL_SOURCE,
                        "revision-1")),
                fragment.sourceAffinities());

        OpportunityPublicRepresentation representation = fragment.representation();
        assertEquals("Published title", representation.title());
        assertEquals(Optional.of("Published description"), representation.description());
        assertEquals(Optional.of("Published eligibility"), representation.eligibilityInformation());
        assertEquals(Optional.of("Provider A"), representation.externalProviderName());
        assertEquals(Optional.of("Source A"), representation.sourceName());
        assertEquals(1, representation.externalLinks().size());

        Set<String> fields = Arrays.stream(OpportunityPublicRepresentation.class.getRecordComponents())
                .map(RecordComponent::getName)
                .collect(java.util.stream.Collectors.toSet());
        assertEquals(Set.of(
                        "title",
                        "description",
                        "eligibilityInformation",
                        "externalProviderName",
                        "sourceName",
                        "externalLinks",
                        "applicationsOpen",
                        "applicationDeadline"),
                fields);
        assertFalse(fields.contains("revisionIdentity"));
        assertFalse(fields.contains("publishFrom"));
        assertFalse(fields.contains("publishUntil"));
        assertFalse(fields.contains("schemaReference"));

        ProjectionSourceEvidence evidence = read.sourceEvidence().stream()
                .filter(candidate -> OpportunityPublicRepresentationProjectionReferences
                        .PUBLISHED_MATERIAL_SOURCE.equals(candidate.sourceReference()))
                .findFirst()
                .orElseThrow();
        assertEquals(Optional.of("revision-1"), evidence.observedProgressIdentifier());
        assertEquals(Optional.of("revision-1"), evidence.requiredCurrentProgressIdentifier());
    }

    @Test
    void positive_exposure_selects_the_same_bounded_fragment() {
        MutableAuthority authority = authorityWithUnpublishedCurrentEdit();
        EstablishedObservationRequest request = request();
        BoundedProjectionRead read =
                new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(authority)
                        .observe(MERCHANT, OPPORTUNITY, OBSERVED_AT)
                        .toBoundedRead(request);
        EstablishedObservationContribution affinity =
                new ObservationContributionConstructionBoundary().construct(
                        request,
                        OpportunityMaterialAffinityObservationContributionRegistration.constructor(read)
                );
        AudienceObservationContext context = new DefaultAudienceObservationContext(
                request,
                new PublicObservationSubject(),
                Set.of(affinity)
        );
        AudienceObservationAdmissionResult admission = admission(request);

        List<ResolvedExposedElement> resolved = resolve(
                context,
                admission,
                read.fragments().getFirst().candidateObservation(),
                new AuthorityBackedOpportunityPublicExposureReadPort(authority)
        );
        assertEquals(1, resolved.size());

        ApiExposureResolution apiExposure = new ApiExposureResolutionBinder().bind(
                new DefaultApiAudienceObservationContext(
                        API_CONTRACT,
                        ApiSurfaceClass.PUBLIC,
                        context
                ),
                admission,
                RELEASE,
                resolved
        ).resolution().orElseThrow();
        PublicCustomerProjectionAssembly assembly =
                new PublicCustomerProjectionAssemblyService().assemble(
                        read,
                        fullyServiceable(read),
                        apiExposure
                );
        OpportunityPublicRepresentationProjectionFragment selected = assertInstanceOf(
                OpportunityPublicRepresentationProjectionFragment.class,
                assembly.selectedFragments().getFirst()
        );
        assertSame(read.fragments().getFirst(), selected);
        assertEquals("Published title", selected.representation().title());

        assertEquals(
                new ObservationContributionKind("publication", "material-affinity"),
                affinity.kind()
        );
        assertSame(read.requestBinding(), affinity.requestBinding());
        assertEquals(MERCHANT, affinity.merchantScope());
        assertEquals(
                ObservationContributionCardinality.SINGLE,
                OpportunityMaterialAffinityObservationContributionRegistration
                        .definition()
                        .cardinality()
        );
    }

    @Test
    void republish_after_bounded_read_withholds_stale_fragment() {
        MutableAuthority authority = authorityWithUnpublishedCurrentEdit();
        EstablishedObservationRequest request = request();
        BoundedProjectionRead read =
                new AuthorityBackedOpportunityPublicRepresentationProjectionReadPort(authority)
                        .observe(MERCHANT, OPPORTUNITY, OBSERVED_AT)
                        .toBoundedRead(request);
        EstablishedObservationContribution affinity =
                new ObservationContributionConstructionBoundary().construct(
                        request,
                        OpportunityMaterialAffinityObservationContributionRegistration.constructor(read)
                );

        authority.publishRevision("revision-2");

        AudienceObservationContext context = new DefaultAudienceObservationContext(
                request,
                new PublicObservationSubject(),
                Set.of(affinity)
        );
        List<ResolvedExposedElement> resolved = resolve(
                context,
                admission(request),
                read.fragments().getFirst().candidateObservation(),
                new AuthorityBackedOpportunityPublicExposureReadPort(authority)
        );

        assertTrue(resolved.isEmpty());
        OpportunityPublicRepresentationProjectionFragment stale = assertInstanceOf(
                OpportunityPublicRepresentationProjectionFragment.class,
                read.fragments().getFirst()
        );
        assertEquals("Published title", stale.representation().title());
        assertEquals("Edited title", authority.revision(
                MERCHANT,
                OPPORTUNITY,
                "revision-2"
        ).orElseThrow().title());
    }

    private static List<ResolvedExposedElement> resolve(
            AudienceObservationContext context,
            AudienceObservationAdmissionResult admission,
            ExposureCandidateObservation candidate,
            OpportunityPublicExposureReadPort readPort
    ) {
        return new ExposureResolver().resolve(
                context,
                admission,
                List.of(candidate),
                OpportunityPublicExposureContractPortfolio.forRelease(RELEASE),
                new ExposureRequirementEvaluatorBindingSnapshot(
                        RELEASE,
                        List.of(new ExposureRequirementEvaluatorBinding(
                                OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT,
                                new OpportunityExposureWindowRequirementEvaluator(readPort)
                        ))
                ),
                new MerchantExposureChoiceEvaluatorBindingSnapshot(RELEASE, List.of())
        );
    }

    private static ProjectionServiceabilityResult fullyServiceable(BoundedProjectionRead read) {
        return new ProjectionServiceabilityResult(
                RELEASE,
                read.contractIdentity(),
                read.readUseIdentity(),
                OBSERVED_AT,
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                Set.of(),
                Set.of(),
                read.sourceEvidence(),
                Set.of(),
                Set.of(),
                Optional.of(read.binding())
        );
    }

    private static AudienceObservationAdmissionResult admission(
            EstablishedObservationRequest request
    ) {
        return AudienceObservationAdmissionResults.admitted(
                new DefaultAudienceObservationInvocationBinding(
                        EstablishedObservationRequestDetails.requestBinding(request)
                ),
                OBSERVED_AT
        );
    }

    private static EstablishedObservationRequest request() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(MERCHANT.merchantIdentifier(), RELEASE),
                MERCHANT,
                RELEASE,
                Optional.of(new ApiObservationRequestProvenance(
                        API_CONTRACT,
                        ApiSurfaceClass.PUBLIC,
                        "public-opportunity-route"
                ))
        );
    }

    private static MutableAuthority authorityWithUnpublishedCurrentEdit() {
        OpportunityPublicationMaterialRevision published = revision(
                "revision-1",
                "Published title",
                "Published description",
                "Published eligibility"
        );
        OpportunityPublicationMaterialRevision edited = revision(
                "revision-2",
                "Edited title",
                "Edited description",
                "Edited eligibility"
        );
        return new MutableAuthority(
                new OpportunityPublicationState(
                        MERCHANT,
                        OPPORTUNITY,
                        "revision-2",
                        PublicationLifecycle.PUBLISHED,
                        Optional.of("revision-1")
                ),
                List.of(published, edited)
        );
    }

    private static OpportunityPublicationMaterialRevision revision(
            String identity,
            String title,
            String description,
            String eligibility
    ) {
        return new OpportunityPublicationMaterialRevision(
                MERCHANT,
                OPPORTUNITY,
                identity,
                title,
                Optional.of(description),
                Optional.of(eligibility),
                Optional.of("Provider A"),
                Optional.of("Source A"),
                List.of(new OpportunityExternalLink(
                        OpportunityExternalLinkRole.OFFICIAL_APPLICATION,
                        URI.create("https://example.com/apply"),
                        Optional.of("Apply")
                )),
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-09-10T09:00:00Z")
                )),
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-09-30T17:00:00Z")
                )),
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-09-01T00:00:00Z")
                )),
                Optional.of(new OpportunityExactInstantBoundary(
                        Instant.parse("2026-10-01T00:00:00Z")
                ))
        );
    }

    private static final class MutableAuthority
            implements OpportunityPublicationStateAuthority {

        private OpportunityPublicationState state;
        private final Map<String, OpportunityPublicationMaterialRevision> revisions =
                new LinkedHashMap<>();

        private MutableAuthority(
                OpportunityPublicationState state,
                List<OpportunityPublicationMaterialRevision> revisions
        ) {
            this.state = state;
            revisions.forEach(revision -> this.revisions.put(
                    revision.revisionIdentity(),
                    revision
            ));
        }

        void publishRevision(String revisionIdentity) {
            this.state = new OpportunityPublicationState(
                    MERCHANT,
                    OPPORTUNITY,
                    revisionIdentity,
                    PublicationLifecycle.PUBLISHED,
                    Optional.of(revisionIdentity)
            );
        }

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
            if (!MERCHANT.equals(merchantScope) || !OPPORTUNITY.equals(opportunityIdentity)) {
                return Optional.empty();
            }
            return Optional.ofNullable(revisions.get(revisionIdentity));
        }

        @Override
        public List<OpportunityPublicationHistoryEntry> publicationHistory(
                MerchantScope merchantScope,
                String opportunityIdentity
        ) {
            return List.of();
        }
    }
}
