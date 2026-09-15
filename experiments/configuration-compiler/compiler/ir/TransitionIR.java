package mainstreet.compiler.ir;

public record TransitionIR(
        String sourceState,
        String targetState
) {
}