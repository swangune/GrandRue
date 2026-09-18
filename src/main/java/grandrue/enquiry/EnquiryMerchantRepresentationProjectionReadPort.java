package grandrue.enquiry;

import grandrue.application.MerchantScope;
import java.time.Instant;

/** Internal request-scoped material acquisition; audience admission and Exposure remain separate. */
@FunctionalInterface
public interface EnquiryMerchantRepresentationProjectionReadPort {
    EnquiryMerchantRepresentationProjectionObservation observe(MerchantScope scope, String enquiryIdentity, Instant observedAt);
}
