# IMP-05-R4B Ordering Impact Assessment Progress — 7 September 2026

**Implementation node:** `IMP-05-R4B — Concrete business-effect and commitment assessments`  
**Status:** `IN_PROGRESS`  
**Tests-first commit:** `2c5f33011893fa2952d1b7d4432c0bf7fffa85ee`  
**Implementation commit:** `6f4f42324af92c8729aab56e30e161960b19aee9`

## Accepted authority

This increment implements only the bounded Ordering capability-membership consequence already determined by:

- composite `MS-PROT-040` — Merchant Configuration Review, Approval, Activation & Change Model, including v1.0 §§20–22 impact analysis/classification and §26 capability deactivation;
- composite `MS-PROT-077` through v1.1 — Order Commitment, Amendment & Lifecycle Model, which owns Order commitment truth, keeps Ordering independently activatable, and preserves historical Order commitment/release boundaries;
- `designs/AUTHORITY-INDEX.md`, which remains authoritative for current accepted-authority navigation.

This evidence document creates no semantic authority.

## Implemented scope

The implementation adds:

- `src/main/java/mainstreet/ordering/OrderingAvailabilityImpactAssessment.java`;
- `src/test/java/mainstreet/ordering/OrderingAvailabilityImpactAssessmentTest.java`.

The assessment compares Ordering membership in:

```text
exact compiled historical base configuration
versus
resolved candidate executable semantic model
```

rather than comparing direct selected capability identifiers.

When Ordering becomes active, the assessment records the bounded consequential effect:

```text
New orders can be accepted through Main Street where applicable ordering requirements are satisfied.
```

When Ordering becomes inactive, it records:

```text
New orders will no longer be accepted through Main Street.
```

No finding is emitted when compiled Ordering membership is unchanged.

## Semantic ownership implemented

This slice owns only configuration-impact evidence for whether **new Ordering activity** becomes newly applicable or ceases to be applicable.

It deliberately preserves the distinction between:

```text
Ordering capability membership
    ≠
current Order commitment
    ≠
remaining Order commitment
    ≠
Order Fulfilment satisfaction
    ≠
merchant order policy
```

The implementation therefore does not turn a capability-membership change into a claim about the fate of existing Orders.

## Negative boundaries

The assessment deliberately does **not** infer any of the following:

1. **Existing Order existence is not outstanding commitment.** Historical Order existence alone does not prove remaining actionable commitment.
2. **Ordering does not own fulfilment satisfaction.** Remaining Order commitment requires composition of Ordering-owned commitment/release facts with Order-Fulfilment-owned satisfied quantity under composite `MS-PROT-077`/`MS-PROT-060`.
3. **Deactivation is not cancellation.** Disabling Ordering for new activity does not cancel, delete, release, refund, fulfil or otherwise reinterpret existing Orders.
4. **Capability membership is not policy meaning.** The slice does not interpret cancellation, amendment, fulfilment, payment or other merchant policy.
5. **Capability membership is not Inventory consequence.** It does not release or establish Inventory Claims or infer stock consequences.
6. **Adjacent capabilities do not imply Ordering.** Publication, Appointment, Booking, Enquiry, Payment or Inventory do not by themselves activate Ordering.
7. **Direct selected identifiers are not sufficient historical evidence.** The assessment recompiles the base configuration under its pinned semantic release so dependency-closure changes remain observable.
8. **Missing historical semantic authority is not treated as disabled membership.** If the base release cannot be resolved, assessment fails closed instead of fabricating an inactive historical state.

## Falsification and counterevidence

The focused test suite contains six tests covering:

- first enablement produces one bounded consequential new-activity effect;
- deactivation produces one bounded consequential new-activity stop effect without claiming cancellation of existing Orders;
- unchanged active membership produces no invented Order-policy or commitment effect;
- unrelated Publication membership does not activate Ordering;
- release-specific dependency closure can independently add or remove Ordering even when direct selections are unchanged;
- a missing historical semantic release fails closed rather than becoming an inactive base.

These counterexamples reject direct-selection comparison, adjacent-capability implication and historical-Order shortcut semantics.

## Tests-first evidence

The tests were committed before production implementation:

```text
2c5f33011893fa2952d1b7d4432c0bf7fffa85ee
Maven Tests #1708
conclusion: FAILURE
```

At that commit the production `OrderingAvailabilityImpactAssessment` did not yet exist.

The minimum production implementation was then added at:

```text
6f4f42324af92c8729aab56e30e161960b19aee9
Maven Tests #1709
conclusion: SUCCESS
```

The successful workflow executed the repository standard full gate:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

under JDK 25 with PostgreSQL 18.

## Remaining `IMP-05-R4B` gaps

This increment closes only Ordering new-activity capability-membership impact.

`IMP-05-R4B` remains `IN_PROGRESS` because accepted owner-backed impact coverage is still incomplete, including at least:

- concrete production policy interpretations where accepted policy owners provide materiality semantics;
- remaining Order commitment / applicable-commitment conflict assessment requiring authoritative composition with Order Fulfilment satisfaction rather than Order existence alone;
- resource-owned effects and other accepted business-semantic owners not yet represented in production impact assessment coverage;
- any remaining cross-capability effects discovered by the complete R4B coverage review.

The current production tree does not yet expose an Order-Fulfilment satisfaction authority suitable for falsely collapsing the remaining-commitment formula into this Ordering slice. That path therefore remains outside this increment rather than being guessed.

No downstream node is unlocked by this checkpoint alone.

## Conclusion

Ordering now contributes deterministic, historical-release-aware configuration-impact evidence for new-activity capability membership without inventing cancellation, residual-commitment, Fulfilment, Inventory or policy semantics.

The slice is conforming within that bounded scope. It does not establish completion of `IMP-05-R4B`.
