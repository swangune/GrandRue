package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoundedProjectionFragmentSelectorFalsificationTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";
    private static final Instant EVALUATED_AT =
            Instant.parse("2026-09-03T12:30:00Z");
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
    private static final ExposableElementReference DISPLAY_NAME =
            new ExposableElementReference("profile", "public-display-name");

    @Test
    void rejects_not_serviceable_result_even_when_positive_member_corresponds() {
        Fixture fixture = fixture("profile-r8", currentEvidence("profile-r8"));
        ProjectionServiceabilityResult notServiceable = result(
                fixture.read(),
                RELEASE,
                ProjectionServiceabilityOutcome.NOT_SERVICEABLE,
                Set.of(ProjectionServiceabilityReasonCode.SOURCE_UNAVAILABLE)
        );

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> selector().select(
                        fixture.read(),
                        notServiceable,
                        exposure(fixture.request(), RELEASE)
                )
        );
    }

    @Test
    void rejects_forged_p2_release_even_with_exact_bounded_read_binding() {
        Fixture fixture = fixture("profile-r8", currentEvidence("profile-r8"));
        ProjectionServiceabilityResult forged = result(
                fixture.read(),
                "semantic-registry-other",
                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                Set.of()
        );

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> selector().select(
                        fixture.read(),
                        forged,
                        exposure(fixture.request(), RELEASE)
                )
        );
    }

    @Test
    void rejects_fragment_when_material_progress_does_not_match_retained_read_evidence() {
        Fixture fixture = fixture("profile-r7", currentEvidence("profile-r8"));

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> selector().select(
                        fixture.read(),
                        result(
                                fixture.read(),
                                RELEASE,
                                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                                Set.of()
                        ),
                        exposure(fixture.request(), RELEASE)
                )
        );
    }

    @Test
    void rejects_fragment_whose_required_source_is_not_applicable() {
        Fixture fixture = fixture("profile-r8", notApplicableEvidence());

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> selector().select(
                        fixture.read(),
                        result(
                                fixture.read(),
                                RELEASE,
                                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                                Set.of()
                        ),
                        exposure(fixture.request(), RELEASE)
                )
        );
    }

    @Test
    void rejects_forged_e4_release_even_when_request_binding_is_exact() {
        Fixture fixture = fixture("profile-r8", currentEvidence("profile-r8"));

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> selector().select(
                        fixture.read(),
                        result(
                                fixture.read(),
                                RELEASE,
                                ProjectionServiceabilityOutcome.FULLY_SERVICEABLE,
                                Set.of()
                        ),
                        exposure(
                                fixture.request(),
                                "semantic-registry-other"
                        )
                )
        );
    }

    @Test
    void selector_is_stateless_and_accepts_only_governed_read_p2_and_e4_inputs()
            throws ReflectiveOperationException {
        assertEquals(
                0,
                BoundedProjectionFragmentSelector.class
                        .getDeclaredFields().length
        );
        var select = BoundedProjectionFragmentSelector.class.getDeclaredMethod(
                "select",
                BoundedProjectionRead.class,
                ProjectionServiceabilityResult.class,
                ApiExposureResolution.class
        );
        assertArrayEquals(
                new Class<?>[]{
                        BoundedProjectionRead.class,
                        ProjectionServiceabilityResult.class,
                        ApiExposureResolution.class
                },
                select.getParameterTypes()
        );
        assertEquals(List.class, select.getReturnType());
    }

    private static Fixture fixture(
            String fragmentProgress,
            ProjectionSourceEvidence evidence
    ) {
        EstablishedObservationRequest request = request();
        ProjectionMaterialFragment fragment = new TestFragment(
                "Bella",
                new ExposureCandidateObservation(
                        DISPLAY_NAME,
                        Optional.empty()
                ),
                Set.of(new ProjectionMaterialSourceAffinity(
                        PROFILE_SOURCE,
                        fragmentProgress
                ))
        );
        BoundedProjectionRead read = new BoundedProjectionRead(
                request,
                PROJECTION_CONTRACT,
                READ_USE,
                Set.of(evidence),
                List.of(fragment)
        );
        return new Fixture(request, read);
    }

    private static ProjectionServiceabilityResult result(
            BoundedProjectionRead read,
            String release,
            ProjectionServiceabilityOutcome outcome,
            Set<ProjectionServiceabilityReasonCode> reasons
    ) {
        return new ProjectionServiceabilityResult(
                release,
                PROJECTION_CONTRACT,
                READ_USE,
                EVALUATED_AT,
                outcome,
                reasons,
                Set.of(),
                read.sourceEvidence(),
                Set.of(),
                Set.of(),
                Optional.of(read.binding())
        );
    }

    private static ProjectionSourceEvidence currentEvidence(String progress) {
        return new ProjectionSourceEvidence(
                PROFILE_SOURCE,
                "profile-evidence-current",
                Optional.of(progress),
                Optional.of(progress),
                EVALUATED_AT.minusSeconds(5),
                ProjectionSourceAvailability.AVAILABLE,
                ProjectionSourceCompleteness.COMPLETE,
                ProjectionSourceRevocationState.CLEAR
        );
    }

    private static ProjectionSourceEvidence notApplicableEvidence() {
        return new ProjectionSourceEvidence(
                PROFILE_SOURCE,
                "profile-evidence-not-applicable",
                Optional.empty(),
                Optional.empty(),
                EVALUATED_AT.minusSeconds(5),
                ProjectionSourceAvailability.NOT_APPLICABLE,
                ProjectionSourceCompleteness.NOT_APPLICABLE,
                ProjectionSourceRevocationState.NOT_APPLICABLE
        );
    }

    private static ApiExposureResolution exposure(
            EstablishedObservationRequest request,
            String release
    ) {
        ResolvedExposedElement member = new ResolvedExposedElement(
                DISPLAY_NAME,
                Optional.empty(),
                new ExposureElementContract(
                        new ExposureElementContractIdentity(
                                "profile",
                                "public-display-name"
                        ),
                        DISPLAY_NAME,
                        SurfaceAudience.PUBLIC,
                        ExposureMemberIdentitySpecification.singleton(),
                        ExposureDecision.EXPOSE,
                        Optional.empty(),
                        Set.of()
                )
        );
        ObservationRequestBinding requestBinding =
                EstablishedObservationRequestDetails.requestBinding(request);
        return new DefaultApiExposureResolution(
                API_CONTRACT,
                SURFACE,
                new DefaultApiExposedElementSet(
                        Map.of(
                                member.membership(),
                                member.contractIdentity()
                        ),
                        requestBinding,
                        new DefaultAudienceObservationInvocationBinding(
                                requestBinding
                        ),
                        release
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

    private static BoundedProjectionFragmentSelector selector() {
        return new BoundedProjectionFragmentSelector();
    }

    private record Fixture(
            EstablishedObservationRequest request,
            BoundedProjectionRead read
    ) {
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
