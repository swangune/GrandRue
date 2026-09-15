package mainstreet.semantic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourceTest {

    @Test
    void resource_requires_valid_identifier() {

        Resource resource =
                new Resource("item");

        assertEquals(
                "item",
                resource.identifier()
        );
    }

    @Test
    void resource_rejects_blank_identifier() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Resource("")
        );
    }

    @Test
    void resource_rejects_null_identifier() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new Resource(null)
        );
    }

    @Test
    void resource_can_define_state() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertEquals(
                item,
                inactive.resource()
        );

        assertEquals(
                "inactive",
                inactive.identifier()
        );
    }

    @Test
    void resource_rejects_blank_state_identifier() {

        Resource item =
                new Resource("item");

        assertThrows(
                IllegalArgumentException.class,
                () -> item.defineState("")
        );
    }

    @Test
    void resource_rejects_null_state_identifier() {

        Resource item =
                new Resource("item");

        assertThrows(
                IllegalArgumentException.class,
                () -> item.defineState(null)
        );
    }

    @Test
    void resource_rejects_duplicate_state_identifier() {

        Resource item =
                new Resource("item");

        item.defineState("inactive");

        assertThrows(
                IllegalArgumentException.class,
                () -> item.defineState("inactive")
        );
    }

    @Test
    void resource_can_retrieve_state_by_identifier() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        assertEquals(
                inactive,
                item.state("inactive")
        );
    }

    @Test
    void resource_state_collection_is_read_only() {

        Resource item =
                new Resource("item");

        item.defineState("inactive");

        assertThrows(
                UnsupportedOperationException.class,
                () -> item.states().put(
                        "active",
                        new State(item, "active")
                )
        );
    }

    @Test
    void resource_can_define_and_retrieve_operation() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition activation =
                new Transition(inactive, active);

        Privilege privilege =
                new Privilege("item.activate");

        Operation operation =
                item.defineOperation(
                        "item.activate",
                        activation,
                        privilege
                );

        assertEquals(
                operation,
                item.operation("item.activate")
        );
    }

    @Test
    void resource_rejects_duplicate_operation_identifier() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition activation =
                new Transition(inactive, active);

        Privilege privilege =
                new Privilege("item.activate");

        item.defineOperation(
                "item.activate",
                activation,
                privilege
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> item.defineOperation(
                        "item.activate",
                        activation,
                        privilege
                )
        );
    }

    @Test
    void resource_operation_collection_is_read_only() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition activation =
                new Transition(inactive, active);

        Privilege privilege =
                new Privilege("item.activate");

        Operation operation =
                item.defineOperation(
                        "item.activate",
                        activation,
                        privilege
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> item.operations().put("other", operation)
        );
    }

    @Test
    void operation_is_registered_by_identifier() {
        Resource item = new Resource("item");

        State inactive = item.defineState("inactive");
        State active = item.defineState("active");

        Transition activation =
                new Transition(inactive, active);

        Privilege privilege =
                new Privilege("item.activate");

        Operation operation =
                item.defineOperation(
                        "item.activate",
                        activation,
                        privilege
            );

        assertSame(
                operation,
                item.operation("item.activate")
        );
    }

    @Test
    void operation_identifiers_must_be_unique_within_resource() {
        Resource item = new Resource("item");

        State inactive = item.defineState("inactive");
        State active = item.defineState("active");

        Transition activation =
                new Transition(inactive, active);

        Privilege privilege =
                new Privilege("item.activate");

        item.defineOperation(
                "item.activate",
                activation,
                privilege
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> item.defineOperation(
                        "item.activate",
                        activation,
                        privilege
                    )
            );
    }

    @Test
    void exposed_operations_collection_is_read_only() {
        Resource item = new Resource("item");

        State inactive = item.defineState("inactive");
        State active = item.defineState("active");

        Transition activation =
                new Transition(inactive, active);

        Privilege privilege =
                new Privilege("item.activate");

        item.defineOperation(
                "item.activate",
                activation,
                privilege
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> item.operations().clear()
        );

        assertEquals(
                1,
                item.operations().size()
        );
    }

    @Test
    void resources_with_same_identifier_are_distinct() {
        Resource first = new Resource("item");
        Resource second = new Resource("item");

        assertNotSame(first, second);
    }

    @Test
    void states_belong_to_their_exact_resource_instance() {
        Resource first = new Resource("item");
        Resource second = new Resource("item");

        State firstState = first.defineState("active");
        State secondState = second.defineState("active");

        assertNotSame(firstState, secondState);
        assertSame(first, firstState.resource());
        assertSame(second, secondState.resource());
    }

    @Test
    void failed_operation_definition_does_not_register_operation() {

        Resource item = new Resource("item");

        State inactive = item.defineState("inactive");
        State active = item.defineState("active");

        Transition activation =
                new Transition(inactive, active);

        Privilege privilege =
                new Privilege("item.activate");

        assertThrows(
                IllegalArgumentException.class,
                () -> item.defineOperation(
                        "",
                        activation,
                        privilege
                    )
            );
        

        assertEquals(
                0,
                item.operations().size()
        );
    }

    @Test
    void resource_rejects_operation_with_transition_from_different_resource() {

        Resource item = new Resource("item");
        Resource order = new Resource("order");

        State itemInactive = item.defineState("inactive");
        State itemActive = item.defineState("active");

        State orderCreated = order.defineState("created");
        State orderAccepted = order.defineState("accepted");

        Transition orderTransition =
                new Transition(orderCreated, orderAccepted);

        Privilege privilege =
                new Privilege("item.activate");

        assertThrows(
                IllegalArgumentException.class,
                () -> item.defineOperation(
                        "item.activate",
                        orderTransition,
                        privilege
                )
        );

        assertEquals(
                0,
                item.operations().size()
        );
    }

    
}
