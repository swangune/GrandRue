# MS-PROT-058 v1.2 — Returned Stock Receipt, Disposition & Sellable Re-entry Contract Amendment

**Document ID:** MS-PROT-058  
**Version:** 1.2  
**Status:** ACCEPTED  
**Approved:** 27 August 2026 — explicit manual approval of the complete revised Target-15 package  
**Authority type:** Semantic/design amendment  
**Governed by:** `DESIGN-RULES.md` v2.1; `DOCUMENT-GOVERNANCE.md`  
**Amends:** composite MS-PROT-058 through v1.1  
**Depends on:** MS-PROT-025; MS-PROT-027; MS-PROT-044 v1.1; composite MS-PROT-058 through v1.1; composite MS-PROT-060 through v1.1; composite MS-PROT-061 through v1.1; MS-PROT-062; MS-PROT-064; MS-PROT-069; MS-PROT-072; composite MS-PROT-077 through v1.1  
**Closes with MS-PROT-061 v1.1:** Inventory-owned Target-15 returned-stock semantics

---

## 1. Governing Decision

Physical returned-stock truth belongs to Inventory and is **independent of Returns capability applicability**.

Canonical:

```text
Returns enabled?
    irrelevant to physical stock truth

merchant physically receives identifiable goods
        ↓
Inventory records ReturnedStockReceipt
        ↓
merchant determines Inventory disposition
        ↓
only SELLABLE_REENTRY increases sellable stock
```

Therefore:

```text
Returns disabled
    ≠ Inventory cannot receive returned goods
```

---

## 2. ReturnedStockReceipt

A `ReturnedStockReceipt` is an immutable Inventory fact establishing that an exact quantity of identifiable previously supplied goods physically re-entered merchant control.

It retains:

```text
receipt identity
MerchantScope
physical-supply provenance
stock-bearing subject
quantity/unit
receiving scope
receivedAt
merchant-authoritative receipt provenance
logical command identity
```

---

## 3. Receipt Does Not Mean Sellable

Hard separation:

```text
physical receipt
    ≠ sellable stock
```

Receipt does not automatically change stock-on-hand available for sale.

---

## 4. Physical-Supply Provenance

Where available, receipt references the exact prior physical supply:

```text
Order Fulfilment Satisfaction Portion
```

or an independently supplied remedial/replacement movement and its Inventory provenance.

This matters because a replacement item may have left Inventory without increasing Order Fulfilment quantity.

---

## 5. Quantity Bound

For an exact supplied physical quantity:

```text
sum(ReturnedStockReceipt quantities)
<=
authoritative physical quantity supplied
```

unless a separately authorised correction establishes that prior evidence was wrong.

Duplicate receipt must not double stock.

---

## 6. `inventory.return.receive`

Owner: Inventory.

It requires:

```text
Inventory applicability
trusted merchant actor
Actor Authorisation
exact physical-supply provenance
quantity/unit compatibility
MerchantScope
prior return-receipt evidence
```

It does **not** require:

```text
Returns capability enabled
Refund
return label
customer return workflow
```

Success records one immutable receipt.

---

## 7. Courier Evidence

Courier evidence alone cannot invoke `inventory.return.receive` authoritatively.

Merchant-controlled physical receipt evidence is required.

---

## 8. Undisposed Quantity

For each receipt:

```text
undisposed quantity
=
receipt quantity
-
sum(authoritative disposition quantities)
```

Undisposed stock is physically present but is not automatically available-to-promise.

---

## 9. ReturnedStockDisposition

An immutable quantity-bearing Inventory fact establishes treatment of receipt quantity.

Each disposition classifies its stock effect as:

```text
SELLABLE_REENTRY
NO_SELLABLE_REENTRY
```

Merchant-facing registered semantics may include:

```text
sellable
damaged
quarantine
repair
dispose
waste
```

without creating a universal status machine.

---

## 10. `inventory.return.disposition`

Owner: Inventory.

It validates:

```text
ReturnedStockReceipt
current undisposed quantity
registered disposition semantic
target Inventory position if required
unit/scope compatibility
trusted merchant actor
```

Invariant:

```text
0 < disposition quantity
<= undisposed quantity
```

Concurrent dispositions cannot over-dispose receipt quantity.

---

## 11. SELLABLE_REENTRY

For `SELLABLE_REENTRY`, one atomic Inventory transaction records:

```text
ReturnedStockDisposition
+
positive Inventory Movement
    cause = RETURN
+
stock-on-hand increase
```

---

## 12. Meaning of Inventory `RETURN`

The previously deferred `RETURN` cause now means:

> An authorised positive sellable-stock movement backed by an existing ReturnedStockReceipt and an explicit `SELLABLE_REENTRY` disposition.

It does **not** mean:

```text
customer requested return
courier says delivered
Refund issued
Returns capability enabled
```

---

## 13. NO_SELLABLE_REENTRY

A non-sellable disposition records Inventory truth but creates no sellable stock.

Examples may include damaged, quarantine or disposal.

---

## 14. Partial Disposition

Example:

```text
receipt = 5

3 sellable
1 damaged
1 undecided
```

Result:

```text
sellable increase = 3
non-sellable disposition = 1
undisposed = 1
```

---

## 15. Old Inventory Claim Remains Historical

Returned goods do not:

```text
reopen original Inventory Claim
delete FULFILLED resolution
reverse original outbound movement
```

The return is a later fact.

---

## 16. Order/Fulfilment Remain Historical

Returning goods does not reduce historical Order Fulfilment quantity.

The fact that goods were supplied remains true.

---

## 17. Refund Remains Independent

Refund does not create physical receipt.

Physical receipt does not create Refund.

---

## 18. Non-Inventory Goods

If Inventory does not apply to the relevant supplied subject, merchant return handling remains possible without fake Inventory objects.

---

## 19. Reads

Initial returned-stock reads remain Inventory owner queries with request-scoped derivation.

No special Returns/Inventory projection is initially required.

---

## 20. Exposure

ReturnedStockReceipt and disposition are merchant/internal facts by default.

No customer-facing:

```text
"We received your return"
```

is inferred without an accepted Exposure contract.

---

## 21. AI Boundary

AI may prepare candidate receipt/disposition commands.

AI cannot:

```text
invent physical receipt
infer sellability from tracking
invent quantity
restock automatically
```

---

## 22. Failure Classes

At least:

```text
VALIDATION_REJECTION
AUTHORISATION_REJECTION
RETURN_SOURCE_NOT_ESTABLISHED
RETURN_QUANTITY_EXCEEDS_SUPPLIED_QUANTITY
RETURN_RECEIPT_IDENTITY_CONFLICT
RETURN_DISPOSITION_EXCEEDS_UNDISPOSED_QUANTITY
INVENTORY_POSITION_NOT_ESTABLISHED
AUTHORITATIVE_CONFLICT
TECHNICAL_FAILURE
EXECUTION_UNCERTAIN
```

---

## 23. Falsification

| Scenario | Result |
|---|---|
| Returns disabled; item physically returned | Inventory receipt valid |
| Courier says returned but merchant has not received it | no receipt |
| Merchant receives 2 of 5 | receipt 2 |
| Same receipt retried | one receipt |
| Two staff double-record same physical item | invariant prevents excess |
| Received item damaged | no sellable stock |
| Merchant marks item sellable | positive RETURN movement |
| Original item plus replacement both return | each physical supply has provenance |
| Refund already happened | Inventory still independent |
| No Refund ever happens | Inventory still valid |
| Original outbound claim fulfilled | remains fulfilled |
| Non-Inventory-tracked subject | no fake Inventory |

---

## 24. Rejected Alternatives

Rejected:

```text
Returns capability owns returned stock

Returns disabled blocks Inventory receipt

courier tracking increments stock

Refund increments stock

physical receipt automatically sellable

reopen original Inventory Claim

reverse old historical movement

one returned-stock status
```

---

## 25. Deferred Scope

Deferred:

```text
serialised returned-unit identity
lot/batch returns
repair workflow
quarantine workflow
return-to-vendor
supplier returns
warehouse reverse logistics
customer-facing receipt projection
automated inspection
condition-scoring AI
exact SQL
exact API
```

---

## 26. Conformance

```text
[ ] returned stock remains Inventory-owned

[ ] Returns applicability is irrelevant
    to physical Inventory truth

[ ] receipt requires actual merchant-controlled evidence

[ ] receipt does not automatically become sellable

[ ] duplicate/partial receipt is quantity-safe

[ ] disposition is quantity-bearing

[ ] SELLABLE_REENTRY alone creates positive RETURN movement

[ ] RETURN has exact provenance

[ ] original claim and outbound movement remain historical

[ ] Refund and Inventory remain independent

[ ] Order Fulfilment remains historical truth
```

---

## 27. Governing Principle

> **Returned goods become Inventory truth because they physically re-enter merchant control, not because Main Street's Returns capability was enabled. Physical receipt, sellable disposition and positive stock re-entry are separate Inventory facts. Structured Returns policy may streamline how a merchant reaches that situation, but it neither creates nor gates physical stock truth.**
