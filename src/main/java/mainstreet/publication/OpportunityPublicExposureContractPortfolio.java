package mainstreet.publication;

import mainstreet.surface.ExposureDecision;
import mainstreet.surface.ExposureElementContract;
import mainstreet.surface.ExposureElementContractIdentity;
import mainstreet.surface.ExposureElementContractRegistrySnapshot;
import mainstreet.surface.ExposureMemberIdentitySpecification;
import mainstreet.surface.SurfaceAudience;

import java.util.Optional;
import java.util.Set;

/**
 * Initial P4 PUBLIC Exposure portfolio for the Opportunity path of IMP-07.
 *
 * <p>The portfolio intentionally registers neither public Opportunity actionability (P6) nor any
 * request-scoped representation material (P5).</p>
 */
public final class OpportunityPublicExposureContractPortfolio {

    private OpportunityPublicExposureContractPortfolio() {
    }

    public static Set<ExposureElementContract> contracts() {
        return Set.of(new ExposureElementContract(
                new ExposureElementContractIdentity(
                        "publication",
                        "public-opportunity-representation"
                ),
                OpportunityPublicExposureReferences.PUBLIC_OPPORTUNITY_REPRESENTATION,
                SurfaceAudience.PUBLIC,
                ExposureMemberIdentitySpecification.instanceQualified(
                        OpportunityPublicExposureReferences.OPPORTUNITY_INSTANCE_KIND
                ),
                ExposureDecision.EXPOSE,
                Optional.empty(),
                Set.of(
                        OpportunityPublicExposureReferences.EXPOSURE_WINDOW_REQUIREMENT
                )
        ));
    }

    public static ExposureElementContractRegistrySnapshot forRelease(
            String semanticRegistryReleaseIdentifier
    ) {
        return new ExposureElementContractRegistrySnapshot(
                semanticRegistryReleaseIdentifier,
                contracts()
        );
    }
}
