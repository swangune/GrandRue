package mainstreet.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.application.TrustedDeviceApplicationContext;
import mainstreet.runtime.ExecutionPrincipal;

import java.time.Clock;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Application service for explicit Merchant Controller device enrolment and
 * revocation. It does not authenticate staff or derive runtime privileges.
 */
public final class MerchantOperationalDeviceAuthoriser {

    private final MerchantOperationalDeviceAuthorisationStore store;
    private final Clock clock;
    private final Supplier<String> authorisationIdentifierFactory;

    public MerchantOperationalDeviceAuthoriser(
            MerchantOperationalDeviceAuthorisationStore store,
            Clock clock,
            Supplier<String> authorisationIdentifierFactory
    ) {
        this.store = Objects.requireNonNull(store, "store");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.authorisationIdentifierFactory = Objects.requireNonNull(
                authorisationIdentifierFactory,
                "authorisationIdentifierFactory"
        );
    }

    public MerchantOperationalDeviceAuthorisation authorise(
            String logicalRequestIdentity,
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            TrustedDeviceApplicationContext deviceContext
    ) {
        requireIdentifier(logicalRequestIdentity, "Logical request identity");
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(controllerPrincipal, "controllerPrincipal");
        Objects.requireNonNull(deviceContext, "deviceContext");

        String authorisationIdentifier = requireIdentifier(
                authorisationIdentifierFactory.get(),
                "Generated device authorisation identifier"
        );
        Instant authorisedAt = clock.instant();

        return store.establishIfCurrentController(
                logicalRequestIdentity,
                merchantScope,
                controllerPrincipal,
                deviceContext,
                authorisationIdentifier,
                authorisedAt
        );
    }

    public boolean revoke(
            MerchantScope merchantScope,
            ExecutionPrincipal controllerPrincipal,
            String authorisationIdentifier
    ) {
        Objects.requireNonNull(merchantScope, "merchantScope");
        Objects.requireNonNull(controllerPrincipal, "controllerPrincipal");
        requireIdentifier(
                authorisationIdentifier,
                "Operational device authorisation identifier"
        );
        return store.revokeIfCurrentController(
                merchantScope,
                controllerPrincipal,
                authorisationIdentifier,
                clock.instant()
        );
    }

    private static String requireIdentifier(String value, String label) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(label + " must not be blank");
        }
        return value;
    }
}
