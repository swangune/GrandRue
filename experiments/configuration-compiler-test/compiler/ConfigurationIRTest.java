package mainstreet.compiler;

import mainstreet.compiler.ir.ConfigurationIR;
import mainstreet.configuration.Configuration;
import mainstreet.configuration.OperationConfiguration;
import mainstreet.configuration.ResourceConfiguration;
import mainstreet.configuration.TransitionConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigurationIRTest {

    @Test
    void valid_configuration_produces_ir() {

        Configuration configuration = new Configuration(
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
                                        "active"
                                ),
                                "item.activate"
                        )
                )
        );

        ConfigurationCompiler compiler =
                new ConfigurationCompiler();

        ConfigurationIR ir =
                compiler.validate(configuration);

        assertEquals("M1", ir.merchantId());

        assertEquals(
                "item",
                ir.resources().get(0).identifier()
        );

        assertEquals(
                List.of("inactive", "active"),
                ir.resources().get(0).states()
        );

        assertEquals(
                "item.activate",
                ir.operations().get(0).identifier()
        );

        assertEquals(
                "item",
                ir.operations().get(0).targetResource()
        );

        assertEquals(
                "inactive",
                ir.operations().get(0)
                        .transition()
                        .sourceState()
        );

        assertEquals(
                "active",
                ir.operations().get(0)
                        .transition()
                        .targetState()
        );

        assertEquals(
                "item.activate",
                ir.operations().get(0)
                        .requiredPrivilege()
        );
    }
}