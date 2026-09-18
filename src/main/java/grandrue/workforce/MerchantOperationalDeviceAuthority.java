package grandrue.workforce;

import mainstreet.application.MerchantScope;
import grandrue.application.TrustedDeviceApplicationContext;

/**
 * Current merchant-scoped staff operational-device trust authority.
 *
 * <p>Later runtime composition may depend on this query boundary without
 * depending on device-authorisation command/persistence APIs.</p>
 */
public interface MerchantOperationalDeviceAuthority {

    boolean isActive(
            MerchantScope merchantScope,
            TrustedDeviceApplicationContext deviceContext
    );
}
