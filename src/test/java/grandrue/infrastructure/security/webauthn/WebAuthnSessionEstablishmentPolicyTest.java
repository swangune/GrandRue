package grandrue.infrastructure.security.webauthn;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WebAuthnSessionEstablishmentPolicyTest {

    @Test
    void controller_session_absolute_lifetime_may_be_twelve_hours_or_shorter() {
        WebAuthnSessionEstablishmentPolicy baseline = assertDoesNotThrow(
                () -> policy(Duration.ofHours(12))
        );
        WebAuthnSessionEstablishmentPolicy shortened = assertDoesNotThrow(
                () -> policy(Duration.ofHours(8))
        );

        assertEquals(Duration.ofHours(12), baseline.absoluteLifetime());
        assertEquals(Duration.ofHours(8), shortened.absoluteLifetime());
    }

    @Test
    void controller_session_absolute_lifetime_cannot_exceed_twelve_hours() {
        IllegalArgumentException failure = assertThrows(
                IllegalArgumentException.class,
                () -> policy(Duration.ofHours(12).plusSeconds(1))
        );

        assertEquals(
                "WebAuthn Controller Session absolute lifetime"
                        + " must not exceed PT12H",
                failure.getMessage()
        );
    }

    private static WebAuthnSessionEstablishmentPolicy policy(
            Duration absoluteLifetime
    ) {
        return new WebAuthnSessionEstablishmentPolicy(
                absoluteLifetime,
                "webauthn-user-verified",
                "webauthn-passkey"
        );
    }
}
