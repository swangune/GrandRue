# MS-PROT-077 v1.1 — Order Amendment, Release, Inventory Consequence & Customer Relationship Execution Contract Amendment

**Document ID:** MS-PROT-077  
**Version:** 1.1  
**Status:** **ACCEPTED by manual approval on 27 August 2026**  
**Approved:** Manual approval on 27 August 2026  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** MS-PROT-077 v1.0 within Order amendment/release operation-contract, Inventory consequence, Ordering read and customer-relationship scope  
**Depends on:** MS-PROT-027 v1.3–v1.5; MS-PROT-043; MS-PROT-049 v1.3; MS-PROT-055; composite MS-PROT-058 through v1.1; MS-PROT-059; MS-PROT-060; MS-PROT-062; MS-PROT-063; MS-PROT-069; MS-PROT-072; MS-PROT-077 v1.0  
**Resolves:** Ordering scope of `MS-PROT-027-V15-DQ-005`  
**Purpose:** Complete production Order amendment/release operations, preserve atomic Inventory consequences, define the customer-to-Order relationship predicate required for secure CUSTOMER surfaces and classify initial Ordering reads.

---

## 1. Governing Decision

MS-PROT-077 v1.0 already defines the correct Order model.

This amendment does **not** introduce:

```text
new Order lifecycle
new Commerce aggregate
new orderable-subject authority
new Customer model
```

Offering/configuration semantics remain authoritative for which supported operations apply to a proposition. MS-PROT-044 already permits an Offering to identify support for Ordering.

The missing production decisions are:

```text
exact amendment execution
exact release execution
Inventory-claim consequences
customer-to-Order relationship evaluation
initial Order-read architecture
```

---

## 2. Existing Order Truth Survives

Unchanged:

```text
original Order existence is immutable historical truth

original Order Commitment Portions are not destructively edited

Order amendment/release facts belong to Ordering

Inventory owns Inventory Claims

Fulfilment owns satisfied quantity

Money owns monetary obligations/evidence

Shipment owns physical movement evidence
```

---

## 3. Order Amendment Identity

An **Order Amendment** is an immutable Ordering-owned fact associated with exactly one Order.

Its identity is merchant-scoped and unique within that Order.

At minimum it retains:

```text
amendment identity
Order identity
logical command/causation identity
released commitment quantities
new commitment portions where any
governing semantic/configuration affinity
authoritative provenance
committedAt
```

An Order Amendment has no mutable status lifecycle.

Once committed, it is historical evidence.

---

## 4. Amendment-Established Commitment Portions

An amendment MAY establish new Order Commitment Portions.

Any new portion receives:

```text
new durable portion identity
new commitment provenance
current authoritative subject/proposition resolution
current authoritative commercial terms
current semantic/configuration affinity
```

It MUST NOT reuse an original portion identity to mean something different.

This permits:

```text
increase quantity
add subject
replace subject
replace material terms
```

without rewriting historical commitment.

---

## 5. Remaining Commitment Formula

MS-PROT-077 v1.0's remaining-commitment formula is refined to include amendment-established portions:

```text
all Order Commitment Portions ever validly established
    - Order-owned released quantities
    - Fulfilment-owned satisfied quantities
    = current remaining commitment
```

where:

```text
all Order Commitment Portions
=
original portions
+
amendment-established portions
```

Original portions remain immutable.

---

## 6. Quantity Reduction

A request to change:

```text
quantity 5 → 3
```

MUST NOT mutate the original portion to 3.

Correct representation:

```text
original committed quantity = 5
+
Order Amendment release = 2
```

Current remaining commitment becomes 3.

---

## 7. Quantity Increase

A request to change:

```text
quantity 5 → 7
```

MUST NOT rewrite the original portion to 7.

Correct representation:

```text
original portion = 5
+
new amendment-established portion = 2
```

or another semantically equivalent decomposition preserving the original five-unit commitment and the later two-unit commitment as distinct provenance.

---

## 8. Subject or Material-Term Replacement

Changing:

```text
Subject A
    →
Subject B
```

or materially replacing committed terms requires:

```text
release applicable remaining commitment against A/old terms
+
establish new commitment portion for B/new terms
```

within one accepted amendment.

The historical Order continues to show what was previously committed.

---

## 9. `AmendOrder` Operation Contract

Canonical operation:

```text
ordering.amend
```

**Owner:** Ordering.

### Principal

A current trusted principal/context must have applicable Order amendment authority.

### Semantic applicability

Ordering and the requested amendment semantics must exist in the exact current active RCP/release.

### Inputs

At minimum:

```text
MerchantScope
logical command identity
Order identity
requested existing-portion release quantities
requested new/replacement commitment intent where any
current execution context
```

Client-supplied values are intent, not committed truth.

### Authoritative reads

The operation revalidates:

```text
current Order
all prior Order Amendments
current remaining commitment
current Fulfilment satisfaction affecting requested portions
current amendment policy
current principal authority
current proposition/terms for new commitment
required Inventory consequences
applicable runtime/entitlement authority
```

### Authoritative mutation

Success establishes exactly one immutable Order Amendment and any new Order Commitment Portions belonging to that amendment.

---

## 10. `AmendOrder` Inventory Atomicity

Where the amendment changes stock-protected commitment, the following local consequences MUST form one atomic consistency decision:

```text
release Inventory Claim quantities
no longer required
+
establish new Inventory Claim quantities
required by replacement/additional commitment
+
Order Amendment
```

If a required replacement Inventory Claim cannot be established:

```text
NO amendment commits
NO old claim quantity is released
NO new portion commits
```

This preserves both stock protection and Order history.

Physical transaction co-location does not transfer Inventory ownership.

---

## 11. Net Inventory Consequence

Implementation MAY sequence internal claim release/claim establishment in any technically safe manner, but the externally authoritative result must be equivalent to evaluating the amendment as one coherent Inventory consequence.

A temporary internal ordering of SQL statements MUST NOT create observable partial amendment truth.

---

## 12. Amendment Rejection

`AmendOrder` MUST reject when, among other applicable conditions:

```text
Order does not exist in MerchantScope
requested portion is not currently amendable
requested release exceeds remaining commitment
required replacement subject/terms are no longer valid
required Inventory Claim cannot be established
merchant policy prohibits the requested amendment
current principal lacks authority
concurrent mutation invalidated the decision basis
```

No partial amendment may survive rejection.

---

## 13. `ReleaseOrderCommitment` Operation Contract

Canonical operation:

```text
ordering.release
```

**Owner:** Ordering.

This is the authoritative Order-side operation behind presentation phrases such as:

```text
cancel item
cancel remaining order
cancel order
```

where the requested semantics map to release of remaining commitment.

---

## 14. Release Input

At minimum:

```text
MerchantScope
logical command identity
Order identity
one or more exact Order Commitment Portion identities
quantity to release for each portion
release provenance/reason where required
current execution context
```

A UI convenience meaning:

```text
release all remaining
```

must be resolved server-side against current authoritative remaining commitment before mutation.

---

## 15. Release Preconditions

The operation MUST establish:

```text
current Order commitment
prior amendments/releases
already fulfilled quantity
remaining releasable quantity
merchant release/cancellation policy
principal authority
required Inventory Claim consequences
```

Already fulfilled quantity cannot be released as though fulfilment never occurred.

---

## 16. Release Success

Successful release creates one immutable release-only Order Amendment.

It does not:

```text
delete Order
delete original portions
delete Fulfilment
delete Shipment
delete Payment history
rewrite original committed terms
```

---

## 17. Partial Release

Suppose:

```text
original quantity = 10
fulfilled = 3
remaining = 7
```

A valid release may release:

```text
2
```

leaving:

```text
remaining = 5
```

This requires corresponding partial Inventory Claim release where stock protection remains represented by the claim.

MS-PROT-058 v1.1 therefore directly composes with Ordering release.

---

## 18. Full Remaining Release

Releasing all remaining commitment does not make the historical Order disappear.

The Order may derive:

```text
remaining commitment = 0
```

without acquiring a universal terminal `CANCELLED` lifecycle state.

Presentation may call that outcome cancelled where appropriate.

---

## 19. Release Inventory Atomicity

Where an active Inventory Claim exists solely to protect the Order commitment quantity being released:

```text
Order-side release
+
required Inventory claim release
```

MUST commit atomically within the local Main Street consistency boundary.

Otherwise Main Street could commit:

```text
Order says quantity no longer committed
but
Inventory still reserves it
```

which would create false available-to-promise state.

---

## 20. Release and Fulfilment Concurrency

The same quantity MUST NOT concurrently become both:

```text
newly fulfilled
and
released
```

Ordering and Fulfilment must preserve a cross-capability consistency/serialization invariant over the remaining Order portion.

Target 14 determines the concrete Fulfilment execution contract, but it MUST respect this already-established Order invariant.

---

## 21. Amendment/Release Concurrency

Initial production SHALL serialise authoritative amendment/release decisions per Order sufficiently to prevent incompatible interpretations of the same remaining commitment.

Permitted mechanisms include:

```text
row locking
optimistic versioning
compare-and-set
equivalent transactional mechanism
```

Technology is implementation scope.

The semantic guarantee is not.

---

## 22. Idempotency

For `ordering.amend` and `ordering.release`:

```text
same logical command
+
same semantic intent
    → same committed result

same logical command identity
+
different semantic intent
    → conflict

new command identity
+
legitimate repeated business intent
    → independently evaluated operation
```

A lost acknowledgement after commit MUST NOT multiply:

```text
Order Amendment
new commitment portions
Inventory releases
Inventory claims
Domain Events
```

---

## 23. Amendment Event

Successful `ordering.amend` SHALL commit:

```text
order.amended
```

as a Domain Event representing an already committed Order Amendment.

Minimum semantic references:

```text
Order identity
Order Amendment identity
command/causation identity
affected portion identities
occurredAt
governing release/configuration provenance where required
```

The event does not claim that Payment, Fulfilment, Shipment or Notification consequences have completed.

---

## 24. Release Event

Successful `ordering.release` SHALL commit:

```text
order.released
```

representing committed Order-side release evidence.

The event MUST NOT mean:

```text
refund completed
stock returned
shipment cancelled
notification delivered
```

unless those facts are independently established by their owners.

Events are published post-commit under MS-PROT-025/MS-PROT-026.

---

## 25. Payment Boundary

Target 12 does not pre-design Target 13.

An Order amendment/release may later have Money consequences.

Those consequences remain Money-owned.

Therefore:

```text
Order release
    ≠ Refund

Order amendment
    ≠ Payment adjustment
```

Provider/payment execution is never pulled inside the local Order/Inventory transaction merely because an Order changes.

---

## 26. Fulfilment Boundary

Target 12 does not redefine Target 14.

It establishes only the invariant that already satisfied quantity is unavailable for later Order release/amendment as though it were unsatisfied.

Order Fulfilment remains the authority for satisfaction truth.

---

## 27. Canonical Order Customer Requirement

Target 12 establishes the owner-qualified Customer Surface Eligibility requirement:

```text
ordering / related-customer-order
```

It means:

> **The current trusted customer access context is authoritatively related to the exact Order being considered within the exact MerchantScope.**

Ordering owns the Order-side relationship evidence.

CustomerContext remains owned by MS-PROT-043.

Surface remains owned by MS-PROT-049.

Exposure remains owned by MS-PROT-027.

---

## 28. CustomerContext Satisfaction Path

The requirement is satisfied through CustomerContext when:

```text
target Order
    has authoritative Order → CustomerContext relationship

AND

current trusted customer context
    establishes legitimate access to that CustomerContext

AND

both belong to the exact MerchantScope
```

CustomerContext existence by itself is insufficient.

---

## 29. Guest Order Satisfaction Path

A CustomerAccount is not universally required.

A valid transaction-specific contextual access context MAY satisfy:

```text
ordering / related-customer-order
```

when it is authoritatively scoped to:

```text
exact MerchantScope
+
exact Order identity
```

and remains currently valid under the authentication/security authority.

Exact secure-link/token/credential representation remains ADR-014-DQ-011 implementation architecture, not Target-12 semantics.

---

## 30. Guest Access Does Not Create Customer Identity

Order-specific contextual access does not automatically create:

```text
CustomerAccount
CustomerContext
global customer identity
access to another Order
access to Booking
access to Appointment
```

The authority remains bounded to the exact accepted scope.

---

## 31. Evidence That Does Not Establish Order Relationship

None of these independently satisfies `ordering / related-customer-order`:

```text
CustomerAccount authenticated
CustomerContext exists
client knows Order ID
client submits CustomerContext ID
email matches
phone matches
name matches
address matches
payment-provider account matches
frontend route
merchant category
AI confidence
previous page state
Exposure result
```

MS-PROT-049 already prohibits client identifiers and authentication from substituting for relationship authority.

---

## 32. CustomerContext Reconciliation Boundary

If:

```text
C2 RECONCILED_TO C1
```

that fact alone MUST NOT grant a customer authenticated for C1 access to every historical Order associated with C2.

Any such access broadening requires accepted relationship/access semantics.

---

## 33. CUSTOMER Surface Eligibility

An Ordering CUSTOMER contribution requiring an Order relationship may reference:

```text
ordering / related-customer-order
```

Canonical flow:

```text
registered Ordering CUSTOMER contribution
+
related-customer-order satisfied
        ↓
Customer Surface Eligibility
        ↓
Projection Serviceability
        ↓
Exposure
```

No layer acquires Ordering ownership.

---

## 34. Customer Visibility Is Not Order Mutation Authority

A customer legitimately observing an Order does not automatically have authority to:

```text
amend
release
refund
change delivery
change payment
```

Every mutation re-enters current:

```text
Trusted Execution Context
Actor Authorisation
merchant policy
Order remaining commitment
Operational Eligibility
Commercial Entitlement where applicable
neighbouring capability invariants
```

---

## 35. Initial Ordering Read Architecture

Initial production Ordering reads consisting purely of Ordering-owned facts SHALL default to:

```text
Ordering authoritative query
        ↓
request-scoped read/composition
```

Examples:

```text
original commitment portions
Order-owned amendments
Order-owned released quantities
governing provenance
derived remaining commitment
where required Fulfilment facts are obtained
through accepted bounded composition
```

A standalone persisted Order-summary projection is not initially required.

---

## 36. Cross-Capability Progress Projection

A rich UI status such as:

```text
Awaiting payment
Processing
Partially fulfilled
Shipped
Refunded
Completed
```

combines facts from multiple authorities.

Target 12 SHALL NOT create that as one authoritative Order state.

As Targets 13 and 14 complete Money/Fulfilment participation, the applicable read responsibility must determine whether MS-PROT-027 projection triggers apply.

---

## 37. Inventory Read Relationship

Ordering MAY query current Inventory information to support a read or commitment preparation.

Such a read is not:

```text
Inventory Claim
Order authority
commitment permission
```

`CommitOrder`/`AmendOrder` still require authoritative Inventory claim establishment where stock protection is required.

---

## 38. Exposure Classification

Target 12 resolves the **Ordering relationship evaluator** required by MS-PROT-027 v1.5.

It does not automatically declare every Order field CUSTOMER-visible.

Any Order element exposed to CUSTOMER/PUBLIC audiences must:

```text
already have an accepted owner-qualified Exposure Element Contract
```

or receive one before production exposure.

Internal claim identifiers, release IDs, audit evidence, semantic-release IDs and internal fulfilment information do not inherit visibility from Order relationship.

---

## 39. Provider Boundary

No provider is required merely because Ordering exists.

Provider Readiness is evaluated only when a separately accepted Ordering-related obligation actually requires a provider.

Provider output MUST NOT:

```text
create Order Amendment
release Order commitment
create customer relationship
manufacture Inventory Claim success
```

---

## 40. AI Boundary

AI MAY:

```text
interpret amendment intent
interpret cancellation/release intent
explain remaining commitment
prepare candidate Ordering command
```

AI MUST NOT:

```text
rewrite committed portions
invent released quantity
invent customer relationship
decide Inventory claim success
bypass amendment/release policy
directly mutate Ordering persistence
```

---

## 41. Failure Taxonomy

Ordering amendment/release MUST preserve:

```text
VALIDATION_REJECTION

AUTHORISATION_REJECTION

ENTITLEMENT_REJECTION

BUSINESS_REJECTION

NO_RELEASABLE_COMMITMENT

INVENTORY_BUSINESS_REJECTION

AUTHORITATIVE_CONFLICT

PROVIDER / TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

A repeated already-committed logical command is an idempotent reconciliation, not a fresh failure.

---

## 42. Falsification

| Scenario | Required result |
|---|---|
| Reduce quantity 5 → 3 | original 5 survives; release 2 |
| Increase quantity 5 → 7 | original 5 survives; new amendment portion 2 |
| Replace A with B | release remaining A + new B commitment |
| Replacement B has no stock | entire amendment rejected; original Order/claim unchanged |
| Partial fulfilment 3 of 10, cancel remainder | only remaining 7 releasable |
| Release only 2 of remaining 7 | release 2; remaining 5 |
| Concurrent fulfil and release same quantity | both cannot commit against same remaining scope |
| Two simultaneous incompatible amendments | serialise/conflict |
| Lost acknowledgement after amendment commit | retry returns same amendment |
| Same amendment command identity, changed intent | identity conflict |
| New legitimate amendment after prior amendment | separately evaluated |
| Guest exact secure Order context | related-customer-order may be satisfied |
| Guest knows Order UUID only | no customer relationship |
| Logged-in account but unrelated Order | no customer relationship |
| Email/address happens to match | no relationship |
| Product terms change after original Order | historical portion unchanged |
| Payment provider reports refund | cannot rewrite Order release truth |
| Public/merchant stale Order projection | never mutation authority |
| Bundle portion has multiple claims | amendment may coordinate multiple Inventory claim quantities without merging ownership |

---

## 43. Rejected Alternatives

Rejected:

```text
destructively edit original Order line

quantity 5 → 3 by rewriting 5 to 3

one giant OrderStatus

cancel = delete Order

cancel = automatic refund

cancel = blind stock increment

Order amendment commits before required replacement stock claim

Inventory owns amendment

CustomerAccount login = Order ownership

Order ID possession = Order ownership

email matching = Order ownership

one universal Customer relationship boolean

persisted Order-progress projection by default
```

---

## 44. Deferred Scope

Still deferred:

```text
advanced amendment policy catalogue
exchanges/substitutions beyond accepted boundary
shopping basket/cart
advanced tax/invoice
offline Ordering
exact secure guest credential representation
Payment consequences — Target 13
Fulfilment/Shipment execution — Target 14
Returns — Target 15
generic background/event completion — Target 18
reconciliation/observability — Target 19
exact API representations — Target 20
```

---

## 45. Conformance Criteria

A conforming implementation must prove:

```text
[ ] original commitment portions never mutate destructively

[ ] amendment-established portions have distinct identity/provenance

[ ] quantity reduction uses release evidence

[ ] quantity increase uses new commitment evidence

[ ] replacement preserves old commitment history

[ ] amendment revalidates current state

[ ] release cannot exceed remaining commitment

[ ] fulfilled quantity cannot subsequently be released

[ ] required claim releases/new claims are atomic with Order amendment/release

[ ] failed new claim leaves original Order/claims unchanged

[ ] incompatible concurrent amendments cannot both commit

[ ] retries do not duplicate amendment/release/claim/event effects

[ ] ordering / related-customer-order is MerchantScope- and Order-specific

[ ] CustomerAccount authentication alone does not satisfy it

[ ] Order ID possession does not satisfy it

[ ] transaction-specific guest access remains exact and bounded

[ ] customer visibility does not grant mutation authority

[ ] initial Ordering reads remain request-scoped unless a Projection trigger appears

[ ] no cross-capability Order status becomes authoritative
```

---

## 46. Target-12 Closure Effect

With both MS-PROT-058 v1.1 and MS-PROT-077 v1.1 approved and formalised, the Target-12 review treats as closed:

```text
Product/Offering/Variant participation
    → already governed by MS-PROT-044

Order Commitment / Portion authority
    → MS-PROT-077

Inventory position / claim authority
    → MS-PROT-058 + v1.1

stock mutation
    → MS-PROT-058 v1.1

partial claim release/fulfilment
    → MS-PROT-058 v1.1

Order amendment/release execution
    → MS-PROT-077 v1.1

Order ↔ Inventory atomicity
    → composite MS-PROT-058 / 077

channel convergence
    → MS-PROT-059

retry/idempotency
    → MS-PROT-059 + these operation contracts

historical committed truth
    → MS-PROT-077

Inventory/Ordering read classification
    → request-scoped owner queries initially

Ordering CUSTOMER relationship
    → ordering / related-customer-order
```

No Target-12 material business rule remains for implementation to invent within the accepted scope.

---

## 47. Governing Principle

> **Main Street preserves an Order by accumulating immutable commitment, amendment and release evidence rather than rewriting history. New or replacement stock-protected commitment becomes authoritative only when its required Inventory Claims can be established atomically; released commitment releases only the corresponding remaining Inventory reservation. Ordering customer access is established from an exact trusted relationship to the Order, never from login, identifiers, contact similarity or presentation.**

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

CUSTOMER RELATIONSHIP BOUNDARY:
    PASS

PAYMENT / FULFILMENT SEQUENCE BOUNDARY:
    PASS

BUSINESS-TYPE NEUTRALITY:
    PASS

AMBIGUITY REVIEW:
    PASS within Target-12 Ordering scope

RECOMMENDATION:
    ACCEPT

MANUAL APPROVAL:
    GRANTED on 27 August 2026

STATUS:
    ACCEPTED
```
