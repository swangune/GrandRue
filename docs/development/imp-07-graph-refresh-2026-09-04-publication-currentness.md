# IMP-07 Graph Refresh — Publication Currentness Foundation

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Refresh basis:** `development@b0ff9a27ee0b04a675408e8c1eb0219b831d875b`  
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — pending cycle-closing CI at the commit containing this record**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Current result

```text
IMP-06 Read / Exposure / Transport Spine
    CONFORMING_COMPLETE
        ↓ HARD
IMP-07 Publication → Enquiry vertical slice
    IN_PROGRESS

IMP-07-P1 Opportunity Publication lifecycle/currentness domain foundation
    CONFORMING_COMPLETE

IMP-07-P2A Durable Opportunity revision/currentness persistence foundation
    READY after this graph/status/evidence cycle-closing head verifies
```

P1 conformance evidence:

`docs/development/imp-07-publication-currentness-conformance-2026-09-04.md`

---

## 2. Authority and design-gate check

Current authority navigation composes `MS-PROT-046 v1.1 + v1.2` for Publication and `MS-PROT-043 v1.2 + v1.3 + v1.4` for Enquiry. `MS-PROT-059 v1.0` governs logical-operation retry/idempotency. The accepted IMP-06 spine remains the prerequisite Projection/Exposure/Surface/API infrastructure.

The current Deferred Decision Register contains no active IMP-07 design gate. `MS-PROT-046 v1.2` deliberately leaves the exact Java representation of Publication identities/revisions and exact PostgreSQL layout/indexes as `DEFERRED — INACTIVE` implementation choices. Those choices therefore do not block P2A, provided implementation does not invent new business semantics.

P2A is intentionally narrower than complete Publication persistence. It may choose persistence mechanics, but it may not redefine Publication identity, lifecycle, material revision meaning, Exposure, Opportunity content semantics or idempotency semantics.

---

## 3. Fine-grained IMP-07 graph

```text
Prerequisite spine
    G0 IMP-06 Projection / Exposure / Surface / API spine       CONFORMING_COMPLETE

Publication authoritative state
    P1 lifecycle/currentness domain foundation                 CONFORMING_COMPLETE
    P2A durable revision/currentness persistence foundation     READY AFTER CYCLE-CLOSING CI
    P2B reconstructible material revisions + lifecycle history  BLOCKED_DEPENDENCY — P2A
    P3 application mutation + transaction + retry idempotency   BLOCKED_DEPENDENCY — P2B

Publication public observation
    P4 Publication PUBLIC Exposure contracts/evaluation         BLOCKED_DEPENDENCY — P3 + G0
    P5 request-scoped public Opportunity representation         BLOCKED_DEPENDENCY — P4 + G0
    P6 Opportunity actionability derivation                     BLOCKED_DEPENDENCY — authoritative Opportunity facts

Public interaction participation
    I1 owner-qualified Opportunity → enquiry/send-enquiry source BLOCKED_DEPENDENCY — P4/P5 + G0
    I2 concrete Public Interaction Binding proof                BLOCKED_DEPENDENCY — I1

Enquiry authoritative state
    E1 durable Enquiry submission/provenance foundation         BLOCKED_DEPENDENCY — I2
    E2 Enquiry logical retry idempotency + transaction proof    BLOCKED_DEPENDENCY — E1
    E3 stale subject-binding authoritative revalidation         BLOCKED_DEPENDENCY — I2 + E1

Merchant observation
    M1 MERCHANT Enquiry Exposure contracts                      BLOCKED_DEPENDENCY — E1 + G0
    M2 request-scoped merchant Enquiry representation           BLOCKED_DEPENDENCY — M1

Concrete transport / vertical proof
    T1 concrete PUBLIC Publication query adapter                ON_DEMAND — P5
    T2 concrete PUBLIC Enquiry submission adapter               ON_DEMAND — E2/E3
    T3 concrete MERCHANT Enquiry query adapter                  ON_DEMAND — M2
    V1 full Publication → Enquiry integration path              BLOCKED_DEPENDENCY — P5/I2/E2/E3/M2 + required adapters
```

The graph is deliberately capability-ordered rather than endpoint-ordered. Concrete adapters remain downstream of capability truth and generic IMP-06 infrastructure.

---

## 4. P2A exact readiness boundary

P2A is the smallest currently dependency-complete executable node.

Its allowed scope is a durable PostgreSQL-backed foundation for Opportunity Publication **revision/currentness evidence**, sufficient to establish:

```text
merchant-scoped Opportunity identity
current revision identity
immutable previously recorded revision identities
durable lifecycle value
durable last-published revision identity where lifecycle requires it
expected-current compare-and-set for same-Opportunity mutation persistence
strict Merchant Scope isolation
```

P2A must be test-first and must prove at least:

1. a new draft can establish one durable merchant-scoped Opportunity/current revision record;
2. a second material revision records a distinct immutable revision identity and advances the current pointer only from the expected revision;
3. stale expected-current advancement fails atomically;
4. two concurrent writers expecting the same current revision cannot both advance it;
5. current Publication lifecycle and published-revision evidence round-trip without structural laundering;
6. another Merchant Scope cannot read or mutate the record through the same Opportunity identity; and
7. revision-identity evidence already recorded cannot be overwritten as though it were a different revision.

P2A SHALL NOT claim complete material-representation reconstructibility merely from revision identifiers. It SHALL NOT introduce generic JSON/payload authority to simulate unresolved Opportunity content. Material revision representation and complete historical publication evidence remain P2B.

P2A also SHALL NOT claim logical-operation retry idempotency. Expected-current concurrency and logical retry identity are separate concerns; idempotency remains downstream in P3 unless an accepted persistence primitive is required as a dependency.

---

## 5. Dependency rationale

```text
P1 → P2A
```

P1 establishes the domain lifecycle/currentness invariants that persistence must preserve.

```text
P2A → P2B
```

Material revision reconstruction requires a durable identity/currentness/history substrate before full revision representation is attached.

```text
P2B → P3
```

Application mutation cannot safely claim production completeness before the durable Publication truth it mutates can retain the required history.

```text
P3 + G0 → P4/P5
```

Public observation must derive from committed Publication truth and reuse the accepted generic Exposure/projection spine; it must not become mutation authority.

```text
P4/P5 + G0 → I1/I2 → E1/E2/E3
```

Subject-specific Enquiry requires a real owner-qualified Opportunity participation source and current public binding before submission can revalidate it.

---

## 6. Persistent watch state

```text
MS-WATCH-001
    WATCHING — PERSISTENT semantic-gravity pressure

MS-WATCH-002
    WATCHING — PROGRAMME/ARCHITECTURE DEPENDENCY
```

IMP-07 is now the first real proof of the generic IMP-06 participation-source boundary. `MS-WATCH-002` remains active until the concrete Opportunity → Enquiry source is proven without moving Publication semantics into Surface/Exposure infrastructure.

---

## 7. Retained distinctions

```text
current revision identity
    ≠ last published revision identity

expected-current concurrency
    ≠ logical-operation idempotency

Publication lifecycle
    ≠ Exposure

PUBLISHED
    ≠ automatically publicly observable

public Exposure
    ≠ Opportunity actionability

Public Interaction Binding
    ≠ participation authority
    ≠ execution authority

Enquiry
    ≠ Conversation
    ≠ Merchant Attention

revision-identity history
    ≠ complete material-representation reconstruction
```

---

## 8. Next governed action

After the cycle-closing commit containing:

```text
P1 conformance evidence
+
this fine-grained graph refresh
+
synchronised implementation-status.md
```

passes the repository full verification gate, execute P2A through `IMPLEMENTATION-RULES.md`:

```text
read accepted authority + persistence conventions
    ↓
state exact P2A contracts
    ↓
RED PostgreSQL integration/concurrency tests
    ↓
minimum persistence implementation
    ↓
targeted tests
    ↓
architecture/corpus/static/full verification
    ↓
conformance evidence
    ↓
graph refresh + status sync
```

Do not begin P2B, P3, Exposure, participation or Enquiry work until their graph dependencies become `READY`.
