package mainstreet.semantic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StateTest {

    @Test
    void state_belongs_to_resource() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertEquals(
                item,
                inactive.resource()
        );
    }

    @Test
    void state_retains_identifier() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertEquals(
                "inactive",
                inactive.identifier()
        );
    }

    @Test
    void state_rejects_null_resource() {

        assertThrows(
                NullPointerException.class,
                () -> new State(null, "inactive")
        );
    }

    @Test
    void state_rejects_null_identifier() {

        Resource item =
                new Resource("item");

        assertThrows(
                NullPointerException.class,
                () -> new State(item, null)
        );
    }
}