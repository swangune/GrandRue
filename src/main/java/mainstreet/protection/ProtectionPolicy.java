package mainstreet.protection;

import java.time.Duration;
import java.util.Objects;

/**
 * Versioned platform-operational rule for one bounded-window protection
 * strategy. The strategy is an implementation mechanism for policies that use
 * bounded time-window consumption; it is not a universal definition of every
 * future protection policy.
 */
public record ProtectionPolicy(
        String policyIdentifier,
        int version,
        ProtectionTarget target,
        String subjectScopeIdentifier,
        String measurementBasisIdentifier,
        long capacityUnits,
        Duration window,
        ProtectionAdmissionDecision exhaustionDecision,
        ProtectionStateFailureBehaviour stateFailureBehaviour
) {

    public ProtectionPolicy {
        if (policyIdentifier == null || policyIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Protection policy identifier must not be blank"
            );
        }
        if (version < 1) {
            throw new IllegalArgumentException(
                    "Protection policy version must be positive"
            );
        }
        Objects.requireNonNull(target, "target");
        if (subjectScopeIdentifier == null || subjectScopeIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Protection policy subject scope must not be blank"
            );
        }
        if (measurementBasisIdentifier == null
                || measurementBasisIdentifier.isBlank()) {
            throw new IllegalArgumentException(
                    "Protection policy measurement basis must not be blank"
            );
        }
        if (capacityUnits < 1) {
            throw new IllegalArgumentException(
                    "Protection policy capacity must be positive"
            );
        }
        Objects.requireNonNull(window, "window");
        if (window.isZero() || window.isNegative()) {
            throw new IllegalArgumentException(
                    "Protection policy window must be positive"
            );
        }
        Objects.requireNonNull(exhaustionDecision, "exhaustionDecision");
        if (exhaustionDecision == ProtectionAdmissionDecision.ADMIT) {
            throw new IllegalArgumentException(
                    "Exhaustion behaviour must be DEFER or REJECT"
            );
        }
        Objects.requireNonNull(stateFailureBehaviour, "stateFailureBehaviour");
    }

    public boolean appliesTo(ProtectionSubject subject) {
        return subjectScopeIdentifier.equals(
                Objects.requireNonNull(subject, "subject").scopeIdentifier()
        );
    }

    public boolean permitsDeferredExhaustion() {
        return exhaustionDecision == ProtectionAdmissionDecision.DEFER;
    }
}
