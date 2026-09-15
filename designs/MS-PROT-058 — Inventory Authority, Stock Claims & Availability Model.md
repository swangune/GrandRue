# MS-PROT-058 — Inventory Authority, Stock Claims & Availability Model

**Document ID:** MS-PROT-058  
**Version:** 1.0  
**Status:** **ACCEPTED after graph-aware falsification and manual approval**  
**Closes:** DDR-OD-009 — Product / Offering / Inventory / variant boundary  
**Depends on:** MS-PROT-044 v1.1, MS-PROT-055, Ordering/Fulfilment authorities and applicable Resource/Allocation semantics  
**Purpose:** Define Inventory as the authoritative owner of stock truth, distinguish stock position from claims and derived availability, and preserve provenance across receipts, fulfilment, returns, transfers, damage, waste and manual corrections without requiring universal Product/Variant ownership or full event sourcing.

---

# 1. Governing principle

Inventory is an independently owned capability concerned with authoritative stock truth.

Inventory shall attach to the semantic subject that actually bears independently tracked stock.

That subject may be:

- Product;
- ProductVariant;
- another explicitly supported stock-bearing subject; or
- in simple cases, an Offering where no separate underlying Product identity is required and the Offering itself legitimately bears the stock semantics.

Main Street shall not require Inventory to belong universally to Product, ProductVariant, Offering or Listing.

> **Inventory ownership follows actual stock identity, not presentation hierarchy or conventional commerce object structure.**

---

# 2. Inventory is optional

Orderability does not imply finite inventory.

A merchant may expose an Offering that is:

```text
ORDERABLE
```

without:

```text
INVENTORY_TRACKED
```

Examples may include digital goods, information products, unlimited made-to-order goods, or other propositions whose availability is governed by a different capability.

Main Street shall not represent non-stocked goods using fake infinite quantities.

---

# 3. Stock position is not availability

Authoritative stock position, outstanding claims and customer-visible availability are distinct concepts.

Conceptually:

```text
STOCK POSITION
        -
AUTHORITATIVE OUTSTANDING CLAIMS
        -
OTHER APPLICABLE CONSTRAINTS
        ↓
DERIVED AVAILABLE QUANTITY
```

The exact calculation may vary by accepted Inventory policy/semantics, but the following invariant holds:

> **Available-to-sell quantity is a derived result, not the sole authoritative inventory fact.**

A merchant-facing/storefront availability projection shall not be treated as equivalent to current physical stock.

---

# 4. Inventory scope is explicit

Inventory truth shall be qualified by the applicable inventory scope.

Examples may include:

```text
Merchant location
warehouse
store
stockroom
other registered inventory scope
```

Example:

```text
Milk 2L

Swansea = 12
Cardiff = 7
```

Main Street shall not collapse this to:

```text
stock = 19
```

where fulfilment or operational availability depends on location.

Inventory scope shall remain explicit through allocation, ordering and fulfilment interactions.

---

# 5. Quantity semantics

Inventory quantities shall use registered quantity/unit semantics rather than assume integer item counts.

Examples:

```text
40 bottles
18.4 kg apples
125 litres fuel
8 dozen eggs
```

Quantity conversion shall occur only where Main Street has registered deterministic unit-conversion semantics.

Unregistered or ambiguous conversion shall not occur implicitly.

---

# 6. Authoritative stock claims

Inventory shall support authoritative stock claims/reservations where necessary to prevent incompatible commitments such as overselling.

Conceptually:

```text
on hand = 1
        ↓
Customer A claim = 1
        ↓
available = 0
```

A second concurrent customer shall not be able to establish an incompatible committed claim merely because an earlier storefront read observed one unit available.

The implementation shall preserve the applicable atomic invariant between:

- authoritative availability revalidation;
- stock claim/allocation; and
- the owning Order or commercial commitment.

The exact transactional boundary may use the established Main Street transactional-core pattern appropriate to the participating capabilities.

---

# 7. Displayed availability does not reserve stock

A storefront or merchant read projection showing:

```text
available = 1
```

creates no stock reservation by itself.

Before a customer commitment that requires stock is established, Main Street shall revalidate authoritative Inventory state.

Canonical flow:

```text
customer selection
        ↓
authoritative Inventory revalidation
        ↓
claim/allocation where required
        ↓
Order commitment
```

This mirrors Main Street's broader rule that derived availability shall not be mistaken for a commitment.

---

# 8. Order owns Order truth; Inventory owns stock truth

Ordering shall not maintain a competing authoritative stock count.

An Order may establish or own the business commitment that creates an Inventory claim.

Inventory owns:

- current stock position;
- applicable claims;
- inventory availability derivation; and
- stock movements/adjustments and their provenance.

Ordering owns:

- what the customer ordered;
- committed quantities and terms;
- Order lifecycle; and
- Order-specific business policy.

Reference or claim relationships do not transfer authority between capabilities.

---

# 9. Cancellation releases claims, not blindly stock

Where an Order is cancelled before fulfilment, Inventory shall release the Order-owned stock claim according to the applicable contract.

Rejected:

```text
order cancelled
    ↓
stock += ordered quantity
```

because the authoritative on-hand position may not yet have been decremented.

Correct principle:

```text
Order cancelled
    ↓
release applicable Inventory claim
    ↓
recalculate derived availability
```

This preserves the distinction between physical stock and reservation/claim state.

---

# 10. Inventory movement/provenance

A governing Inventory model consisting only of destructive quantity overwrites is insufficient.

Main Street shall preserve semantic cause/provenance sufficient to explain and audit material changes to current stock truth.

Registered causes may include, where supported:

- receipt;
- sale/fulfilment;
- return;
- damage;
- waste;
- manual correction;
- transfer;
- internal consumption; and
- other accepted causes.

The exact implementation may use:

```text
transactional current position
+
immutable movement/adjustment evidence
```

or another equivalent model.

Full event sourcing is **not** required by this authority.

---

# 11. Fulfilment consequence

Where fulfilment physically supplies or consumes stock, the owning Fulfilment/Order operation shall produce the appropriate authorised Inventory consequence.

Inventory shall record the actual stock-bearing subject and quantity affected.

The customer-visible Order commitment shall not be overwritten merely to make inventory accounting easier.

---

# 12. Returns do not automatically restore sellable stock

A returned item does not necessarily become available for sale.

Possible conditions include:

```text
damaged
opened
expired
quarantined
requires inspection
```

Therefore:

```text
RETURNED
    ≠
AVAILABLE_FOR_SALE
```

The Inventory model shall permit disposition/condition semantics where evidence requires them.

This authority does not freeze a universal warehouse-disposition catalogue.

Detailed disposition semantics remain capability-evidence driven.

---

# 13. Substitution preserves historical Order truth

Where fulfilment substitutes one product for another, Main Street shall preserve:

```text
what customer originally ordered
```

separately from:

```text
what was actually fulfilled
```

Inventory decrements the actual supplied stock-bearing subject.

Rejected:

```text
rewrite original Order line to substituted Product
```

merely for fulfilment/inventory convenience.

Substitution permission and customer/merchant policy remain separately governed.

---

# 14. Transfers preserve both sides

A stock transfer between inventory scopes shall preserve the source and destination consequences under one coherent provenance chain.

Example:

```text
Swansea → Cardiff
3 units
```

At minimum, the authoritative outcome must account for:

```text
Swansea -3
Cardiff +3
```

with shared transfer provenance.

Where transfer is not instantaneous, an in-transit representation may be required by future evidence. This authority does not mandate one prematurely.

---

# 15. Internal consumption

Inventory may be consumed by non-retail operations.

Example:

```text
Salon service
    consumes
Professional Shampoo Product
```

This is strong evidence that Inventory shall attach to actual stock-bearing subjects rather than only to customer-facing Offerings.

A service operation may therefore create an authorised Inventory consumption consequence without turning the consumed Product into a service Offering or ProductVariant.

---

# 16. Bundle availability

A bundle Offering assembled on demand may derive availability from component Inventory.

Example:

```text
Breakfast Bundle
    Coffee ×1
    Sandwich ×1
    Juice ×1
```

If components are the real stock-bearing subjects, bundle availability derives from those component positions/claims.

If a bundle is physically pre-packed and independently stocked, the bundle subject may instead carry Inventory directly.

No universal `BundleStock` abstraction is required.

---

# 17. Modifiers may consume inventory without owning inventory identity

A transaction-time modifier may cause Inventory consumption.

Example:

```text
Burger
    + extra cheese
```

The modifier does not need to become a ProductVariant merely because extra cheese consumption affects Inventory.

The owning Order/Fulfilment semantics may establish the appropriate inventory-consumption requirement against the actual stock-bearing Product.

---

# 18. ProductVariant and Inventory

Where a ProductVariant has independent stock identity, Inventory may attach directly to the ProductVariant.

Example:

```text
T-Shirt
    Black / Medium
        independently tracked stock
```

Where variants do not have independent stock identity, Inventory shall not be duplicated merely to preserve a variant hierarchy.

The rule is operational identity, not object nesting.

---

# 19. Specific physical units

Some Inventory domains may require specific physical instance identity, such as serialised equipment.

Example:

```text
Laptop Product
    serial ABC123
    serial XYZ987
```

Specific physical instances are not ProductVariants merely because they are individually identifiable.

Where required, specific-unit identity shall use the appropriate Resource/Inventory semantics.

This authority preserves that boundary without mandating a full serialised-inventory model now.

---

# 20. Inventory availability is a projection/derived decision

Customer-facing and merchant-facing availability may derive from:

- current stock position;
- current authoritative claims;
- inventory scope;
- disposition/condition where applicable;
- merchant policy; and
- other registered deterministic constraints.

The resulting availability is suitable for read/presentation and pre-commit decision support.

Before a conflicting commitment is established, authoritative Inventory state must be revalidated.

---

# 21. Transactional core and event-driven reactions

Where stock invariants require atomicity, Main Street shall use the transactional core appropriate to the participating capability boundaries.

Post-commit facts may then drive independent reactions such as:

- analytics;
- notifications;
- projections; and
- replenishment suggestions.

Events shall not replace the authoritative mutation needed to prevent overselling.

This preserves Main Street's composite architecture.

---

# 22. AI boundary

AI may assist merchants with Inventory operations such as:

- interpreting a natural-language stock adjustment request;
- suggesting likely Product/ProductVariant matches;
- explaining low-stock conditions;
- proposing transfers or replenishment; and
- helping configure inventory-related settings.

AI shall not:

- invent new stock-bearing semantics;
- create ProductVariant identity merely from ambiguous text;
- bypass merchant/manual validation where required;
- directly overwrite authoritative Inventory position without an authorised operation; or
- resolve unsupported unit conversions probabilistically.

Specialist agents infer; deterministic Inventory operations execute.

---

# 23. Rejected models

The following are rejected:

- `Offering.stock` as the universal Inventory model;
- Inventory universally owned by Product;
- Inventory universally owned by ProductVariant;
- inventory quantity represented only as integer counts;
- displayed availability treated as authoritative stock;
- storefront reads creating reservations;
- Ordering maintaining competing stock truth;
- cancellation blindly incrementing stock;
- destructive quantity updates with no material provenance;
- returned stock automatically becoming sellable;
- substitution rewriting historical Order truth;
- ProductVariant used for serialised physical instances;
- fake infinite Inventory for non-stocked Offerings; and
- universal event-sourced Inventory mandated without evidence.

---

# 24. Falsification record

The accepted Inventory boundary was tested against:

- simple retail goods;
- concurrent last-unit ordering;
- location-scoped stock;
- quantity units such as kg/litre/dozen;
- order cancellation;
- fulfilment consumption;
- returns and unsellable condition;
- substitutions;
- location transfers;
- bundles assembled from components;
- pre-packed bundles;
- transaction-time modifiers that consume stock;
- hybrid merchants selling and internally consuming the same Product;
- ProductVariants with independent stock; and
- serialised physical equipment.

The initial candidate `Offering.stock = quantity` failed because it could not safely represent claims, location scope, provenance, returns, transfers or internal consumption.

The revised position/claim/provenance model survived the tested cases without requiring a universal e-commerce hierarchy or full event sourcing.

---

# 25. Validation matrix

| Constraint | Result |
|---|---|
| Inventory ownership follows actual stock identity | PASS |
| Inventory remains optional | PASS |
| Position and derived availability separated | PASS |
| Authoritative claims prevent incompatible commitments | PASS |
| Location/scope-aware stock supported | PASS |
| Quantity/unit semantics supported | PASS |
| Order and Inventory authority remain separate | PASS |
| Cancellation releases claims correctly | PASS |
| Inventory movements preserve provenance | PASS |
| Full event sourcing not required | PASS |
| Returns need not restore sellable stock | PASS |
| Substitutions preserve original Order truth | PASS |
| Transfers preserve source/destination consequences | PASS |
| Internal service consumption supported | PASS |
| Bundles do not force bundle-owned stock | PASS |
| Modifiers can affect stock without becoming Variants | PASS |
| ProductVariant stock remains optional/operationally justified | PASS |
| Serialised physical units remain distinct from Variants | PASS |
| Composite architecture preserved | PASS |

---

# 26. Accepted result

DDR-OD-009 is fully closed by MS-PROT-044 v1.1 plus this authority.

The accepted architecture is:

```text
OFFERING
    commercial proposition

PRODUCT
    optional durable good/material subject

PRODUCT VARIANT
    optional independently meaningful Product form

INVENTORY
    authoritative stock position
    + claims
    + scope
    + provenance

AVAILABLE QUANTITY
    derived result

ORDER
    customer commitment

FULFILMENT
    actual supply/consumption truth
```

> **Main Street shall model Inventory according to actual stock-bearing identity and operational invariants. Product, ProductVariant, Offering and Inventory are related but not hierarchically compulsory. Availability is derived, claims are authoritative where needed, and historical Order/Fulfilment truth shall not be rewritten merely to simplify stock accounting.**
