package mainstreet.surface;

import java.util.Objects;
import java.util.Optional;

/** Package-owned positive Exposure member with exact contract provenance. */
final class ResolvedExposedElement {

    private final ExposedElementMembership membership;
    private final ExposureElementContractIdentity contractIdentity;

    ResolvedExposedElement(
            ExposableElementReference elementReference,
            Optional<ExposureCandidateInstanceReference> candidateInstanceReference,
            ExposureElementContract contract
    ) {
        Objects.requireNonNull(elementReference, "elementReference");
        candidateInstanceReference = Objects.requireNonNull(
                candidateInstanceReference,
                "candidateInstanceReference"
        );
        Objects.requireNonNull(contract, "contract");
        if (!contract.exposableElementReference().equals(elementReference)) {
            throw new IllegalArgumentException(
                    "Resolved exposed element must match its exact Exposure contract candidate"
            );
        }

        Optional<ExposureCandidateInstanceReference> memberInstance;
        if (contract.memberIdentitySpecification()
                instanceof ExposureMemberIdentitySpecification.Singleton) {
            memberInstance = Optional.empty();
        } else if (contract.memberIdentitySpecification()
                instanceof ExposureMemberIdentitySpecification.InstanceQualified qualified) {
            ExposureCandidateInstanceReference instance = candidateInstanceReference
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Instance-qualified Exposure member requires a candidate instance"
                    ));
            ExposureCandidateInstanceKindReference required =
                    qualified.instanceKindReference();
            if (!required.ownerIdentifier().equals(instance.ownerIdentifier())
                    || !required.instanceKindIdentifier().equals(
                            instance.instanceKindIdentifier()
                    )) {
                throw new IllegalArgumentException(
                        "Exposure candidate instance does not match required member kind"
                );
            }
            memberInstance = Optional.of(instance);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported Exposure member identity specification"
            );
        }

        this.membership = new ExposedElementMembership(
                elementReference,
                memberInstance
        );
        this.contractIdentity = contract.identity();
    }

    ExposedElementMembership membership() {
        return membership;
    }

    ExposureElementContractIdentity contractIdentity() {
        return contractIdentity;
    }
}
