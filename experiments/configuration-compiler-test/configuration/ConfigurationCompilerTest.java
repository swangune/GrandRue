package mainstreet.configuration;

import mainstreet.semantic.Operation;
import mainstreet.semantic.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationCompilerTest {

    @Test
    void valid_configuration_produces_semantic_graph() {

        Configuration configuration =
                new Configuration(
                        "M1",

                        List.of(
                                new ResourceConfiguration(
                                        "item",
                                        List.of(
                                                "inactive",
                                                "active"
                                        )
                                )
                        ),

                        List.of(
                                new OperationConfiguration(
                                        "item.activate",
                                        "item",
                                        new TransitionConfiguration(
                                                "inactive",
                                                "active"
                                        ),
                                        "item.activate"
                                )
                        )
                );

        ConfigurationCompiler compiler =
                new ConfigurationCompiler();

        ConfigurationCompilerResult result =
                compiler.compile(configuration);

        Resource item =
                result.resource("item");

        assertNotNull(item);
        assertNotNull(item.state("inactive"));
        assertNotNull(item.state("active"));

        Operation operation =
                result.operation("item.activate");

        assertNotNull(operation);
        assertEquals(
                "item",
                operation.target().identifier()
        );

        assertEquals(
                "inactive",
                operation.transition()
                        .source()
                        .identifier()
        );

        assertEquals(
                "active",
                operation.transition()
                        .target()
                        .identifier()
        );
    }
}


@Test
void operation_cannot_reference_unknown_resource() {

    Configuration configuration =
            new Configuration(
                    "M1",
                    List.of(),
                    List.of(
                            new OperationConfiguration(
                                    "item.activate",
                                    "item",
                                    new TransitionConfiguration(
                                            "inactive",
                                            "active"
                                    ),
                                    "item.activate"
                            )
                    )
            );

    ConfigurationCompiler compiler =
            new ConfigurationCompiler();

    assertThrows(
            ConfigurationException.class,
            () -> compiler.compile(configuration)
    );
}


@Test
void transition_cannot_reference_unknown_state() {

    Configuration configuration =
            new Configuration(
                    "M1",
                    List.of(
                            new ResourceConfiguration(
                                    "item",
                                    List.of("inactive", "active")
                            )
                    ),
                    List.of(
                            new OperationConfiguration(
                                    "item.activate",
                                    "item",
                                    new TransitionConfiguration(
                                            "inactive",
                                            "archived"
                                    ),
                                    "item.activate"
                            )
                    )
            );

    ConfigurationCompiler compiler =
            new ConfigurationCompiler();

    assertThrows(
            ConfigurationException.class,
            () -> compiler.compile(configuration)
    );
}


@Test
void duplicate_resource_identifiers_are_rejected() {

    Configuration configuration =
            new Configuration(
                    "M1",
                    List.of(
                            new ResourceConfiguration(
                                    "item",
                                    List.of("inactive")
                            ),
                            new ResourceConfiguration(
                                    "item",
                                    List.of("active")
                            )
                    ),
                    List.of()
            );

    ConfigurationCompiler compiler =
            new ConfigurationCompiler();

    assertThrows(
            ConfigurationException.class,
            () -> compiler.compile(configuration)
    );
}