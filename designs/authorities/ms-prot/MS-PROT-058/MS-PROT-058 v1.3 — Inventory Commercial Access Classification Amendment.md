# MS-PROT-058 v1.3 — Inventory Commercial Access Classification Amendment

**Document ID:** MS-PROT-058  
**Version:** 1.3  
**Status:** ACCEPTED  
**Approved:** 16 September 2026 by explicit manual approval in ChatGPT  
**Authority type:** Inventory commercial-access classification amendment  
**Governed by:** `MS-DESIGN-RULES-001` v2.4; `DOCUMENT-GOVERNANCE.md`; `MS-FUNDAMENTAL-VISION-001`  
**Amends:** Composite MS-PROT-058 through v1.2 within commercial-access classification only  
**Depends on:** Composite MS-PROT-058 through v1.2; composite MS-PROT-056 through v1.9; composite MS-PROT-060; composite MS-PROT-061; composite MS-PROT-062; composite MS-PROT-077 through v1.2; applicable Actor Authorisation, Merchant Configuration, Resource Protection and Exposure authorities  
**Preserves:** Inventory ownership of stock position, claims, resolutions, movements, returned-stock receipt/disposition and availability derivation; Ordering and Fulfilment ownership boundaries  
**Partially resolves:** `MS-PROT-056-V17-DQ-001` by supplying the missing Inventory owner classifications  
**Implementation activation:** NONE  
**Recommendation:** ACCEPT  
**Vision Conformance:** VISION-CONFORMING WITH JUSTIFIED COMPLEXITY

**Product identity note:** inherited `Main Street` references identify the product currently named **GrandRue**. Stable `MS-*` and `mainstreet.*` identifiers remain unchanged pending separately governed migration.

---

# 0. Fundamental Vision Conformance

**Outcome: VISION-CONFORMING WITH JUSTIFIED COMPLEXITY**

GrandRue SHALL treat active Inventory administration as BUSINESS operating value without using commercial loss to falsify physical stock, strand existing customer commitments or prevent safe restriction of overstated availability.

Canonical:

```text
merchant actively administers stock
        ↓
BUSINESS commercial permission required

existing Inventory truth
or already-established customer commitment
        ↓
observation / resolution / protective restriction
        ↓
no independent Inventory entitlement
subject to all other authority
```

The merchant-facing outcome remains simple:

```text
BUSINESS
    → manage stock

commercial access ends
    → no new ordinary stock administration
    → existing truth remains visible where authorised
    → existing commitments remain resolvable
    → GrandRue may still record bounded
      restrictive/corrective facts needed
      not to overstate stock
```

This complexity remains internal.

---

# 1. Governing Decision

Composite MS-PROT-058 SHALL define one protected Inventory commercial purpose:

```text
ADMINISTER_INVENTORY_STOCK
```

Its protected owner-qualified access contract is:

| Exact access contract | Protected purpose | Standard allocation | Target family |
|---|---|---|---|
| `inventory/stock-administration-access@1` | `ADMINISTER_INVENTORY_STOCK` | BUSINESS + GROWTH | `OPERATION_ACCESS` |

FREE SHALL NOT receive this protected purpose through the standard catalogue.

The following bounded contracts SHALL require no independent Commercial Entitlement:

```text
inventory/merchant-observation-access@1

inventory/commitment-support-access@1

inventory/protective-stock-restriction-access@1

inventory/returned-stock-resolution-access@1
```

Missing classification is not an exemption.

---

# 2. Ownership Boundary

Inventory continues to own:

```text
Inventory Stock Position

Inventory Movement

Inventory Claim

Inventory Claim Resolution

available-to-promise derivation

ReturnedStockReceipt

ReturnedStockDisposition

sellable stock re-entry
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

Ordering continues to own Order commitment.

Order Fulfilment continues to own satisfaction truth.

Returns continues to own only its accepted merchant-policy responsibility.

Commercial classification SHALL NOT transfer any of those authorities.

---

# 3. Protected Stock Administration

`inventory/stock-administration-access@1` SHALL govern ordinary merchant use of Inventory to establish or deliberately administer stock.

It includes, where otherwise valid under composite MS-PROT-058:

```text
inventory.position.establish

ordinary inventory.position.adjust

inventory.transfer
```

and comparable future operations only where a later accepted Inventory authority explicitly places them under this exact commercial purpose.

---

# 4. Position Establishment

`inventory.position.establish` SHALL require:

```text
ADMINISTER_INVENTORY_STOCK
```

Establishing a new Inventory Position introduces authoritative Inventory administration for an exact:

```text
Merchant Scope
+
stock-bearing subject
+
Inventory scope
```

It therefore belongs to BUSINESS + GROWTH.

Commercial permission does not establish Inventory applicability.

Canonical:

```text
Inventory semantically applicable
+
current actor authority
+
ADMINISTER_INVENTORY_STOCK
+
valid opening stock evidence
        ↓
position may be established
```

A paid grant SHALL NOT manufacture Inventory tracking for a non-stock-bearing or otherwise inapplicable subject.

---

# 5. Ordinary Stock Adjustment

Ordinary merchant-directed Inventory adjustment SHALL require:

```text
ADMINISTER_INVENTORY_STOCK
```

This includes accepted operational uses such as:

```text
RECEIPT

INTERNAL_CONSUMPTION

ordinary merchant-entered DELTA adjustment

upward CORRECT_TO

other accepted adjustment whose effect
actively administers usable Inventory
rather than merely restricts overstated stock
```

The existing cause, quantity, provenance and active-claim rules remain mandatory.

Commercial permission SHALL NOT legitimise an otherwise invalid adjustment.

---

# 6. Inventory Transfer

`inventory.transfer` SHALL require:

```text
ADMINISTER_INVENTORY_STOCK
```

A transfer deliberately reallocates merchant stock between Inventory scopes.

It is ordinary Inventory operation, not residual commitment resolution.

Therefore commercial loss SHALL deny new discretionary transfers unless another independently valid grant source supplies the protected Inventory purpose.

The operation still requires:

```text
valid source position

valid destination position

compatible stock-bearing semantics

sufficient unprotected quantity

actor authority

atomic source/destination consequence
```

Commercial permission supplies none of those facts.

---

# 7. Merchant Observation

GrandRue SHALL define:

```text
inventory/merchant-observation-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

It covers otherwise-authorised merchant inspection of existing Inventory truth, including where applicable:

```text
current stock position

current remaining claims

available-to-promise derivation

retained movement evidence

retained claim-resolution evidence

returned-stock receipt/disposition evidence

authorised logical-operation results
```

This exemption does not permit mutation.

It SHALL NOT create:

```text
public stock access

cross-Merchant access

arbitrary export authority

actor privileges

customer Inventory access

unbounded retained-history access
```

A downgrade therefore does not make existing stock records disappear merely because ordinary stock administration is no longer commercially available.

---

# 8. Commitment-Support Access

GrandRue SHALL define:

```text
inventory/commitment-support-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

for the bounded Inventory Claim operations:

```text
inventory.claim

inventory.claim.release

inventory.claim.expire

inventory.claim.fulfil
```

when invoked as an exact supporting consequence of an independently authorised owner-qualified use.

---

# 9. Why Claim Establishment Has No Independent Inventory Entitlement

`inventory.claim` does not create an independent merchant business proposition.

It protects capacity for another accepted use.

Canonical:

```text
Order commitment operation
        ↓
Ordering commercial permission
+
all Ordering predicates
        ↓
Inventory Claim required
        ↓
Inventory performs supporting
stock-protection authority
```

The same principle applies to another future accepted claim-owning use.

Therefore the Inventory Claim itself SHALL NOT introduce another independent paid prerequisite.

This does **not** mean a FREE merchant may create Orders through Inventory.

Without the owning operation's required commercial permission:

```text
owning use is not authorised
        ↓
inventory.claim has no legitimate
owning-use authority
        ↓
claim does not commit
```

An Inventory Claim is supporting stock authority, not a commercial bypass.

---

# 10. Claim Release

`inventory.claim.release` SHALL require no independent Commercial Entitlement when resolving an existing authoritative claim.

Release:

```text
reduces reserved quantity
increases available-to-promise
does not increase stock-on-hand
```

and may be required after:

```text
Order release

Booking/customer-commitment release
where applicable

other owner-qualified commitment reduction
```

Commercial loss SHALL NOT trap stock behind a claim that the owning business commitment has legitimately released.

The owning capability remains responsible for why the release is valid.

---

# 11. Claim Expiry

`inventory.claim.expire` SHALL require no independent Commercial Entitlement.

Expiry resolves already-established reservation authority under the applicable accepted owner/policy.

It does not establish new stock or new customer commitment.

This exemption creates no:

```text
universal claim TTL

automatic expiry schedule

merchant right to expire arbitrary claims
```

All accepted expiry authority remains mandatory.

---

# 12. Claim Fulfilment

`inventory.claim.fulfil` SHALL require no independent Commercial Entitlement when consuming stock as the Inventory consequence of an independently authorised existing fulfilment/use.

Canonical:

```text
existing authoritative commitment
        ↓
valid fulfilment/use authority
        ↓
inventory.claim.fulfil
        ↓
FULFILLED claim resolution
+
Inventory Movement
+
stock-on-hand reduction
```

A merchant SHALL NOT be forced to renew a subscription merely to honour a customer commitment legitimately established while entitled.

The no-entitlement classification applies only to the Inventory supporting consequence.

The owning fulfilment operation retains its own commercial classification and all other authority.

---

# 13. No Claim Wildcard

`inventory/commitment-support-access@1` SHALL NOT permit arbitrary Inventory Claims.

Every claim SHALL still bind:

```text
exact Inventory position

exact quantity/unit

exact owning-use reference

legitimate owner-qualified use

current availability

current semantic applicability
```

A caller cannot create a stock reservation merely by supplying a fabricated use identifier.

No-entitlement classification is not semantic authority.

---

# 14. Protective Stock Restriction

GrandRue SHALL define:

```text
inventory/protective-stock-restriction-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

for a narrowly bounded adjustment whose sole effect is to prevent GrandRue from overstating stock already believed or observed to exist.

Eligible effect classes are limited to accepted Inventory operations that:

```text
do not establish a new Inventory Position

do not increase stock-on-hand

do not transfer stock to another scope

do not establish a new Inventory Claim

do not create new sellable capacity

do not represent ordinary INTERNAL_CONSUMPTION
or another discretionary paid operation
```

and whose source evidence supports an accepted restrictive cause such as:

```text
DAMAGE

WASTE

downward physical CORRECTION
```

where otherwise semantically valid.

---

# 15. Reason for Protective Restriction

Commercial loss SHALL NOT force GrandRue to continue representing stock as sellable when current authoritative merchant evidence establishes that the physical stock is lower.

Example:

```text
GrandRue says on hand = 10

merchant physically counts = 6

BUSINESS entitlement has ended
```

GrandRue SHALL NOT require:

```text
keep claiming 10 exists
```

until the merchant purchases Inventory again.

A bounded downward correction may therefore preserve truthful, safer state.

This exemption cannot be used to obtain new Inventory operating capacity because it cannot increase or transfer stock.

---

# 16. Protective Restriction Is Not Free Inventory Administration

The exemption SHALL NOT cover:

```text
RECEIPT

upward CORRECT_TO

stock transfer

ordinary INTERNAL_CONSUMPTION

opening a new Inventory Position

creating replacement stock

adding sellable quantity

merchant-planned stock relocation
```

Those remain protected by:

```text
ADMINISTER_INVENTORY_STOCK
```

Where one requested adjustment contains both a restrictive consequence and a materially new administrative consequence, the protected commercial requirement applies to the complete authoritative mutation before commit.

---

# 17. Existing Claims Survive Protective Correction

The existing v1.1 rule remains:

```text
physical stock correction
    ≠ automatic claim release
```

If a protective downward correction produces:

```text
stock on hand
<
remaining active claims
```

then:

```text
physical stock truth remains true
claims remain true
new incompatible claims remain blocked
```

The no-entitlement correction does not resolve the operational shortfall.

Any commitment or fulfilment remediation remains separately governed.

---

# 18. Returned-Stock Resolution

GrandRue SHALL define:

```text
inventory/returned-stock-resolution-access@1
```

with:

```text
NO INDEPENDENT COMMERCIAL ENTITLEMENT
```

for exactly the existing bounded returned-stock operations:

```text
inventory.return.receive

inventory.return.disposition
```

within composite MS-PROT-058 v1.2.

---

# 19. Why Returned Stock Is Residual Inventory Truth

A customer may legitimately return goods after the merchant's paid Inventory entitlement has ended.

Physical reality does not depend on current subscription:

```text
goods were supplied historically

goods physically return
        ↓
merchant-controlled receipt evidence exists
```

GrandRue SHALL be able to preserve that fact.

Therefore:

```text
inventory.return.receive
```

requires no independent Commercial Entitlement.

It remains bounded by:

```text
exact prior physical-supply provenance

quantity bound

Merchant Scope

actor authority

duplicate protection
```

The contract cannot be used as a generic free stock-receipt operation.

---

# 20. Returned-Stock Disposition

`inventory.return.disposition` also requires no independent Commercial Entitlement for receipt quantity already authoritatively recorded.

This includes:

```text
SELLABLE_REENTRY

NO_SELLABLE_REENTRY
```

where otherwise valid.

A `SELLABLE_REENTRY` may increase sellable stock even after commercial downgrade because the increase is strictly bounded by:

```text
existing ReturnedStockReceipt
+
undisposed quantity
+
exact physical-supply provenance
```

It is restoration/disposition of already supplied physical goods, not general stock acquisition.

The operation SHALL NOT accept unrelated stock under the return pathway.

---

# 21. Return and Refund Remain Independent

The no-entitlement returned-stock contract does not grant:

```text
Refund

Order amendment

Return-policy approval

replacement fulfilment

Shipment

customer communication
```

Likewise a Refund does not authorise Inventory receipt or disposition.

Composite MS-PROT-058 v1.2 remains authoritative:

```text
physical receipt
    ≠ Refund
    ≠ sellable re-entry
```

---

# 22. Inventory and Ordering Remain Independent Commercial Boundaries

Possession of:

```text
ESTABLISH_ORDER_PURCHASE_COMMITMENT
```

does not itself grant:

```text
ADMINISTER_INVENTORY_STOCK
```

A merchant whose operation genuinely needs both obtains both through BUSINESS/GROWTH's explicit grant set.

However, an otherwise-authorised Order commitment may consume:

```text
inventory/commitment-support-access@1
```

without creating another independent Inventory paywall around its required claim.

Canonical:

```text
BUSINESS standard grant set
    contains explicit Ordering
    and Inventory administration grants

runtime operation
    still evaluates exact owner contracts
```

No plan-name implication is authoritative.

---

# 23. Inventory Without Ordering

Inventory remains independently applicable.

A merchant may legitimately need Inventory for:

```text
internal consumption

stock administration

physical stock control

other accepted non-Ordering operation
```

Therefore Inventory commercial access SHALL NOT be defined as merely:

```text
part of Ordering
```

`ADMINISTER_INVENTORY_STOCK` is independently bindable even though BUSINESS includes both portfolios in the standard catalogue.

This preserves the capability architecture.

---

# 24. Ordering Without Inventory

Likewise:

```text
orderable
    ≠
Inventory tracked
```

A valid Order may require no Inventory semantics.

Therefore the standard catalogue's Inventory grant SHALL NOT become a prerequisite for every Order.

Runtime consumes Inventory only where the merchant's actual configured semantics require it.

---

# 25. Configuration Independence

Commercial loss SHALL NOT remove Inventory applicability from Merchant Configuration.

Rejected:

```text
BUSINESS → FREE
        ↓
delete Inventory configuration
```

Canonical:

```text
Inventory remains part of
merchant operating model

+

ordinary Inventory administration
commercially unavailable

+

existing truth/resolution paths
remain governed as above
```

A later valid grant may restore ordinary Inventory administration without reconstructing the merchant's business model, subject to current compatibility.

---

# 26. Public and Customer Exposure

This amendment establishes no public Inventory access contract.

Composite MS-PROT-058 currently does not make exact stock or availability automatically PUBLIC.

Therefore:

```text
inventory/merchant-observation-access@1
```

SHALL NOT be reused for customers or public website delivery.

A future public/customer Inventory representation requires its own accepted owner-qualified Exposure/access authority before catalogue admission.

Product presentation under FREE does not itself grant public Inventory observation.

---

# 27. Retry and Recovery

Recovery of an already-committed Inventory operation result SHALL NOT be treated as a new protected stock-administration effect.

Example:

```text
Inventory receipt committed
acknowledgement lost
paid grant later ends
        ↓
authorised recovery returns
the committed result
```

A retry that would perform a new protected administrative effect SHALL satisfy current `ADMINISTER_INVENTORY_STOCK`.

Claim/returned-stock/protective-resolution retries retain their own no-independent-entitlement classifications and exact idempotency/currentness rules.

---

# 28. Failure Semantics

Inventory commercial evaluation SHALL remain distinguishable from Inventory business and technical outcomes.

At minimum:

```text
COMMERCIAL_PERMISSION_DENIED

COMMERCIAL_PERMISSION_UNRESOLVED

INVENTORY_NOT_APPLICABLE

INVENTORY_POSITION_NOT_ESTABLISHED

INSUFFICIENT_AVAILABLE_QUANTITY

CLAIM_RESOLUTION_EXCEEDS_REMAINING_QUANTITY

RETURN_SOURCE_NOT_ESTABLISHED

AUTHORISATION_REJECTION

AUTHORITATIVE_CONFLICT

TECHNICAL_FAILURE

EXECUTION_UNCERTAIN
```

Commercial denial SHALL NOT be reported as:

```text
out of stock

position absent

invalid quantity

claim conflict
```

unless that condition independently exists.

Missing Commercial Access Binding information SHALL fail closed.

---

# 29. Standard Allocation

The future canonical entitlement binding for:

```text
inventory/stock-administration-access@1
+
ADMINISTER_INVENTORY_STOCK
```

SHALL be granted explicitly by:

```text
BUSINESS

GROWTH
```

and SHALL NOT be granted by:

```text
FREE
```

through the standard catalogue.

This amendment does not mint the final `CommercialEntitlementIdentity`.

Commercial retains that responsibility under:

```text
MS-PROT-056-V17-DQ-001
```

---

# 30. Catalogue Consequences

The complete Commercial manifest SHALL record:

```text
protected:
    inventory/stock-administration-access@1
    +
    ADMINISTER_INVENTORY_STOCK

no independent entitlement:
    inventory/merchant-observation-access@1

    inventory/commitment-support-access@1

    inventory/protective-stock-restriction-access@1

    inventory/returned-stock-resolution-access@1
```

The manifest SHALL NOT convert the no-entitlement support contracts into wildcard authority.

Every consumer must still satisfy their semantic and actor requirements.

---

# 31. Review and Falsification

| Challenge | Required outcome |
|---|---|
| FREE merchant never used Inventory | No Inventory is manufactured |
| BUSINESS merchant establishes first stock position | Protected Inventory administration required |
| BUSINESS merchant receives ordinary new stock | Protected Inventory administration required |
| Merchant transfers stock between locations | Protected Inventory administration required |
| BUSINESS expires while stock records remain | Existing stock remains observable to authorised merchant |
| Existing Order has an active Inventory Claim | Claim remains authoritative |
| Existing Order is cancelled after downgrade | Claim release remains possible |
| Existing Order is fulfilled after downgrade | Claim fulfilment remains possible as supporting Inventory consequence |
| Merchant discovers 10 recorded but only 6 physically exist | Bounded downward protective correction remains possible |
| Merchant attempts to change 6 recorded to 10 after downgrade | Protected Inventory administration required |
| Merchant wants to transfer stock after downgrade | Denied absent another valid protected grant |
| Customer returns goods after downgrade | Physical returned-stock receipt remains recordable |
| Returned item is damaged | Non-sellable disposition remains recordable |
| Returned item is genuinely sellable | Sellable re-entry remains permitted only against bounded ReturnedStockReceipt quantity |
| Merchant labels ordinary new stock as a return | Missing physical-supply provenance rejects the path |
| FREE product is displayed on website | No public Inventory access is manufactured |
| Orderable digital service has no stock | No Inventory grant becomes an Order prerequisite |
| Salon tracks shampoo consumed internally | Inventory remains independently meaningful from Ordering |
| Two customers race for last claimed unit | Existing Inventory atomicity remains controlling |

The design survives downgrade, return, cross-capability and non-Ordering Inventory cases without turning Inventory into an Ordering submodule or allowing full stock administration on FREE.

---

# 32. Low-Software-Capacity Merchant Falsifier

A small shop owner should experience:

```text
BUSINESS:
    stock arrives
        → record stock

    stock damaged
        → record damage

    customer orders
        → GrandRue protects stock

    order fulfilled
        → GrandRue updates stock consequence
```

After downgrade:

```text
existing stock remains visible

existing orders can still finish

returned goods can still be recorded safely

wrongly overstated stock can still be reduced

but:

new ordinary stock administration
new transfers
new upward stock creation
    require BUSINESS again
```

The merchant need not understand:

```text
Inventory Claim
Commercial Access Binding
Claim Resolution
Residual Access
```

Those distinctions remain internal.

---

# 33. Alternatives Rejected

## One `INVENTORY_ENABLED` Boolean

Rejected because it collapses:

```text
Merchant Configuration
Commercial Entitlement
existing stock truth
claim resolution
Inventory semantics
```

## Protect every Inventory operation

Rejected because it could strand existing customer commitments, stale claims and returned physical goods after downgrade.

## Make all Inventory operations free after first use

Rejected because that would nullify the accepted BUSINESS allocation for Inventory administration.

## Ordering entitlement automatically grants Inventory administration

Rejected because Inventory is independently applicable and independently owned.

## Separate entitlement for every Inventory operation

Rejected as unnecessary commercial fragmentation.

## Treat returned sellable re-entry as ordinary paid stock receipt

Rejected because it could make legitimate return resolution depend on a later subscription even though the physical supply originated from an earlier valid business commitment.

## Allow unrestricted free correction

Rejected because upward correction or general stock administration would bypass the BUSINESS boundary.

---

# 34. Amendment Effect

This amendment closes the **Inventory owner-classification blocker** discovered during completion of `MS-PROT-056-V17-DQ-001`.

It does not:

```text
mint final CommercialEntitlementIdentity values

classify Payment commercial access

classify Order Fulfilment / Shipment commercial access

classify Returns commercial access beyond
the Inventory-owned returned-stock operations

create public Inventory exposure

publish the standard catalogue

resolve pricing

activate implementation
```

`MS-PROT-056-V17-DQ-001` remains OPEN until the remaining owner-classification blockers and complete Commercial manifest are accepted.

# Recommendation

**RECOMMENDATION: ACCEPT**
