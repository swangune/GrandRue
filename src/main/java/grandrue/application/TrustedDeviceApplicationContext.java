package grandrue.application;

import java.util.Objects;

/**
 * Trusted application-boundary representation of one independently verified
 * device/application-instance binding.
 *
 * <p>The binding reference is produced downstream of the security mechanisms
 * governed by MS-PROT-063, MS-PROT-067 and MS-PROT-074. It is deliberately not
 * a client-supplied deviceId, cookie, localStorage flag, user-agent or IP
 * address and it grants no merchant authority by itself.</p>
 */
public record TrustedDeviceApplicationContext(String bindingReference) {

    public TrustedDeviceApplicationContext {
        Objects.requireNonNull(bindingReference, "bindingReference");
        if (bindingReference.isBlank()) {
            throw new IllegalArgumentException(
                    "Trusted device/application binding reference must not be blank"
            );
        }
    }
}
