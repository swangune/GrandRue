package grandrue.semantic;

public sealed interface AllocationScope
        permits QuantityAllocationScope,
        TimeWindowAllocationScope {
}
