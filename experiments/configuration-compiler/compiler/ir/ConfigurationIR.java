package mainstreet.compiler.ir;

import java.util.List;

public record ConfigurationIR(
        String merchantId,
        List<ResourceIR> resources,
        List<OperationIR> operations
) {
    public ConfigurationIR {
        resources = List.copyOf(resources);
        operations = List.copyOf(operations);
    }
}