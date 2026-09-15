package mainstreet.surface;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/** Immutable release-affined contract for observing one semantic/read element. */
public record ExposureElementContract(
        ExposureElementContractIdentity identity,
        ExposableElementReference exposableElementReference,
        SurfaceAudience audience,
        ExposureMemberIdentitySpecification memberIdentitySpecification,
        ExposureDecision baselineDecision,
        Optional<MerchantExposureChoiceSourceReference> merchantChoiceSource,
        Set<ExposureRequirementReference> requirementReferences
) {
    public ExposureElementContract {
        identity = Objects.requireNonNull(identity, "identity");
        exposableElementReference = Objects.requireNonNull(
                exposableElementReference,
                "exposableElementReference"
        );
        if (!identity.ownerIdentifier().equals(
                exposableElementReference.ownerIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Exposure contract owner must match exposable element owner"
            );
        }
        audience = Objects.requireNonNull(audience, "audience");
        memberIdentitySpecification = Objects.requireNonNull(
                memberIdentitySpecification,
                "memberIdentitySpecification"
        );
        if (memberIdentitySpecification
                instanceof ExposureMemberIdentitySpecification.InstanceQualified qualified
                && !qualified.instanceKindReference().ownerIdentifier().equals(
                exposableElementReference.ownerIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Exposure member instance-kind owner must match exposable element owner"
            );
        }
        baselineDecision = Objects.requireNonNull(
                baselineDecision,
                "baselineDecision"
        );
        merchantChoiceSource = Objects.requireNonNull(
                merchantChoiceSource,
                "merchantChoiceSource"
        );
        requirementReferences = Set.copyOf(
                Objects.requireNonNull(
                        requirementReferences,
                        "requirementReferences"
                )
        );
    }
}
