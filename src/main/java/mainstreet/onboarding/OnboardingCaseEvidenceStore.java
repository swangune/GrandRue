package mainstreet.onboarding;

import grandrue.onboarding.AcknowledgeInitialConfigurationIntentCommand;
import grandrue.onboarding.StartInitialOnboardingCaseCommand;
import grandrue.onboarding.OnboardingAnswerEvidenceRevision;
import grandrue.onboarding.OnboardingCase;
import grandrue.onboarding.OnboardingAnswerMutationResult;
import grandrue.onboarding.OnboardingCaseEvidenceSnapshot;
import grandrue.onboarding.InitialConfigurationIntentIdentity;
import grandrue.onboarding.OnboardingQuestionIdentity;
import grandrue.onboarding.OnboardingCaseRevision;
import grandrue.onboarding.OnboardingCaseIdentity;
import grandrue.onboarding.OnboardingSubmissionAuthority;
import grandrue.application.MerchantScope;

import java.util.List;
import java.util.Optional;

/**
 * Durable owner of ordinary initial Onboarding Case state and answer evidence.
 *
 * <p>Stored evidence is non-executable and does not become Merchant
 * Configuration or runtime authority.</p>
 */
public interface OnboardingCaseEvidenceStore {

    OnboardingCase startInitial(
            StartInitialOnboardingCaseCommand command
    );

    Optional<OnboardingCase> currentInitial(
            MerchantScope merchantScope
    );

    Optional<OnboardingCase> caseByIdentity(
            OnboardingCaseIdentity onboardingCaseIdentity
    );

    OnboardingAnswerMutationResult recordAnswer(
            RecordOnboardingAnswerCommand command
    );

    List<OnboardingAnswerEvidenceRevision> answerHistory(
            OnboardingCaseIdentity onboardingCaseIdentity,
            OnboardingQuestionIdentity questionIdentity,
            Optional<String> contextScopeReference
    );

    List<OnboardingAnswerEvidenceRevision> effectiveAnswers(
            OnboardingCaseIdentity onboardingCaseIdentity
    );

    OnboardingCaseEvidenceSnapshot evidenceSnapshotAtRevision(
            OnboardingCaseIdentity onboardingCaseIdentity,
            OnboardingCaseRevision revision
    );

    InitialConfigurationIntent submitInitialConfigurationIntent(
            SubmitInitialConfigurationIntentCommand command,
            OnboardingCompletionPolicy completionPolicy,
            OnboardingSubmissionAuthority authority
    );

    Optional<InitialConfigurationIntent>
            initialConfigurationIntentByIdentity(
                    InitialConfigurationIntentIdentity intentIdentity
            );

    OnboardingCase acknowledgeInitialConfigurationIntent(
            AcknowledgeInitialConfigurationIntentCommand command
    );
}
