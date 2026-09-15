# IMP-07 Graph Refresh — P2A Publication Persistence Closure

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Refresh basis:** `development@ac44523be7b7c86dadeff49dcbc27a11611f3d4f` + GitHub Actions run `33841400441` **SUCCESS**  
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P2A CONFORMING_COMPLETE; P2B READY**

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
    CONFORMING_COMPLETE

IMP-07-P2B Reconstructible material revisions + lifecycle history
    READY
```

P2A conformance evidence:

`docs/development/imp-07-publication-p2a-persistence-conformance-2026-09-04.md`

---

## 2. P2A closure basis

P2A has executable proof for all seven requirements defined by the previous graph:

```text
durable merchant-scoped draft/current revision                  PASS
distinct immutable revision identity + expected-current advance PASS
stale expected-current atomic failure                            PASS
concurrent same-expected-current exactly-one-winner              PASS
lifecycle + published-revision round-trip                        PASS
Merchant Scope isolation                                         PASS
already-recorded revision identity cannot be reused              PASS
```

Pre-closure full verification:

```text
head    0a42e52f056b71e6be1165d64f59b3c094149984
run     33840984639
result  SUCCESS
```

Cycle-closing full verification:

```text
head    ac44523be7b7c86dadeff49dcbc27a11611f3d4f
run     33841400441
result  SUCCESS
```

The cycle-closing success satisfies the closure condition that was recorded before the run existed. No P2A closure finding requires a semantic/design amendment. The current Deferred Decision Register exposes no active IMP-07 design gate applicable to this closure.

---

## 3. Fine-grained IMP-07 graph

```text
Prerequisite spine
    G0 IMP-06 Projection / Exposure / Surface / API spine       CONFORMING_COMPLETE

Publication authoritative state
    P1 lifecycle/currentness domain foundation                  CONFORMING_COMPLETE
    P2A durable revision/currentness persistence foundation      CONFORMING_COMPLETE
    P2B reconstructible material revisions + lifecycle history   READY
    P3 application mutation + transaction + retry idempotency    BLOCKED_DEPENDENCY — P2B

Publication public observation
    P4 Publication PUBLIC Exposure contracts/evaluation          BLOCKED_DEPENDENCY — P3 + G0
    P5 request-scoped public Opportunity representation          BLOCKED_DEPENDENCY — P4 + G0
    P6 Opportunity actionability derivation                      BLOCKED_DEPENDENCY — authoritative Opportunity facts

Public interaction participation
    I1 owner-qualified Opportunity → enquiry/send-enquiry source BLOCKED_DEPENDENCY — P4/P5 + G0
    I2 concrete Public Interaction Binding proof                 BLOCKED_DEPENDENCY — I1

Enquiry authoritative state
    E1 durable Enquiry submission/provenance foundation          BLOCKED_DEPENDENCY — I2
    E2 Enquiry logical retry idempotency + transaction proof     BLOCKED_DEPENDENCY — E1
    E3 stale subject-binding authoritative revalidation          BLOCKED_DEPENDENCY — I2 + E1

Merchant observation
    M1 MERCHANT Enquiry Exposure contracts                       BLOCKED_DEPENDENCY — E1 + G0
    M2 request-scoped merchant Enquiry representation            BLOCKED_DEPENDENCY — M1

Concrete transport / vertical proof
    T1 concrete PUBLIC Publication query adapter                 ON_DEMAND — P5
    T2 concrete PUBLIC Enquiry submission adapter                ON_DEMAND — E2/E3
    T3 concrete MERCHANT Enquiry query adapter                   ON_DEMAND — M2
    V1 full Publication → Enquiry integration path               BLOCKED_DEPENDENCY — P5/I2/E2/E3/M2 + required adapters
```

The graph remains capability-ordered rather than endpoint-ordered. Concrete adapters remain downstream of capability truth and the generic IMP-06 infrastructure.

---

## 4. READY boundary — P2B

P2B is now the smallest dependency-complete executable node.

P2B is currently identified only by its accepted programme responsibility:

```text
reconstructible material revisions
+
required lifecycle history
```

Before production code, its exact implementation contracts MUST be re-derived from the surviving accepted Publication authority and existing persistence conventions through `IMPLEMENTATION-RULES.md`.

This graph does not invent the representation of Opportunity material content, lifecycle-history schema, indexes or application orchestration.

### 4.1 Non-recursive cycle-closing verification

A cycle-closing graph/status/evidence commit can only be verified after it exists. Therefore a recorded conditional closure is satisfied by the external CI result of that exact commit; successful post-commit verification does not require a second status-only commit merely to write its run id back into the repository.

Canonical:

```text
POST_COMMIT_CI_RESULT_DOES_NOT_REQUIRE_STATUS_REWRITE

cycle-closing commit
        ↓
CI on exact commit
        ↓ SUCCESS
recorded completion/readiness condition is satisfied
        ↓
continue to READY work
```

A new synchronisation commit is required if verification fails or if the graph/evidence/programme state materially changes. It is not required merely because successful CI metadata became available after commit creation.

---

## 5. Retained downstream blocks

P2A closure does **not** make any of the following READY:

```text
P3 logical-operation retry/idempotency and application mutation
P4/P5 Publication Exposure/public representation
P6 Opportunity actionability
I1/I2 Opportunity → Enquiry participation/binding proof
E1/E2/E3 Enquiry authoritative persistence/idempotency/revalidation
M1/M2 merchant Enquiry observation
T1/T2/T3 concrete adapters unless their owner nodes are complete
V1 full Publication → Enquiry vertical proof
```

Expected-current concurrency remains distinct from logical-operation retry idempotency.

---

## 6. Persistent watch state

```text
MS-WATCH-001
    WATCHING — PERSISTENT semantic-gravity pressure

MS-WATCH-002
    WATCHING — PROGRAMME/ARCHITECTURE DEPENDENCY
```

`MS-WATCH-002` remains active until the concrete Opportunity → Enquiry participation source is proven without moving Publication semantics into Surface/Exposure infrastructure.

---

## 7. Recovery trace retained

The P1 cycle-closing run `33827472011` at `a41934c407eb67da17be8fcedc61071a2e3dc0bc` remains historically **FAILURE** because the governance harness was stale; history is not rewritten.

The P2A fixture defect was corrected at `7472a3a38a407e2fd85b51553ab250a0482c09da`, verified by run `33840713030` **SUCCESS**. The direct revision-identity immutability proof was added at `0a42e52f056b71e6be1165d64f59b3c094149984`, verified by run `33840984639` **SUCCESS**. The P2A cycle-closing commit `ac44523be7b7c86dadeff49dcbc27a11611f3d4f` was then verified by run `33841400441` **SUCCESS**, making P2A closure effective and P2B READY.

---

## 8. Next governed action

```text
P2A
    CONFORMING_COMPLETE
        ↓
P2B
    READY
        ↓
read accepted Publication authority + persistence conventions
        ↓
derive exact P2B implementation contracts
        ↓
execute P2B through IMPLEMENTATION-RULES.md
```

The governance-repair commit that updates this graph, the synchronised status and the programme-gate assertion must itself pass the repository full verification gate before automated P2B execution continues. That verification protects the repair and does not reopen P2A. A successful repair CI run requires no follow-up status-only commit merely to record that run.

Do not begin P3, Exposure, participation, Enquiry or concrete transport work before their graph dependencies become `READY`.
