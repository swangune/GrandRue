package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BoundedProjectionFragmentSelectorTest {

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
    void selects_only_e4_positive_p2_eligible_fragments_from_same_read_in_read_order() {
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
        TestFragment tagline = fragment(
                "Local care",
                TAGLINE,
                PROFILE_SOURCE,
                "profile-r8"
        );
        BoundedProjectionRead read = read(
                request,
                List.of(name, hours, tagline),
                Set.of(
                        evidence(PROFILE_SOURCE, "profile-e8", "profile-r8"),
                        evidence(HOURS_SOURCE, "hours-e4", "hours-r4")
                )
        );

        ApiExposureResolution exposure = exposure(
                request,
                List.of(
                        exposed(OPENING_HOURS, "public-opening-hours"),
                        exposed(DISPLAY_NAME, "public-display-name")
                )
        );

        List<ProjectionMaterialFragment> selected = selector().select(
                read,
                fullyServiceable(read),
                exposure
        );

        assertEquals(List.of(name, hours), selected);
        assertThrows(
                UnsupportedOperationException.class,
                () -> selected.add(tagline)
        );
    }

    @Test
    void fails_structurally_when_p2_result_belongs_to_another_bounded_read() {
        EstablishedObservationRequest request = request();
        TestFragment name = fragment(
                "Bella",
                DISPLAY_NAME,
                PROFILE_SOURCE,
                "profile-r8"
        );
        Set<ProjectionSourceEvidence> evidence = Set.of(
                evidence(PROFILE_SOURCE, "profile-e8", "profile-r8")
        );
        BoundedProjectionRead target = read(request, List.of(name), evidence);
        BoundedProjectionRead other = read(request, List.of(name), evidence);

        assertThrows(
                ProjectionFragmentSelectionStructuralException.class,
                () -> selector().select(
                        target,
                        fullyServiceable(other),
                        exposure(
                                request,
                                List.of(exposed(
                                        DISPLAY_NAME,
                                        "public-display-name"
                                ))
                        )
                )
        );
    }

    @Test
    void fails_structurally_when_e4_resolution_belongs_to_another_request() {
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
                () -> selector().select(
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
    void fails_structurally_when_positive_membership_has_no_fragment_in_same_read() {
        EstablishedObservationRequest request = request();
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
                () -> selector().select(
                        read,
                        fullyServiceable(read),
                        exposure(
                                request,
                                List.of(exposed(TAGLINE, "public-tagline"))
                        )
                )
        );
    }

    @Test
    void fails_whole_attempt_when_any_positive_member_is_p2_omitted() {
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
                () -> selector().select(
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
    void empty_positive_membership_produces_empty_immutable_selection() {
        EstablishedObservationRequest request = request();
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

        List<ProjectionMaterialFragment> selected = selector().select(
                read,
                fullyServiceable(read),
                exposure(request, List.of())
        );

        assertEquals(List.of(), selected);
    }

    private static BoundedProjectionFragmentSelector selector() {
        return new BoundedProjectionFragmentSelector();
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
