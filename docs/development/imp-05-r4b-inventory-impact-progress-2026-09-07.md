# IMP-05-R4B Inventory Impact Assessment Progress — 7 September 2026

**Implementation node:** `IMP-05-R4B — Concrete business-effect and commitment assessments`  
**Status:** `IN_PROGRESS`  
**Tests-first commit:** `6dc3498b6020d93fcd3e7040a12e92d40b8b424b`  
**Implementation commit:** `6a4f24cfdb05e7856550529bbe1c6e585e7d7055`

## Accepted authority

This increment implements only the bounded Inventory capability-membership consequence already determined by:

- composite `MS-PROT-040` — Merchant Configuration Review, Approval, Activation & Change Model, including impact analysis, existing-commitment protection and capability deactivation;
- composite `MS-PROT-058` through v1.1 — Inventory Authority, Stock Claims & Availability Model plus Inventory Position, Mutation & Quantity Claim Execution Contract Amendment, which owns Inventory position/claim/movement truth and keeps Inventory optional and independently owned;
- `designs/AUTHORITY-INDEX.md`, which remains authoritative for current accepted-authority navigation.

This evidence document creates no semantic authority.

## Implemented scope

The implementation adds:

- `src/main/java/mainstreet/inventory/InventoryAvailabilityImpactAssessment.java`;
- `src/test/java/mainstreet/inventory/InventoryAvailabilityImpactAssessmentTest.java`.

The assessment compares Inventory membership in:

```text
exact compiled historical base configuration
versus
resolved candidate executable semantic model
```

rather than comparing direct selected capability identifiers.

When Inventory becomes active, the assessment records the bounded consequential effect:

```text
New inventory-tracked activity can use Main Street Inventory where applicable inventory requirements are satisfied.
```

When Inventory becomes inactive, it records:

```text
New inventory-tracked activity will no longer be initiated through Main Street Inventory.
```

No finding is emitted when compiled Inventory membership is unchanged.

## Semantic ownership implemented

This slice owns only configuration-impact evidence for whether **new Inventory-tracked activity** becomes newly applicable or ceases to be applicable.

It deliberately preserves the distinction between:

```text
Inventory capability membership
    ≠
Inventory Position
    ≠
Inventory Claim
    ≠
claim resolution/release
    ≠
Inventory Movement
    ≠
catalogue sellability or resource availability owned elsewhere
```

The implementation therefore does not turn a capability-membership change into a claim about the fate of existing Inventory truth.

## Negative boundaries

The assessment deliberately does **not** infer any of the following:

1. **Enablement is not stock creation.** Activating Inventory does not create stock positions, claims, resolutions or movements.
2. **Deactivation is not abandonment.** Disabling Inventory for new activity does not delete, release, cancel, rewrite or otherwise reinterpret existing stock positions, claims, resolutions or movements.
3. **Inventory does not own catalogue sellability.** This slice does not infer that a catalogue item becomes sellable/unsellable or that any resource becomes available/unavailable.
4. **Capability membership is not quantity truth.** The assessment does not infer on-hand, reserved, available or other stock quantities.
5. **Adjacent capabilities do not imply Inventory.** Publication or other unrelated capability membership does not by itself activate Inventory.
6. **Direct selected identifiers are not sufficient historical evidence.** The assessment recompiles the base configuration under its pinned semantic release so dependency-closure changes remain observable.
7. **Missing historical semantic authority is not treated as disabled membership.** If the base release cannot be resolved, assessment fails closed instead of fabricating an inactive historical state.

## Falsification and counterevidence

The focused test suite contains six tests covering:

- first enablement produces one bounded consequential new-activity effect without claiming stock/claim creation;
- deactivation produces one bounded consequential new-activity stop effect without abandoning existing Inventory truth;
- unchanged active membership produces no invented stock-position, claim or movement effects;
- unrelated Publication membership does not activate Inventory;
- release-specific dependency closure can independently add or remove Inventory even when direct selections are unchanged;
- a missing historical semantic release fails closed rather than becoming an inactive base.

These counterexamples reject direct-selection comparison, adjacent-capability implication and capability-membership-as-Inventory-truth shortcuts.

## Tests-first evidence

The tests were committed before production implementation:

```text
6dc3498b6020d93fcd3e7040a12e92d40b8b424b
Maven Tests #1713
run 34166823879
conclusion: FAILURE
```

At that commit the production `InventoryAvailabilityImpactAssessment` did not yet exist. The Maven/PostgreSQL gate failed at test compilation with the expected missing-symbol errors for that class rather than an unrelated infrastructure failure.

The minimum production implementation was then added at:

```text
6a4f24cfdb05e7856550529bbe1c6e585e7d7055
Maven Tests #1714
run 34166833217
conclusion: SUCCESS
```

The successful workflow executed the repository standard full gate:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

under JDK 25 with PostgreSQL.

## Remaining `IMP-05-R4B` gaps

This increment closes only Inventory new-activity capability-membership impact.

`IMP-05-R4B` remains `IN_PROGRESS` because accepted owner-backed impact coverage is still incomplete, including at least:

- concrete production policy interpretations where accepted policy owners provide materiality semantics;
- remaining owner-backed commitment-conflict assessment;
- catalogue sellability/resource-owned consequences that cannot be inferred from Inventory capability membership alone;
- other accepted business-semantic owners not yet represented in production impact assessment coverage;
- any remaining cross-capability effects discovered by the complete R4B coverage review.

No downstream node is unlocked by this checkpoint alone.

## Conclusion

Inventory now contributes deterministic, historical-release-aware configuration-impact evidence for new-activity capability membership without inventing stock, claim, movement, catalogue or resource semantics.

The slice is conforming within that bounded scope. It does not establish completion of `IMP-05-R4B`.
