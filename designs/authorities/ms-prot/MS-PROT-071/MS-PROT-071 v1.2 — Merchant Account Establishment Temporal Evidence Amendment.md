# MS-PROT-071 v1.2 — Merchant Account Establishment Temporal Evidence Amendment

**Document ID:** MS-PROT-071  
**Version:** 1.2  
**Status:** ACCEPTED after governed review, falsification and manual approval  
**Amends:** MS-PROT-071 v1.0 and v1.1 within the temporal-establishment-evidence scope only  
**Depends on:** MS-PROT-026, MS-PROT-056 v1.5, MS-PROT-063, MS-PROT-071 v1.0/v1.1, MS-PROT-072  
**Purpose:** Make the temporal occurrence of Merchant Account establishment an explicit immutable Merchant Account-owned fact so post-commit consequences can reproduce their historical effective intervals without deriving authority from technical timestamps or delivery time.

---

## 1. Governing decision

The minimal Merchant Account identity/existence fact remains:

```text
MerchantAccount
{
    merchantIdentifier
}
```

This amendment does **not** add mutable lifecycle/commercial/configuration state to `MerchantAccount`.

Merchant Account establishment additionally owns one immutable establishment-evidence fact:

```text
MerchantAccountEstablishment
{
    establishmentIdentity
    merchantIdentifier
    logicalEstablishmentRequestIdentity
    establishedAt
}
```

Canonical separation:

```text
MerchantAccount
    ≠ MerchantAccountEstablishment
    ≠ MerchantAccountEstablished Domain Event
    ≠ Commercial Standing Free source
```

`MerchantAccountEstablishment.establishedAt` is the authoritative instant at which Main Street committed Merchant Account establishment for temporal consequences whose accepted semantics begin at Merchant Account establishment.

---

## 2. Semantic ownership

The **Merchant Account context** owns:

- Merchant Account existence;
- immutable `merchantIdentifier`;
- the establishment identity;
- the authoritative `establishedAt` instant;
- logical-establishment-request affinity.

It does **not** own:

- Standing Free meaning or entitlement grants;
- trial meaning or timing;
- paid Commercial Agreement state;
- Merchant Configuration;
- onboarding completion;
- publication/Exposure;
- trust or provider readiness.

---

## 3. Establishment atomicity

For an ordinary successful bootstrap, the authoritative transaction MUST atomically commit:

```text
Merchant Account existence
+
initial ACTIVE Merchant Controller relationship
+
logical establishment request evidence
+
MerchantAccountEstablishment
+
durable publication intent for MerchantAccountEstablished
```

If any required element above fails before commit, the new Merchant Account establishment MUST NOT partially commit.

A cross-capability Commercial Standing Free row MUST NOT be forced into this transaction.

---

## 4. Establishment time assignment

`establishedAt` MUST be assigned exactly once for the logical Merchant Account establishment and MUST remain immutable.

Replay of the same logical establishment request after a successful commit MUST return the original Merchant Account and the original establishment evidence, including the original `establishedAt`.

The following MUST NOT become substitutes for `establishedAt`:

```text
database row insertion metadata
migration time
worker execution time
first login
first Configuration activation
trial start
first paid Commercial Agreement
Instant.MIN or equivalent synthetic epoch
```

---

## 5. Domain Event propagation

After the authoritative establishment fact exists, Merchant Account may publish:

```text
MerchantAccountEstablished
{
    factIdentity / establishmentIdentity
    merchantIdentifier
    logicalEstablishmentRequestIdentity
    occurredAt
}
```

with the invariant:

```text
MerchantAccountEstablished.occurredAt
    = MerchantAccountEstablishment.establishedAt
```

The Domain Event reports an already-committed fact; it is not the source that creates that fact.

Required publication intent MUST be durable with the originating establishment transaction or provide an equivalent process-failure-safe guarantee under MS-PROT-026/MS-PROT-072.

Duplicate delivery is permitted and consumers MUST be idempotent.

---

## 6. Post-commit consequence semantics

A downstream consumer MAY establish its own capability-owned consequence from `MerchantAccountEstablished`.

A delayed consumer MUST preserve the authoritative occurrence time from the establishment fact rather than using delivery/processing time.

Example:

```text
T0 Merchant Account establishment commits
T1 post-commit worker executes

accepted downstream temporal anchor = T0
not T1
```

---

## 7. Historical/pre-amendment data

This amendment does not authorise fabrication of historical establishment instants for Merchant Accounts whose authoritative establishment evidence was never recorded.

For pre-amendment non-production/development data, reset/recreation through the amended establishment path is acceptable.

Any future production migration/backfill policy for accounts lacking authoritative establishment evidence requires separately accepted evidence rules. Migration execution time MUST NOT be labelled as historical `establishedAt` without such authority.

---

## 8. Trial relationship remains unchanged

MS-PROT-056 v1.4 continues to govern the initial full-experience trial:

```text
first successfully committed Configuration Revision activation
    → initial 720-hour full-experience trial
```

Therefore:

```text
Merchant Account establishedAt
    ≠ trial startsAt
```

This amendment does not move the trial anchor.

---

## 9. Acceptance invariants

Implementation evidence MUST prove at least:

1. one authoritative establishment fact per Merchant Account;
2. bootstrap commits account + initial Controller + request evidence + establishment evidence + durable publication intent atomically;
3. replay returns the original `establishedAt` rather than assigning a new time;
4. concurrent duplicate bootstrap produces one establishment fact and one durable publication intent;
5. publication retry/duplicate handling does not create a second establishment fact;
6. publication completion does not erase historical establishment evidence;
7. downstream consumers receive the exact establishment time rather than delivery time.

---

## 10. Acceptance statement

Main Street now explicitly distinguishes Merchant Account identity/existence from immutable temporal establishment evidence. The Merchant Account context owns when authoritative establishment occurred; Domain Events propagate that committed fact; downstream capabilities may derive their own time-anchored consequences without inventing history or joining the Merchant Account transaction.
