package grandrue.runtime;

import grandrue.semantic.Resource;
import grandrue.semantic.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceInstanceTest {

    @Test
    void resource_instance_retains_identifier() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        ResourceInstance instance =
                new ResourceInstance(
                        "item-001",
                        item,
                        inactive
                );

        assertEquals(
                "item-001",
                instance.identifier()
        );
    }

    @Test
    void resource_instance_retains_resource() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        ResourceInstance instance =
                new ResourceInstance(
                        "item-001",
                        item,
                        inactive
                );

        assertSame(
                item,
                instance.resource()
        );
    }

    @Test
    void resource_instance_starts_in_initial_state() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        ResourceInstance instance =
                new ResourceInstance(
                        "item-001",
                        item,
                        inactive
                );

        assertSame(
                inactive,
                instance.currentState()
        );
    }

    @Test
    void resource_instance_rejects_null_resource() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertThrows(
                NullPointerException.class,
                () -> new ResourceInstance(
                        "item-001",
                        null,
                        inactive
                )
        );
    }

    @Test
    void resource_instance_rejects_null_initial_state() {

        Resource item =
                new Resource("item");

        assertThrows(
                NullPointerException.class,
                () -> new ResourceInstance(
                        "item-001",
                        item,
                        null
                )
        );
    }

    @Test
    void resource_instance_rejects_initial_state_from_different_resource() {

        Resource item =
                new Resource("item");

        Resource order =
                new Resource("order");

        State orderCreated =
                order.defineState("created");

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResourceInstance(
                        "item-001",
                        item,
                        orderCreated
                )
        );
    }

    @Test
    void resource_instance_can_transition_to_valid_state() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        ResourceInstance instance =
                new ResourceInstance(
                        "item-001",
                        item,
                        inactive
                );

        instance.transitionTo(active);

        assertSame(
                active,
                instance.currentState()
        );
    }

    @Test
    void resource_instance_rejects_transition_to_null_state() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        ResourceInstance instance =
                new ResourceInstance(
                        "item-001",
                        item,
                        inactive
                );

        assertThrows(
                NullPointerException.class,
                () -> instance.transitionTo(null)
        );

        assertSame(
                inactive,
                instance.currentState()
        );
    }

    @Test
    void resource_instance_rejects_transition_to_state_from_different_resource() {

        Resource item =
                new Resource("item");

        Resource order =
                new Resource("order");

        State inactive =
                item.defineState("inactive");

        State created =
                order.defineState("created");

        ResourceInstance instance =
                new ResourceInstance(
                        "item-001",
                        item,
                        inactive
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> instance.transitionTo(created)
        );

        assertSame(
                inactive,
                instance.currentState()
        );
    }

    @Test
    void failed_transition_does_not_change_current_state() {

        Resource item =
                new Resource("item");

        Resource order =
                new Resource("order");

        State inactive =
                item.defineState("inactive");

        State created =
                order.defineState("created");

        ResourceInstance instance =
                new ResourceInstance(
                        "item-001",
                        item,
                        inactive
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> instance.transitionTo(created)
        );

        assertEquals(
                inactive,
                instance.currentState()
        );
    }

    @Test
    void resource_instance_identifier_must_not_be_blank() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResourceInstance(
                        "",
                        item,
                        inactive
                )
        );
    }

    @Test
    void resource_instance_identifier_must_not_be_null() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertThrows(
                IllegalArgumentException.class,
                () -> new ResourceInstance(
                        null,
                        item,
                        inactive
                )
        );
    }
}