package grandrue.merchantprofile;

import grandrue.merchantprofile.MerchantContactPointExposure;

import grandrue.merchantprofile.MerchantContactPointExposureChoiceReadPort;

import grandrue.surface.ExposableElementReference;
import grandrue.surface.ExposureCandidateEvaluationSubmission;
import grandrue.surface.ExposureCandidateInstanceKindReference;
import grandrue.surface.ExposureCandidateInstanceReference;
import grandrue.surface.ExposureCandidateObservation;
import grandrue.surface.MerchantExposureChoiceBatchEvaluation;
import grandrue.surface.MerchantExposureChoiceCandidateEvaluation;
import grandrue.surface.MerchantExposureChoiceEvaluationDecision;
import grandrue.surface.MerchantExposureChoiceEvaluator;
import grandrue.surface.MerchantExposureChoiceSourceReference;
import grandrue.surface.OwnerExposureEvaluationContext;
import grandrue.surface.ProjectionSourceDependencyReference;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Profile-owned merchant-choice evaluator for public Contact Point observation. */
public final class ProfileContactPointExposureChoiceEvaluator
        implements MerchantExposureChoiceEvaluator {

    public static final MerchantExposureChoiceSourceReference CHOICE_SOURCE_REFERENCE =
            new MerchantExposureChoiceSourceReference(
                    "profile",
                    "contact-point-public-exposure"
            );

    private static final ExposableElementReference ELEMENT_REFERENCE =
            new ExposableElementReference("profile", "public-contact-point");

    private static final ExposureCandidateInstanceKindReference INSTANCE_KIND =
            new ExposureCandidateInstanceKindReference("profile", "contact-point");

    private static final ProjectionSourceDependencyReference MATERIAL_SOURCE =
            new ProjectionSourceDependencyReference("profile", "contact-points");

    private final MerchantContactPointExposureChoiceReadPort readPort;

    public ProfileContactPointExposureChoiceEvaluator(
            MerchantContactPointExposureChoiceReadPort readPort
    ) {
        this.readPort = Objects.requireNonNull(readPort, "readPort");
    }

    @Override
    public MerchantExposureChoiceBatchEvaluation evaluateBatch(
            MerchantExposureChoiceSourceReference choiceSourceReference,
            OwnerExposureEvaluationContext context,
            List<ExposureCandidateEvaluationSubmission> submissions
    ) {
        Objects.requireNonNull(choiceSourceReference, "choiceSourceReference");
        Objects.requireNonNull(context, "context");
        List<ExposureCandidateEvaluationSubmission> submitted =
                List.copyOf(Objects.requireNonNull(submissions, "submissions"));
        if (!CHOICE_SOURCE_REFERENCE.equals(choiceSourceReference)) {
            throw new IllegalArgumentException(
                    "Contact Point evaluator requires exact Profile Contact Point choice source"
            );
        }

        return new MerchantExposureChoiceBatchEvaluation(
                choiceSourceReference,
                submitted.stream()
                        .map(submission -> evaluateCandidate(context, submission))
                        .toList()
        );
    }

    private MerchantExposureChoiceCandidateEvaluation evaluateCandidate(
            OwnerExposureEvaluationContext context,
            ExposureCandidateEvaluationSubmission submission
    ) {
        ExposureCandidateObservation candidate = submission.candidate();
        ExposureCandidateInstanceReference instance = requireInstance(candidate);
        MerchantExposureChoiceEvaluationDecision decision = currentExposure(
                context,
                candidate,
                instance
        ).map(ProfileContactPointExposureChoiceEvaluator::decision)
                .orElse(MerchantExposureChoiceEvaluationDecision.UNRESOLVED);
        return new MerchantExposureChoiceCandidateEvaluation(
                submission.evaluationBinding(),
                decision
        );
    }

    private Optional<MerchantContactPointExposure> currentExposure(
            OwnerExposureEvaluationContext context,
            ExposureCandidateObservation candidate,
            ExposureCandidateInstanceReference instance
    ) {
        List<ProfileMaterialAffinityObservationContribution> contributions =
                context.contributions().stream()
                        .filter(ProfileMaterialAffinityObservationContribution.class::isInstance)
                        .map(ProfileMaterialAffinityObservationContribution.class::cast)
                        .toList();

        if (contributions.isEmpty()) {
            return readPort.currentActiveExposure(
                    context.merchantScope(),
                    instance.instanceIdentifier()
            );
        }

        List<ProfileMaterialAffinityEntry> matchingEntries = contributions.stream()
                .flatMap(contribution -> contribution.entries().stream())
                .filter(entry -> candidate.equals(entry.candidateObservation()))
                .filter(entry -> MATERIAL_SOURCE.equals(entry.sourceReference()))
                .toList();
        if (matchingEntries.size() != 1) {
            return Optional.empty();
        }

        return readPort.currentActiveExposureAtProgress(
                context.merchantScope(),
                instance.instanceIdentifier(),
                matchingEntries.getFirst().expectedProgressIdentifier()
        );
    }

    private static ExposureCandidateInstanceReference requireInstance(
            ExposureCandidateObservation candidate
    ) {
        Objects.requireNonNull(candidate, "candidate");
        if (!ELEMENT_REFERENCE.equals(candidate.elementReference())) {
            throw new IllegalArgumentException(
                    "Contact Point evaluator requires the public Contact Point element"
            );
        }
        ExposureCandidateInstanceReference instance = candidate.instanceReference()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Contact Point evaluator requires a candidate instance"
                ));
        if (!INSTANCE_KIND.ownerIdentifier().equals(instance.ownerIdentifier())
                || !INSTANCE_KIND.instanceKindIdentifier().equals(
                        instance.instanceKindIdentifier()
                )) {
            throw new IllegalArgumentException(
                    "Contact Point evaluator requires the Profile Contact Point instance kind"
            );
        }
        return instance;
    }

    private static MerchantExposureChoiceEvaluationDecision decision(
            MerchantContactPointExposure exposure
    ) {
        return switch (exposure) {
            case PUBLIC -> MerchantExposureChoiceEvaluationDecision.EXPOSE;
            case PRIVATE_INTERNAL -> MerchantExposureChoiceEvaluationDecision.WITHHOLD;
        };
    }
}
