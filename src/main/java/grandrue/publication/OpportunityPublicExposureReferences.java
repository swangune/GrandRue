package grandrue.publication;

import grandrue.surface.ExposableElementReference;
import grandrue.surface.ExposureCandidateInstanceKindReference;
import grandrue.surface.ExposureCandidateInstanceReference;
import grandrue.surface.ExposureCandidateObservation;
import grandrue.surface.ExposureRequirementReference;

import java.util.Objects;
import java.util.Optional;

/** Exact owner-qualified P4 references for public Opportunity Exposure. */
public final class OpportunityPublicExposureReferences {

    public static final ExposableElementReference PUBLIC_OPPORTUNITY_REPRESENTATION =
            new ExposableElementReference(
                    "publication",
                    "public-opportunity-representation"
            );

    public static final ExposureCandidateInstanceKindReference OPPORTUNITY_INSTANCE_KIND =
            new ExposureCandidateInstanceKindReference(
                    "publication",
                    "opportunity"
            );

    /**
     * Implementation identifier for the accepted Publication-owned public Opportunity
     * publishFrom/publishUntil requirement.
     */
    public static final ExposureRequirementReference EXPOSURE_WINDOW_REQUIREMENT =
            new ExposureRequirementReference(
                    "publication",
                    "public-opportunity-exposure-window"
            );

    private OpportunityPublicExposureReferences() {
    }

    public static ExposureCandidateObservation candidate(String opportunityIdentity) {
        requireText(opportunityIdentity, "opportunityIdentity");
        return new ExposureCandidateObservation(
                PUBLIC_OPPORTUNITY_REPRESENTATION,
                Optional.of(new ExposureCandidateInstanceReference(
                        OPPORTUNITY_INSTANCE_KIND.ownerIdentifier(),
                        OPPORTUNITY_INSTANCE_KIND.instanceKindIdentifier(),
                        opportunityIdentity
                ))
        );
    }

    static String requireOpportunityIdentity(ExposureCandidateObservation candidate) {
        Objects.requireNonNull(candidate, "candidate");
        if (!PUBLIC_OPPORTUNITY_REPRESENTATION.equals(candidate.elementReference())) {
            throw new IllegalArgumentException(
                    "Opportunity Exposure evaluator requires public Opportunity representation"
            );
        }

        ExposureCandidateInstanceReference instance = candidate.instanceReference()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Public Opportunity Exposure requires a stable Opportunity instance"
                ));

        if (!OPPORTUNITY_INSTANCE_KIND.ownerIdentifier().equals(instance.ownerIdentifier())
                || !OPPORTUNITY_INSTANCE_KIND.instanceKindIdentifier().equals(
                        instance.instanceKindIdentifier()
                )) {
            throw new IllegalArgumentException(
                    "Public Opportunity Exposure requires the Publication Opportunity instance kind"
            );
        }

        requireText(instance.instanceIdentifier(), "opportunityIdentity");
        return instance.instanceIdentifier();
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
