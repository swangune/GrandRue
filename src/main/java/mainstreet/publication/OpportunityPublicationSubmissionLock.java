package mainstreet.publication;

import grandrue.application.MerchantScope;
import java.util.Optional;

/**
 * Publication-owned consistency boundary for Enquiry submission. Locks the exact current row
 * against revision/lifecycle changes until the enclosing submission transaction ends.
 * Must reject use without an active transaction. No Publication mutation is authorized.
 */
@FunctionalInterface
public interface OpportunityPublicationSubmissionLock {
    Optional<OpportunityPublicationState> lockCurrent(MerchantScope scope, String opportunityIdentity);
}
