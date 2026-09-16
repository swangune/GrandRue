# MS-PROT-077 v1.2 — Ordering Commercial Access Classification Amendment

**Document ID:** MS-PROT-077  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Ordering commercial-access classification amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-077 through v1.1 within commercial-access classification only  
**Depends on:** Composite MS-PROT-077 through v1.1; composite MS-PROT-056 through v1.9; composite MS-PROT-058; composite MS-PROT-059; composite MS-PROT-060; composite MS-PROT-062; applicable CustomerContext, Money/Payment, Inventory, Fulfilment, Actor Authorisation and Exposure authorities  
**Preserves:** Ordering ownership of Order commitment truth; Inventory ownership of stock truth; Payment ownership of monetary obligations/execution; Fulfilment ownership of satisfaction truth  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` by supplying the missing Ordering owner classifications  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 0. Fundamental Vision Conformance

GrandRue SHALL preserve the merchant's real Order history independently of current subscription level.

Commercial permission SHALL govern acquisition of new Order commitment scope.

It SHALL NOT make a merchant unable to inspect, reduce, release or otherwise resolve an Order that was legitimately established while commercial permission existed.

Canonical:

```text
Order semantics applicable
        ↓
new purchase commitment requested?
        ↓
YES
    current Ordering commercial permission required

existing commitment only?
        ↓
observation / restriction / release
        ↓
no independent Commercial Entitlement
subject to all other authority
```

This keeps subscription packaging separate from Order truth while preventing downgrade from becoming a route to unlimited new Ordering activity.

---

# 1. Governing Decision

Composite MS-PROT-077 SHALL define exactly one initially protected Ordering commercial purpose:

```text
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

The protected owner-qualified access contract SHALL be:

| Exact access contract | Protected commercial purpose | Standard allocation | Target family |
|---|---|---|---|
| `ordering/purchase-commitment-establishment-access@1` | `ESTABLISH_ORDER_PURCHASE_COMMITMENT` | BUSINESS + GROWTH | `OPERATION_ACCESS` |

FREE SHALL NOT receive this protected purpose through the standard catalogue.

The following exact bounded contracts SHALL require no independent Commercial Entitlement:

```text
ordering/new-commitment-preparation-access@1

ordering/existing-commitment-observation-access@1

ordering/existing-commitment-resolution-access@1
```

Missing classification is not an exemption.

---

# 2. Ownership Boundary

Ordering continues to own:

```text
Order identity

original Order Commitment Portions

amendment-established commitment portions

Ordering-owned release evidence

committed subject and quantity truth

committed Order commercial-term provenance

remaining Order commitment composition
```

Commercial owns:

```text
CommercialEntitlementIdentity

CommercialEntitlementDefinition

Commercial Access Binding

grant provenance

effective commercial permission

plan-revision grant sets

catalogue publication
```

Inventory continues to own:

```text
Inventory Position
Inventory Claim
Inventory Claim Resolution
stock availability
stock movement
```

Payment continues to own:

```text
Payment Obligation
Payment Application
provider payment execution/evidence
Refund
```

Order Fulfilment continues to own satisfaction of Order commitment.

No commercial classification in this amendment transfers those boundaries.

---

# 3. Protected Order Commitment Establishment

`ordering/purchase-commitment-establishment-access@1` SHALL govern an Ordering operation whenever its successful effect establishes materially new Order commitment scope.

It therefore covers the initial authoritative:

```text
CommitOrder
```

operation.

It also covers `ordering.amend` to the extent that the amendment establishes new commitment.

Protected examples include:

```text
create a new Order

add another Order Commitment Portion

increase committed quantity

add another subject

replace an existing subject
with a new subject

replace released commitment
with new commitment

materially establish new
commercial commitment terms

otherwise acquire purchase/order
commitment not already contained
within the current remaining Order
```

Commercial permission supplies only the commercial predicate.

It does not prove:

```text
subject is currently orderable

price/terms are valid

Inventory is available

required Inventory Claims can commit

customer relationship is valid

Payment requirements are satisfied

actor is authorised

merchant policy permits the Order
```

Every applicable independent predicate remains mandatory.

---

# 4. Channel Invariance

The same protected purpose SHALL apply when the same Ordering business operation originates through different supported channels.

Canonical:

```text
online customer Order
POS Order
telephone-assisted Order
merchant-entered Order
other accepted interaction origin
        ↓
same Ordering commitment semantics
        ↓
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

GrandRue SHALL NOT create:

```text
ONLINE_ORDER_ENTITLEMENT

POS_ORDER_ENTITLEMENT

PHONE_ORDER_ENTITLEMENT
```

merely because interaction provenance differs.

MS-PROT-059 channel convergence remains authoritative.

Interaction origin is provenance, not commercial entitlement identity.

---

# 5. Non-Committing Preparation

GrandRue SHALL define:

```text
ordering/new-commitment-preparation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

The contract may support bounded work such as:

```text
resolve currently applicable
orderable proposition

structure a candidate order

validate candidate quantity

show authoritative current terms

identify missing customer input

identify Inventory-dependent
availability where authorised

calculate deterministic
non-committing totals where governed

prepare a checkout/order candidate
```

Preparation SHALL NOT:

```text
create an Order

establish an Order Commitment Portion

establish an Inventory Claim

establish a Payment Obligation

reserve stock

create durable commercial permission
```

A successful preparation result creates no grandfathered right to commit an Order.

`CommitOrder` or another protected commitment-establishing effect SHALL revalidate current commercial permission.

---

# 6. Existing Order Observation

GrandRue SHALL define:

```text
ordering/existing-commitment-observation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

The contract applies only to otherwise-authorised observation of an exact existing Order.

It MAY include, where independently authorised:

```text
original committed portions

Ordering-owned amendments

Ordering-owned released quantities

committed terms/provenance

derived remaining commitment

authorised logical-operation results
```

Observation does not independently grant:

```text
CustomerContext access

related-customer authority

Fulfilment information

Shipment information

Payment information

Inventory information

Actor Authorisation

Exposure

cross-Merchant access
```

Those requirements remain with their owners.

Loss of BUSINESS/GROWTH permission SHALL NOT make a legitimate existing Order disappear from required merchant or customer handling.

---

# 7. Existing Order Resolution

GrandRue SHALL define:

```text
ordering/existing-commitment-resolution-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

It applies only when the authoritative Ordering effect:

```text
reduces
releases
restricts
or otherwise resolves
```

existing Order commitment without acquiring materially new Order commitment scope.

It includes, where independently semantically valid:

```text
ordering.release

release of all remaining Order commitment

partial quantity reduction

removal of an unfulfilled commitment portion

other pure restriction of
existing remaining commitment
```

Commercial downgrade SHALL NOT require a merchant to continue owing a customer a commitment merely because the merchant can no longer purchase new Ordering access.

---

# 8. `ordering.amend` Is Effect-Sensitive

`ordering.amend` SHALL NOT receive one blanket commercial classification.

Its commercial requirement follows the authoritative commitment effect.

## 8.1 Pure commitment restriction

Example:

```text
quantity 5
→
quantity 3
```

where the operation is represented solely as:

```text
release 2
```

and creates no replacement commitment.

This is:

```text
existing-commitment resolution
```

and requires no independent Commercial Entitlement.

## 8.2 Quantity increase

Example:

```text
quantity 5
→
quantity 7
```

where two additional units become newly committed.

This establishes new commitment scope.

It requires:

```text
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

## 8.3 Subject replacement

Example:

```text
Subject A
→
Subject B
```

where the accepted implementation:

```text
releases A
+
establishes new commitment B
```

The new B commitment requires:

```text
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

The fact that the same Order identity survives does not turn B into residual access.

## 8.4 Mixed release and acquisition

A single amendment may contain both:

```text
release existing scope
+
establish new scope
```

If any authoritative component requires materially new Order commitment, the amendment SHALL satisfy the protected commercial purpose before the atomic amendment commits.

The no-entitlement release component SHALL NOT bypass the protected acquisition component.

---

# 9. Material Commercial-Term Changes

A material Order amendment that establishes replacement commitment terms SHALL be treated as protected new commitment where MS-PROT-077 represents the result through a new amendment-established commitment portion.

Canonical:

```text
old commitment terms
        ↓
release / supersede applicable
remaining commitment
        +
establish materially new
commitment terms
```

requires:

```text
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

This does not mean every correction of presentation metadata is protected.

The classification follows the Ordering-owned authoritative commitment effect.

A correction that does not change Order commitment truth is not made commercially protected merely because a UI calls it an edit.

---

# 10. No Entitlement Through Inventory

Inventory participation SHALL NOT satisfy Ordering commercial permission.

Canonical:

```text
Inventory available
    ≠
Order commercially permitted
```

and:

```text
Inventory Claim established
    ≠
Order entitlement granted
```

Where an Order commitment requires stock protection:

```text
Ordering commercial permission
+
current Inventory authority
+
atomic Order/Inventory invariant
```

must all independently succeed.

A FREE merchant SHALL NOT obtain BUSINESS Ordering simply because Inventory state exists.

---

# 11. No Entitlement Through Customer Payment

The following remain distinct:

```text
customer pays merchant
        ≠
merchant possesses GrandRue
Ordering entitlement
```

A provider payment result SHALL NOT supply:

```text
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

Likewise:

```text
GrandRue Ordering entitlement
        ≠
customer payment requirement satisfied
```

Payment and subscription-commercial authority remain independent.

---

# 12. Existing Order and Payment Consequences

An Ordering release or reduction may cause a separately governed Payment consequence.

For example:

```text
Order commitment reduced
        ↓
Payment authority may need
adjustment/refund evaluation
```

But:

```text
ordering/existing-commitment-resolution-access@1
```

does not itself grant:

```text
Refund execution

Payment Obligation adjustment

provider execution

settlement modification
```

The applicable Payment commercial classification must independently govern those effects.

Conversely, Payment permission SHALL NOT authorise Order mutation.

---

# 13. Fulfilment Boundary

Order Fulfilment remains independently owned.

An Order that has already been validly committed may require later fulfilment after the merchant loses Ordering entitlement.

Ordering commercial loss SHALL NOT reinterpret already-satisfied or still-outstanding commitment.

The commercial treatment of:

```text
new fulfilment execution
shipment
redelivery
replacement fulfilment
```

remains with MS-PROT-060 commercial classification.

This amendment therefore does not attempt to make Ordering entitlement the commercial owner of Fulfilment.

---

# 14. Existing Commitment History

Commercial entitlement loss SHALL NOT:

```text
delete the Order

delete Order Commitment Portions

rewrite committed subjects

rewrite committed terms

erase amendments

erase releases

erase Fulfilment history

erase Payment history

erase Shipment history
```

Retention remains separately governed.

Historical existence does not itself grant unlimited new activity.

The distinction is:

```text
historical / current existing commitment
        ≠
permission to acquire another commitment
```

---

# 15. Multiple Grant Sources

The protected commercial purpose MAY be satisfied by any independently valid Commercial grant source accepted by MS-PROT-056.

Examples include:

```text
initial full-experience trial

paid Merchant Commercial Agreement

commercial remediation

future separately accepted grant source
```

A runtime consumer SHALL NOT evaluate:

```text
plan == BUSINESS
```

as commercial authority.

It SHALL consume the exact entitlement binding produced by the final Commercial catalogue.

---

# 16. Configuration Independence

Loss of Ordering commercial permission SHALL NOT itself mutate Merchant Configuration.

Rejected:

```text
BUSINESS expires
    ↓
remove Ordering capability
```

Rejected:

```text
FREE merchant
    ↓
forget merchant sells products
```

Canonical:

```text
merchant business configuration
remains independently authoritative

+

commercial permission determines
whether protected new Ordering
activity is presently permitted
```

This permits a later valid upgrade to restore access without reconstructing the merchant's operating model, subject to normal compatibility/currentness rules.

---

# 17. Customer Surface Boundary

A rendered Order interaction is not authority.

Before protected commitment:

```text
displayed product
checkout candidate
displayed total
displayed stock
button enabled
```

do not substitute for current:

```text
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

At the commitment boundary, Ordering SHALL revalidate the exact current commercial requirement.

Existing customer Order observation remains governed by:

```text
ordering / related-customer-order
```

plus current source access and Exposure.

The no-entitlement observation classification does not make an Order public.

---

# 18. Retry and Recovery

A retry that merely resolves an already-committed Order operation result SHALL NOT be treated as new protected Ordering activity.

Canonical:

```text
CommitOrder committed
acknowledgement lost
commercial grant later ends
        ↓
authorised recovery of the
already-committed result
does not create another Order
```

Recovery remains subject to applicable scope, disclosure and actor authority.

A retry that would produce a new authoritative effect SHALL satisfy the commercial classification applicable to that new effect.

Idempotency does not preserve expired commercial permission for uncommitted work.

---

# 19. Failure Semantics

Protected new Ordering activity SHALL distinguish at least:

```text
COMMERCIAL_PERMISSION_DENIED

COMMERCIAL_PERMISSION_UNRESOLVED

SEMANTICALLY_INAPPLICABLE

ACTOR_NOT_AUTHORISED

SUBJECT_NOT_ORDERABLE

TERMS_CHANGED

INVENTORY_UNAVAILABLE

POLICY_UNSATISFIED

CONFLICT

TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

Commercial denial SHALL NOT be reported as:

```text
out of stock

invalid product

payment failed

order cancelled
```

unless those independently authoritative conditions are actually true.

Missing Commercial Access Binding information SHALL fail closed.

---

# 20. Standard Allocation

The resulting canonical entitlement binding for:

```text
ordering/purchase-commitment-establishment-access@1
+
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

SHALL be granted by:

```text
BUSINESS
GROWTH
```

and SHALL NOT be granted by:

```text
FREE
```

through the standard catalogue.

This amendment does not mint that final `CommercialEntitlementIdentity`.

Commercial retains that responsibility under `MS-PROT-056-V17-DQ-001`.

---

# 21. Catalogue Consequences

The complete MS-PROT-056 manifest SHALL record:

```text
protected binding:
    ordering/purchase-commitment-establishment-access@1
    +
    ESTABLISH_ORDER_PURCHASE_COMMITMENT

explicit no-entitlement classifications:
    ordering/new-commitment-preparation-access@1
    ordering/existing-commitment-observation-access@1
    ordering/existing-commitment-resolution-access@1
```

Supporting Inventory, Payment, Fulfilment, Notification and presentation requirements SHALL retain their own exact classifications.

The Ordering binding SHALL NOT be treated as a wildcard entitlement to those neighbouring owners.

---

# 22. Review and Falsification

| Challenge | Required outcome |
|---|---|
| FREE merchant has Ordering configured | Configuration survives; new Order commitment denied |
| Merchant has BUSINESS | New otherwise-valid Order may satisfy the commercial predicate |
| Trial supplies entitlement | New Order may be commercially permitted independently of paid plan |
| Existing Order remains after downgrade | Order remains observable to independently authorised actors |
| Merchant cancels all unfulfilled remaining quantity after downgrade | Existing-commitment release remains available |
| Quantity changes 5 → 3 | Pure release may use residual resolution |
| Quantity changes 5 → 7 | Additional quantity requires protected Ordering permission |
| Subject A changes to Subject B | New subject commitment requires protected permission |
| Amendment releases A and adds B atomically | Protected requirement applies because B is new commitment |
| Customer already paid | Payment does not manufacture Ordering entitlement |
| Inventory has ample stock | Inventory does not manufacture Ordering entitlement |
| POS staff creates same semantic Order | Same commercial purpose; no POS-specific entitlement |
| Online customer creates same semantic Order | Same commercial purpose; no online-specific entitlement |
| Lost acknowledgement after committed Order and entitlement later expires | Authorised result recovery does not create another protected effect |
| Existing Order must later be fulfilled | Fulfilment remains independently classified by its owner |

The design survives these cases without tier-owned Order semantics, channel-specific Order entitlements or a blanket post-downgrade lock on existing commitments.

---

# 23. Alternatives Rejected

## Blanket `ORDERING_ENABLED`

Rejected because it collapses:

```text
Merchant Configuration
Commercial Entitlement
Actor Authorisation
Inventory eligibility
Order semantics
```

into one flag.

## BUSINESS required for every Order operation

Rejected because it would prevent legitimate resolution of pre-existing commitments after entitlement loss.

## Every amendment treated as residual access

Rejected because quantity increases, added subjects and replacement commitment can acquire materially new service.

## Every amendment treated as new activity

Rejected because a pure reduction or release acquires no new Order commitment.

## Separate channel entitlements

Rejected because Online/POS/telephone origins do not create different Order semantics.

## Ordering entitlement grants Inventory or Payment

Rejected because that would violate bounded-context ownership.

---

# 24. Amendment Effect

This amendment closes the **Ordering owner-classification blocker** discovered during completion of `MS-PROT-056-V17-DQ-001`.

It does not:

```text
mint final CommercialEntitlementIdentity values

classify Payment commercial access

classify Inventory commercial access

classify Order Fulfilment / Shipment commercial access

publish the standard catalogue

resolve pricing

activate implementation
```

`MS-PROT-056-V17-DQ-001` remains OPEN until the remaining owner-classification blockers and final Commercial manifest are accepted.
