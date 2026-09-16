# MS-PROT-044 v1.1 — Product, Offering & Variant Boundary Amendment

**Document ID:** MS-PROT-044  
**Version:** 1.1  
**Status:** **ACCEPTED after graph-aware falsification and manual approval**  
**Amends:** MS-PROT-044 v1.0  
**Closes in part:** DDR-OD-009 — Product / Offering / Inventory / variant boundary  
**Depends on:** MS-PROT-044 v1.0, MS-PROT-055 and applicable Ordering/Inventory authorities  
**Purpose:** Define when Product and ProductVariant are justified in Main Street, preserve Offering as the merchant-controlled commercial proposition, and prevent conventional e-commerce hierarchies from becoming universal platform semantics.

---

# 1. Governing principle

Main Street shall not assume the conventional hierarchy:

```text
Product
  ↓
Variant
  ↓
SKU
  ↓
InventoryItem
```

as a universal domain model.

The accepted rule is:

> **Problem semantics and operational invariants determine whether Product, ProductVariant or another representation is required. Offering remains the merchant-controlled commercial proposition. Product and ProductVariant are introduced only when they own materially distinct business meaning.**

---

# 2. Offering remains the commercial proposition

MS-PROT-044 v1.0 remains authoritative that Offering represents what a merchant makes available for a supported customer interaction.

Examples include:

```text
60-minute consultation
Bottle of milk
Oil change
Room category
Equipment rental option
```

Offering shall not be replaced by Product merely because the interaction involves commerce.

A service Offering may require no Product semantics at all.

---

# 3. Product is optional, not universal

A Product is justified where Main Street must reason about a durable merchant-controlled good or material subject independently of a particular commercial proposition.

Examples of evidence that may justify Product identity include:

- the same good participates in multiple Offerings;
- inventory is tracked independently of one commercial proposition;
- the good is consumed internally as well as sold;
- persistent good/material identity matters operationally; or
- another capability must refer to the good independently of a customer-facing Offering.

Example:

```text
Product
Professional Shampoo
      │
      ├── Offering
      │   "Buy shampoo"
      │
      └── internal consumption
          by salon service
```

Product shall not be created merely because something can be ordered.

---

# 4. Offering may be sufficient as the business subject

Where no independent Product identity is required, the Offering may itself be the sufficient durable subject.

Example:

```text
Offering
Coca-Cola 500ml
£1.80
```

A separate Product shall not be required solely to satisfy a conventional commerce object hierarchy.

This preserves the MS-PROT-044 v1.0 rule that an Offering may itself be sufficient as the business subject.

---

# 5. Multiple Offerings for one Product

Where one durable Product participates in multiple propositions, those propositions remain separate Offerings.

Example:

```text
Product
Coffee Beans 1kg
      │
      ├── Retail Offering
      │   £18 each
      │
      └── Wholesale Offering
          commercial case terms
```

The Product owns durable good identity.

Each Offering owns its commercial proposition and applicable commercial terms.

A change to one Offering shall not silently redefine the Product or another Offering.

---

# 6. ProductVariant is Product-bounded, not platform-universal

`ProductVariant` may be introduced within Product semantics where a selectable product form has independent operational identity.

Evidence may include distinct:

- inventory;
- SKU or barcode;
- commercial terms;
- availability;
- fulfilment requirements; or
- other accepted Product-specific operational behaviour.

Example:

```text
T-Shirt Product
    │
    ├── Black / Medium
    │      stock = independently tracked
    │
    └── White / Medium
           stock = independently tracked
```

Such forms may legitimately be ProductVariants.

---

# 7. Variant shall not become a universal selection abstraction

Main Street shall not use ProductVariant merely because a customer chooses among values.

The following do not automatically imply Variant identity:

```text
size
colour
portion preference
extra topping
no onions
service option
appointment duration choice
room category
```

The correct representation depends on the owning capability and whether the selected form has independent operational identity.

Therefore:

> **Selectable does not imply Variant.**

---

# 8. Modifiers are not ProductVariants

Transaction-time configuration or modifiers shall remain distinct from ProductVariant where they do not represent a durable product form.

Example:

```text
Burger Offering
    + extra cheese
    + bacon
    - onions
```

The Order may preserve those modifier selections while the underlying Offering remains Burger.

A modifier may have commercial or fulfilment consequences and may even cause inventory consumption, but that does not by itself make the modifier a ProductVariant.

---

# 9. ProductVariant is not a physical instance

A ProductVariant describes a durable product form.

It shall not be used merely to identify a specific physical item instance.

Example:

```text
Laptop Product
    variant: 16GB / 512GB

physical units:
    serial ABC123
    serial XYZ987
```

The serialised physical instances are not ProductVariants merely because they are distinct units.

Specific-unit semantics belong to the appropriate Resource/Inventory model where justified.

---

# 10. SKU and barcode are identifiers/value semantics

SKU, barcode, EAN and similar codes are normally identifiers or value semantics associated with the relevant Product, ProductVariant or other legitimate stock-bearing subject.

They are not Operational Objects merely because they are stored separately in some commerce systems.

A new entity/lifecycle is justified only if later domain evidence establishes independent authority or behaviour.

---

# 11. Quantity and unit are reusable value semantics

Authoritative quantities shall not be restricted to integer item counts.

Main Street shall support quantity semantics sufficient for cases such as:

```text
18.4 kg apples
8 dozen eggs
125 litres fuel
40 bottles
```

Conceptually:

```text
Quantity
    amount
    unit
```

Registered conversion semantics may be introduced where required.

Quantity/unit values do not create ProductVariant identity merely because a size or unit appears in product data.

---

# 12. Pack size may justify independent product form

A pack size may become a ProductVariant or other Product-owned form when it has independent operational meaning.

Example:

```text
Water

Single bottle
    barcode A
    price £1
    independent stock

6-pack
    barcode B
    price £5
    independent stock
```

This differs from a purely descriptive size attribute with no independent commercial or inventory consequence.

---

# 13. Bundles do not determine inventory ownership

A commercial bundle may be an Offering whose fulfilment depends on components.

Example:

```text
Breakfast Bundle
    Coffee ×1
    Sandwich ×1
    Juice ×1
```

If the bundle is assembled on demand, availability may derive from component inventory.

If the merchant physically pre-packs hampers or bundles as independently stocked goods, those bundle units may themselves be legitimate inventory-bearing subjects.

Therefore:

> **Bundle semantics do not decide inventory ownership; actual operational stock identity does.**

---

# 14. Digital and non-stocked Offerings

An orderable Offering does not imply inventory tracking.

Digital goods, information products or effectively unlimited goods may be orderable without finite stock semantics.

Rejected:

```text
orderable = inventory quantity infinity
```

Correct:

```text
ORDERABLE
    ≠
INVENTORY_TRACKED
```

---

# 15. Hybrid merchant consequence

A merchant may use the same Product in multiple operational contexts.

Example salon:

```text
Product
Professional Shampoo
       │
       ├── sold to customer
       └── consumed during service
```

This is evidence that Product can legitimately own durable good/material identity independently of a retail Offering.

No business-specific subclass such as `SalonProduct` is required.

---

# 16. Ordering and historical commercial truth

Order commitments shall preserve what the customer actually selected and the commercial terms applicable at commitment time.

A later Product, ProductVariant or Offering change shall not silently rewrite an existing Order.

Substitution shall not overwrite the original ordered subject merely for fulfilment or inventory convenience.

Example:

```text
Order:
Whole Milk 2L

Fulfilment:
Semi-skimmed 2L substituted
```

The Order retains original committed truth.

Fulfilment records actual supplied truth.

Inventory records actual stock consequence.

---

# 17. ADT selection rule

Main Street shall choose the semantic representation that preserves the real invariants rather than preserve an initially proposed ADT.

Applied to this cluster:

```text
Offering
    proposition

Product
    optional durable good/material subject

ProductVariant
    optional independently meaningful product form

Modifier
    transaction-time configuration where applicable

SKU / barcode
    identifier/value semantics

Quantity / unit
    reusable value semantics
```

These abstractions shall not be collapsed solely for implementation convenience.

---

# 18. Rejected models

The following are rejected:

- Product as parent of every Offering;
- Product required for every orderable proposition;
- ProductVariant as a universal Main Street primitive;
- every selectable option becoming a Variant;
- modifiers represented as Variants by default;
- serialised physical units represented as ProductVariants;
- SKU/barcode as Operational Objects without independent semantics;
- Listing required for product catalogue presentation;
- digital orderability represented through fake infinite inventory; and
- business-category-specific Product hierarchies where composition suffices.

---

# 19. Falsification record

The accepted boundary was tested against:

- consultancy services;
- simple retail goods;
- size/colour combinations;
- restaurant modifiers;
- hotel room categories;
- pack-size products;
- bundles assembled on demand;
- pre-packed bundles;
- multiple Offerings for one Product;
- digital goods;
- serialised physical equipment; and
- hybrid merchants that both sell and internally consume the same Product.

The conventional `Product → Variant → SKU → InventoryItem` hierarchy failed as a universal model.

The accepted composition survived the tested scenarios without requiring business-category runtime branches.

---

# 20. Accepted result

The accepted Product/Offering boundary is:

```text
OFFERING
    merchant commercial proposition

PRODUCT
    optional durable good/material subject
    where independent identity is required

PRODUCT VARIANT
    optional Product-owned form
    only where independent operational identity exists

MODIFIER
    separate transaction-time selection semantics

IDENTIFIERS / QUANTITY
    value semantics rather than entities by default
```

> **Main Street shall introduce Product and ProductVariant only when the business invariants require them. Offering remains the proposition. Product remains optional. Variant remains Product-bounded. Selection, inventory and presentation shall not dictate semantic identity merely because conventional commerce platforms often model them together.**
