package mainstreet.semantic.execution;

/** Raised when an invocation is assigned to a path without proven support. */
public final class UnsupportedExecutableSupportException
        extends IllegalStateException {

    private final String implementationPathIdentifier;
    private final ExecutableSupportRequirement requirement;

    public UnsupportedExecutableSupportException(
            String implementationPathIdentifier,
            ExecutableSupportRequirement requirement
    ) {
        super("Executable path does not support the exact semantic execution "
                + "requirement: " + implementationPathIdentifier);
        this.implementationPathIdentifier = implementationPathIdentifier;
        this.requirement = requirement;
    }

    public String implementationPathIdentifier() {
        return implementationPathIdentifier;
    }

    public ExecutableSupportRequirement requirement() {
        return requirement;
    }
}
