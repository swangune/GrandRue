package mainstreet.configuration;

import java.util.List;
import java.util.Objects;

public final class Configuration {

    private final String merchantId;
    private final List<ResourceConfiguration> resources;
    private final List<OperationConfiguration> operations;

    public Configuration(
            String merchantId,
            List<ResourceConfiguration> resources,
            List<OperationConfiguration> operations
    ) {
        this.merchantId =
                Objects.requireNonNull(merchantId);

        this.resources =
                List.copyOf(resources);

        this.operations =
                List.copyOf(operations);
    }

    public String merchantId() {
        return merchantId;
    }

    public List<ResourceConfiguration> resources() {
        return resources;
    }

    public List<OperationConfiguration> operations() {
        return operations;
    }
}