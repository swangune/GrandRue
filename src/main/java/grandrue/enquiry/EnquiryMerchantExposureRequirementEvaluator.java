package grandrue.enquiry;

import grandrue.runtime.ActorAuthorisationAuthority;
import grandrue.runtime.ExecutionPrincipal;
import grandrue.semantic.Privilege;
import mainstreet.surface.ExposableElementReference;
import mainstreet.surface.ExposureCandidateEvaluationSubmission;
import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.ExposureRequirementBatchEvaluation;
import mainstreet.surface.ExposureRequirementCandidateEvaluation;
import mainstreet.surface.ExposureRequirementEvaluationDecision;
import mainstreet.surface.ExposureRequirementEvaluator;
import mainstreet.surface.ExposureRequirementReference;
import mainstreet.surface.OwnerExposureEvaluationContext;
import mainstreet.surface.SurfaceAudience;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Enquiry-owned requirement for an already admitted MERCHANT observation. The generic admission
 * boundary establishes current authentication and merchant association; this evaluator separately
 * checks current scoped Actor Authorisation for each exact family and current material candidacy.
 * Required privileges must be explicitly supplied for all four families by owner composition.
 * There is no universal built-in read privilege, role grant, or allow-on-missing fallback.
 */
public final class EnquiryMerchantExposureRequirementEvaluator implements ExposureRequirementEvaluator {
    private final EnquiryMerchantExposureCandidateSource candidates;
    private final ActorAuthorisationAuthority actors;
    private final Map<ExposableElementReference, Privilege> privileges;

    public EnquiryMerchantExposureRequirementEvaluator(EnquirySubmissionStore submissions,
            ActorAuthorisationAuthority actors, Map<ExposableElementReference, Privilege> privileges) {
        this.candidates = new EnquiryMerchantExposureCandidateSource(submissions);
        this.actors = Objects.requireNonNull(actors, "actors");
        this.privileges = Map.copyOf(Objects.requireNonNull(privileges, "privileges"));
        if (!this.privileges.keySet().equals(EnquiryMerchantExposureReferences.elements())) {
            throw new IllegalArgumentException("Every exact MERCHANT Enquiry family requires an explicit privilege");
        }
    }

    @Override
    public ExposureRequirementBatchEvaluation evaluateBatch(ExposureRequirementReference reference,
            OwnerExposureEvaluationContext context, List<ExposureCandidateEvaluationSubmission> submissions) {
        Objects.requireNonNull(context, "context");
        if (!EnquiryMerchantExposureReferences.OBSERVATION_REQUIREMENT.equals(reference)
                || context.audience() != SurfaceAudience.MERCHANT) {
            throw new IllegalArgumentException("Enquiry observation requirement requires its exact MERCHANT context");
        }
        List<ExposureCandidateEvaluationSubmission> submitted = List.copyOf(submissions);
        // One owner read per Enquiry in this invocation, never a cross-request authorization cache.
        Map<String, List<ExposureCandidateObservation>> current = new HashMap<>();
        return new ExposureRequirementBatchEvaluation(reference, submitted.stream().map(submission ->
                new ExposureRequirementCandidateEvaluation(submission.evaluationBinding(),
                        evaluate(context, submission.candidate(), current))).toList());
    }

    private ExposureRequirementEvaluationDecision evaluate(OwnerExposureEvaluationContext context,
            ExposureCandidateObservation candidate, Map<String, List<ExposureCandidateObservation>> current) {
        String identity = EnquiryMerchantExposureReferences.requireIdentity(candidate);
        var principal = context.subjectBinding().principalIdentifier();
        if (principal.isEmpty()) return ExposureRequirementEvaluationDecision.UNRESOLVED;
        try {
            if (!actors.isAuthorised(context.merchantScope(), new ExecutionPrincipal(principal.orElseThrow()),
                    privileges.get(candidate.elementReference()))) {
                return ExposureRequirementEvaluationDecision.UNSATISFIED;
            }
            return current.computeIfAbsent(identity, id -> candidates.currentCandidates(context.merchantScope(), id))
                    .contains(candidate) ? ExposureRequirementEvaluationDecision.SATISFIED
                    : ExposureRequirementEvaluationDecision.UNSATISFIED;
        } catch (RuntimeException unavailable) {
            return ExposureRequirementEvaluationDecision.UNRESOLVED;
        }
    }
}
