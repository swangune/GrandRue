package grandrue.runtime;

import grandrue.application.MerchantScope;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TrustedExecutionContextTest {

    @Test
    void attributable_system_execution_does_not_require_a_fake_human_session() {
        MerchantScope scope = new MerchantScope("merchant-a");
        ExecutionPrincipal system = new ExecutionPrincipal("system:scheduled-process");

        TrustedExecutionContext context = new TrustedExecutionContext(
                scope,
                system,
                Optional.empty()
        );

        assertSame(scope, context.merchantScope());
        assertSame(system, context.principal());
        assertTrue(context.authentication().isEmpty());
    }
}
