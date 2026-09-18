package grandrue.publication;

import grandrue.application.MerchantScope;
import grandrue.surface.ExposureCandidateObservation;

import java.util.Objects;
import java.util.Optional;

/**
 * Publication-owned P4 candidate-formation boundary for public Opportunity observation.
 *
 * <p>DRAFT and WITHDRAWN Opportunities produce no candidate. This keeps lifecycle candidacy
 * upstream of the generic, value-blind Exposure resolver as required by MS-PROT-027.</p>
 */
public final class OpportunityPublicExposureCandidateSource {

    private final OpportunityPublicExposureReadPort readPort;

    public OpportunityPublicExposureCandidateSource(
            OpportunityPublicExposureReadPort readPort
    ) {
        this.readPort = Objects.requireNonNull(readPort, "readPort");
    }

    public Optional<ExposureCandidateObservation> currentCandidate(
            MerchantScope merchantScope,
            String opportunityIdentity
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        requireText(opportunityIdentity, "opportunityIdentity");

        return readPort.currentPublishedExposure(merchantScope, opportunityIdentity)
                .map(evidence -> {
                    requireEvidenceAffinity(
                            merchantScope,
                            opportunityIdentity,
                            evidence
                    );
                    return OpportunityPublicExposureReferences.candidate(
                            opportunityIdentity
                    );
                });
    }

    private static void requireEvidenceAffinity(
            MerchantScope merchantScope,
            String opportunityIdentity,
            OpportunityPublicExposureEvidence evidence
    ) {
        Objects.requireNonNull(evidence, "evidence");
        if (!merchantScope.equals(evidence.merchantScope())
                || !opportunityIdentity.equals(evidence.opportunityIdentity())) {
            throw new IllegalStateException(
                    "Publication public-candidate evidence has mismatched Opportunity affinity"
            );
        }
    }

    private static void requireText(String value, String field) {
        Objects.requireNonNull(value, field);
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }
}
