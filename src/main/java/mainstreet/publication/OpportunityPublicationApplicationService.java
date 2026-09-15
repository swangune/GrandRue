package mainstreet.publication;

import mainstreet.application.ApplicationRequestIdentity;
import mainstreet.application.MerchantScope;

/**
 * Channel-independent application boundary for authoritative Opportunity Publication mutation.
 *
 * <p>Logical request identity is distinct from Opportunity/revision identity. Implementations
 * must reconcile a retry of the same logical operation to its committed result without
 * multiplying authoritative effect.</p>
 */
public interface OpportunityPublicationApplicationService {

    OpportunityPublicationState establishDraft(
            ApplicationRequestIdentity requestIdentity,
            OpportunityPublicationMaterialRevision initialRevision
    );

    OpportunityPublicationState revise(
            ApplicationRequestIdentity requestIdentity,
            String expectedCurrentRevisionIdentity,
            OpportunityPublicationMaterialRevision newRevision
    );

    OpportunityPublicationState publish(
            ApplicationRequestIdentity requestIdentity,
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedCurrentRevisionIdentity
    );

    OpportunityPublicationState withdraw(
            ApplicationRequestIdentity requestIdentity,
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedCurrentRevisionIdentity
    );

    OpportunityPublicationState republish(
            ApplicationRequestIdentity requestIdentity,
            MerchantScope merchantScope,
            String opportunityIdentity,
            String expectedCurrentRevisionIdentity
    );
}
