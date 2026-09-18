package grandrue.inventory;

import grandrue.semantic.AllocationResolution;
import grandrue.semantic.AllocationResolutionType;

import java.util.Objects;

public record QuantityFulfilment(
        AllocationResolution resolution,
        InventoryMovement inventoryMovement
) {

    public QuantityFulfilment {
        Objects.requireNonNull(resolution);
        Objects.requireNonNull(inventoryMovement);

        if (resolution.type()
                != AllocationResolutionType.FULFILLED) {
            throw new IllegalArgumentException(
                    "Quantity fulfilment requires a fulfilled resolution"
            );
        }

        if (!resolution.claimIdentifier().equals(
                inventoryMovement.allocationClaimIdentifier()
        )) {
            throw new IllegalArgumentException(
                    "Inventory movement does not belong to resolution"
            );
        }
    }
}
