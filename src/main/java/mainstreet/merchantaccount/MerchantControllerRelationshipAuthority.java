package mainstreet.merchantaccount;

import mainstreet.application.MerchantScope;

import java.util.Optional;

/**
 * Read authority for the current active Merchant Controller relationship.
 *
 * <p>Authentication/session code may consult this authority to establish a
 * contextual Controller principal. The relationship does not itself grant
 * operation privileges; downstream authorisation remains separate.</p>
 */
@FunctionalInterface
public interface MerchantControllerRelationshipAuthority {

    Optional<MerchantControllerRelationship> activeController(
            MerchantScope merchantScope
    );
}
