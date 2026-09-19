package grandrue.commercial;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Owner-qualified conditional supporting-commercial relationship.
 *
 * <p>The condition is not interpreted by Commercial. The source authority owns
 * which alternative applies; this retained evidence only states the exact
 * Commercial purposes that become required for each admitted alternative.</p>
 */
public record CommercialConditionalSupportingAccessRequirement(
        String conditionAuthority,
        Set<CommercialConditionalSupportAlternative> alternatives,
        String classificationAuthority
) {
    public CommercialConditionalSupportingAccessRequirement {
        CommercialAccessTarget.requireText(conditionAuthority, "conditionAuthority");
        alternatives = Set.copyOf(Objects.requireNonNull(alternatives, "alternatives"));
        if (alternatives.isEmpty()) {
            throw new IllegalArgumentException("Conditional support requires at least one alternative");
        }
        Set<String> conditions = new HashSet<>();
        for (var alternative : alternatives) {
            if (!conditions.add(alternative.conditionValue())) {
                throw new IllegalArgumentException(
                        "Conditional support has duplicate condition value: " + alternative.conditionValue());
            }
        }
        CommercialAccessTarget.requireText(classificationAuthority, "classificationAuthority");
    }
}
