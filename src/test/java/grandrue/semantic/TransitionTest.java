package grandrue.semantic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransitionTest {

    @Test
    void transition_connects_states_of_same_resource() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition transition =
                new Transition(
                        inactive,
                        active
                );

        assertEquals(
                inactive,
                transition.source()
        );

        assertEquals(
                active,
                transition.target()
        );
    }

    @Test
    void transition_cannot_cross_resources() {

        Resource item =
                new Resource("item");

        Resource order =
                new Resource("order");

        State inactive =
                item.defineState("inactive");

        State created =
                order.defineState("created");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Transition(
                        inactive,
                        created
                )
        );
    }

    @Test
    void transition_rejects_same_source_and_target_state() {

        Resource item =
                new Resource("item");

        State active =
                item.defineState("active");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Transition(
                        active,
                        active
                )
        );
    }

    @Test
    void transition_rejects_null_source() {

        Resource item =
                new Resource("item");

        State active =
                item.defineState("active");

        assertThrows(
                NullPointerException.class,
                () -> new Transition(
                            null,
                            active
                    )
        );
    }

    @Test
    void transition_rejects_null_target() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertThrows(
                NullPointerException.class,
            () -> new Transition(
                        inactive,
                        null
                )
        );
    }
}
