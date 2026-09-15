package mainstreet.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.application.TrustedDeviceApplicationContext;
import mainstreet.runtime.AuthorizationException;
import mainstreet.runtime.ExecutionPrincipal;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MerchantOperationalDeviceAuthorisationTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final ExecutionPrincipal CONTROLLER =
            new ExecutionPrincipal("identity-controller");
    private static final TrustedDeviceApplicationContext DEVICE =
            new TrustedDeviceApplicationContext("verified-binding-1");
    private static final Instant NOW = Instant.parse("2026-08-24T04:30:00Z");

    @Test
    void trusted_device_context_represents_server_validated_binding_not_merchant_authority() {
        TrustedDeviceApplicationContext context =
                new TrustedDeviceApplicationContext("binding-ref");

        assertEquals("binding-ref", context.bindingReference());
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrustedDeviceApplicationContext(" ")
        );
    }

    @Test
    void explicit_authorisation_delegates_commit_to_current_controller_guarded_port() {
        AtomicReference<String> observedRequest = new AtomicReference<>();
        AtomicReference<ExecutionPrincipal> observedController = new AtomicReference<>();

        MerchantOperationalDeviceAuthorisationStore store =
                new MerchantOperationalDeviceAuthorisationStore() {
                    @Override
                    public MerchantOperationalDeviceAuthorisation establishIfCurrentController(
                            String logicalRequestIdentity,
                            MerchantScope merchantScope,
                            ExecutionPrincipal controllerPrincipal,
                            TrustedDeviceApplicationContext deviceContext,
                            String authorisationIdentifier,
                            Instant authorisedAt
                    ) {
                        observedRequest.set(logicalRequestIdentity);
                        observedController.set(controllerPrincipal);
                        return MerchantOperationalDeviceAuthorisation.authorised(
                                authorisationIdentifier,
                                merchantScope,
                                deviceContext.bindingReference(),
                                controllerPrincipal.identifier(),
                                authorisedAt
                        );
                    }

                    @Override
                    public boolean revokeIfCurrentController(
                            MerchantScope merchantScope,
                            ExecutionPrincipal controllerPrincipal,
                            String authorisationIdentifier,
                            Instant revokedAt
                    ) {
                        return false;
                    }

                    @Override
                    public Optional<MerchantOperationalDeviceAuthorisation> find(
                            String authorisationIdentifier
                    ) {
                        return Optional.empty();
                    }

                    @Override
                    public boolean isActive(
                            MerchantScope merchantScope,
                            TrustedDeviceApplicationContext deviceContext
                    ) {
                        return false;
                    }
                };

        MerchantOperationalDeviceAuthoriser authoriser =
                new MerchantOperationalDeviceAuthoriser(
                        store,
                        Clock.fixed(NOW, ZoneOffset.UTC),
                        () -> "device-auth-1"
                );

        MerchantOperationalDeviceAuthorisation result = authoriser.authorise(
                "request-1",
                MERCHANT,
                CONTROLLER,
                DEVICE
        );

        assertEquals("request-1", observedRequest.get());
        assertEquals(CONTROLLER, observedController.get());
        assertEquals(MERCHANT, result.merchantScope());
        assertEquals("verified-binding-1", result.bindingReference());
        assertEquals("identity-controller", result.authorisedByIdentityReference());
        assertEquals(MerchantOperationalDeviceAuthorisationLifecycle.ACTIVE, result.lifecycle());
    }

    @Test
    void non_controller_rejection_propagates_and_produces_no_authorisation_fact() {
        MerchantOperationalDeviceAuthorisationStore rejectingStore =
                new RejectingStore();
        MerchantOperationalDeviceAuthoriser authoriser =
                new MerchantOperationalDeviceAuthoriser(
                        rejectingStore,
                        Clock.fixed(NOW, ZoneOffset.UTC),
                        () -> "device-auth-1"
                );

        assertThrows(
                AuthorizationException.class,
                () -> authoriser.authorise(
                        "request-1",
                        MERCHANT,
                        new ExecutionPrincipal("identity-staff"),
                        DEVICE
                )
        );
        assertFalse(rejectingStore.isActive(MERCHANT, DEVICE));
    }

    @Test
    void revocation_is_terminal_for_one_device_authorisation_identity() {
        MerchantOperationalDeviceAuthorisation authorisation =
                MerchantOperationalDeviceAuthorisation.authorised(
                        "device-auth-1",
                        MERCHANT,
                        DEVICE.bindingReference(),
                        CONTROLLER.identifier(),
                        NOW
                );

        assertTrue(authorisation.isActive());
        authorisation.revoke(NOW.plusSeconds(60));

        assertFalse(authorisation.isActive());
        assertEquals(
                MerchantOperationalDeviceAuthorisationLifecycle.REVOKED,
                authorisation.lifecycle()
        );
        assertThrows(
                IllegalStateException.class,
                () -> authorisation.revoke(NOW.plusSeconds(120))
        );
    }

    private static final class RejectingStore
            implements MerchantOperationalDeviceAuthorisationStore {

        @Override
        public MerchantOperationalDeviceAuthorisation establishIfCurrentController(
                String logicalRequestIdentity,
                MerchantScope merchantScope,
                ExecutionPrincipal controllerPrincipal,
                TrustedDeviceApplicationContext deviceContext,
                String authorisationIdentifier,
                Instant authorisedAt
        ) {
            throw new AuthorizationException(
                    "Current Merchant Controller authority is required"
            );
        }

        @Override
        public boolean revokeIfCurrentController(
                MerchantScope merchantScope,
                ExecutionPrincipal controllerPrincipal,
                String authorisationIdentifier,
                Instant revokedAt
        ) {
            throw new AuthorizationException(
                    "Current Merchant Controller authority is required"
            );
        }

        @Override
        public Optional<MerchantOperationalDeviceAuthorisation> find(
                String authorisationIdentifier
        ) {
            return Optional.empty();
        }

        @Override
        public boolean isActive(
                MerchantScope merchantScope,
                TrustedDeviceApplicationContext deviceContext
        ) {
            return false;
        }
    }
}
