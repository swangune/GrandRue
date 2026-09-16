# MS-PROT-058 v1.1 — Inventory Position, Mutation & Quantity Claim Execution Contract Amendment

**Document ID:** MS-PROT-058  
**Version:** 1.1  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Approved:** Manual approval on 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-058 v1.0  
**Supersedes:** ADR-007 only where ADR-007 assumes that release, expiry or fulfilment necessarily resolves an entire Quantity Allocation Claim in one step  
**Depends on:** ADR-007; MS-PROT-020; MS-PROT-023; MS-PROT-025; MS-PROT-027 v1.3–v1.5; MS-PROT-044 v1.1; MS-PROT-058 v1.0; MS-PROT-059; MS-PROT-062; MS-PROT-069; MS-PROT-072; MS-PROT-077  
**Purpose:** Complete the production Inventory mutation and quantity-claim contract required by MS-PROT-079 Target 12 without transferring stock authority to Ordering or introducing a universal commerce/availability engine.

---

## 1. Governing Decision

Inventory remains the sole authority for:

```text
stock position
Inventory Claims
claim resolutions
stock movement provenance
available-to-promise derivation
```

Production Inventory SHALL expose explicit authoritative operations for:

```text
establishing a stock position
adjusting a stock position
transferring stock
establishing an Inventory Claim
partially or fully releasing a claim
expiring remaining claim quantity
partially or fully fulfilling a claim
```

Hard invariant:

> **Original Inventory Claim quantity is immutable. Changes to the quantity still reserved are represented by immutable resolution facts, not by destructive mutation of the original claim.**

---

## 2. Scope

This amendment governs:

- stock-position establishment;
- absence versus known-zero stock;
- authoritative stock adjustments;
- transfer between Inventory scopes;
- quantity claim admission;
- partial claim resolution;
- release;
- expiry;
- fulfilment-driven stock consumption;
- Inventory concurrency;
- retry/idempotency;
- stock correction when active claims exist;
- available-to-promise reads; and
- Target-12 Projection/Exposure classification.

It does not redefine Order, Fulfilment, Return or Payment ownership.

---

## 3. Surviving MS-PROT-058 Authority

The following remain unchanged:

```text
Inventory is optional

Inventory follows actual stock-bearing identity

orderable ≠ inventory tracked

stock position ≠ availability

Inventory owns stock truth

Ordering owns Order truth

displayed availability creates no reservation

claims prevent incompatible commitments

stock movements preserve provenance

returns do not automatically restore sellable stock

transfers preserve both source and destination consequences

quantity/unit semantics are not restricted to integer counts
```

MS-PROT-058 v1.0 remains governing except where this amendment is explicitly more precise.

---

## 4. Inventory Stock Position Identity

An authoritative Inventory Stock Position is identified by:

```text
MerchantScope
+
stock-bearing subject identity
+
applicable Inventory scope
+
all additional registered stock-partition dimensions
required by the active Inventory semantics
```

Examples of Inventory scope may include merchant location, stockroom or warehouse.

The exact Java key or relational primary key remains implementation scope.

Two positions MUST NOT be merged merely because they contain the same stock-bearing subject.

---

## 5. Position Absence Is Not Known Zero

Main Street SHALL distinguish:

```text
POSITION ESTABLISHED
on-hand = 0
```

from:

```text
NO POSITION ESTABLISHED
```

Therefore:

```text
missing Inventory position
    ≠ known zero stock
```

If Inventory is not applicable to the subject, no Inventory position is required.

If Inventory **is** applicable to the exact stock scope but its position has not been established, Inventory-dependent mutation/read operations MUST fail as:

```text
INVENTORY_POSITION_NOT_ESTABLISHED
```

or an equivalent semantic outcome.

They MUST NOT silently invent:

```text
stock = 0
```

The current PostgreSQL adapter's `missing row → 0` behaviour is therefore implementation evidence requiring correction, not semantic authority.

---

## 6. Known Zero

An explicitly established Stock Position whose authoritative quantity is zero represents known zero stock.

That position may legitimately produce:

```text
available-to-promise = 0
```

This is materially different from an unestablished position.

---

## 7. Inventory Claim Quantity Model

An Inventory Claim retains immutable:

```text
claim identity
MerchantScope
stock-position/scope identity
original claimed quantity + unit semantics
use/commitment reference
claim provenance
claimedAt
```

The original claimed quantity MUST NOT be reduced in place.

---

## 8. Inventory Claim Resolution

A Claim may accumulate zero or more immutable **Inventory Claim Resolution** facts.

Each resolution identifies:

```text
resolution identity
claim identity
resolution type
resolved quantity
quantity/unit semantics
occurredAt
causation/provenance
```

Initial resolution types remain:

```text
RELEASED
EXPIRED
FULFILLED
```

No universal claim-status field is required.

---

## 9. Remaining Claimed Quantity

For one claim:

```text
remaining claim quantity
=
original claim quantity
-
sum(RELEASED quantity)
-
sum(EXPIRED quantity)
-
sum(FULFILLED quantity)
```

Each resolution quantity MUST be:

```text
> 0
and
<= currently remaining claim quantity
```

A Claim is fully resolved only when:

```text
remaining claim quantity = 0
```

This replaces ADR-007's assumption that the first release/expiry/fulfilment necessarily closes the entire claim.

---

## 10. Available-to-Promise

For one exact Inventory position:

```text
uncommitted stock balance
=
stock on hand
-
sum(all remaining active claim quantities)
```

For admission of a new claim:

```text
available-to-promise
=
max(0, uncommitted stock balance)
```

This means physical stock correction may reveal:

```text
stock on hand < active claim quantity
```

without Inventory falsifying either fact.

New incompatible claims MUST then be rejected.

---

## 11. `EstablishInventoryPosition`

Canonical operation:

```text
inventory.position.establish
```

**Owner:** Inventory.

**Preconditions:** Inventory is applicable to the exact subject/scope; no position already exists.

**Inputs:** MerchantScope, stock-bearing subject, Inventory scope, opening quantity/unit semantics, logical command identity and authorised provenance.

**Mutation:** Establish one authoritative stock position and its opening-balance provenance.

**Atomicity:** Position and opening provenance commit together.

**Success:** One position exists with the supplied authoritative opening quantity.

**Rejection:** Existing position, invalid scope, unsupported units or invalid negative quantity.

**Retry:** Same logical command and same intent reconciles to the existing establishment. Reuse of the same command identity for materially different intent is a conflict.

No Domain Event is universally mandated merely because a position was established.

---

## 12. `AdjustInventoryPosition`

Canonical operation:

```text
inventory.position.adjust
```

Two modes are permitted.

### DELTA

Records an authoritative increase or decrease:

```text
current quantity
+
signed registered adjustment
=
new quantity
```

The adjustment MUST preserve:

```text
quantity
unit
cause
provenance
occurredAt
logical command identity
```

### CORRECT_TO

Records that an authorised physical/operational observation establishes:

```text
actual on-hand quantity = Q
```

A correction MUST preserve:

```text
previous authoritative position
corrected quantity
correction provenance
observation/concurrency basis
```

It MUST NOT be implemented as an unexplained destructive overwrite.

---

## 13. Inventory Adjustment Cause

Every adjustment MUST use a registered Inventory cause whose semantics determine why the authoritative position changed.

Initial accepted cause families include:

```text
RECEIPT
INTERNAL_CONSUMPTION
DAMAGE
WASTE
CORRECTION
TRANSFER
FULFILMENT
```

`RETURN` remains subject to Target-15 return/re-entry semantics before it can automatically restore sellable stock.

Cause representation may be extensible registered semantics rather than one frozen Java enum.

---

## 14. Active-Claim Protection During Adjustment

A planned discretionary decrease, such as internal consumption, MUST NOT knowingly consume quantity already protected by active claims.

Therefore it must preserve:

```text
resulting stock on hand
>=
aggregate remaining active claim quantity
```

unless separately accepted semantics explicitly permit otherwise.

However a correction, damage or waste observation MAY reveal that physical stock has already fallen below active claims.

When that happens:

```text
current stock truth remains current stock truth
active claims remain active claim truth
```

Inventory MUST NOT silently release claims or falsify the physical count merely to make the arithmetic comfortable.

The resulting shortfall requires operational reconciliation but does not create another Inventory authority.

---

## 15. `TransferInventory`

Canonical operation:

```text
inventory.transfer
```

**Owner:** Inventory.

**Inputs:** MerchantScope, exact source position, exact destination position, quantity/unit, command identity and transfer provenance.

Both positions MUST already exist and belong to compatible stock-bearing semantics.

The operation atomically establishes:

```text
source movement -Q
+
destination movement +Q
+
shared transfer provenance
```

Source and destination MUST NOT be committed independently.

A normal planned transfer MUST preserve active source claims.

Therefore stock protected by a source-scoped claim cannot simply be transferred away.

Future in-transit stock semantics remain deferred.

---

## 16. `ClaimInventory`

Canonical operation:

```text
inventory.claim
```

This operation implements ADR-007's authoritative reservation semantics.

**Owner:** Inventory.

**Requester:** An accepted capability/application operation with authority to establish the referenced use.

**Inputs:** claim identity, exact Inventory position/scope, quantity/unit, owning-use reference, logical invocation identity.

**Authoritative reads:**

```text
position existence
stock on hand
remaining active claims
quantity/unit compatibility
```

**Atomicity:**

```text
availability check
+
claim establishment
```

MUST be one authoritative consistency decision.

If:

```text
requested quantity > available-to-promise
```

the claim is rejected and no partial claim is established.

---

## 17. Claim Retry

For the same logical claim invocation:

```text
same identity
+
same semantic intent
```

retry MUST reconcile with the committed Claim.

The same identity with materially different quantity, scope or use is a conflict.

A genuinely different business commitment may establish another claim.

---

## 18. `ReleaseInventoryClaim`

Canonical operation:

```text
inventory.claim.release
```

The operation accepts an explicit quantity to release.

Required:

```text
0 < released quantity <= remaining claim quantity
```

Success records one immutable `RELEASED` resolution.

Release:

```text
increases available-to-promise
does not increase stock-on-hand
does not erase claim history
```

A partial release leaves the claim active for its remaining quantity.

---

## 19. `ExpireInventoryClaim`

Canonical operation:

```text
inventory.claim.expire
```

Expiry may execute only when an accepted owner/policy establishes that the claim's remaining reservation is legitimately expired.

Target 12 does **not** introduce:

```text
10-minute hold
universal claim TTL
automatic expiry schedule
```

Initial expiry resolves the **entire remaining claim quantity**.

Automatic expiry timing remains deliberately deferred, consistent with ADR-007.

---

## 20. `FulfilInventoryClaim`

Canonical operation:

```text
inventory.claim.fulfil
```

This operation records physical/operational consumption of quantity already protected by an Inventory Claim.

Input includes:

```text
claim identity
quantity fulfilled
inventory movement identity
fulfilment/use provenance
logical invocation identity
```

Required:

```text
0 < fulfilled quantity <= remaining claim quantity
```

Within one Inventory-local atomic transaction it MUST:

```text
record FULFILLED claim-resolution quantity
+
record Inventory Movement
+
reduce stock-on-hand by the fulfilled quantity
```

If any part fails, none commits.

A partial fulfilment leaves the remainder of the claim active.

This preserves ADR-007 while making it compatible with accepted partial Order Fulfilment.

---

## 21. Fulfilment Shortfall

If physical correction/loss has made:

```text
stock on hand
<
quantity currently requested for claim fulfilment
```

`inventory.claim.fulfil` MUST NOT drive stock-on-hand negative.

The fulfilment attempt fails without partially resolving the claim.

The active claim remains authoritative.

Target 14 determines the broader Order-Fulfilment recovery workflow; Inventory does not invent one.

---

## 22. Resolution Concurrency

Two concurrent resolutions against the same remaining claim quantity MUST NOT over-resolve the claim.

Example:

```text
claim remaining = 3

release 2
fulfil 2
```

Both cannot independently succeed.

The authoritative Inventory boundary MUST serialise or detect conflict such that:

```text
sum committed resolution quantities
<= original claim quantity
```

The exact locking/version/CAS mechanism remains implementation scope.

---

## 23. Order Release Relationship

Where Ordering releases an Order Commitment Portion whose stock protection is represented by Inventory Claims, Ordering may request corresponding `inventory.claim.release` operations.

Inventory owns the claim resolution.

Ordering owns why the business commitment is being released.

The relationship does not transfer either authority.

---

## 24. Read Contract

Initial production Inventory reads remain owner queries.

Canonical direction:

```text
Inventory authoritative position
+
remaining active claims
        ↓
request-scoped Inventory query
        ↓
current read result
```

Initial Target-12 implementation SHALL NOT require:

```text
Inventory availability cache
Redis stock projection
persistent available_quantity table
generic Availability service
```

This follows MS-PROT-027 v1.4's default request-scoped architecture.

---

## 25. Availability Read Is Not Reservation

A read result such as:

```text
available-to-promise = 1
```

creates no claim.

The current result may be invalidated immediately by another legitimate claim.

Commitment paths always re-enter `inventory.claim`.

---

## 26. Projection Classification

No dedicated Inventory Projection Contract is required initially because Target 12 demonstrates no current need for persistence, asynchronous maintenance, deliberate stale serving or another MS-PROT-027 v1.3 trigger.

If later implementation caches/materialises Inventory availability, the applicable Projection Contract MUST be accepted first.

---

## 27. Exposure Classification

Inventory stock/availability is not automatically PUBLIC.

A future audience-facing availability element requires an applicable owner-qualified Exposure Element Contract before exposure.

Target 12 does not infer:

```text
stock exists
    → public may see exact stock
```

from storefront presentation.

MS-PROT-027's Exposure boundary remains governing.

---

## 28. Provider Boundary

Target 12 introduces no universal Inventory provider.

Provider readiness MUST NOT manufacture:

```text
stock position
claim
stock movement
availability
```

If a future external stock provider becomes authoritative evidence, its exact capability/provider contract must be accepted separately.

---

## 29. AI Boundary

AI MAY interpret merchant requests such as:

> We received 20 bottles.

or:

> I counted 8, not 10.

It may produce candidate registered Inventory operations.

AI MUST NOT:

```text
directly overwrite stock
invent quantities
release claims
decide that unknown stock means zero
manufacture unit conversion
bypass deterministic Inventory validation
```

---

## 30. Failure Taxonomy

Inventory MUST distinguish at least:

```text
VALIDATION_REJECTION

AUTHORISATION_REJECTION

INVENTORY_POSITION_NOT_ESTABLISHED

INSUFFICIENT_AVAILABLE_QUANTITY

CLAIM_ALREADY_RESOLVED

CLAIM_RESOLUTION_EXCEEDS_REMAINING_QUANTITY

AUTHORITATIVE_CONFLICT

PROVIDER / TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

Physical exception names remain implementation scope.

---

## 31. Events

The authoritative Inventory Claim, Resolution and Movement facts are sufficient business authority.

This amendment does not mandate a new Domain Event for every Inventory mutation.

Where a registered event is later introduced, it MUST describe an already committed Inventory fact and obey MS-PROT-026.

---

## 32. Falsification

The amendment survives:

| Scenario | Required behaviour |
|---|---|
| Last unit, website/POS race | only one compatible claim commits |
| Claim 10, fulfil 3 | stock −3; claim remaining 7 |
| Then release 2 | stock unchanged; claim remaining 5 |
| Duplicate fulfil retry | no duplicate stock movement/resolution |
| Duplicate release retry | no duplicate release |
| Established position = 0 | known zero |
| Inventory applies but position absent | `POSITION_NOT_ESTABLISHED`, not zero |
| Non-stock Offering | no Inventory position/claim required |
| Correct 10 → 6 while claims total 8 | stock becomes 6; claims remain 8; no new claim admitted |
| Internal consumption would invade reserved quantity | reject |
| Physical damage reveals stock below claims | preserve physical truth and claims; reconcile |
| Transfer attempts to move reserved stock | reject |
| Swansea/Cardiff positions | no accidental aggregation |
| kg/litre/dozen semantics | no integer-only design assumption |
| Bundle Order | one Order portion may retain multiple claims |
| Partial Order fulfilment | no one-claim-per-unit workaround required |

Current implementation handles entire claim resolution rather than partial quantity resolution, confirming that implementation must change after approval rather than being treated as authority.

---

## 33. Rejected Alternatives

Rejected:

```text
missing position = zero

raw stock field overwrite

one claim per physical unit

mutating original claim quantity downward

release = stock-on-hand increment

fulfilment = claim resolution without stock movement

Order owns stock arithmetic

public stock projection as claim authority

automatic universal claim expiry

discretionary stock movement consuming reserved stock

correction silently releasing claims
```

---

## 34. Deferred Scope

Still deferred:

```text
automatic claim-expiry timing
backorders/overselling policy
batch/lot selection
serialised-unit architecture
in-transit stock
advanced warehouse management
provider-specific stock integrations
return re-entry/disposition completion — Target 15
exact Inventory API — Target 20
exact SQL/jOOQ schema and locking
```

---

## 35. Conformance Criteria

A conforming implementation must prove that:

```text
[ ] position absence is distinct from known zero
[ ] stock position is explicit by subject/scope
[ ] original claim quantity is immutable
[ ] claim resolutions carry quantity
[ ] partial fulfilment is supported
[ ] partial release is supported
[ ] total resolved quantity cannot exceed original claim
[ ] fulfilment changes stock-on-hand atomically with resolution
[ ] release does not change stock-on-hand
[ ] new claims use remaining active-claim quantity
[ ] stock correction preserves provenance
[ ] physical correction may reveal claim shortfall without falsifying either fact
[ ] planned stock consumption protects active claims
[ ] transfers preserve both sides atomically
[ ] technical retries do not duplicate effects
[ ] availability reads create no reservation
[ ] no Inventory projection/cache is introduced without MS-PROT-027 justification
[ ] current Long-only implementation does not redefine accepted quantity semantics
```

---

## 36. Governing Principle

> **Inventory owns current stock truth and the reservation facts protecting that stock. Stock Positions are explicitly established; absence is not zero. Inventory Claims retain immutable original quantity and are reduced only through immutable quantity-bearing resolution facts. Release, expiry and fulfilment may resolve claim quantity without rewriting history; fulfilment additionally records the authoritative stock movement. Reads never reserve stock, and Ordering never becomes Inventory authority.**

---

## Governance Assessment

```text
DESIGN / PROPOSE:
    COMPLETE

AUTHORITY TRACE:
    COMPLETE

OWNERSHIP REVIEW:
    PASS

OPERATION-CONTRACT REVIEW:
    PASS

PARTIAL-QUANTITY FALSIFICATION:
    PASS

ORDER / INVENTORY ATOMICITY:
    PASS

READ / PROJECTION CLASSIFICATION:
    PASS

BUSINESS-TYPE NEUTRALITY:
    PASS

AMBIGUITY REVIEW:
    PASS within Target-12 Inventory scope

RECOMMENDATION:
    ACCEPT

MANUAL APPROVAL:
    GRANTED on 27 August 2026

STATUS:
    ACCEPTED
```
