package grandrue.runtime;

import grandrue.application.MerchantScope;
import grandrue.application.StandingFreeEventReactionContract;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisteredScheduledEventReactionExecutionAuthorityTest {
    @Test
    void establishes_scope_bound_non_session_context_only_for_registered_reaction_principal() {
        var principal = new ExecutionPrincipal("scheduled/standing-free");
        var authority = new RegisteredScheduledEventReactionExecutionAuthority(
                Map.of(StandingFreeEventReactionContract.IDENTITY, principal));
        var scope = new MerchantScope("merchant-1");

        TrustedExecutionContext context = authority.establish(
                StandingFreeEventReactionContract.DEFINITION, scope);

        assertEquals(scope, context.merchantScope());
        assertEquals(principal, context.principal());
        assertTrue(context.authentication().isEmpty());
        assertTrue(context.deviceApplicationContext().isEmpty());
    }

    @Test
    void rejects_an_unregistered_reaction_contract() {
        var authority = new RegisteredScheduledEventReactionExecutionAuthority(Map.of());

        assertThrows(IllegalStateException.class, () -> authority.establish(
                StandingFreeEventReactionContract.DEFINITION,
                new MerchantScope("merchant-1")));
    }
}
