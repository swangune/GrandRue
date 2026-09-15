package mainstreet.workforce;

import mainstreet.application.MerchantScope;
import mainstreet.runtime.ExecutionPrincipal;
import mainstreet.semantic.Privilege;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkforceActorAuthorisationAuthorityTest {

    private static final Instant NOW = Instant.parse("2026-08-24T05:00:00Z");
    private static final MerchantScope MERCHANT = new MerchantScope("merchant-a");

    @Test
    void resolves_current_workforce_authority_from_scope_principal_privilege_and_clock() {
        AtomicReference<MerchantScope> capturedScope = new AtomicReference<>();
        AtomicReference<String> capturedIdentity = new AtomicReference<>();
        AtomicReference<Privilege> capturedPrivilege = new AtomicReference<>();
        AtomicReference<Instant> capturedInstant = new AtomicReference<>();

        MerchantWorkforceAuthority workforceAuthority = (scope, identity, privilege, instant) -> {
            capturedScope.set(scope);
            capturedIdentity.set(identity);
            capturedPrivilege.set(privilege);
            capturedInstant.set(instant);
            return true;
        };
        WorkforceActorAuthorisationAuthority authority =
                new WorkforceActorAuthorisationAuthority(
                        workforceAuthority,
                        Clock.fixed(NOW, ZoneOffset.UTC)
                );

        assertTrue(authority.isAuthorised(
                MERCHANT,
                new ExecutionPrincipal("identity-1"),
                new Privilege("booking.create")
        ));
        assertTrue(capturedScope.get().equals(MERCHANT));
        assertTrue(capturedIdentity.get().equals("identity-1"));
        assertTrue(capturedPrivilege.get().equals(new Privilege("booking.create")));
        assertTrue(capturedInstant.get().equals(NOW));
    }

    @Test
    void does_not_invent_authority_when_current_workforce_authority_rejects() {
        MerchantWorkforceAuthority workforceAuthority =
                (scope, identity, privilege, instant) -> false;
        WorkforceActorAuthorisationAuthority authority =
                new WorkforceActorAuthorisationAuthority(
                        workforceAuthority,
                        Clock.fixed(NOW, ZoneOffset.UTC)
                );

        assertFalse(authority.isAuthorised(
                MERCHANT,
                new ExecutionPrincipal("identity-1"),
                new Privilege("booking.create")
        ));
    }
}
