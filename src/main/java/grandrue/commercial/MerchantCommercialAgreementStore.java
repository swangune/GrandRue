package grandrue.commercial;

import mainstreet.commercial.CommercialEntitlementGrantAuthority;
import grandrue.commercial.MerchantCommercialAgreement;
import grandrue.commercial.MerchantCommercialAgreementTransition;

import grandrue.application.MerchantScope;

import java.time.Instant;
import java.util.Optional;

/**
 * Commercial-owned persistence/query port for authoritative paid Merchant
 * Commercial Agreement transitions and agreement-derived entitlement grants.
 *
 * <p>The port owns no payment-provider behaviour, Merchant Configuration,
 * Actor Authorisation, trust, provider readiness or capability lifecycle.</p>
 */
public interface MerchantCommercialAgreementStore
        extends CommercialEntitlementGrantAuthority {

    MerchantCommercialAgreement apply(
            MerchantCommercialAgreementTransition transition
    );

    Optional<MerchantCommercialAgreement> committedAgreement(
            String logicalRequestIdentity
    );

    Optional<MerchantCommercialAgreement> effectiveAgreement(
            MerchantScope merchantScope,
            Instant instant
    );
}
