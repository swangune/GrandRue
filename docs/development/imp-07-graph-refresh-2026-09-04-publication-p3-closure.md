# IMP-07 Graph Refresh — Publication P3 Closure

**Date:** 4 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Authority basis:** accepted Publication/application/transaction authority, including `MS-PROT-059 v1.0`  
**Code-bearing verification:** `development@f481ec46c4253d0db3dbfa95e397cf596c010860`, GitHub Actions run `33883111122` — **SUCCESS**  
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P3 closure effective when this cycle-closing head passes full verification**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Current result

```text
G0  IMP-06 Projection / Exposure / Surface / API spine        CONFORMING_COMPLETE
P1  Publication lifecycle/currentness domain foundation       CONFORMING_COMPLETE
P2A durable revision/currentness persistence foundation       CONFORMING_COMPLETE
P2B reconstructible material revisions + Publication History  CONFORMING_COMPLETE
P3  application mutation + transaction + retry idempotency    CONFORMING_COMPLETE AFTER CYCLE-CLOSING CI
P4  Publication PUBLIC Exposure contracts/evaluation          READY AFTER THE SAME CYCLE-CLOSING CI
P5  request-scoped public Opportunity representation          BLOCKED_DEPENDENCY — P4 + G0
```

P3 conformance evidence:

`docs/development/imp-07-publication-p3-conformance-2026-09-04.md`

---

## 2. P3 closure basis

The application mutation boundary now has direct executable proof for:

```text
canonical application-request identity independent of transport/trace/object identity PASS
same request + same intent reconciliation                                              PASS
reconciliation before fresh current-state evaluation                                  PASS
same request + changed intent explicit conflict                                       PASS
distinct stale request remains revision conflict                                      PASS
atomic rollback of state/material/request composition                                 PASS
concurrent same-request delivery produces one committed business effect                PASS
P2B retains Publication state/material/history ownership                              PASS
no Exposure/Enquiry/transport/actor-authority expansion                               PASS
```

Code-bearing full verification:

```text
head    f481ec46c4253d0db3dbfa95e397cf596c010860
run     33883111122
result  SUCCESS
```

The earlier fixture compatibility repair at `556ff3c9458a53981834f4dbf4b4742ea55db558` also passed full CI run `33881445271` and did not change P3 production semantics.

No closure finding requires another semantic/design amendment.

---

## 3. Fine-grained IMP-07 graph

```text
Prerequisite spine
    G0 IMP-06 Projection / Exposure / Surface / API spine       CONFORMING_COMPLETE

Publication authoritative state
    P1 lifecycle/currentness domain foundation                   CONFORMING_COMPLETE
    P2A durable revision/currentness persistence foundation       CONFORMING_COMPLETE
    P2B reconstructible material revisions + Publication History  CONFORMING_COMPLETE
    P3 application mutation + transaction + retry idempotency     CONFORMING_COMPLETE AFTER CYCLE-CLOSING CI

Publication public observation
    P4 Publication PUBLIC Exposure contracts/evaluation           READY AFTER THE SAME CYCLE-CLOSING CI
    P5 request-scoped public Opportunity representation           BLOCKED_DEPENDENCY — P4 + G0
    P6 Opportunity actionability derivation                       BLOCKED_DEPENDENCY — authoritative Opportunity facts

Public interaction participation
    I1 owner-qualified Opportunity → enquiry/send-enquiry source  BLOCKED_DEPENDENCY — P4/P5 + G0
    I2 concrete Public Interaction Binding proof                  BLOCKED_DEPENDENCY — I1

Enquiry authoritative state
    E1 durable Enquiry submission/provenance foundation           BLOCKED_DEPENDENCY — I2
    E2 Enquiry logical retry idempotency + transaction proof      BLOCKED_DEPENDENCY — E1
    E3 stale subject-binding authoritative revalidation           BLOCKED_DEPENDENCY — I2 + E1

Merchant observation
    M1 MERCHANT Enquiry Exposure contracts                        BLOCKED_DEPENDENCY — E1 + G0
    M2 request-scoped merchant Enquiry representation             BLOCKED_DEPENDENCY — M1

Concrete transport / vertical proof
    T1 concrete PUBLIC Publication query adapter                  ON_DEMAND — P5
    T2 concrete PUBLIC Enquiry submission adapter                 ON_DEMAND — E2/E3
    T3 concrete MERCHANT Enquiry query adapter                    ON_DEMAND — M2
    V1 full Publication → Enquiry integration path                BLOCKED_DEPENDENCY — downstream owners
```

---

## 4. Next READY boundary — P4

After the cycle-closing head containing this graph, the P3 conformance evidence, synchronised `implementation-status.md` and updated programme-gate test passes the repository full verification gate, P4 becomes the smallest dependency-complete READY node.

P4 responsibility is exactly the graph-owned next step:

```text
Publication PUBLIC Exposure contracts/evaluation
```

Its resolved prerequisites are:

```text
P3 — Publication application mutation + transaction + retry idempotency
G0 — IMP-06 Projection / Exposure / Surface / API spine
```

P5 remains blocked until P4 closes. P4 MUST NOT silently absorb request-scoped public Opportunity representation, Opportunity → Enquiry participation, Enquiry persistence or concrete transport.

---

## 5. Next governed action

```text
cycle-closing head containing:
    P3 conformance evidence
    + this graph refresh
    + synchronised implementation-status.md
    + programme-gate assertion update
        ↓
repository full verification gate
        ↓ SUCCESS
P3 = CONFORMING_COMPLETE
        ↓
P4 = READY
        ↓
re-derive P4 contracts from accepted authority
        ↓
execute P4 through IMPLEMENTATION-RULES.md
```

Do not begin P4 before this cycle-closing CI succeeds. Do not begin P5, Participation, Enquiry or concrete transport before their own graph dependencies become READY.
