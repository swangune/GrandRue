package grandrue.semantic;

public final class AllocationConflictException
        extends IllegalStateException {

    private final AllocationClaim conflictingClaim;

    public AllocationConflictException(
            AllocationClaim conflictingClaim
    ) {
        super(
                "Allocation scope conflicts with claim: "
                        + conflictingClaim.identifier()
        );
        this.conflictingClaim = conflictingClaim;
    }

    public AllocationClaim conflictingClaim() {
        return conflictingClaim;
    }
}
