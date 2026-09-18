package grandrue.publication;

import mainstreet.surface.ExposureCandidateEvaluationSubmission;
import mainstreet.surface.ExposureRequirementBatchEvaluation;
import mainstreet.surface.ExposureRequirementCandidateEvaluation;
import mainstreet.surface.ExposureRequirementEvaluationDecision;
import mainstreet.surface.ExposureRequirementEvaluator;
import mainstreet.surface.ExposureRequirementReference;
import mainstreet.surface.OwnerExposureEvaluationContext;
import mainstreet.surface.SurfaceAudience;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Publication-owned, read-only evaluator for an Opportunity's publishFrom/publishUntil window.
 *
 * <p>When P5 supplies request-bound material affinity, the evaluator also requires the exact
 * published revision observed by the bounded read to remain current. A changed binding fails
 * closed instead of authorising stale material.</p>
 */
public final class OpportunityExposureWindowRequirementEvaluator
        implements ExposureRequirementEvaluator {

    private final OpportunityPublicExposureReadPort readPort;

    public OpportunityExposureWindowRequirementEvaluator(
            OpportunityPublicExposureReadPort readPort
    ) {
        this.readPort = Objects.requireNonNull(readPort, "readPort");
    }

    @Override
    public ExposureRequirementBatchEvaluation evaluateBatch(
            ExposureRequirementReference reference,
            OwnerExposureEvaluationContext context,
            List<ExposureCandidateEvaluationSubmission> submissions
    ) {
        Objects.requireNonNull(reference, "reference");
        Objects.requireNonNull(context, "context");
        List<ExposureCandidateEvaluationSubmission> submitted =
                List.copyOf(Objects.requireNonNull(submissions, "submissions"));

        if (!OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT.equals(
                reference
        )) {
            throw new IllegalArgumentException(
                    "Opportunity window evaluator requires the exact Publication window requirement"
            );
        }
        if (context.audience() != SurfaceAudience.PUBLIC) {
            throw new IllegalArgumentException(
                    "Opportunity public exposure window may be evaluated only for PUBLIC audience"
            );
        }

        return new ExposureRequirementBatchEvaluation(
                reference,
                submitted.stream()
                        .map(submission -> evaluateCandidate(context, submission))
                        .toList()
        );
    }

    private ExposureRequirementCandidateEvaluation evaluateCandidate(
            OwnerExposureEvaluationContext context,
            ExposureCandidateEvaluationSubmission submission
    ) {
        Objects.requireNonNull(submission, "submission");
        String opportunityIdentity =
                OpportunityPublicExposureReferences.requireOpportunityIdentity(
                        submission.candidate()
                );

        ExposureRequirementEvaluationDecision decision = currentEvidence(
                context,
                submission,
                opportunityIdentity
        ).map(evidence -> evaluateEvidence(
                context,
                opportunityIdentity,
                evidence
        )).orElse(ExposureRequirementEvaluationDecision.UNRESOLVED);

        return new ExposureRequirementCandidateEvaluation(
                submission.evaluationBinding(),
                decision
        );
    }

    private Optional<OpportunityPublicExposureEvidence> currentEvidence(
            OwnerExposureEvaluationContext context,
            ExposureCandidateEvaluationSubmission submission,
            String opportunityIdentity
    ) {
        List<OpportunityMaterialAffinityObservationContribution> contributions =
                context.contributions().stream()
                        .filter(OpportunityMaterialAffinityObservationContribution.class::isInstance)
                        .map(OpportunityMaterialAffinityObservationContribution.class::cast)
                        .toList();

        if (contributions.isEmpty()) {
            return readPort.currentPublishedExposure(
                    context.merchantScope(),
                    opportunityIdentity
            );
        }
        if (contributions.size() != 1) {
            return Optional.empty();
        }

        List<OpportunityMaterialAffinityEntry> matches = contributions.getFirst()
                .entries()
                .stream()
                .filter(entry -> submission.candidate().equals(entry.candidate()))
                .filter(entry -> OpportunityPublicRepresentationProjectionReferences
                        .PUBLISHED_MATERIAL_SOURCE.equals(entry.sourceReference()))
                .toList();
        if (matches.size() != 1) {
            return Optional.empty();
        }

        return readPort.currentPublishedExposureAtProgress(
                context.merchantScope(),
                opportunityIdentity,
                matches.getFirst().expectedPublishedRevisionIdentity()
        );
    }

    private static ExposureRequirementEvaluationDecision evaluateEvidence(
            OwnerExposureEvaluationContext context,
            String opportunityIdentity,
            OpportunityPublicExposureEvidence evidence
    ) {
        requireEvidenceAffinity(context, opportunityIdentity, evidence);

        return OpportunityPublicExposureWindow.contains(evidence, context.evaluatedAt())
                ? ExposureRequirementEvaluationDecision.SATISFIED
                : ExposureRequirementEvaluationDecision.UNSATISFIED;
    }

    private static void requireEvidenceAffinity(
            OwnerExposureEvaluationContext context,
            String opportunityIdentity,
            OpportunityPublicExposureEvidence evidence
    ) {
        Objects.requireNonNull(evidence, "evidence");
        if (!context.merchantScope().equals(evidence.merchantScope())
                || !opportunityIdentity.equals(evidence.opportunityIdentity())) {
            throw new IllegalStateException(
                    "Publication exposure evaluator received mismatched Opportunity evidence"
            );
        }
    }

}
