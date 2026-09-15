package mainstreet.compiler.ir;

import java.util.List;

public record ResourceIR(
        String identifier,
        List<String> states
) {
    public ResourceIR {
        states = List.copyOf(states);
    }
}