package grandrue.semantic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class RequirementDefinitionTest {

    @Test
    void requirement_belongs_to_defining_operation() {

        Operation bookService = operation(
                "service",
                "book_service"
        );

        RequirementDefinition vehicleRegistration =
                bookService.defineRequirement(
                        "vehicle_registration"
                );

        assertSame(
                bookService,
                vehicleRegistration.owner()
        );

        assertSame(
                vehicleRegistration,
                bookService.requirement("vehicle_registration")
        );
    }

    @Test
    void requirement_does_not_float_between_operations() {

        Operation bookService = operation(
                "service",
                "book_service"
        );

        Operation requestViewing = operation(
                "viewing",
                "request_viewing"
        );

        bookService.defineRequirement(
                "vehicle_registration"
        );

        assertNull(
                requestViewing.requirement("vehicle_registration")
        );
    }

    private Operation operation(
            String resourceIdentifier,
            String operationIdentifier
    ) {

        Resource resource = new Resource(resourceIdentifier);
        State requested = resource.defineState("requested");
        State confirmed = resource.defineState("confirmed");

        return resource.defineOperation(
                operationIdentifier,
                new Transition(requested, confirmed),
                new Privilege(operationIdentifier)
        );
    }
}
