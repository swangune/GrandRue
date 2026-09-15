package mainstreet.merchantprofile;

import mainstreet.surface.ExposableElementReference;
import mainstreet.surface.ExposureCandidateEvaluationSubmission;
import mainstreet.surface.ExposureCandidateInstanceKindReference;
import mainstreet.surface.ExposureCandidateInstanceReference;
import mainstreet.surface.ExposureCandidateObservation;
import mainstreet.surface.MerchantExposureChoiceBatchEvaluation;
import mainstreet.surface.MerchantExposureChoiceCandidateEvaluation;
import mainstreet.surface.MerchantExposureChoiceEvaluationDecision;
import mainstreet.surface.MerchantExposureChoiceEvaluator;
import mainstreet.surface.MerchantExposureChoiceSourceReference;
import mainstreet.surface.OwnerExposureEvaluationContext;
import mainstreet.surface.ProjectionSourceDependencyReference;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** Profile-owned merchant-choice evaluator for public Merchant Location observation. */
public final class ProfileMerchantLocationExposureChoiceEvaluator
        implements MerchantExposureChoiceEvaluator {

    public static final MerchantExposureChoiceSourceReference CHOICE_SOURCE_REFERENCE =
            new MerchantExposureChoiceSourceReference(
                    "profile",
                    "merchant-location-public-exposure"
            );

    private static final ExposableElementReference ELEMENT_REFERENCE =
            new ExposableElementReference("profile", "public-merchant-location");

    private static final ExposureCandidateInstanceKindReference INSTANCE_KIND =
            new ExposureCandidateInstanceKindReference("profile", "merchant-location");

    private static final ProjectionSourceDependencyReference MATERIAL_SOURCE =
            new ProjectionSourceDependencyReference("profile", "merchant-locations");

    private final MerchantLocationExposureChoiceReadPort readPort;

    public ProfileMerchantLocationExposureChoiceEvaluator(
            MerchantLocationExposureChoiceReadPort readPort
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
                    "Merchant Location evaluator requires exact Profile Location choice source"
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
        ).map(ProfileMerchantLocationExposureChoiceEvaluator::decision)
                .orElse(MerchantExposureChoiceEvaluationDecision.UNRESOLVED);
        return new MerchantExposureChoiceCandidateEvaluation(
                submission.evaluationBinding(),
                decision
        );
    }

    private Optional<MerchantLocationExposure> currentExposure(
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
                    "Merchant Location evaluator requires the public Merchant Location element"
            );
        }
        ExposureCandidateInstanceReference instance = candidate.instanceReference()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Merchant Location evaluator requires a candidate instance"
                ));
        if (!INSTANCE_KIND.ownerIdentifier().equals(instance.ownerIdentifier())
                || !INSTANCE_KIND.instanceKindIdentifier().equals(
                        instance.instanceKindIdentifier()
                )) {
            throw new IllegalArgumentException(
                    "Merchant Location evaluator requires the Profile Merchant Location instance kind"
            );
        }
        return instance;
    }

    private static MerchantExposureChoiceEvaluationDecision decision(
            MerchantLocationExposure exposure
    ) {
        return switch (exposure) {
            case PUBLIC -> MerchantExposureChoiceEvaluationDecision.EXPOSE;
            case PRIVATE_INTERNAL -> MerchantExposureChoiceEvaluationDecision.WITHHOLD;
        };
    }
}
