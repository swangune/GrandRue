package mainstreet.surface;

import grandrue.api.ApiContractIdentity;
import grandrue.api.ApiSurfaceClass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Package-owned shallow binder for already-resolved positive Exposure members.
 * It establishes no upstream authority and performs no E4 evaluation.
 */
final class ApiExposureResolutionBinder {

    ApiExposureResolutionBindingResult bind(
            ApiAudienceObservationContext apiContext,
            AudienceObservationAdmissionResult admission,
            String semanticRegistryReleaseIdentifier,
            List<ResolvedExposedElement> exposedMembers
    ) {
        Objects.requireNonNull(apiContext, "apiContext");
        Objects.requireNonNull(admission, "admission");
        requireIdentifier(
                semanticRegistryReleaseIdentifier,
                "Semantic registry release identifier"
        );
        Objects.requireNonNull(exposedMembers, "exposedMembers");

        if (!admission.admitted()) {
            return ApiExposureResolutionBindingResult.failed(
                    ApiExposureResolutionBindingFailure.AUDIENCE_NOT_ADMITTED
            );
        }

        AudienceObservationContext internalContext =
                ApiAudienceObservationContextDetails.context(apiContext);
        EstablishedObservationRequest request =
                AudienceObservationContextDetails.request(internalContext);
        ObservationRequestBinding requestBinding =
                EstablishedObservationRequestDetails.requestBinding(request);
        if (!requestBinding.equals(
                AudienceObservationInvocationBindingDetails.requestBinding(
                        admission.invocationBinding()
                )
        )) {
            return ApiExposureResolutionBindingResult.failed(
                    ApiExposureResolutionBindingFailure
                            .AUDIENCE_ADMISSION_REQUEST_MISMATCH
            );
        }

        String establishedRelease =
                EstablishedObservationRequestDetails
                        .semanticRegistryReleaseIdentifier(request);
        if (!establishedRelease.equals(semanticRegistryReleaseIdentifier)) {
            return ApiExposureResolutionBindingResult.failed(
                    ApiExposureResolutionBindingFailure
                            .SEMANTIC_RELEASE_MISMATCH
            );
        }

        ApiContractIdentity establishedContract =
                EstablishedObservationRequestDetails.apiContractIdentity(request)
                        .orElse(null);
        ApiSurfaceClass establishedSurface =
                EstablishedObservationRequestDetails.apiSurface(request)
                        .orElse(null);
        if (!apiContext.contractIdentity().equals(establishedContract)
                || apiContext.surface() != establishedSurface) {
            return ApiExposureResolutionBindingResult.failed(
                    ApiExposureResolutionBindingFailure.API_PROVENANCE_MISMATCH
            );
        }

        Map<ExposedElementMembership, ExposureElementContractIdentity>
                memberContracts = new LinkedHashMap<>();
        for (ResolvedExposedElement member : exposedMembers) {
            Objects.requireNonNull(member, "resolved exposed member");
            if (memberContracts.putIfAbsent(
                    member.membership(),
                    member.contractIdentity()
            ) != null) {
                return ApiExposureResolutionBindingResult.failed(
                        ApiExposureResolutionBindingFailure
                                .DUPLICATE_EXPOSED_ELEMENT
                );
            }
        }

        DefaultApiExposedElementSet exposedElementSet =
                new DefaultApiExposedElementSet(
                        memberContracts,
                        requestBinding,
                        admission.invocationBinding(),
                        semanticRegistryReleaseIdentifier
                );
        return ApiExposureResolutionBindingResult.resolved(
                new DefaultApiExposureResolution(
                        apiContext.contractIdentity(),
                        apiContext.surface(),
                        exposedElementSet
                )
        );
    }

    private static void requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
    }
}
