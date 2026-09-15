package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicInteractionBindingProjectorAdversarialTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final String MODEL_ID = "model-a";
    private static final long MODEL_VERSION = 7L;

    private static final ProjectionContractIdentity PROJECTION_CONTRACT =
            new ProjectionContractIdentity("platform", "public-interaction-subjects");
    private static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("platform", "public-interaction-subjects");

    private static final SurfaceContributionIdentity APPOINTMENT =
            new SurfaceContributionIdentity("appointment", "arrange-appointment");
    private static final String APPOINTMENT_OPERATION = "appointment.confirm";

    private static final PublicInteractionParticipationSourceIdentity SOURCE_A =
            new PublicInteractionParticipationSourceIdentity(
                    "appointment",
                    "source-a"
            );
    private static final PublicInteractionParticipationSourceIdentity SOURCE_B =
            new PublicInteractionParticipationSourceIdentity(
                    "appointment",
                    "source-b"
            );

    @Test
    void rejects_assembly_from_another_bounded_read_even_when_subject_identity_matches() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));
        BoundedProjectionRead otherRead = read(MERCHANT, RELEASE, List.of(subject));
        PublicCustomerProjectionAssembly otherAssembly =
                assembly(otherRead, List.of(subject));

        assertThrows(
                PublicInteractionBindingProjectionStructuralException.class,
                () -> projector().project(
                        model(MERCHANT.merchantIdentifier(), RELEASE),
                        catalogue(interaction(APPOINTMENT, APPOINTMENT_OPERATION)),
                        read,
                        otherAssembly,
                        registry(RELEASE, SOURCE_A, request -> Set.of())
                )
        );
    }

    @Test
    void rejects_cross_merchant_model_reuse() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));

        assertThrows(
                PublicInteractionBindingProjectionStructuralException.class,
                () -> projector().project(
                        model("merchant-b", RELEASE),
                        catalogue(interaction(APPOINTMENT, APPOINTMENT_OPERATION)),
                        read,
                        assembly(read, List.of(subject)),
                        registry(RELEASE, SOURCE_A, request -> Set.of())
                )
        );
    }

    @Test
    void rejects_cross_release_model_and_registry_reuse() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));
        PublicCustomerProjectionAssembly assembly = assembly(read, List.of(subject));
        StaticSurfaceContributionCatalogue catalogue =
                catalogue(interaction(APPOINTMENT, APPOINTMENT_OPERATION));

        assertThrows(
                PublicInteractionBindingProjectionStructuralException.class,
                () -> projector().project(
                        model(MERCHANT.merchantIdentifier(), "semantic-registry-2.0"),
                        catalogue,
                        read,
                        assembly,
                        registry(RELEASE, SOURCE_A, request -> Set.of())
                )
        );

        assertThrows(
                PublicInteractionBindingProjectionStructuralException.class,
                () -> projector().project(
                        model(MERCHANT.merchantIdentifier(), RELEASE),
                        catalogue,
                        read,
                        assembly,
                        registry(
                                "semantic-registry-2.0",
                                SOURCE_A,
                                request -> Set.of()
                        )
                )
        );
    }

    @Test
    void rejects_fact_from_another_resolved_model_context() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));

        PublicInteractionParticipationSource wrongModel = request -> Set.of(
                fact(
                        request,
                        subject.candidateObservation(),
                        SOURCE_A,
                        request.resolvedModelIdentifier() + "-other",
                        request.resolvedModelVersion(),
                        request.contribution().identity(),
                        APPOINTMENT_OPERATION,
                        Optional.empty()
                )
        );
        PublicInteractionParticipationSource wrongVersion = request -> Set.of(
                fact(
                        request,
                        subject.candidateObservation(),
                        SOURCE_A,
                        request.resolvedModelIdentifier(),
                        request.resolvedModelVersion() + 1,
                        request.contribution().identity(),
                        APPOINTMENT_OPERATION,
                        Optional.empty()
                )
        );

        assertStructural(read, subject, registry(RELEASE, SOURCE_A, wrongModel));
        assertStructural(read, subject, registry(RELEASE, SOURCE_A, wrongVersion));
    }

    @Test
    void rejects_contribution_laundering_and_unsupported_operation() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));
        SurfaceContributionIdentity otherContribution =
                new SurfaceContributionIdentity("appointment", "cancel-appointment");

        PublicInteractionParticipationSource wrongContribution = request -> Set.of(
                fact(
                        request,
                        subject.candidateObservation(),
                        SOURCE_A,
                        request.resolvedModelIdentifier(),
                        request.resolvedModelVersion(),
                        otherContribution,
                        APPOINTMENT_OPERATION,
                        Optional.empty()
                )
        );
        PublicInteractionParticipationSource unsupportedOperation = request -> Set.of(
                fact(
                        request,
                        subject.candidateObservation(),
                        SOURCE_A,
                        request.resolvedModelIdentifier(),
                        request.resolvedModelVersion(),
                        request.contribution().identity(),
                        "appointment.cancel",
                        Optional.empty()
                )
        );

        assertStructural(
                read,
                subject,
                registry(RELEASE, SOURCE_A, wrongContribution)
        );
        assertStructural(
                read,
                subject,
                registry(RELEASE, SOURCE_A, unsupportedOperation)
        );
    }

    @Test
    void rejects_source_provenance_laundering() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));

        PublicInteractionParticipationSource source = request -> Set.of(
                fact(
                        request,
                        subject.candidateObservation(),
                        SOURCE_B,
                        request.resolvedModelIdentifier(),
                        request.resolvedModelVersion(),
                        request.contribution().identity(),
                        APPOINTMENT_OPERATION,
                        Optional.empty()
                )
        );

        assertStructural(read, subject, registry(RELEASE, SOURCE_A, source));
    }

    @Test
    void rejects_duplicate_exact_participation_ownership_across_sources() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));

        PublicInteractionParticipationSourceRegistration first = registration(
                SOURCE_A,
                request -> Set.of(fact(request, subject.candidateObservation(), SOURCE_A))
        );
        PublicInteractionParticipationSourceRegistration second = registration(
                SOURCE_B,
                request -> Set.of(fact(request, subject.candidateObservation(), SOURCE_B))
        );

        assertThrows(
                PublicInteractionBindingProjectionStructuralException.class,
                () -> projector().project(
                        model(MERCHANT.merchantIdentifier(), RELEASE),
                        catalogue(interaction(APPOINTMENT, APPOINTMENT_OPERATION)),
                        read,
                        assembly(read, List.of(subject)),
                        new PublicInteractionParticipationSourceRegistrySnapshot(
                                RELEASE,
                                Set.of(first, second)
                        )
                )
        );
    }

    @Test
    void non_public_interaction_contribution_never_invokes_participation_sources() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));
        AtomicInteger calls = new AtomicInteger();
        PublicInteractionParticipationSource source = request -> {
            calls.incrementAndGet();
            return Set.of();
        };

        List<PublicInteractionBinding> bindings = projector().project(
                model(MERCHANT.merchantIdentifier(), RELEASE),
                catalogue(new StaticSurfaceContribution(
                        new SurfaceContributionIdentity("appointment", "workspace-card"),
                        SurfaceAudience.PUBLIC,
                        SurfaceContributionKind.WORKSPACE,
                        Optional.empty(),
                        Set.of(),
                        Set.of()
                )),
                read,
                assembly(read, List.of(subject)),
                registry(RELEASE, SOURCE_A, source)
        );

        assertTrue(bindings.isEmpty());
        assertEquals(0, calls.get());
    }

    @Test
    void one_interaction_can_bind_multiple_selected_subjects() {
        TestFragment first = fragment("offering-1");
        TestFragment second = fragment("offering-2");
        BoundedProjectionRead read = read(
                MERCHANT,
                RELEASE,
                List.of(first, second)
        );
        PublicInteractionParticipationSource source = request -> Set.of(
                fact(request, first.candidateObservation(), SOURCE_A),
                fact(request, second.candidateObservation(), SOURCE_A)
        );

        List<PublicInteractionBinding> bindings = projector().project(
                model(MERCHANT.merchantIdentifier(), RELEASE),
                catalogue(interaction(APPOINTMENT, APPOINTMENT_OPERATION)),
                read,
                assembly(read, List.of(first, second)),
                registry(RELEASE, SOURCE_A, source)
        );

        assertEquals(2, bindings.size());
        assertEquals(
                Set.of(
                        first.candidateObservation(),
                        second.candidateObservation()
                ),
                bindings.stream()
                        .map(PublicInteractionBinding::subject)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }

    @Test
    void one_selected_subject_can_bind_multiple_interactions() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));
        SurfaceContributionIdentity enquiry =
                new SurfaceContributionIdentity("appointment", "ask-question");
        String enquiryOperation = "appointment.enquire";

        PublicInteractionParticipationSource source = request -> Set.of(
                new PublicInteractionParticipationFact(
                        request.merchantScope(),
                        request.semanticRegistryReleaseIdentifier(),
                        request.resolvedModelIdentifier(),
                        request.resolvedModelVersion(),
                        request.contribution().identity(),
                        request.contribution().supportedOperationReferences()
                                .iterator()
                                .next(),
                        subject.candidateObservation(),
                        SOURCE_A,
                        Optional.empty()
                )
        );

        List<PublicInteractionBinding> bindings = projector().project(
                model(MERCHANT.merchantIdentifier(), RELEASE),
                catalogue(
                        interaction(APPOINTMENT, APPOINTMENT_OPERATION),
                        interaction(enquiry, enquiryOperation)
                ),
                read,
                assembly(read, List.of(subject)),
                registry(RELEASE, SOURCE_A, source)
        );

        assertEquals(2, bindings.size());
        assertEquals(
                Set.of(APPOINTMENT, enquiry),
                bindings.stream()
                        .map(PublicInteractionBinding::contributionIdentity)
                        .collect(java.util.stream.Collectors.toSet())
        );
    }

    @Test
    void registry_and_projected_bindings_are_immutable_snapshots() {
        TestFragment subject = fragment("offering-1");
        BoundedProjectionRead read = read(MERCHANT, RELEASE, List.of(subject));
        Set<PublicInteractionParticipationSourceRegistration> mutable =
                new HashSet<>();
        mutable.add(registration(
                SOURCE_A,
                request -> Set.of(fact(request, subject.candidateObservation(), SOURCE_A))
        ));
        PublicInteractionParticipationSourceRegistrySnapshot snapshot =
                new PublicInteractionParticipationSourceRegistrySnapshot(
                        RELEASE,
                        mutable
                );
        mutable.clear();

        assertEquals(1, snapshot.registrations().size());
        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.registrations().clear()
        );

        List<PublicInteractionBinding> bindings = projector().project(
                model(MERCHANT.merchantIdentifier(), RELEASE),
                catalogue(interaction(APPOINTMENT, APPOINTMENT_OPERATION)),
                read,
                assembly(read, List.of(subject)),
                snapshot
        );
        assertEquals(1, bindings.size());
        assertThrows(
                UnsupportedOperationException.class,
                () -> bindings.add(bindings.getFirst())
        );
    }

    @Test
    void participation_fact_and_binding_do_not_smuggle_downstream_authority() {
        assertNoComponentType(
                PublicInteractionParticipationFact.class,
                BoundedProjectionReadBinding.class
        );
        assertNoComponentType(
                PublicInteractionBinding.class,
                BoundedProjectionReadBinding.class
        );

        Set<String> forbidden = Set.of(
                "Repository",
                "Provider",
                "Availability",
                "Authorization",
                "Execution",
                "Http",
                "JsonNode",
                "PublicInteractionBindingExposureAuthority"
        );
        assertNoForbiddenRecordComponents(
                PublicInteractionParticipationFact.class,
                forbidden
        );
        assertNoForbiddenRecordComponents(PublicInteractionBinding.class, forbidden);
        assertNoForbiddenDeclaredFields(
                PublicInteractionBindingProjector.class,
                forbidden
        );
        assertEquals(0, PublicInteractionBindingProjector.class.getDeclaredFields().length);
    }

    private static void assertStructural(
            BoundedProjectionRead read,
            TestFragment subject,
            PublicInteractionParticipationSourceRegistrySnapshot registry
    ) {
        assertThrows(
                PublicInteractionBindingProjectionStructuralException.class,
                () -> projector().project(
                        model(MERCHANT.merchantIdentifier(), RELEASE),
                        catalogue(interaction(APPOINTMENT, APPOINTMENT_OPERATION)),
                        read,
                        assembly(read, List.of(subject)),
                        registry
                )
        );
    }

    private static void assertNoComponentType(
            Class<?> recordType,
            Class<?> forbiddenType
    ) {
        assertTrue(recordType.isRecord());
        for (RecordComponent component : recordType.getRecordComponents()) {
            assertFalse(
                    component.getType().equals(forbiddenType),
                    () -> recordType.getSimpleName()
                            + " must not carry "
                            + forbiddenType.getSimpleName()
            );
        }
    }

    private static void assertNoForbiddenRecordComponents(
            Class<?> recordType,
            Set<String> forbiddenFragments
    ) {
        for (RecordComponent component : recordType.getRecordComponents()) {
            String typeName = component.getGenericType().getTypeName();
            for (String forbidden : forbiddenFragments) {
                assertFalse(
                        typeName.contains(forbidden),
                        () -> recordType.getSimpleName()
                                + " leaks forbidden authority type "
                                + typeName
                );
            }
        }
    }

    private static void assertNoForbiddenDeclaredFields(
            Class<?> type,
            Set<String> forbiddenFragments
    ) {
        for (Field field : type.getDeclaredFields()) {
            String typeName = field.getGenericType().getTypeName();
            for (String forbidden : forbiddenFragments) {
                assertFalse(typeName.contains(forbidden));
            }
        }
    }

    private static PublicInteractionBindingProjector projector() {
        return new PublicInteractionBindingProjector();
    }

    private static PublicInteractionParticipationSourceRegistrySnapshot registry(
            String release,
            PublicInteractionParticipationSourceIdentity identity,
            PublicInteractionParticipationSource source
    ) {
        return new PublicInteractionParticipationSourceRegistrySnapshot(
                release,
                Set.of(registration(identity, source))
        );
    }

    private static PublicInteractionParticipationSourceRegistration registration(
            PublicInteractionParticipationSourceIdentity identity,
            PublicInteractionParticipationSource source
    ) {
        return new PublicInteractionParticipationSourceRegistration(
                identity,
                source
        );
    }

    private static PublicInteractionParticipationFact fact(
            PublicInteractionParticipationRequest request,
            ExposureCandidateObservation subject,
            PublicInteractionParticipationSourceIdentity sourceIdentity
    ) {
        return fact(
                request,
                subject,
                sourceIdentity,
                request.resolvedModelIdentifier(),
                request.resolvedModelVersion(),
                request.contribution().identity(),
                request.contribution().supportedOperationReferences()
                        .iterator()
                        .next(),
                Optional.empty()
        );
    }

    private static PublicInteractionParticipationFact fact(
            PublicInteractionParticipationRequest request,
            ExposureCandidateObservation subject,
            PublicInteractionParticipationSourceIdentity sourceIdentity,
            String resolvedModelIdentifier,
            long resolvedModelVersion,
            SurfaceContributionIdentity contributionIdentity,
            String operationReference,
            Optional<String> role
    ) {
        return new PublicInteractionParticipationFact(
                request.merchantScope(),
                request.semanticRegistryReleaseIdentifier(),
                resolvedModelIdentifier,
                resolvedModelVersion,
                contributionIdentity,
                operationReference,
                subject,
                sourceIdentity,
                role
        );
    }

    private static StaticSurfaceContribution interaction(
            SurfaceContributionIdentity identity,
            String operation
    ) {
        return new StaticSurfaceContribution(
                identity,
                SurfaceAudience.PUBLIC,
                SurfaceContributionKind.PUBLIC_INTERACTION,
                Optional.of("public/primary-actions"),
                Set.of(),
                Set.of(operation)
        );
    }

    private static StaticSurfaceContributionCatalogue catalogue(
            StaticSurfaceContribution... contributions
    ) {
        return new StaticSurfaceContributionCatalogue(List.of(contributions));
    }

    private static ExecutableMerchantModel model(
            String merchantIdentifier,
            String release
    ) {
        return new ExecutableMerchantModel(
                merchantIdentifier,
                MODEL_ID,
                MODEL_VERSION,
                release,
                Set.of("appointment"),
                List.of(),
                List.of(),
                List.of()
        );
    }

    private static BoundedProjectionRead read(
            MerchantScope merchantScope,
            String release,
            List<? extends ProjectionMaterialFragment> fragments
    ) {
        return new BoundedProjectionRead(
                request(merchantScope, release),
                PROJECTION_CONTRACT,
                READ_USE,
                fragments
        );
    }

    private static PublicCustomerProjectionAssembly assembly(
            BoundedProjectionRead read,
            List<? extends ProjectionMaterialFragment> selected
    ) {
        return new PublicCustomerProjectionAssembly(read.binding(), selected);
    }

    private static EstablishedObservationRequest request(
            MerchantScope merchantScope,
            String release
    ) {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        merchantScope.merchantIdentifier(),
                        release
                ),
                merchantScope,
                release,
                Optional.empty()
        );
    }

    private static TestFragment fragment(String subjectIdentifier) {
        return new TestFragment(
                new ExposureCandidateObservation(
                        new ExposableElementReference(
                                "appointment",
                                "offering-summary"
                        ),
                        Optional.of(new ExposureCandidateInstanceReference(
                                "appointment",
                                "offering",
                                subjectIdentifier
                        ))
                ),
                Set.of()
        );
    }

    private record TestFragment(
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {
        private TestFragment {
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
