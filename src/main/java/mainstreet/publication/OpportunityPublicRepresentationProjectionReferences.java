package mainstreet.publication;

import mainstreet.surface.ProjectionContractIdentity;
import mainstreet.surface.ProjectionReadUseIdentity;
import mainstreet.surface.ProjectionSourceDependencyReference;

/** Stable implementation references for the P5 bounded public Opportunity projection. */
public final class OpportunityPublicRepresentationProjectionReferences {

    public static final ProjectionContractIdentity PROJECTION_CONTRACT =
            new ProjectionContractIdentity("publication", "public-opportunity-representation");

    public static final ProjectionReadUseIdentity READ_USE =
            new ProjectionReadUseIdentity("publication", "public-opportunity-representation");

    public static final ProjectionSourceDependencyReference PUBLISHED_MATERIAL_SOURCE =
            new ProjectionSourceDependencyReference(
                    "publication",
                    "opportunity-published-material"
            );

    private OpportunityPublicRepresentationProjectionReferences() {
    }
}
