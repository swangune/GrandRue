package mainstreet.semantic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PrivilegeTest {

    @Test
    void privilege_requires_valid_identifier() {

        Privilege privilege =
                new Privilege("item.activate");

        assertEquals(
                "item.activate",
                privilege.identifier()
        );
    }

    @Test
    void privilege_rejects_blank_identifier() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Privilege("")
        );
    }

    @Test
    void privilege_rejects_null_identifier() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Privilege(null)
        );
    }

    @Test
    void privileges_with_same_identifier_are_equal() {

        Privilege first =
                new Privilege("item.activate");

        Privilege second =
                new Privilege("item.activate");

        assertEquals(
                first,
                second
        );

        assertEquals(
                first.hashCode(),
                second.hashCode()
        );
    }

    @Test
    void privileges_with_different_identifiers_are_not_equal() {

        Privilege activate =
                new Privilege("item.activate");

        Privilege delete =
                new Privilege("item.delete");

        assertNotEquals(
                activate,
                delete
        );
    }

    @Test
    void privileges_with_same_identifier_behave_as_one_set_value() {

        Privilege first =
                new Privilege("item.activate");

        Privilege second =
                new Privilege("item.activate");

        java.util.Set<Privilege> privileges =
                new java.util.HashSet<>();

        privileges.add(first);
        privileges.add(second);

        assertEquals(
                1,
                privileges.size()
        );
    }
}