package grandrue.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiSurfaceClass;
import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PublicCustomerProjectionAssemblyServiceTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT =
            Instant.parse("2026-09-03T12:00:00Z");
    private static final ProjectionContractIdentity PROJECTION_CONTRACT =
            new ProjectionContractIdentity("platform", "merchant-presence");
    private static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("platform", "public-merchant-presence");
    private static final ApiContractIdentity API_CONTRACT =
            new ApiContractIdentity("profile", "merchant-presence-query");
    private static final ApiSurfaceClass SURFACE = ApiSurfaceClass.PUBLIC;

    private static final ProjectionSourceDependencyReference PROFILE_SOURCE =
            new ProjectionSourceDependencyReference(
                    "profile",
                    "merchant-public-descriptor"
            );
    private static final ProjectionSourceDependencyReference HOURS_SOURCE =
            new ProjectionSourceDependencyReference(
                    "business-hours",
                    "merchant-public-hours"
            );
    private static final ExposableElementReference DISPLAY_NAME =
            new ExposableElementReference("profile", "public-display-name");
    private static final ExposableElementReference OPENING_HOURS =
            new ExposableElementReference("business-hours", "opening-hours");
    private static final ExposableElementReference TAGLINE =
            new ExposableElementReference("profile", "public-tagline");

    @Test
    void assembles_only_br6_selected_material_and_preserves_exact_read_order_and_affinity() {
        EstablishedObservationRequest request = request();
        TestFragment name = fragment(
                "Bella",
                DISPLAY_NAME,
                PROFILE_SOURCE,
                "profile-r8"
        );
        TestFragment hours = fragment(
                "09:00-17:00",
                OPENING_HOURS,
                HOURS_SOURCE,
                "hours-r4"
        );
        TestFragment withheldTagline = fragment(
                "Local care",
                TAGLINE,
                PROFILE_SOURCE,
                "profile-r8"
        );
        BoundedProjectionRead read = read(
                request,
                List.of(name, hours, withheldTagline),
                Set.of(
                        evidence(PROFILE_SOURCE, "profile-e8", "profile-r8"),
                        evidence(HOURS_SOURCE, "hours-e4", "hours-r4")
                )
        );

        PublicCustomerProjectionAssembly assembly = service().assemble(
                read,
                fullyServiceable(read),
                exposure(
                        request,
                        List.of(
                                exposed(OPENING_HOURS, "public-opening-hours"),
                                exposed(DISPLAY_NAME, "public-display-name")
                        )
                )
        );

        assertSame(read.binding(), assembly.boundedProjectionReadBinding());
        assertEquals(List.of(name, hours), assembly.selectedFragments());
        assertThrows(
                UnsupportedOperationException.class,
                () -> assembly.selectedFragments().add(withheldTagline)
        );
    }

    @Test
    void structural_affinity_failure_invalidates_whole_assembly_attempt() {
        EstablishedObservationRequest request = request();
        EstablishedObservationRequest otherRequest = request();
        TestFragment name = fragment(
                "Bella",
                DISPLAY_NAME,
                PROFILE_SOURCE,
                "profile-r8"
        );
        BoundedProjectionRead read = read(
                request,
                List.of(name),
                Set.of(evidence(
                        PROFILE_SOURCE,
                        "profile-e8",
                        "profile-r8"
                ))
        );

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> service().assemble(
                        read,
                        fullyServiceable(read),
                        exposure(
                                otherRequest,
                                List.of(exposed(
                                        DISPLAY_NAME,
                                        "public-display-name"
                                ))
                        )
                )
        );
    }

    @Test
    void p2_omitted_positive_material_cannot_enter_final_assembly() {
        EstablishedObservationRequest request = request();
        TestFragment name = fragment(
                "Bella",
                DISPLAY_NAME,
                PROFILE_SOURCE,
                "profile-r8"
        );
        TestFragment hours = fragment(
                "09:00-17:00",
                OPENING_HOURS,
                HOURS_SOURCE,
                "hours-r4"
        );
        BoundedProjectionRead read = read(
                request,
                List.of(name, hours),
                Set.of(
                        evidence(PROFILE_SOURCE, "profile-e8", "profile-r8"),
                        evidence(HOURS_SOURCE, "hours-e4", "hours-r4")
                )
        );
        ProjectionServiceabilityResult reduced = new ProjectionServiceabilityResult(
                RELEASE,
                PROJECTION_CONTRACT,
                READ_USE,
                EVALUATED_AT,
                ProjectionServiceabilityOutcome.REDUCED_SERVICEABLE,
                Set.of(ProjectionServiceabilityReasonCode
                        .REDUCED_TRUTHFUL_REPRESENTATION),
                Set.of(),
                read.sourceEvidence(),
                Set.of(HOURS_SOURCE),
                Set.of(),
                Optional.of(read.binding())
        );

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> service().assemble(
                        read,
                        reduced,
                        exposure(
                                request,
                                List.of(
                                        exposed(
                                                DISPLAY_NAME,
                                                "public-display-name"
                                        ),
                                        exposed(
                                                OPENING_HOURS,
                                                "public-opening-hours"
                                        )
                                )
                        )
                )
        );
    }

    @Test
    void assembly_service_has_no_repository_provider_or_capability_execution_dependency() {
        for (Field field : PublicCustomerProjectionAssemblyService.class
                .getDeclaredFields()) {
            String dependency = field.getType().getName().toLowerCase();
            assertTrue(
                    !dependency.contains("repository")
                            && !dependency.contains("provider")
                            && !dependency.contains("capability")
                            && !dependency.contains("interaction"),
                    () -> "Forbidden S2 dependency: " + field.getType().getName()
            );
        }
    }

    private static PublicCustomerProjectionAssemblyService service() {
        return new PublicCustomerProjectionAssemblyService();
    }

    private static ProjectionServiceabilityResult fullyServiceable(
            BoundedProjectionRead read
    ) {
        return new ProjectionServiceabilityResult(
                RELEASE,
                PROJECTION_CONTRACT,
                READ_USE,
                EVALUATED_AT,
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                Set.of(),
                Set.of(),
                read.sourceEvidence(),
                Set.of(),
                Set.of(),
                Optional.of(read.binding())
        );
    }

    private static BoundedProjectionRead read(
            EstablishedObservationRequest request,
            List<ProjectionMaterialFragment> fragments,
            Set<ProjectionSourceEvidence> evidence
    ) {
        return new BoundedProjectionRead(
                request,
                PROJECTION_CONTRACT,
                READ_USE,
                evidence,
                fragments
        );
    }

    private static TestFragment fragment(
            String value,
            ExposableElementReference reference,
            ProjectionSourceDependencyReference source,
            String progress
    ) {
        return new TestFragment(
                value,
                new ExposureCandidateObservation(reference, Optional.empty()),
                Set.of(new ProjectionMaterialSourceAffinity(source, progress))
        );
    }

    private static ProjectionSourceEvidence evidence(
            ProjectionSourceDependencyReference source,
            String evidenceIdentifier,
            String progress
    ) {
        return new ProjectionSourceEvidence(
                source,
                evidenceIdentifier,
                Optional.of(progress),
                Optional.of(progress),
                EVALUATED_AT.minusSeconds(5),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.CLEAR
        );
    }

    private static ApiExposureResolution exposure(
            EstablishedObservationRequest request,
            List<ResolvedExposedElement> exposed
    ) {
        ApiAudienceObservationContext apiContext =
                new DefaultApiAudienceObservationContext(
                        API_CONTRACT,
                        SURFACE,
                        new DefaultAudienceObservationContext(
                                request,
                                new PublicObservationSubject()
                        )
                );
        AudienceObservationAdmissionResult admission =
                AudienceObservationAdmissionResults.admitted(
                        new DefaultAudienceObservationInvocationBinding(
                                EstablishedObservationRequestDetails
                                        .requestBinding(request)
                        ),
                        EVALUATED_AT.minusSeconds(1)
                );
        return new ApiExposureResolutionBinder().bind(
                apiContext,
                admission,
                RELEASE,
                exposed
        ).resolution().orElseThrow();
    }

    private static ResolvedExposedElement exposed(
            ExposableElementReference reference,
            String contractIdentifier
    ) {
        return new ResolvedExposedElement(
                reference,
                Optional.empty(),
                new ExposureElementContract(
                        new ExposureElementContractIdentity(
                                reference.ownerIdentifier(),
                                contractIdentifier
                        ),
                        reference,
                        SurfaceAudience.PUBLIC,
                        ExposureMemberIdentitySpecification.singleton(),
                        ExposureDecision.EXPOSE,
                        Optional.empty(),
                        Set.of()
                )
        );
    }

    private static EstablishedObservationRequest request() {
        return new DefaultEstablishedObservationRequest(
                TestReleases.activeRelease(
                        MERCHANT.merchantIdentifier(),
                        RELEASE
                ),
                MERCHANT,
                RELEASE,
                Optional.of(new ApiObservationRequestProvenance(
                        API_CONTRACT,
                        SURFACE,
                        "public-route"
                ))
        );
    }

    private record TestFragment(
            String value,
            ExposureCandidateObservation candidateObservation,
            Set<ProjectionMaterialSourceAffinity> sourceAffinities
    ) implements ProjectionMaterialFragment {
        private TestFragment {
            sourceAffinities = Set.copyOf(sourceAffinities);
        }
    }
}
