package grandrue.publication;

import grandrue.application.MerchantScope;

import java.util.List;
import java.util.Optional;

/**
 * Durable Publication state, immutable material-revision and Publication-History boundary for one
 * merchant-owned Opportunity.
 *
 * <p>The port stores already-valid Publication-owned state and typed Opportunity material. It does
 * not decide lifecycle transitions, perform Exposure, own Enquiry, or provide logical-operation
 * retry idempotency.</p>
 */
public interface OpportunityPublicationStateAuthority {

    OpportunityPublicationState establish(
            OpportunityPublicationState initialState,
            OpportunityPublicationMaterialRevision initialRevision
    );

    OpportunityPublicationState compareAndSet(
            OpportunityPublicationState expectedState,
            OpportunityPublicationState nextState,
            Optional<OpportunityPublicationMaterialRevision> newRevision
    );

    Optional<OpportunityPublicationState> current(
            MerchantScope merchantScope,
            String opportunityIdentity
    );

    Optional<OpportunityPublicationMaterialRevision> revision(
            MerchantScope merchantScope,
            String opportunityIdentity,
            String revisionIdentity
    );

    List<OpportunityPublicationHistoryEntry> publicationHistory(
            MerchantScope merchantScope,
            String opportunityIdentity
    );
}
