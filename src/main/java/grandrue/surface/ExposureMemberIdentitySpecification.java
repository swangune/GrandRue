package grandrue.surface;

import java.util.Objects;

/** Exact positive-membership identity semantics for one Exposure Element Contract. */
public sealed interface ExposureMemberIdentitySpecification
        permits ExposureMemberIdentitySpecification.Singleton,
        ExposureMemberIdentitySpecification.InstanceQualified {

    static ExposureMemberIdentitySpecification singleton() {
        return new Singleton();
    }

    static ExposureMemberIdentitySpecification instanceQualified(
            ExposureCandidateInstanceKindReference instanceKindReference
    ) {
        return new InstanceQualified(instanceKindReference);
    }

    record Singleton() implements ExposureMemberIdentitySpecification {
    }

    record InstanceQualified(
            ExposureCandidateInstanceKindReference instanceKindReference
    ) implements ExposureMemberIdentitySpecification {
        public InstanceQualified {
            Objects.requireNonNull(instanceKindReference, "instanceKindReference");
        }
    }
}
