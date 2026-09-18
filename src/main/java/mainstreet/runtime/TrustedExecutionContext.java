package mainstreet.runtime;

import grandrue.runtime.AuthenticationProvenance;
import grandrue.application.MerchantScope;
import grandrue.application.TrustedDeviceApplicationContext;

import java.util.Objects;
import java.util.Optional;

/**
 * Trusted application-boundary context propagated into protected runtime
 * evaluation. It carries attribution and immutable establishment provenance;
 * mutable privileges and relationship authority remain external and current.
 *
 * <p>Authentication provenance is optional because legitimate guest, system,
 * scheduled and other non-session principals must not fabricate a human
 * authentication session merely to satisfy the runtime context shape.</p>
 *
 * <p>Trusted device/application context is likewise optional globally, but it
 * is required by the staff-operational establishment path governed by
 * MS-PROT-063 v1.1 / MS-PROT-074. A missing device context must therefore not
 * be interpreted as staff-operational trust.</p>
 */
public record TrustedExecutionContext(
        MerchantScope merchantScope,
        ExecutionPrincipal principal,
        Optional<AuthenticationProvenance> authentication,
        Optional<TrustedDeviceApplicationContext> deviceApplicationContext
) {
    public TrustedExecutionContext {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(principal, "principal");
        authentication = Objects.requireNonNull(authentication, "authentication");
        deviceApplicationContext = Objects.requireNonNull(
                deviceApplicationContext,
                "deviceApplicationContext"
        );
    }

    public TrustedExecutionContext(
            MerchantScope merchantScope,
            ExecutionPrincipal principal,
            Optional<AuthenticationProvenance> authentication
    ) {
        this(
                merchantScope,
                principal,
                authentication,
                Optional.empty()
        );
    }
}
