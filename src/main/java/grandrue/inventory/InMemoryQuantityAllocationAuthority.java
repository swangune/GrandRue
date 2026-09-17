package grandrue.inventory;

import mainstreet.semantic.AllocationAuthority;
import mainstreet.semantic.AllocationClaim;
import mainstreet.semantic.AllocationResolution;
import mainstreet.semantic.AllocationResolutionType;
import mainstreet.semantic.AllocationScope;
import mainstreet.semantic.QuantityAllocationScope;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class InMemoryQuantityAllocationAuthority
        implements AllocationAuthority {

    private State state;

    public InMemoryQuantityAllocationAuthority(
            Map<String, Long> initialStockOnHand
    ) {
        Objects.requireNonNull(initialStockOnHand);

        Map<String, Long> validatedStock =
                new LinkedHashMap<>();

        initialStockOnHand.forEach((identifier, quantity) -> {
            requireIdentifier(identifier, "Stock subject identifier");
            Objects.requireNonNull(quantity);

            if (quantity < 0) {
                throw new IllegalArgumentException(
                        "Stock on hand must not be negative"
                );
            }

            validatedStock.put(identifier, quantity);
        });

        state = State.initial(validatedStock);
    }

    @Override
    public synchronized AllocationClaim claim(
            String identifier,
            AllocationScope scope,
            String useIdentifier,
            Instant claimedAt
    ) {
        QuantityAllocationScope quantityScope =
                requireQuantityScope(scope);

        if (state.claims().containsKey(identifier)) {
            throw new IllegalArgumentException(
                    "Allocation claim identifier already used: "
                            + identifier
            );
        }

        long available = availableToPromise(
                quantityScope.subjectIdentifier()
        );

        if (quantityScope.quantity() > available) {
            throw new InsufficientQuantityException(
                    quantityScope.subjectIdentifier(),
                    quantityScope.quantity(),
                    available
            );
        }

        AllocationClaim claim = new AllocationClaim(
                identifier,
                quantityScope,
                useIdentifier,
                claimedAt
        );

        state = state.withClaim(claim);
        return claim;
    }

    public synchronized long stockOnHand(
            String subjectIdentifier
    ) {
        return requiredStock(subjectIdentifier);
    }

    public synchronized long availableToPromise(
            String subjectIdentifier
    ) {
        long stock = requiredStock(subjectIdentifier);
        long claimed = state.claims().values().stream()
                .filter(claim -> !state.resolutionsByClaim()
                        .containsKey(claim.identifier()))
                .map(AllocationClaim::scope)
                .map(QuantityAllocationScope.class::cast)
                .filter(scope -> scope.subjectIdentifier().equals(
                        subjectIdentifier
                ))
                .mapToLong(QuantityAllocationScope::quantity)
                .reduce(0L, Math::addExact);

        return Math.subtractExact(stock, claimed);
    }

    public synchronized Optional<AllocationClaim> claim(
            String identifier
    ) {
        return Optional.ofNullable(state.claims().get(identifier));
    }

    public synchronized AllocationResolution release(
            String resolutionIdentifier,
            String claimIdentifier,
            Instant releasedAt
    ) {
        return resolve(
                resolutionIdentifier,
                claimIdentifier,
                AllocationResolutionType.RELEASED,
                releasedAt
        );
    }

    public synchronized AllocationResolution expire(
            String resolutionIdentifier,
            String claimIdentifier,
            Instant expiredAt
    ) {
        return resolve(
                resolutionIdentifier,
                claimIdentifier,
                AllocationResolutionType.EXPIRED,
                expiredAt
        );
    }

    public synchronized Optional<AllocationResolution> resolution(
            String claimIdentifier
    ) {
        return Optional.ofNullable(
                state.resolutionsByClaim().get(claimIdentifier)
        );
    }

    public synchronized QuantityFulfilment fulfil(
            String resolutionIdentifier,
            String movementIdentifier,
            String claimIdentifier,
            Instant fulfilledAt
    ) {
        AllocationClaim claim = requiredActiveClaim(claimIdentifier);
        AllocationResolution resolution = prepareResolution(
                resolutionIdentifier,
                claim,
                AllocationResolutionType.FULFILLED,
                fulfilledAt
        );

        if (state.inventoryMovements().containsKey(
                movementIdentifier
        )) {
            throw new IllegalArgumentException(
                    "Inventory movement identifier already used: "
                            + movementIdentifier
            );
        }

        QuantityAllocationScope scope =
                requireQuantityScope(claim.scope());
        long remainingStock = Math.subtractExact(
                requiredStock(scope.subjectIdentifier()),
                scope.quantity()
        );

        if (remainingStock < 0) {
            throw new IllegalStateException(
                    "Fulfilment would make stock on hand negative"
            );
        }

        InventoryMovement movement = new InventoryMovement(
                movementIdentifier,
                scope.subjectIdentifier(),
                scope.quantity(),
                claimIdentifier,
                fulfilledAt
        );
        QuantityFulfilment fulfilment = new QuantityFulfilment(
                resolution,
                movement
        );

        state = state.withFulfilment(
                scope.subjectIdentifier(),
                remainingStock,
                resolution,
                movement
        );
        return fulfilment;
    }

    public synchronized Optional<InventoryMovement> inventoryMovement(
            String identifier
    ) {
        return Optional.ofNullable(
                state.inventoryMovements().get(identifier)
        );
    }

    private AllocationResolution resolve(
            String resolutionIdentifier,
            String claimIdentifier,
            AllocationResolutionType type,
            Instant occurredAt
    ) {
        AllocationClaim claim = requiredActiveClaim(claimIdentifier);
        AllocationResolution resolution = prepareResolution(
                resolutionIdentifier,
                claim,
                type,
                occurredAt
        );

        state = state.withResolution(resolution);
        return resolution;
    }

    private AllocationClaim requiredActiveClaim(
            String claimIdentifier
    ) {
        AllocationClaim claim = state.claims().get(claimIdentifier);

        if (claim == null) {
            throw new IllegalArgumentException(
                    "Unknown allocation claim: " + claimIdentifier
            );
        }

        if (state.resolutionsByClaim().containsKey(claimIdentifier)) {
            throw new IllegalStateException(
                    "Allocation claim already resolved: "
                            + claimIdentifier
            );
        }

        return claim;
    }

    private AllocationResolution prepareResolution(
            String resolutionIdentifier,
            AllocationClaim claim,
            AllocationResolutionType type,
            Instant occurredAt
    ) {
        if (state.resolutions().containsKey(resolutionIdentifier)) {
            throw new IllegalArgumentException(
                    "Allocation resolution identifier already used: "
                            + resolutionIdentifier
            );
        }

        AllocationResolution resolution =
                new AllocationResolution(
                        resolutionIdentifier,
                        claim.identifier(),
                        type,
                        occurredAt
                );

        if (occurredAt.isBefore(claim.claimedAt())) {
            throw new IllegalArgumentException(
                    "Allocation resolution cannot precede its claim"
            );
        }

        return resolution;
    }

    private long requiredStock(String subjectIdentifier) {
        requireIdentifier(
                subjectIdentifier,
                "Stock subject identifier"
        );

        Long stock = state.stockOnHand().get(subjectIdentifier);

        if (stock == null) {
            throw new IllegalArgumentException(
                    "Unknown stock subject: " + subjectIdentifier
            );
        }

        return stock;
    }

    private static QuantityAllocationScope requireQuantityScope(
            AllocationScope scope
    ) {
        Objects.requireNonNull(scope);

        if (!(scope instanceof QuantityAllocationScope quantityScope)) {
            throw new IllegalArgumentException(
                    "Quantity allocation authority requires a quantity scope"
            );
        }

        return quantityScope;
    }

    private static void requireIdentifier(
            String identifier,
            String label
    ) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException(
                    label + " must not be blank"
            );
        }
    }

    private record State(
            Map<String, Long> stockOnHand,
            Map<String, AllocationClaim> claims,
            Map<String, AllocationResolution> resolutions,
            Map<String, AllocationResolution> resolutionsByClaim,
            Map<String, InventoryMovement> inventoryMovements
    ) {

        private static State initial(
                Map<String, Long> stockOnHand
        ) {
            return new State(
                    Map.copyOf(stockOnHand),
                    Map.of(),
                    Map.of(),
                    Map.of(),
                    Map.of()
            );
        }

        private State withClaim(AllocationClaim claim) {
            Map<String, AllocationClaim> updatedClaims =
                    new LinkedHashMap<>(claims);
            updatedClaims.put(claim.identifier(), claim);

            return new State(
                    stockOnHand,
                    Map.copyOf(updatedClaims),
                    resolutions,
                    resolutionsByClaim,
                    inventoryMovements
            );
        }

        private State withResolution(
                AllocationResolution resolution
        ) {
            Map<String, AllocationResolution> updatedResolutions =
                    new LinkedHashMap<>(resolutions);
            Map<String, AllocationResolution> updatedByClaim =
                    new LinkedHashMap<>(resolutionsByClaim);

            updatedResolutions.put(
                    resolution.identifier(),
                    resolution
            );
            updatedByClaim.put(
                    resolution.claimIdentifier(),
                    resolution
            );

            return new State(
                    stockOnHand,
                    claims,
                    Map.copyOf(updatedResolutions),
                    Map.copyOf(updatedByClaim),
                    inventoryMovements
            );
        }

        private State withFulfilment(
                String subjectIdentifier,
                long remainingStock,
                AllocationResolution resolution,
                InventoryMovement movement
        ) {
            Map<String, Long> updatedStock =
                    new LinkedHashMap<>(stockOnHand);
            Map<String, AllocationResolution> updatedResolutions =
                    new LinkedHashMap<>(resolutions);
            Map<String, AllocationResolution> updatedByClaim =
                    new LinkedHashMap<>(resolutionsByClaim);
            Map<String, InventoryMovement> updatedMovements =
                    new LinkedHashMap<>(inventoryMovements);

            updatedStock.put(subjectIdentifier, remainingStock);
            updatedResolutions.put(
                    resolution.identifier(),
                    resolution
            );
            updatedByClaim.put(
                    resolution.claimIdentifier(),
                    resolution
            );
            updatedMovements.put(movement.identifier(), movement);

            return new State(
                    Map.copyOf(updatedStock),
                    claims,
                    Map.copyOf(updatedResolutions),
                    Map.copyOf(updatedByClaim),
                    Map.copyOf(updatedMovements)
            );
        }
    }
}
