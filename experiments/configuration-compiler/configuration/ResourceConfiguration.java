package mainstreet.configuration;

import java.util.List;
import java.util.Objects;

public final class ResourceConfiguration {

    private final String identifier;
    private final List<String> states;

    public ResourceConfiguration(
            String identifier,
            List<String> states
    ) {
        this.identifier =
                Objects.requireNonNull(identifier);

        this.states =
                List.copyOf(states);
    }

    public String identifier() {
        return identifier;
    }

    public List<String> states() {
        return states;
    }
}