package grandrue.money;

import mainstreet.application.MerchantScope;

import java.util.List;
import java.util.Optional;

/** Provider-neutral authoritative payment facts and reconciliation boundary. */
public interface PaymentAuthorityStore {

    PaymentObligation registerObligation(PaymentObligation obligation);

    ProviderPaymentEvidence recordProviderEvidence(ProviderPaymentEvidence evidence);

    PaymentApplication applyPayment(PaymentApplication application);

    Optional<PaymentObligation> obligation(
            MerchantScope merchantScope,
            String obligationIdentity
    );

    Optional<ProviderPaymentEvidence> providerEvidence(
            MerchantScope merchantScope,
            String evidenceIdentity
    );

    List<PaymentApplication> applications(
            MerchantScope merchantScope,
            String obligationIdentity
    );

    MonetaryAmount currentAmountDue(
            MerchantScope merchantScope,
            String obligationIdentity
    );
}
