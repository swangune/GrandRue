package mainstreet.runtime;

import mainstreet.semantic.Privilege;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExecutionPrincipalTest {

    @Test
    void principal_retains_only_its_attributable_identifier() {
        ExecutionPrincipal principal = new ExecutionPrincipal("identity-123");

        assertEquals("identity-123", principal.identifier());
    }

    @Test
    void principal_rejects_null_identifier() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExecutionPrincipal(null)
        );
    }

    @Test
    void principal_rejects_blank_identifier() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ExecutionPrincipal(" ")
        );
    }

    @Test
    void principal_does_not_expose_embedded_privilege_authority() {
        assertThrows(
                NoSuchMethodException.class,
                () -> ExecutionPrincipal.class.getMethod(
                        "hasPrivilege",
                        Privilege.class
                )
        );
    }
}
