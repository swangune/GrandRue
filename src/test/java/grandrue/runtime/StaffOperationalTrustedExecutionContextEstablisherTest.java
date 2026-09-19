package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.application.TrustedDeviceApplicationContext;
import grandrue.testing.TestSessionRecordStore;
import grandrue.workforce.MerchantMembershipAuthority;
import grandrue.workforce.MerchantOperationalDeviceAuthority;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StaffOperationalTrustedExecutionContextEstablisherTest {

    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");
    private static final TrustedDeviceApplicationContext DEVICE =
            new TrustedDeviceApplicationContext("device-binding-1");
    private static final Instant AUTHENTICATED_AT =
            Instant.parse("2026-08-25T09:00:00Z");
    private static final Instant NOW =
            Instant.parse("2026-08-25T10:00:00Z");
    private static final Instant EXPIRES_AT =
            Instant.parse("2026-08-25T17:00:00Z");

    @Test
    void staff_operational_context_requires_bearer_membership_and_device_authorisation() {
        AtomicBoolean membershipActive = new AtomicBoolean(true);
        AtomicBoolean deviceActive = new AtomicBoolean(true);
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        TestSessionRecordStore sessions = new TestSessionRecordStore();
        sessions.create(record(credential));
        MerchantMembershipAuthority memberships = (scope, identityReference) ->
                membershipActive.get()
                        && scope.equals(MERCHANT)
                        && identityReference.equals("identity-123");
        MerchantOperationalDeviceAuthority devices = (scope, deviceContext) ->
                deviceActive.get()
                        && scope.equals(MERCHANT)
                        && deviceContext.equals(DEVICE);
        ExecutionPrincipal principal = new ExecutionPrincipal("staff-1");
        ScopedExecutionPrincipalResolver principals = (scope, identityReference) -> {
            assertSame(MERCHANT, scope);
            assertEquals("identity-123", identityReference);
            return principal;
        };
        StaffOperationalTrustedExecutionContextEstablisher establisher =
                new StaffOperationalTrustedExecutionContextEstablisher(
                        new SessionCredentialResolver(
                                sessions,
                                identityReference -> "security-generation-1",
                                Clock.fixed(NOW, ZoneOffset.UTC)
                        ),
                        memberships,
                        devices,
                        principals
                );

        TrustedExecutionContext established = establisher.establish(
                MERCHANT,
                credential.value(),
                DEVICE
        );

        assertSame(MERCHANT, established.merchantScope());
        assertSame(principal, established.principal());
        assertEquals(DEVICE, established.deviceApplicationContext().orElseThrow());
        assertEquals(
                "session-1",
                established.authentication().orElseThrow().sessionIdentifier()
        );

        deviceActive.set(false);
        AuthenticationException revokedDevice = assertThrows(
                AuthenticationException.class,
                () -> establisher.establish(MERCHANT, credential.value(), DEVICE)
        );
        assertEquals(
                AuthenticationFailureCategory.PRINCIPAL_ESTABLISHMENT_FAILED,
                revokedDevice.category()
        );

        deviceActive.set(true);
        membershipActive.set(false);
        AuthenticationException suspendedMembership = assertThrows(
                AuthenticationException.class,
                () -> establisher.establish(MERCHANT, credential.value(), DEVICE)
        );
        assertEquals(
                AuthenticationFailureCategory.PRINCIPAL_ESTABLISHMENT_FAILED,
                suspendedMembership.category()
        );
    }

    @Test
    void session_identity_cannot_replace_bearer_and_other_merchant_device_fails() {
        MerchantScope merchantB = new MerchantScope("merchant-b");
        OpaqueSessionCredential credential = OpaqueSessionCredential.generate(
                new SecureRandom()
        );
        TestSessionRecordStore sessions = new TestSessionRecordStore();
        sessions.create(record(credential));
        MerchantMembershipAuthority memberships = (scope, identityReference) -> true;
        MerchantOperationalDeviceAuthority devices = (scope, deviceContext) ->
                scope.equals(MERCHANT);
        StaffOperationalTrustedExecutionContextEstablisher establisher =
                new StaffOperationalTrustedExecutionContextEstablisher(
                        new SessionCredentialResolver(
                                sessions,
                                identityReference -> "security-generation-1",
                                Clock.fixed(NOW, ZoneOffset.UTC)
                        ),
                        memberships,
                        devices,
                        (scope, identityReference) -> new ExecutionPrincipal("staff-1")
                );

        assertThrows(
                AuthenticationException.class,
                () -> establisher.establish(MERCHANT, "session-1", DEVICE)
        );
        assertThrows(
                AuthenticationException.class,
                () -> establisher.establish(merchantB, credential.value(), DEVICE)
        );
    }

    private static SessionRecord record(OpaqueSessionCredential credential) {
        return new SessionRecord(
                "session-1",
                "identity-123",
                credential.verifier(),
                AUTHENTICATED_AT,
                "phishing-resistant",
                "webauthn-passkey",
                EXPIRES_AT,
                AUTHENTICATED_AT,
                "security-generation-1",
                Optional.empty(),
                Optional.empty()
        );
    }
}
