package mainstreet.experiments;

import mainstreet.runtime.Actor;
import mainstreet.runtime.OperationRuntime;
import mainstreet.runtime.ResourceInstance;
import mainstreet.semantic.Operation;
import mainstreet.semantic.Privilege;
import mainstreet.semantic.Resource;
import mainstreet.semantic.State;
import mainstreet.semantic.Transition;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GenericityExperimentTest {

    @Test
    void retail_product_behaviour_uses_generic_runtime() {

        Resource product =
                new Resource("product");

        State unavailable =
                product.defineState("unavailable");

        State available =
                product.defineState("available");

        Transition makeAvailable =
                new Transition(
                        unavailable,
                        available
                );

        Privilege privilege =
                new Privilege("product.make_available");

        Operation operation =
                product.defineOperation(
                        "product.make_available",
                        makeAvailable,
                        privilege
                );

        ResourceInstance instance =
                new ResourceInstance(
                        "product-001",
                        product,
                        unavailable
                );

        Actor actor =
                new Actor("staff-001", Set.of(privilege));

        OperationRuntime runtime =
                new OperationRuntime();

        runtime.execute(
                actor,
                operation,
                instance
        );

        assertEquals(
                available,
                instance.currentState()
        );
    }

    @Test
    void food_preparation_behaviour_uses_generic_runtime() {

        Resource meal =
                new Resource("meal");

        State preparing =
                meal.defineState("preparing");

        State ready =
                meal.defineState("ready");

        Transition markReady =
                new Transition(
                        preparing,
                        ready
                );

        Privilege privilege =
                new Privilege("meal.mark_ready");

        Operation operation =
                meal.defineOperation(
                        "meal.mark_ready",
                        markReady,
                        privilege
                );

        ResourceInstance instance =
                new ResourceInstance(
                        "meal-001",
                        meal,
                        preparing
                );

        Actor actor =
                new Actor("staff-001", Set.of(privilege));

        OperationRuntime runtime =
                new OperationRuntime();

        runtime.execute(
                actor,
                operation,
                instance
        );

        assertEquals(
                ready,
                instance.currentState()
        );
    }

    @Test
    void service_job_behaviour_uses_generic_runtime() {

        Resource job =
                new Resource("job");

        State requested =
                job.defineState("requested");

        State accepted =
                job.defineState("accepted");

        Transition accept =
                new Transition(
                        requested,
                        accepted
                );

        Privilege privilege =
                new Privilege("job.accept");

        Operation operation =
                job.defineOperation(
                        "job.accept",
                        accept,
                        privilege
                );

        ResourceInstance instance =
                new ResourceInstance(
                        "job-001",
                        job,
                        requested
                );

        Actor actor =
                new Actor("staff-001", Set.of(privilege));

        OperationRuntime runtime =
                new OperationRuntime();

        runtime.execute(
                actor,
                operation,
                instance
        );

        assertEquals(
                accepted,
                instance.currentState()
        );
    }
}
