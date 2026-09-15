# IMP-07 Graph Refresh — P2B Publication Reconstruction / History Closure

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Refresh basis:** `development@2b0aa60d34eca6fc36c8e2feb3f38f80f5f538c2` + GitHub Actions run `33846476217` **SUCCESS**  
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P2B closure effective when the cycle-closing head containing this record passes full verification**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Current result

```text
IMP-06 Read / Exposure / Transport Spine
    CONFORMING_COMPLETE
        ↓ HARD
IMP-07 Publication → Enquiry vertical slice
    IN_PROGRESS

P1  Opportunity Publication lifecycle/currentness domain foundation
    CONFORMING_COMPLETE

P2A Durable Opportunity revision/currentness persistence foundation
    CONFORMING_COMPLETE

P2B Reconstructible material revisions + lifecycle history
    CONFORMING_COMPLETE
    effective after cycle-closing CI

P3  Application mutation + transaction + retry idempotency
    READY after the same cycle-closing CI
```

P2B conformance evidence:

`docs/development/imp-07-publication-p2b-reconstruction-conformance-2026-09-04.md`

---

## 2. P2B closure basis

Executable proof now exists for:

```text
historical material reconstruction by exact revision            PASS
immutable schema-version affinity                               PASS
registered field-identity preservation                          PASS
ordered establish/publish/withdraw/republish evidence           PASS
publish of newer revision while lifecycle stays PUBLISHED       PASS
stale-write rollback with no orphan material                    PASS
Merchant Scope history/material isolation                       PASS
concurrent same-expected-current one-winner material persistence PASS
material required exactly on revision advance                   PASS
P2A currentness/CAS regression suite                             PASS
```

Verification trace:

```text
RED specification head
    cc7019749b93dba8deac71aba34da9953f3b24ad
run 33845980433
result FAILURE — expected pre-implementation RED

GREEN implementation head
    2b0aa60d34eca6fc36c8e2feb3f38f80f5f538c2
run 33846476217
result SUCCESS
```

No implementation finding requires reopening Publication/schema semantics.

---

## 3. Fine-grained IMP-07 graph

```text
Prerequisite spine
    G0 IMP-06 Projection / Exposure / Surface / API spine       CONFORMING_COMPLETE

Publication authoritative state
    P1 lifecycle/currentness domain foundation                  CONFORMING_COMPLETE
    P2A durable revision/currentness persistence foundation      CONFORMING_COMPLETE
    P2B reconstructible material revisions + lifecycle history   CONFORMING_COMPLETE AFTER CYCLE-CLOSING CI
    P3 application mutation + transaction + retry idempotency    READY AFTER P2B CYCLE-CLOSING CI

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

No downstream edge is waived by P2B completion.

---

## 4. P3 READY boundary

After the cycle-closing head passes the full verification gate, P3 becomes the smallest dependency-complete executable node.

P3 is bounded to:

```text
capability-owned Publication application mutation
+
transactional orchestration over P2B persistence
+
logical-operation retry idempotency
```

Before production code, exact contracts must be re-derived from the accepted Publication/application/idempotency authority, principally:

```text
MS-PROT-046 v1.1 + v1.2
MS-PROT-059
existing application/transaction/authorisation conventions
IMPLEMENTATION-RULES.md
```

Required distinctions:

```text
expected-current CAS
    ≠ logical-operation retry idempotency

persistence port
    ≠ mutation authority

registered FieldDefinition conformance
    ≠ arbitrary persistence field acceptance at application boundary

application transaction
    ≠ transport endpoint
```

P3 must not make P4/P5/Enquiry/transport READY prematurely.

---

## 5. P2B architectural consequence

P2B establishes this durable foundation:

```text
merchant-owned Opportunity
        ↓
stable object identity
        ↓
immutable logical revision identities
        ↓
exact schema-affined material representation per revision
        ↓
currentness CAS
        +
ordered lifecycle/publication evidence
```

It does not establish:

```text
generic Publication metadata
closed global Opportunity types
generic PATCH semantics
Exposure visibility
Opportunity actionability
Public Interaction participation
Enquiry authority
provider execution
transport
```

`MS-WATCH-002` therefore remains active for the later Opportunity → Enquiry capability-composition proof.

---

## 6. Retained downstream blocks

P2B closure does **not** make any of the following independently READY:

```text
P4/P5 Publication Exposure/public representation
P6 Opportunity actionability
I1/I2 Opportunity → Enquiry participation/binding
E1/E2/E3 Enquiry persistence/idempotency/revalidation
M1/M2 merchant Enquiry observation
T1/T2/T3 concrete adapters except when their owning nodes complete
V1 full Publication → Enquiry vertical proof
```

P3 is the only newly eligible node after successful P2B cycle closure.

---

## 7. Non-recursive cycle-closing rule retained

```text
POST_COMMIT_CI_RESULT_DOES_NOT_REQUIRE_STATUS_REWRITE
```

Canonical closure:

```text
cycle-closing commit contains:
    P2B conformance evidence
    + this graph refresh
    + synchronised implementation-status.md
    + updated programme-gate assertion
        ↓
CI on that exact commit
        ↓ SUCCESS
P2B closure condition satisfied externally
        ↓
P3 becomes READY
```

A successful CI result does not require another status-only commit solely to write its own run id back into the repository. A new synchronisation commit is required if the gate fails or graph/evidence/programme state materially changes.

---

## 8. Next governed action

```text
cycle-closing P2B state
        ↓
repository full verification gate
        ↓ SUCCESS
P2B = CONFORMING_COMPLETE
        ↓
P3 = READY
        ↓
read accepted Publication + MS-PROT-059 + application authority
        ↓
derive exact P3 contracts
        ↓
write failing tests
        ↓
minimum implementation
```

Do not begin Exposure, Opportunity → Enquiry participation, Enquiry persistence or concrete transport before their graph dependencies become READY.
