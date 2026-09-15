package mainstreet.prototype.delivery;

import mainstreet.prototype.PrototypeMerchantView;

import java.util.Set;

/** HTTP boundary DTO for the prototype active-merchant projection. */
public record PrototypeMerchantResponse(
        String merchantIdentifier,
        String configurationIdentifier,
        String releaseIdentifier,
        Set<String> capabilityIdentifiers,
        Set<String> operationIdentifiers
) {

    public static PrototypeMerchantResponse from(PrototypeMerchantView view) {
        return new PrototypeMerchantResponse(
                view.merchantIdentifier(),
                view.configurationIdentifier(),
                view.releaseIdentifier(),
                view.capabilityIdentifiers(),
                view.operationIdentifiers()
        );
    }
}
