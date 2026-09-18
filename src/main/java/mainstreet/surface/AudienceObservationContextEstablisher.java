package mainstreet.surface;

import grandrue.application.MerchantScope;
import mainstreet.runtime.AuthenticationProvenance;
import mainstreet.runtime.SessionRecord;
import grandrue.runtime.SessionRecordStore;
import mainstreet.runtime.TrustedExecutionContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Trusted factory for the initial closed observation subjects.
 *
 * <p>It validates stable principal/provenance/session binding and exact typed
 * contribution binding. Revocation, expiry, security generation and current
 * owner/merchant authority remain current audience-admission or E4 work.</p>
 */
public final class AudienceObservationContextEstablisher {

    private final SessionRecordStore sessionRecords;
    private final ContextualAccessProofRuntimeBindingSnapshot
            contextualProofBindings;
    private final Optional<ObservationContributionDefinitionRegistrySnapshot>
            contributionDefinitions;
    private final ObservationContributionRuntimeBindingSnapshot
            contributionBindings;

    public AudienceObservationContextEstablisher(
            SessionRecordStore sessionRecords
    ) {
        this(
                sessionRecords,
                new ContextualAccessProofRuntimeBindingSnapshot(List.of())
        );
    }

    public AudienceObservationContextEstablisher(
            SessionRecordStore sessionRecords,
            ContextualAccessProofRuntimeBindingSnapshot contextualProofBindings
    ) {
        this.sessionRecords = Objects.requireNonNull(
                sessionRecords,
                "sessionRecords"
        );
        this.contextualProofBindings = Objects.requireNonNull(
                contextualProofBindings,
                "contextualProofBindings"
        );
        this.contributionDefinitions = Optional.empty();
        this.contributionBindings =
                new ObservationContributionRuntimeBindingSnapshot(List.of());
    }

    public AudienceObservationContextEstablisher(
            SessionRecordStore sessionRecords,
            ContextualAccessProofRuntimeBindingSnapshot contextualProofBindings,
            ObservationContributionDefinitionRegistrySnapshot
                    contributionDefinitions,
            ObservationContributionRuntimeBindingSnapshot contributionBindings
    ) {
        this.sessionRecords = Objects.requireNonNull(
                sessionRecords,
                "sessionRecords"
        );
        this.contextualProofBindings = Objects.requireNonNull(
                contextualProofBindings,
                "contextualProofBindings"
        );
        this.contributionDefinitions = Optional.of(
                Objects.requireNonNull(
                        contributionDefinitions,
                        "contributionDefinitions"
                )
        );
        this.contributionBindings = Objects.requireNonNull(
                contributionBindings,
                "contributionBindings"
        );
    }

    public AudienceObservationContextEstablishmentResult establishPublic(
            EstablishedObservationRequest request
    ) {
        return establishPublic(request, List.of());
    }

    public AudienceObservationContextEstablishmentResult establishPublic(
            EstablishedObservationRequest request,
            Collection<EstablishedObservationContribution> contributions
    ) {
        Objects.requireNonNull(request, "request");
        return established(
                request,
                new PublicObservationSubject(),
                contributions
        );
    }

    public AudienceObservationContextEstablishmentResult
    establishCustomerPrincipal(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext
    ) {
        return establishCustomerPrincipal(
                request,
                executionContext,
                List.of()
        );
    }

    public AudienceObservationContextEstablishmentResult
    establishCustomerPrincipal(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext,
            Collection<EstablishedObservationContribution> contributions
    ) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(executionContext, "executionContext");
        Optional<AudienceObservationContextEstablishmentFailure> failure =
                validateAuthenticatedBinding(request, executionContext);
        if (failure.isPresent()) {
            return rejected(failure.orElseThrow());
        }
        return established(
                request,
                new CustomerPrincipalObservationSubject(executionContext),
                contributions
        );
    }

    public AudienceObservationContextEstablishmentResult
    establishCustomerContextual(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext,
            EstablishedContextualAccessProof contextualAccessProof
    ) {
        return establishCustomerContextual(
                request,
                executionContext,
                contextualAccessProof,
                List.of()
        );
    }

    public AudienceObservationContextEstablishmentResult
    establishCustomerContextual(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext,
            EstablishedContextualAccessProof contextualAccessProof,
            Collection<EstablishedObservationContribution> contributions
    ) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(executionContext, "executionContext");
        Objects.requireNonNull(
                contextualAccessProof,
                "contextualAccessProof"
        );
        Optional<AudienceObservationContextEstablishmentFailure> failure =
                validateContextualBinding(
                        request,
                        executionContext,
                        contextualAccessProof
                );
        if (failure.isPresent()) {
            return rejected(failure.orElseThrow());
        }
        return established(
                request,
                new CustomerContextualObservationSubject(
                        executionContext,
                        contextualAccessProof
                ),
                contributions
        );
    }

    public AudienceObservationContextEstablishmentResult
    establishMerchantInteractive(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext
    ) {
        return establishMerchantInteractive(
                request,
                executionContext,
                List.of()
        );
    }

    public AudienceObservationContextEstablishmentResult
    establishMerchantInteractive(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext,
            Collection<EstablishedObservationContribution> contributions
    ) {
        Objects.requireNonNull(request, "request");
        Objects.requireNonNull(executionContext, "executionContext");
        Optional<AudienceObservationContextEstablishmentFailure> failure =
                validateAuthenticatedBinding(request, executionContext);
        if (failure.isPresent()) {
            return rejected(failure.orElseThrow());
        }
        return established(
                request,
                new MerchantInteractiveObservationSubject(executionContext),
                contributions
        );
    }

    private Optional<AudienceObservationContextEstablishmentFailure>
    validateContextualBinding(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext,
            EstablishedContextualAccessProof contextualAccessProof
    ) {
        MerchantScope requestScope =
                EstablishedObservationRequestDetails.merchantScope(request);
        if (!requestScope.equals(executionContext.merchantScope())) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .SUBJECT_SCOPE_MISMATCH
            );
        }
        if (contextualAccessProof.requestBinding()
                != EstablishedObservationRequestDetails.requestBinding(
                        request
                )) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTEXTUAL_PROOF_REQUEST_MISMATCH
            );
        }
        if (!requestScope.equals(contextualAccessProof.merchantScope())) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTEXTUAL_PROOF_SCOPE_MISMATCH
            );
        }

        String principalIdentifier =
                executionContext.principal().identifier();
        if (!principalIdentifier.equals(
                contextualAccessProof.principalIdentifier()
        )) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTEXTUAL_PROOF_PRINCIPAL_MISMATCH
            );
        }
        if (contextualAccessProof.accessBinding() == null
                || contextualAccessProof.accessBinding().isBlank()) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTEXTUAL_PROOF_ACCESS_BINDING_INVALID
            );
        }

        Optional<AuthenticationProvenance> authentication =
                executionContext.authentication();
        if (authentication.isPresent()) {
            if (!principalIdentifier.equals(
                    authentication.orElseThrow().identityIdentifier()
            )) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTEXTUAL_PROOF_PRINCIPAL_MISMATCH
                );
            }
            Optional<AudienceObservationContextEstablishmentFailure>
                    authenticationFailure =
                    validateAuthenticatedBinding(
                            request,
                            executionContext
                    );
            if (authenticationFailure.isPresent()) {
                return authenticationFailure;
            }
        }

        ContextualAccessProofKind kind = contextualAccessProof.kind();
        if (kind == null || contextualProofBindings.binding(kind).isEmpty()) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTEXTUAL_PROOF_OWNER_OR_KIND_MISMATCH
            );
        }
        if (!contextualProofBindings.matches(
                kind,
                contextualAccessProof.getClass()
        )) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTEXTUAL_PROOF_CLASS_MISMATCH
            );
        }
        return Optional.empty();
    }

    private Optional<AudienceObservationContextEstablishmentFailure>
    validateAuthenticatedBinding(
            EstablishedObservationRequest request,
            TrustedExecutionContext executionContext
    ) {
        MerchantScope requestScope =
                EstablishedObservationRequestDetails.merchantScope(request);
        if (!requestScope.equals(executionContext.merchantScope())) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .SUBJECT_SCOPE_MISMATCH
            );
        }

        Optional<AuthenticationProvenance> optionalAuthentication =
                executionContext.authentication();
        if (optionalAuthentication.isEmpty()) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .AUTHENTICATION_REQUIRED
            );
        }

        AuthenticationProvenance authentication =
                optionalAuthentication.orElseThrow();
        if (!authentication.identityIdentifier().equals(
                executionContext.principal().identifier()
        )) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .AUTHENTICATION_PRINCIPAL_MISMATCH
            );
        }

        Optional<SessionRecord> resolvedSession;
        try {
            resolvedSession = sessionRecords.sessionByIdentity(
                    authentication.sessionIdentifier()
            );
        } catch (RuntimeException failure) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .AUTHENTICATION_SESSION_UNRESOLVED
            );
        }
        if (resolvedSession == null || resolvedSession.isEmpty()) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .AUTHENTICATION_SESSION_UNRESOLVED
            );
        }

        SessionRecord session = resolvedSession.orElseThrow();
        if (!session.sessionIdentity().equals(
                authentication.sessionIdentifier()
        ) || !session.identityReference().equals(
                authentication.identityIdentifier()
        )) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .AUTHENTICATION_SESSION_IDENTITY_MISMATCH
            );
        }
        if (!session.establishedAt().equals(
                authentication.authenticatedAt()
        )) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .AUTHENTICATION_SESSION_ESTABLISHMENT_MISMATCH
            );
        }
        return Optional.empty();
    }

    private Optional<AudienceObservationContextEstablishmentFailure>
    validateContributions(
            EstablishedObservationRequest request,
            SurfaceAudience audience,
            Collection<EstablishedObservationContribution> contributions
    ) {
        Objects.requireNonNull(contributions, "contributions");
        if (contributions.isEmpty()) {
            return Optional.empty();
        }
        if (contributionDefinitions.isEmpty()) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTRIBUTION_DEFINITION_MISMATCH
            );
        }

        ObservationContributionDefinitionRegistrySnapshot definitions =
                contributionDefinitions.orElseThrow();
        if (!definitions.semanticRegistryReleaseIdentifier().equals(
                EstablishedObservationRequestDetails
                        .semanticRegistryReleaseIdentifier(request)
        )) {
            return Optional.of(
                    AudienceObservationContextEstablishmentFailure
                            .CONTRIBUTION_DEFINITION_MISMATCH
            );
        }

        Set<ObservationContributionKind> seenKinds = new HashSet<>();
        for (EstablishedObservationContribution contribution : contributions) {
            if (contribution == null || contribution.kind() == null) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTRIBUTION_DEFINITION_MISMATCH
                );
            }
            ObservationContributionKind kind = contribution.kind();
            if (!seenKinds.add(kind)) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTRIBUTION_DUPLICATE
                );
            }

            Optional<ObservationContributionDefinition> resolved =
                    definitions.definition(kind);
            if (resolved.isEmpty()) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTRIBUTION_DEFINITION_MISMATCH
                );
            }
            ObservationContributionDefinition definition =
                    resolved.orElseThrow();
            if (!contributionBindings.matches(
                    kind,
                    contribution.getClass()
            )) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTRIBUTION_RUNTIME_BINDING_MISMATCH
                );
            }
            if (!definition.permittedAudiences().contains(audience)) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTRIBUTION_AUDIENCE_MISMATCH
                );
            }
            if (contribution.requestBinding()
                    != EstablishedObservationRequestDetails.requestBinding(
                            request
                    )) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTRIBUTION_REQUEST_MISMATCH
                );
            }
            if (!contribution.merchantScope().equals(
                    EstablishedObservationRequestDetails.merchantScope(request)
            )) {
                return Optional.of(
                        AudienceObservationContextEstablishmentFailure
                                .CONTRIBUTION_SCOPE_MISMATCH
                );
            }
        }
        return Optional.empty();
    }

    private AudienceObservationContextEstablishmentResult established(
            EstablishedObservationRequest request,
            ObservationSubject subject,
            Collection<EstablishedObservationContribution> contributions
    ) {
        Objects.requireNonNull(contributions, "contributions");
        Optional<AudienceObservationContextEstablishmentFailure> failure =
                validateContributions(
                        request,
                        ObservationSubjectDetails.audience(subject),
                        contributions
                );
        if (failure.isPresent()) {
            return rejected(failure.orElseThrow());
        }
        return AudienceObservationContextEstablishmentResults.established(
                new DefaultAudienceObservationContext(
                        request,
                        subject,
                        Set.copyOf(contributions)
                )
        );
    }

    private static AudienceObservationContextEstablishmentResult rejected(
            AudienceObservationContextEstablishmentFailure failure
    ) {
        return AudienceObservationContextEstablishmentResults.rejected(failure);
    }
}
