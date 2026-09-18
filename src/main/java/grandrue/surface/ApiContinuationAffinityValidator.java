package grandrue.surface;

import grandrue.api.ApiBoundedCollectionContract;
import grandrue.api.ApiSurfaceClass;
import grandrue.application.MerchantScope;

import java.util.Objects;
import java.util.Optional;

/**
 * T4b semantic continuation-affinity validator.
 *
 * <p>The validator compares an untrusted decoded candidate with the current
 * registered bounded-query semantics and the server-established API Audience
 * Observation Context. It performs no token decoding/encoding, cursor handling,
 * repository access, query execution or transport mapping.</p>
 */
public final class ApiContinuationAffinityValidator {

    public Optional<ApiContinuationAffinityValidationFailure> validate(
            ApiBoundedCollectionContract contract,
            ApiAudienceObservationContext currentContext,
            ApiContinuationAffinityCandidate candidate
    ) {
        Objects.requireNonNull(contract, "contract");
        Objects.requireNonNull(currentContext, "currentContext");
        Objects.requireNonNull(candidate, "candidate");

        AudienceObservationContext internalContext =
                ApiAudienceObservationContextDetails.context(currentContext);
        EstablishedObservationRequest request =
                AudienceObservationContextDetails.request(internalContext);

        if (!contract.queryContractIdentity().equals(
                currentContext.contractIdentity()
        ) || EstablishedObservationRequestDetails.apiContractIdentity(request)
                .filter(currentContext.contractIdentity()::equals)
                .isEmpty()
                || EstablishedObservationRequestDetails.apiSurface(request)
                .filter(currentContext.surface()::equals)
                .isEmpty()) {
            return failure(ApiContinuationAffinityValidationFailure
                    .CURRENT_QUERY_CONTEXT_MISMATCH);
        }

        if (!matchesQuerySemantics(contract, candidate)) {
            return failure(ApiContinuationAffinityValidationFailure
                    .QUERY_SEMANTICS_MISMATCH);
        }

        MerchantScope currentScope =
                EstablishedObservationRequestDetails.merchantScope(request);
        if (!currentScope.equals(candidate.merchantScope())) {
            return failure(ApiContinuationAffinityValidationFailure
                    .MERCHANT_SCOPE_MISMATCH);
        }

        SurfaceAudience currentAudience =
                AudienceObservationContextDetails.audience(internalContext);
        if (currentAudience != candidate.audience()
                || !surfacePermits(currentContext.surface(), currentAudience)) {
            return failure(ApiContinuationAffinityValidationFailure
                    .AUDIENCE_MISMATCH);
        }

        if (!EstablishedObservationRequestDetails
                .semanticRegistryReleaseIdentifier(request)
                .equals(candidate.semanticRegistryReleaseIdentifier())) {
            return failure(ApiContinuationAffinityValidationFailure
                    .SEMANTIC_RELEASE_MISMATCH);
        }

        return Optional.empty();
    }

    private static boolean matchesQuerySemantics(
            ApiBoundedCollectionContract contract,
            ApiContinuationAffinityCandidate candidate
    ) {
        return contract.queryContractIdentity().equals(
                candidate.queryContractIdentity()
        )
                && contract.maximumPageSize() == candidate.maximumPageSize()
                && contract.stableOrderingBasisReference().equals(
                        candidate.stableOrderingBasisReference()
                )
                && contract.permittedFilterReferences().equals(
                        candidate.permittedFilterReferences()
                )
                && contract.continuationSemanticsReference().equals(
                        candidate.continuationSemanticsReference()
                )
                && contract.scopeQueryAffinityRuleReference().equals(
                        candidate.scopeQueryAffinityRuleReference()
                );
    }

    private static boolean surfacePermits(
            ApiSurfaceClass surface,
            SurfaceAudience audience
    ) {
        return switch (surface) {
            case PUBLIC -> audience == SurfaceAudience.PUBLIC;
            case CUSTOMER_CONTEXTUAL -> audience == SurfaceAudience.CUSTOMER;
            case MERCHANT_OPERATIONAL -> audience == SurfaceAudience.MERCHANT;
            case PLATFORM_IDENTITY_BOOTSTRAP,
                 PLATFORM_ADMINISTRATIVE,
                 INTEGRATION_INGRESS -> false;
        };
    }

    private static Optional<ApiContinuationAffinityValidationFailure> failure(
            ApiContinuationAffinityValidationFailure failure
    ) {
        return Optional.of(failure);
    }
}
