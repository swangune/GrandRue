package mainstreet.ordering;

import java.util.List;
import java.util.Objects;

/** Result of authoritative proposition/commercial prerequisite revalidation. */
public record ResolvedOrderCommitment(
        OrderCommitmentPortion commitmentPortion,
        List<OrderInventoryClaimRequest> requiredInventoryClaims
) {
    public ResolvedOrderCommitment {
        Objects.requireNonNull(commitmentPortion);
        requiredInventoryClaims = List.copyOf(
                Objects.requireNonNull(requiredInventoryClaims)
        );
        for (OrderInventoryClaimRequest claim : requiredInventoryClaims) {
            if (!commitmentPortion.identifier().equals(
                    claim.commitmentPortionIdentifier()
            )) {
                throw new IllegalArgumentException(
                        "Inventory claim requirement belongs to another Order portion"
                );
            }
        }
    }
}
