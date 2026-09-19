package grandrue.commercial;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * One owner-qualified semantic condition and the Commercial purposes required
 * only when that exact condition applies.
 */
public record CommercialConditionalSupportAlternative(
        String conditionValue,
        Set<CommercialRequiredPurpose> requiredPurposes
) {
    public CommercialConditionalSupportAlternative {
        CommercialAccessTarget.requireExactReference(conditionValue, "conditionValue");
        requiredPurposes = Set.copyOf(Objects.requireNonNull(requiredPurposes, "requiredPurposes"));
    }

    public CommercialConditionalSupportAlternative(
            String conditionValue,
            CommercialAccessTarget target,
            Set<String> requiredPurposes
    ) {
        this(conditionValue, required(target, requiredPurposes));
    }

    private static Set<CommercialRequiredPurpose> required(
            CommercialAccessTarget target,
            Set<String> purposes
    ) {
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(purposes, "requiredPurposes");
        Set<CommercialRequiredPurpose> result = new HashSet<>();
        for (String purpose : purposes) {
            result.add(new CommercialRequiredPurpose(target, purpose));
        }
        return Set.copyOf(result);
    }
}
