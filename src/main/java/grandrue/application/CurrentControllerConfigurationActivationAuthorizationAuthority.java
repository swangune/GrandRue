package grandrue.application;

import grandrue.merchantaccount.MerchantAccountLifecycle;
import grandrue.merchantaccount.MerchantAccountLifecycleStore;
import grandrue.merchantaccount.MerchantControllerRelationshipLifecycle;
import grandrue.application.MerchantScope;
import grandrue.semantic.configuration
        .ConfigurationActivationAuthorizationAuthority;

import java.util.Objects;

/**
 * Concrete ordinary Configuration activation authority for the current MVP.
 *
 * <p>This class composes current Merchant Account-owned facts. It does not own
 * Merchant Account lifecycle, suspension or Controller relationship truth.</p>
 *
 * <p>The caller must invoke this authority while holding the Merchant
 * Account/Configuration current-authority serialization fence. The production
 * Jooq Configuration activation adapter does so before evaluating this
 * authority.</p>
 *
 * Authority: approved MS-PROT-040 v1.7.
 */
public final class
        CurrentControllerConfigurationActivationAuthorizationAuthority
        implements ConfigurationActivationAuthorizationAuthority {

    private final MerchantAccountLifecycleStore merchantAccounts;

    public CurrentControllerConfigurationActivationAuthorizationAuthority(
            MerchantAccountLifecycleStore merchantAccounts
    ) {
        this.merchantAccounts =
                Objects.requireNonNull(
                        merchantAccounts,
                        "merchantAccounts"
                );
    }

    @Override
    public boolean isAuthorized(
            String initiatingPrincipalIdentifier,
            String merchantIdentifier,
            String configurationRevisionIdentifier
    ) {
        if (isBlank(initiatingPrincipalIdentifier)
                || isBlank(merchantIdentifier)
                || isBlank(configurationRevisionIdentifier)) {
            return false;
        }

        MerchantScope merchantScope;

        try {
            merchantScope =
                    new MerchantScope(
                            merchantIdentifier
                    );

            if (merchantAccounts.lifecycle(merchantScope)
                    != MerchantAccountLifecycle.OPEN) {
                return false;
            }

        } catch (IllegalArgumentException unknownMerchant) {
            return false;
        }

        if (!merchantAccounts
                .effectiveSuspensions(merchantScope)
                .isEmpty()) {
            return false;
        }

        return merchantAccounts
                .activeController(merchantScope)
                .filter(controller ->
                        controller.lifecycle()
                                == MerchantControllerRelationshipLifecycle.ACTIVE
                )
                .filter(controller ->
                        controller.merchantScope()
                                .equals(merchantScope)
                )
                .filter(controller ->
                        controller.identityIdentifier()
                                .equals(
                                        initiatingPrincipalIdentifier
                                )
                )
                .isPresent();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}