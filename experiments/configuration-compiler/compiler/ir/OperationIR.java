package mainstreet.compiler.ir;

public record OperationIR(
        String identifier,
        String targetResource,
        TransitionIR transition,
        String requiredPrivilege
) {
}