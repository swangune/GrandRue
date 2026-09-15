package mainstreet.surface;

import mainstreet.api.ApiContractIdentity;
import mainstreet.api.ApiSurfaceClass;
import mainstreet.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApiExposureResolutionBoundaryTest {

    private static final ApiContractIdentity CONTRACT =
            new ApiContractIdentity("profile", "merchant-presence-query");
    private static final ApiSurfaceClass SURFACE = ApiSurfaceClass.PUBLIC;
    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final String RELEASE = "semantic-registry-1.0";

    @Test
    void successful_resolution_contains_only_immutable_owner_qualified_membership() {
        ResolutionFixture fixture = fixture();
        ExposableElementReference profileName =
                new ExposableElementReference("profile", "display-name");
        ExposableElementReference hoursNameCollision =
                new ExposableElementReference("business-hours", "display-name");

        ApiExposureResolutionBindingResult result = binder().bind(
                fixture.apiContext(),
                fixture.admission(),
                RELEASE,
                List.of(
                        exposed(profileName, "public-display-name"),
                        exposed(hoursNameCollision, "public-hours-display-name")
                )
        );

        ApiExposureResolution resolution = result.resolution().orElseThrow();
        assertTrue(result.failure().isEmpty());
        assertEquals(CONTRACT, resolution.contractIdentity());
        assertEquals(SURFACE, resolution.surface());
        assertEquals(
                Set.of(
                        new ExposedElementMembership(profileName, Optional.empty()),
                        new ExposedElementMembership(
                                hoursNameCollision,
                                Optional.empty()
                        )
                ),
                resolution.exposedElements().members()
        );
        assertThrows(
                UnsupportedOperationException.class,
                () -> resolution.exposedElements().members().add(
                        new ExposedElementMembership(
                                new ExposableElementReference("profile", "tagline"),
                                Optional.empty()
                        )
                )
        );
    }

    @Test
    void successful_empty_resolution_is_distinct_from_structural_failure() {
        ResolutionFixture fixture = fixture();

        ApiExposureResolutionBindingResult empty = binder().bind(
                fixture.apiContext(),
                fixture.admission(),
                RELEASE,
                List.of()
        );
        assertTrue(empty.resolution().isPresent());
        assertTrue(empty.resolution().orElseThrow()
                .exposedElements().members().isEmpty());
        assertTrue(empty.failure().isEmpty());

        ApiExposureResolutionBindingResult failed = binder().bind(
                fixture.apiContext(),
                fixture.admission(),
                "semantic-registry-other",
                List.of()
        );
        assertTrue(failed.resolution().isEmpty());
        assertEquals(
                Optional.of(ApiExposureResolutionBindingFailure
                        .SEMANTIC_RELEASE_MISMATCH),
                failed.failure()
        );
    }

    @Test
    void duplicate_exact_member_identity_fails_closed_instead_of_silent_deduplication() {
        ResolutionFixture fixture = fixture();
        ExposableElementReference reference =
                new ExposableElementReference("profile", "display-name");

        ApiExposureResolutionBindingResult result = binder().bind(
                fixture.apiContext(),
                fixture.admission(),
                RELEASE,
                List.of(
                        exposed(reference, "public-display-name"),
                        exposed(reference, "alternate-contract")
                )
        );

        assertTrue(result.resolution().isEmpty());
        assertEquals(
                Optional.of(ApiExposureResolutionBindingFailure
                        .DUPLICATE_EXPOSED_ELEMENT),
                result.failure()
        );
    }

    @Test
    void rejected_audience_admission_cannot_become_empty_or_partial_success() {
        ResolutionFixture fixture = fixture();
        AudienceObservationAdmissionResult rejected =
                AudienceObservationAdmissionResults.rejected(
                        fixture.admission().invocationBinding(),
                        Instant.parse("2026-09-02T00:00:00Z"),
                        AudienceObservationAdmissionFailure
                                .PLATFORM_PROTECTION_UNSATISFIED
                );

        ApiExposureResolutionBindingResult result = binder().bind(
                fixture.apiContext(),
                rejected,
                RELEASE,
                List.of(exposed(
                        new ExposableElementReference(
                                "profile",
                                "display-name"
                        ),
                        "public-display-name"
                ))
        );

        assertTrue(result.resolution().isEmpty());
        assertEquals(
                Optional.of(ApiExposureResolutionBindingFailure
                        .AUDIENCE_NOT_ADMITTED),
                result.failure()
        );
    }

    @Test
    void admission_from_a_different_established_request_fails_closed() {
        ResolutionFixture target = fixture();
        ResolutionFixture otherRequest = fixture();

        ApiExposureResolutionBindingResult result = binder().bind(
                target.apiContext(),
                otherRequest.admission(),
                RELEASE,
                List.of(exposed(
                        new ExposableElementReference(
                                "profile",
                                "display-name"
                        ),
                        "public-display-name"
                ))
        );

        assertTrue(result.resolution().isEmpty());
        assertEquals(
                Optional.of(ApiExposureResolutionBindingFailure
                        .AUDIENCE_ADMISSION_REQUEST_MISMATCH),
                result.failure()
        );
    }

    @Test
    void api_result_contract_exposes_no_context_admission_failure_or_internal_unwrap() {
        assertEquals(
                Set.of("contractIdentity", "surface", "exposedElements"),
                Arrays.stream(ApiExposureResolution.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ApiExposureResolution.class)
                        .map(Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertEquals(
                Set.of("members"),
                Arrays.stream(ApiExposedElementSet.class.getMethods())
                        .filter(method -> method.getDeclaringClass()
                                == ApiExposedElementSet.class)
                        .map(Method::getName)
                        .collect(java.util.stream.Collectors.toUnmodifiableSet())
        );
        assertFalse(Serializable.class.isAssignableFrom(
                ApiExposureResolution.class
        ));
        assertFalse(Serializable.class.isAssignableFrom(
                ApiExposedElementSet.class
        ));
        assertTrue(Arrays.stream(ApiExposureResolution.class
                        .getPermittedSubclasses())
                .flatMap(type -> Arrays.stream(type.getDeclaredConstructors()))
                .noneMatch(constructor -> Modifier.isPublic(
                        constructor.getModifiers()
                )));
        assertTrue(Arrays.stream(ApiExposedElementSet.class
                        .getPermittedSubclasses())
                .flatMap(type -> Arrays.stream(type.getDeclaredConstructors()))
                .noneMatch(constructor -> Modifier.isPublic(
                        constructor.getModifiers()
                )));
    }

    @Test
    void private_result_provenance_retains_request_invocation_release_and_contract_identity() {
        ResolutionFixture fixture = fixture();
        ExposableElementReference reference =
                new ExposableElementReference("profile", "display-name");
        ExposureElementContract exposureContract = exposureContract(
                reference,
                "public-display-name"
        );
        ExposedElementMembership membership =
                new ExposedElementMembership(reference, Optional.empty());

        ApiExposureResolution resolution = binder().bind(
                fixture.apiContext(),
                fixture.admission(),
                RELEASE,
                List.of(new ResolvedExposedElement(
                        reference,
                        Optional.empty(),
                        exposureContract
                ))
        ).resolution().orElseThrow();

        assertEquals(
                EstablishedObservationRequestDetails.requestBinding(
                        fixture.request()
                ),
                ApiExposureResolutionDetails.requestBinding(resolution)
        );
        assertEquals(
                fixture.admission().invocationBinding(),
                ApiExposureResolutionDetails.invocationBinding(resolution)
        );
        assertEquals(
                RELEASE,
                ApiExposureResolutionDetails
                        .semanticRegistryReleaseIdentifier(resolution)
        );
        assertEquals(
                exposureContract.identity(),
                ApiExposureResolutionDetails.memberContractIdentity(
                        resolution,
                        membership
                ).orElseThrow()
        );
    }

    private static ApiExposureResolutionBinder binder() {
        return new ApiExposureResolutionBinder();
    }

    private static ResolvedExposedElement exposed(
            ExposableElementReference reference,
            String contractIdentifier
    ) {
        return new ResolvedExposedElement(
                reference,
                Optional.empty(),
                exposureContract(reference, contractIdentifier)
        );
    }

    private static ExposureElementContract exposureContract(
            ExposableElementReference reference,
            String contractIdentifier
    ) {
        return new ExposureElementContract(
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
        );
    }

    private static ResolutionFixture fixture() {
        EstablishedObservationRequest request =
                new DefaultEstablishedObservationRequest(
                        TestReleases.activeRelease(
                                MERCHANT.merchantIdentifier(),
                                RELEASE
                        ),
                        MERCHANT,
                        RELEASE,
                        Optional.of(new ApiObservationRequestProvenance(
                                CONTRACT,
                                SURFACE,
                                "public-route"
                        ))
                );
        AudienceObservationContext internal =
                new DefaultAudienceObservationContext(
                        request,
                        new PublicObservationSubject()
                );
        ApiAudienceObservationContext apiContext =
                new DefaultApiAudienceObservationContext(
                        CONTRACT,
                        SURFACE,
                        internal
                );
        AudienceObservationAdmissionResult admission =
                AudienceObservationAdmissionResults.admitted(
                        new DefaultAudienceObservationInvocationBinding(
                                EstablishedObservationRequestDetails
                                        .requestBinding(request)
                        ),
                        Instant.parse("2026-09-02T00:00:00Z")
                );
        return new ResolutionFixture(
                request,
                apiContext,
                admission
        );
    }

    private record ResolutionFixture(
            EstablishedObservationRequest request,
            ApiAudienceObservationContext apiContext,
            AudienceObservationAdmissionResult admission
    ) {
    }
}
