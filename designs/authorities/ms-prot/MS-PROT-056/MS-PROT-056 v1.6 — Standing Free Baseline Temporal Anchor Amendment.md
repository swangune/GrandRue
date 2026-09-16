# MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment

**Document ID:** MS-PROT-056  
**Version:** 1.6  
**Status:** ACCEPTED after governed review, falsification and manual approval  
**Amends:** MS-PROT-056 v1.0–v1.5 within Standing Free source establishment/temporal anchoring only  
**Depends on:** MS-PROT-026, MS-PROT-056 v1.0–v1.5, MS-PROT-071 v1.2, MS-PROT-072  
**Purpose:** Define the authoritative origin, temporal anchor, plan-revision affinity, idempotency and post-commit establishment semantics of the permanent Standing Free commercial baseline without modelling it as a paid Merchant Commercial Agreement or deriving its history from asynchronous processing time.

---

## 1. Governing decision

Standing Free remains an independent Commercial-owned entitlement source.

It is established as a required post-commit consequence of authoritative Merchant Account establishment.

Canonical path:

```text
MerchantAccountEstablishment
    establishedAt = T0
        ↓
MerchantAccountEstablished
        ↓ durable post-commit reaction
Commercial
        ↓
FREE plan revision effective at T0
        ↓
StandingFreeBaseline
    effectiveFrom = T0
```

The Commercial reaction may execute after `T0`; its execution/delivery time MUST NOT redefine the effective start of Standing Free or select a later mutable FREE plan revision.

---

## 2. Semantic ownership

Merchant Account owns:

```text
that the Merchant Account was established
when authoritative establishment occurred
```

Commercial owns:

```text
what Standing Free means
which versioned FREE plan revision governs the baseline
which entitlement definitions that revision grants
Standing Free grant provenance
effective Commercial Entitlement derivation
```

Therefore:

```text
MerchantAccountEstablishment
    ≠ StandingFreeBaseline
```

The two facts have causal/provenance affinity but retain separate owners.

---

## 3. Standing Free baseline fact

Conceptually:

```text
StandingFreeBaseline
{
    baselineIdentity
    merchantScope
    originatingMerchantAccountEstablishmentIdentity
    effectiveFrom
    freePlanRevisionIdentity
    entitlementSnapshot
}
```

Initial policy has no ordinary expiry:

```text
effectiveUntilExclusive = absent
```

Hard invariants:

```text
StandingFreeBaseline.effectiveFrom
    = originating MerchantAccountEstablishment.establishedAt

StandingFreeBaseline.freePlanRevisionIdentity
    = FREE plan revision effective at effectiveFrom

StandingFreeBaseline.entitlementSnapshot
    = explicit entitlement set of that FREE plan revision
```

The plan revision is resolved against its accepted effective policy window at the authoritative establishment instant. Delayed processing MUST NOT bind the baseline to whichever FREE revision happens to be current when a worker later executes.

Once established, the baseline retains its exact plan-revision identity and entitlement snapshot for historical reproducibility. Later changes to FREE packaging do not silently rewrite the historical meaning of an already-established baseline; any future policy that migrates existing Standing Free baselines requires its own accepted Commercial transition semantics.

---

## 4. No fake FREE agreement

Standing Free MUST NOT be represented as a synthetic paid-style `MerchantCommercialAgreement` merely because it contributes entitlements.

Rejected:

```text
fake Commercial Agreement
plan = FREE
payment = none
```

Accepted:

```text
StandingFreeBaseline source
+
Trial source
+
Paid Agreement source
+
other accepted Commercial sources
        ↓
Effective Commercial Entitlement
```

---

## 5. Idempotency and duplicate delivery

At most one Standing Free baseline may exist for one Merchant Account under this policy.

Repeated or concurrent delivery of the same Merchant Account establishment consequence MUST converge on the same baseline and MUST NOT restart, extend or duplicate Standing Free history.

The originating establishment identity MUST be retained as provenance.

If the first Commercial materialisation committed but acknowledgement was lost, retry MUST resolve the committed baseline.

A replay after catalogue change MUST still resolve the FREE revision effective at the original establishment instant and MUST NOT rebind the baseline to a later FREE revision.

---

## 6. Delayed propagation

Temporary absence of the materialised Standing Free row immediately after Merchant Account commit is a recoverable post-commit propagation state, not evidence that the merchant's historical Standing Free start moved later.

Example:

```text
T0 = Merchant Account establishment commits
T1 = Commercial worker receives consequence

StandingFreeBaseline.effectiveFrom = T0
FREE revision selection instant = T0
```

not `T1`.

No cross-capability distributed transaction is required solely to remove this propagation interval.

---

## 7. Runtime behaviour during propagation gap

Until the Standing Free source has materialised, Commercial runtime MUST NOT invent a baseline from direct Merchant Account table inspection, technical timestamps or cached assumptions.

Initial implementation SHALL fail closed for the absent source and rely on durable prompt reconciliation/materialisation.

A future explicitly accepted reconciliation path may use the Merchant Account establishment authority through a defined port; direct hidden cross-context table coupling remains prohibited.

---

## 8. Trial relationship

Standing Free and the initial full-experience trial remain independent sources:

```text
Merchant Account establishment
    → Standing Free effectiveFrom

First Configuration activation
    → Initial Full-Experience Trial startsAt
```

The two instants may differ.

Acceptance of a paid agreement during trial does not terminate Standing Free or trial unless a separately accepted Commercial policy says so.

---

## 9. Historical data

No production-history backfill rule is introduced here for Merchant Accounts created before authoritative establishment evidence existed.

Migration time, first observed Commercial access, first Configuration activation, first login and `Instant.MIN` MUST NOT be used as fabricated Standing Free history.

Development/test data may be reset and recreated through the authoritative post-amendment bootstrap path.

---

## 10. Required implementation evidence

Implementation MUST prove at least:

1. Standing Free can materialise after Merchant Account commit without cross-capability transactional coupling;
2. its `effectiveFrom` equals the originating establishment `establishedAt` exactly;
3. it binds to the FREE plan revision effective at that same authoritative instant and persists the exact entitlement snapshot;
4. duplicate/concurrent consequence delivery creates at most one baseline;
5. old delivery replay returns the same historical baseline even if catalogue packaging later changes;
6. Commercial entitlement resolution can include Standing Free independently of trial and paid Agreement grants;
7. trial expiry does not remove Standing Free grants;
8. paid Agreement replacement does not rewrite Standing Free history;
9. merchant scope and establishment provenance cannot cross tenants.

---

## 11. Acceptance statement

Standing Free is now an explicit Commercial-owned baseline whose historical start is causally anchored to immutable Merchant Account establishment evidence and whose meaning is bound to the versioned FREE plan revision effective at that same instant. This preserves bounded-context ownership, asynchronous recovery, historical reproducibility and independent Commercial grant composition without fake agreements, mutable live plan inheritance or synthetic timestamps.
