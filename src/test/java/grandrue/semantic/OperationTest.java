package grandrue.semantic;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperationTest {

    @Test
    void operation_constructor_is_not_public()
            throws NoSuchMethodException {

        int modifiers =
                Operation.class
                        .getDeclaredConstructor(
                                String.class,
                                Resource.class,
                                Transition.class,
                                Privilege.class
                        )
                        .getModifiers();

        assertFalse(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isProtected(modifiers));
        assertFalse(Modifier.isPrivate(modifiers));
    }

    @Test
    void operation_references_resource_transition_and_privilege() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition activation =
                new Transition(
                        inactive,
                        active
                );

        Privilege privilege =
                new Privilege("item.activate");

        Operation operation =
                new Operation(
                        "item.activate",
                        item,
                        activation,
                        privilege
                );

        assertEquals(
                "item.activate",
                operation.identifier()
        );

        assertEquals(
                item,
                operation.target()
        );

        assertEquals(
                activation,
                operation.transition()
        );

        assertEquals(
                privilege,
                operation.requiredPrivilege()
        );
    }

    @Test
    void operation_rejects_blank_identifier() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition activation =
                new Transition(
                        inactive,
                        active
                );

        Privilege privilege =
                new Privilege("item.activate");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Operation(
                        "",
                        item,
                        activation,
                        privilege
                )
        );
    }

    @Test
    void operation_rejects_null_target() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition activation =
                new Transition(
                        inactive,
                        active
                );

        Privilege privilege =
                new Privilege("item.activate");

        assertThrows(
                NullPointerException.class,
                () -> new Operation(
                        "item.activate",
                        null,
                        activation,
                        privilege
                )
        );
    }

    @Test
    void operation_rejects_null_transition() {

        Resource item =
                new Resource("item");

        Privilege privilege =
                new Privilege("item.activate");

        assertThrows(
                NullPointerException.class,
                () -> new Operation(
                        "item.activate",
                        item,
                        null,
                        privilege
                )
        );
    }

    @Test
    void operation_rejects_null_required_privilege() {

        Resource item =
                new Resource("item");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        Transition activation =
                new Transition(
                        inactive,
                        active
                );

        assertThrows(
                NullPointerException.class,
                () -> new Operation(
                        "item.activate",
                        item,
                        activation,
                        null
                )
        );
    }

    @Test
    void operation_rejects_transition_from_different_resource() {

        Resource item =
                new Resource("item");

        Resource order =
                new Resource("order");

        State inactive =
                item.defineState("inactive");

        State active =
                item.defineState("active");

        State created =
                order.defineState("created");

        State completed =
                order.defineState("completed");

        Transition orderCompletion =
                new Transition(
                        created,
                        completed
                );

        Privilege privilege =
                new Privilege("item.activate");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Operation(
                        "item.activate",
                        item,
                        orderCompletion,
                        privilege
                )
        );
    }

}
