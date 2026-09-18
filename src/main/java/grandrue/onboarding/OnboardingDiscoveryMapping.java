package grandrue.onboarding;

import grandrue.onboarding.OnboardingQuestionIdentity;
import grandrue.onboarding.OnboardingQuestionDefinitionVersion;
import grandrue.onboarding.OnboardingSemanticSeed;
import java.util.Objects;
import java.util.Set;

/**
 * Exact registered interpretation of one discovery option.
 */
public record OnboardingDiscoveryMapping(
        OnboardingQuestionIdentity sourceQuestionIdentity,
        OnboardingQuestionDefinitionVersion sourceQuestionVersion,
        String sourceOptionIdentifier,
        Set<OnboardingSemanticSeed> proposedSemanticSeeds
) {

    public OnboardingDiscoveryMapping {
        Objects.requireNonNull(
                sourceQuestionIdentity,
                "sourceQuestionIdentity"
        );
        Objects.requireNonNull(
                sourceQuestionVersion,
                "sourceQuestionVersion"
        );
        if (sourceOptionIdentifier == null
                || sourceOptionIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Source option identifier must not be blank"
            );
        }
        proposedSemanticSeeds = Set.copyOf(
                Objects.requireNonNull(
                        proposedSemanticSeeds,
                        "proposedSemanticSeeds"
                )
        );
    }
}
