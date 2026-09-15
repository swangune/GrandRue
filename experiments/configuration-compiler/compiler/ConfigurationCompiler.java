package mainstreet.compiler;

import mainstreet.compiler.ir.ConfigurationIR;
import mainstreet.configuration.Configuration;

public final class ConfigurationCompiler {

    public ConfigurationIR validate(
            Configuration configuration
    ) {
        // validation
        // resolution
        // normalization

        return ...;
    }

    public ConfigurationCompilerResult compile(
            Configuration configuration
    ) {
        ConfigurationIR ir = validate(configuration);

        // IR → semantic model

        return ...;
    }
}