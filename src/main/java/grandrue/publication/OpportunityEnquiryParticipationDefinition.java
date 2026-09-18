package grandrue.publication;

import grandrue.semantic.registry.SemanticRegistrySnapshot;
import grandrue.surface.ExposureCandidateInstanceKindReference;
import grandrue.surface.PublicInteractionParticipationSourceIdentity;
import grandrue.surface.SurfaceContributionIdentity;

import java.util.Objects;

/**
 * Explicit Publication-owned Opportunity → enquiry/send-enquiry definition under
 * MS-PROT-046 v1.2 §§16–18 and MS-PROT-049 v1.4 §§3, 15. Registering this definition
 * establishes the family relationship; Exposure or an active Enquiry capability alone cannot.
 */
public final class OpportunityEnquiryParticipationDefinition {
    private static final PublicInteractionParticipationSourceIdentity SOURCE =
            new PublicInteractionParticipationSourceIdentity("publication", "opportunity-enquiry");
    private static final SurfaceContributionIdentity CONTRIBUTION =
            new SurfaceContributionIdentity("enquiry", "send-enquiry");
    private static final String OPERATION = "send-enquiry";

    private final String semanticRegistryReleaseIdentifier;

    private OpportunityEnquiryParticipationDefinition(String release) {
        this.semanticRegistryReleaseIdentifier = release;
    }

    public static OpportunityEnquiryParticipationDefinition forRelease(
            SemanticRegistrySnapshot registry
    ) {
        Objects.requireNonNull(registry, "registry");
        boolean opportunityRegistered = registry.capability("publication")
                .stream().flatMap(capability -> capability.operationalObjects().stream())
                .anyMatch(object -> object.identifier().equals("opportunity"));
        boolean operationRegistered = registry.capability("enquiry")
                .stream().flatMap(capability -> capability.operations().stream())
                .anyMatch(operation -> operation.identifier().equals(OPERATION));
        if (!opportunityRegistered || !operationRegistered) {
            throw new IllegalArgumentException(
                    "Opportunity participation requires registered publication/opportunity "
                            + "and enquiry/send-enquiry semantics in the exact release"
            );
        }
        return new OpportunityEnquiryParticipationDefinition(registry.version());
    }

    public String semanticRegistryReleaseIdentifier() {
        return semanticRegistryReleaseIdentifier;
    }

    public PublicInteractionParticipationSourceIdentity sourceIdentity() {
        return SOURCE;
    }

    public ExposureCandidateInstanceKindReference subjectKind() {
        return OpportunityPublicExposureReferences.OPPORTUNITY_INSTANCE_KIND;
    }

    public SurfaceContributionIdentity contributionIdentity() {
        return CONTRIBUTION;
    }

    public String operationReference() {
        return OPERATION;
    }
}
