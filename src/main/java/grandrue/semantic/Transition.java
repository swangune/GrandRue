package grandrue.semantic;

import java.util.Objects;

public final class Transition {

    private final State source;
    private final State target;

    public Transition(State source, State target) {

        this.source = Objects.requireNonNull(source);
        this.target = Objects.requireNonNull(target);

        if (source.resource() != target.resource()) {
            throw new IllegalArgumentException(
                    "Transition states must belong to the same resource"
            );
        }

        if (source == target) {
            throw new IllegalArgumentException(
                    "Transition source and target must differ"
            );
        }
    }

    public State source() {
        return source;
    }

    public State target() {
        return target;
    }
}