package mainstreet.semantic;

public sealed interface AllocationScope
        permits QuantityAllocationScope,
        TimeWindowAllocationScope {
}
