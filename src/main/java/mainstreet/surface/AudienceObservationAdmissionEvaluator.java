package mainstreet.surface;

import mainstreet.application.MerchantScope;
import mainstreet.merchantaccount.MerchantControllerRelationship;
import mainstreet.merchantaccount.MerchantControllerRelationshipAuthority;
import mainstreet.merchantaccount.MerchantControllerRelationshipLifecycle;
import mainstreet.runtime.AuthenticationProvenance;
import mainstreet.runtime.AuthenticationSessionCurrentness;
import mainstreet.runtime.AuthenticationSessionCurrentnessAuthority;
import mainstreet.runtime.TrustedExecutionContext;
import grandrue.workforce.MerchantMembershipAuthority;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Performs one current, invocation-bound audience-admission evaluation.
 *
 * <p>The result is not stored in the observation context and this evaluator
 * grants no element-level Exposure, privilege, device or business authority.</p>
 */
public final class AudienceObservationAdmissionEvaluator {

    private final AuthenticationSessionCurrentnessAuthority currentness;
    private final MerchantControllerRelationshipAuthority controllers;
    private final MerchantMembershipAuthority memberships;
    private final AudienceObservationPlatformProtectionAuthority protection;
    private final Clock clock;

    public AudienceObservationAdmissionEvaluator(
            AuthenticationSessionCurrentnessAuthority currentness,
            MerchantControllerRelationshipAuthority controllers,
            MerchantMembershipAuthority memberships,
            AudienceObservationPlatformProtectionAuthority protection,
            Clock clock
    ) {
        this.currentness = Objects.requireNonNull(
                currentness,
                "currentness"
        );
        this.controllers = Objects.requireNonNull(
                controllers,
                "controllers"
        );
        this.memberships = Objects.requireNonNull(
                memberships,
                "memberships"
        );
        this.protection = Objects.requireNonNull(
                protection,
                "protection"
        );
        this.clock = Objects.requireNonNull(clock, "clock");
    }

    public AudienceObservationAdmissionResult evaluate(
            AudienceObservationContext context
    ) {
        Objects.requireNonNull(context, "context");
        Instant evaluatedAt = clock.instant();
        EstablishedObservationRequest request =
                AudienceObservationContextDetails.request(context);
        AudienceObservationInvocationBinding binding =
                new DefaultAudienceObservationInvocationBinding(
                        EstablishedObservationRequestDetails.requestBinding(
                                request
                        )
                );

        Optional<AudienceObservationAdmissionFailure> subjectFailure =
                switch (AudienceObservationContextDetails.subject(context)) {
                    case PublicObservationSubject ignored -> Optional.empty();
                    case CustomerPrincipalObservationSubject ignored ->
                            authenticationFailure(context);
                    case CustomerContextualObservationSubject ignored ->
                            Optional.of(
                                    AudienceObservationAdmissionFailure
                                            .CONTEXTUAL_AUTHORITY_UNAVAILABLE_OR_UNRESOLVED
                            );
                    case MerchantInteractiveObservationSubject ignored ->
                            merchantFailure(context);
                };
        if (subjectFailure.isPresent()) {
            return AudienceObservationAdmissionResults.rejected(
                    binding,
                    evaluatedAt,
                    subjectFailure.orElseThrow()
            );
        }

        AudienceObservationPlatformProtection protectionResult;
        try {
            protectionResult = protection.evaluate(
                    context,
                    binding,
                    evaluatedAt
            );
        } catch (RuntimeException failure) {
            protectionResult =
                    AudienceObservationPlatformProtection.UNRESOLVED;
        }

        if (protectionResult == null
                || protectionResult
                == AudienceObservationPlatformProtection.UNRESOLVED) {
            return AudienceObservationAdmissionResults.rejected(
                    binding,
                    evaluatedAt,
                    AudienceObservationAdmissionFailure
                            .PLATFORM_PROTECTION_UNRESOLVED
            );
        }
        if (protectionResult
                == AudienceObservationPlatformProtection.UNSATISFIED) {
            return AudienceObservationAdmissionResults.rejected(
                    binding,
                    evaluatedAt,
                    AudienceObservationAdmissionFailure
                            .PLATFORM_PROTECTION_UNSATISFIED
            );
        }

        return AudienceObservationAdmissionResults.admitted(
                binding,
                evaluatedAt
        );
    }

    private Optional<AudienceObservationAdmissionFailure>
    authenticationFailure(AudienceObservationContext context) {
        Optional<AuthenticationProvenance> provenance =
                AudienceObservationContextDetails.executionContext(context)
                        .flatMap(TrustedExecutionContext::authentication);
        if (provenance.isEmpty()) {
            return Optional.of(
                    AudienceObservationAdmissionFailure
                            .AUTHENTICATION_CURRENTNESS_UNRESOLVED
            );
        }

        AuthenticationSessionCurrentness result;
        try {
            result = currentness.evaluate(provenance.orElseThrow());
        } catch (RuntimeException failure) {
            result = AuthenticationSessionCurrentness.UNRESOLVED;
        }
        if (result == AuthenticationSessionCurrentness.CURRENT) {
            return Optional.empty();
        }
        if (result == AuthenticationSessionCurrentness.NOT_CURRENT) {
            return Optional.of(
                    AudienceObservationAdmissionFailure
                            .AUTHENTICATION_NOT_CURRENT
            );
        }
        return Optional.of(
                AudienceObservationAdmissionFailure
                        .AUTHENTICATION_CURRENTNESS_UNRESOLVED
        );
    }

    private Optional<AudienceObservationAdmissionFailure> merchantFailure(
            AudienceObservationContext context
    ) {
        Optional<AudienceObservationAdmissionFailure> authentication =
                authenticationFailure(context);
        if (authentication.isPresent()) {
            return authentication;
        }

        TrustedExecutionContext execution =
                AudienceObservationContextDetails.executionContext(context)
                        .orElseThrow();
        MerchantScope merchantScope =
                EstablishedObservationRequestDetails.merchantScope(
                        AudienceObservationContextDetails.request(context)
                );
        String identity = execution.principal().identifier();

        try {
            Optional<MerchantControllerRelationship> controller =
                    controllers.activeController(merchantScope);
            if (controller == null) {
                return Optional.of(
                        AudienceObservationAdmissionFailure
                                .MERCHANT_ASSOCIATION_UNRESOLVED
                );
            }
            if (controller.isPresent()) {
                MerchantControllerRelationship relationship =
                        controller.orElseThrow();
                if (!relationship.merchantScope().equals(merchantScope)
                        || relationship.lifecycle()
                        != MerchantControllerRelationshipLifecycle.ACTIVE) {
                    return Optional.of(
                            AudienceObservationAdmissionFailure
                                    .MERCHANT_ASSOCIATION_UNRESOLVED
                    );
                }
                if (relationship.identityIdentifier().equals(identity)) {
                    return Optional.empty();
                }
            }

            if (memberships.isActive(merchantScope, identity)) {
                return Optional.empty();
            }
            return Optional.of(
                    AudienceObservationAdmissionFailure
                            .MERCHANT_ASSOCIATION_UNSATISFIED
            );
        } catch (RuntimeException failure) {
            return Optional.of(
                    AudienceObservationAdmissionFailure
                            .MERCHANT_ASSOCIATION_UNRESOLVED
            );
        }
    }
}
