package grandrue.surface;

import grandrue.application.MerchantScope;
import grandrue.semantic.executable.ExecutableMerchantModel;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicInteractionBindingProjectorTest {

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

    private static final PublicInteractionParticipationSourceIdentity TEST_SOURCE =
            new PublicInteractionParticipationSourceIdentity(
                    "appointment",
                    "test-participation"
            );

    @Test
    void zero_registered_sources_returns_empty_bindings_without_inference() {
        TestFragment exposed = fragment("offering-1");
        BoundedProjectionRead read = read(List.of(exposed));
        PublicCustomerProjectionAssembly assembly = assembly(read, List.of(exposed));

        List<PublicInteractionBinding> bindings = projector().project(
                model(),
                catalogue(),
                read,
                assembly,
                new PublicInteractionParticipationSourceRegistrySnapshot(
                        RELEASE,
                        Set.of()
                )
        );

        assertTrue(bindings.isEmpty());
    }

    @Test
    void one_owner_established_test_source_projects_one_exact_selected_binding() {
        TestFragment exposed = fragment("offering-1");
        BoundedProjectionRead read = read(List.of(exposed));
        PublicCustomerProjectionAssembly assembly = assembly(read, List.of(exposed));

        PublicInteractionParticipationSource source = request -> Set.of(
                fact(request, exposed.candidateObservation())
        );

        List<PublicInteractionBinding> bindings = projector().project(
                model(),
                catalogue(),
                read,
                assembly,
                registry(source)
        );

        assertEquals(1, bindings.size());
        PublicInteractionBinding binding = bindings.getFirst();
        assertEquals(MERCHANT, binding.merchantScope());
        assertEquals(RELEASE, binding.semanticRegistryReleaseIdentifier());
        assertEquals(MODEL_ID, binding.resolvedModelIdentifier());
        assertEquals(MODEL_VERSION, binding.resolvedModelVersion());
        assertEquals(APPOINTMENT, binding.contributionIdentity());
        assertEquals(APPOINTMENT_OPERATION, binding.operationReference());
        assertEquals(exposed.candidateObservation(), binding.subject());
        assertEquals(TEST_SOURCE, binding.participationSourceIdentity());
        assertEquals(Optional.empty(), binding.participationRoleReference());
    }

    @Test
    void participation_for_unselected_subject_does_not_bind_raw_read_material() {
        TestFragment exposed = fragment("offering-1");
        TestFragment notSelected = fragment("offering-2");
        BoundedProjectionRead read = read(List.of(exposed, notSelected));
        PublicCustomerProjectionAssembly assembly = assembly(read, List.of(exposed));

        PublicInteractionParticipationSource source = request -> Set.of(
                fact(request, notSelected.candidateObservation())
        );

        List<PublicInteractionBinding> bindings = projector().project(
                model(),
                catalogue(),
                read,
                assembly,
                registry(source)
        );

        assertTrue(bindings.isEmpty());
    }

    private static PublicInteractionBindingProjector projector() {
        return new PublicInteractionBindingProjector();
    }

    private static PublicInteractionParticipationSourceRegistrySnapshot registry(
            PublicInteractionParticipationSource source
    ) {
        return new PublicInteractionParticipationSourceRegistrySnapshot(
                RELEASE,
                Set.of(new PublicInteractionParticipationSourceRegistration(
                        TEST_SOURCE,
                        source
                ))
        );
    }

    private static PublicInteractionParticipationFact fact(
            PublicInteractionParticipationRequest request,
            ExposureCandidateObservation subject
    ) {
        return new PublicInteractionParticipationFact(
                request.merchantScope(),
                request.semanticRegistryReleaseIdentifier(),
                request.resolvedModelIdentifier(),
                request.resolvedModelVersion(),
                request.contribution().identity(),
                APPOINTMENT_OPERATION,
                subject,
                TEST_SOURCE,
                Optional.empty()
        );
    }

    private static StaticSurfaceContributionCatalogue catalogue() {
        return new StaticSurfaceContributionCatalogue(List.of(
                new StaticSurfaceContribution(
                        APPOINTMENT,
                        SurfaceAudience.PUBLIC,
                        SurfaceContributionKind.PUBLIC_INTERACTION,
                        Optional.of("public/primary-actions"),
                        Set.of(),
                        Set.of(APPOINTMENT_OPERATION)
                )
        ));
    }

    private static ExecutableMerchantModel model() {
        return new ExecutableMerchantModel(
                MERCHANT.merchantIdentifier(),
                MODEL_ID,
                MODEL_VERSION,
                RELEASE,
                Set.of("appointment"),
                List.of(),
                List.of(),
                List.of()
        );
    }

    private static BoundedProjectionRead read(
            List<? extends ProjectionMaterialFragment> fragments
    ) {
        return new BoundedProjectionRead(
                request(),
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

    private static EstablishedObservationRequest request() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        MERCHANT.merchantIdentifier(),
                        RELEASE
                ),
                MERCHANT,
                RELEASE,
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
