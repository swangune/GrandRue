package mainstreet.configuration;

import java.util.Objects;

public record TransitionConfiguration(
        String source,
        String target
) {
    public TransitionConfiguration {
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
    }
}