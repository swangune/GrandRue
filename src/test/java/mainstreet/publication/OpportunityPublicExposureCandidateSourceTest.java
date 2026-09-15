package mainstreet.publication;

import mainstreet.application.MerchantScope;
import mainstreet.surface.ExposureCandidateInstanceReference;
import mainstreet.surface.ExposureCandidateObservation;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OpportunityPublicExposureCandidateSourceTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");

    @Test
    void forms_only_stable_instance_qualified_candidates_from_current_published_evidence() {
        OpportunityPublicExposureReadPort readPort = (merchantScope, identity) -> {
            if (!"published".equals(identity)) {
                return Optional.empty();
            }
            return Optional.of(new OpportunityPublicExposureEvidence(
                    merchantScope,
                    identity,
                    "revision-1",
                    Optional.empty(),
                    Optional.empty()
            ));
        };
        OpportunityPublicExposureCandidateSource source =
                new OpportunityPublicExposureCandidateSource(readPort);

        ExposureCandidateObservation candidate =
                source.currentCandidate(MERCHANT, "published").orElseThrow();

        assertEquals(
                OpportunityPublicExposureReferences.PUBLIC_OPPORTUNITY_REPRESENTATION,
                candidate.elementReference()
        );
        assertEquals(
                Optional.of(new ExposureCandidateInstanceReference(
                        "publication",
                        "opportunity",
                        "published"
                )),
                candidate.instanceReference()
        );
        assertTrue(source.currentCandidate(MERCHANT, "not-published").isEmpty());
    }
}
