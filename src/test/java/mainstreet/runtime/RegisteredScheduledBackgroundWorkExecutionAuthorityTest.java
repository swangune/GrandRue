package mainstreet.runtime;

import mainstreet.application.MerchantScope;
import mainstreet.application.StandingFreeBackgroundWorkContract;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisteredScheduledBackgroundWorkExecutionAuthorityTest {
    private static final MerchantScope SCOPE = new MerchantScope("merchant-1");
    private static final ExecutionPrincipal PRINCIPAL =
            new ExecutionPrincipal("scheduled/standing-free-reconciliation");

    @Test
    void exact_registered_contract_establishes_a_non_session_bounded_principal() {
        var authority = new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of(
                StandingFreeBackgroundWorkContract.IDENTITY, PRINCIPAL));

        TrustedExecutionContext context = authority.establish(
                StandingFreeBackgroundWorkContract.DEFINITION, SCOPE);

        assertEquals(SCOPE, context.merchantScope());
        assertEquals(PRINCIPAL, context.principal());
        assertTrue(context.authentication().isEmpty());
        assertTrue(context.deviceApplicationContext().isEmpty());
    }

    @Test
    void unregistered_contract_has_no_implicit_system_authority() {
        var authority = new RegisteredScheduledBackgroundWorkExecutionAuthority(Map.of());

        assertThrows(IllegalStateException.class, () -> authority.establish(
                StandingFreeBackgroundWorkContract.DEFINITION, SCOPE));
    }
}
