# IMP-07 Graph Refresh — Publication P4 Closure

**Date:** 5 September 2026
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-07 — Publication → Enquiry vertical slice
**Authority basis:** accepted Publication PUBLIC Exposure authority in `MS-PROT-046 v1.2 + v1.3` and accepted Exposure mechanics in `MS-PROT-027 v1.11`
**Pre-closure verification base:** `development@452426de5eaba5590d1ce08f53e47f0f632862b6` plus staged P4 implementation
**Pre-closure full verification:** 968 unit/governance + 329 PostgreSQL integration tests — **BUILD SUCCESS**
**Post-inspection adversarial proof:** `development@96550fb6e8e04fad233b8d3e9e1193d43d4fb7b8` — full GitHub Actions gate **SUCCESS**
**Evidence-alignment verification:** `development@859119784657768b3d96a4627237ede66f8907ef`, run `33947743143` — **SUCCESS**
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P4 closure / P5 readiness effective when the cycle-closing synchronisation head containing this graph, implementation-status.md and the programme-gate assertion passes full verification**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Current result

```text
G0  IMP-06 Projection / Exposure / Surface / API spine        CONFORMING_COMPLETE
P1  Publication lifecycle/currentness domain foundation       CONFORMING_COMPLETE
P2A durable revision/currentness persistence foundation       CONFORMING_COMPLETE
P2B reconstructible material revisions + Publication History  CONFORMING_COMPLETE
P3  application mutation + transaction + retry idempotency    CONFORMING_COMPLETE
P4  Publication PUBLIC Exposure contracts/evaluation          CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONISATION CI
P5  request-scoped public Opportunity representation          READY ON THE SAME CI
```

P4 conformance evidence:

`docs/development/imp-07-publication-p4-conformance-2026-09-05.md`

---

## 2. P4 closure basis

The PUBLIC Opportunity Exposure boundary has executable proof for:

```text
PUBLISHED-only upstream candidacy                                             PASS
DRAFT/WITHDRAWN cannot be resurrected by Exposure                            PASS
owner-qualified instance membership                                           PASS
published revision remains Exposure authority across unpublished current edit PASS
exact publishFrom lower-bound semantics                                       PASS
exact publishUntil upper-cutoff semantics                                     PASS
calendar-date inclusive local-date upper semantics                            PASS
trusted request-scoped evaluatedAt                                            PASS
missing/unstable Publication evidence fails closed                            PASS
published binding changed between authority reads fails closed                PASS
mismatched published-revision affinity is rejected                            PASS
generic Exposure resolution remains mechanical/value-blind                    PASS
P5 representation not absorbed                                                PASS
P6 actionability not absorbed                                                 PASS
Participation/Enquiry/transport not absorbed                                  PASS
```

Verification trace:

```text
pre-closure targeted P4 tests       7/7 SUCCESS
pre-closure unit/governance         968/968 SUCCESS
pre-closure PostgreSQL integration  329/329 SUCCESS
adversarial proof repair            96550fb6... full gate SUCCESS
evidence-alignment head             85911978... run 33947743143 SUCCESS
```

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
    P3 application mutation + transaction + retry idempotency     CONFORMING_COMPLETE

Publication public observation
    P4 Publication PUBLIC Exposure contracts/evaluation           CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONISATION CI
    P5 request-scoped public Opportunity representation           READY ON THE SAME CI
    P6 Opportunity actionability derivation                       BLOCKED_DEPENDENCY — authoritative Opportunity facts

Public interaction participation
    I1 owner-qualified Opportunity → enquiry/send-enquiry source  BLOCKED_DEPENDENCY — P5 + G0
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

P6 Opportunity actionability remains separate. P4 establishes whether an Opportunity representation is eligible for PUBLIC Exposure; it does not determine whether an exposed Opportunity is currently actionable.

---

## 4. Next READY boundary — P5

The P4 production implementation, direct proof and conformance evidence have already passed full verification. A post-inspection proof repair added the two missing adversarial cases without production-code change, and the aligned evidence head `859119784657768b3d96a4627237ede66f8907ef` passed run `33947743143` successfully.

Implementation Rules additionally require the cycle-closing graph, `implementation-status.md`, evidence and programme-gate assertion to be synchronised before the next node begins. Therefore P5 becomes READY only after the cycle-closing synchronisation head containing those navigation assertions passes the repository full verification gate.

P5 responsibility is exactly:

```text
request-scoped public Opportunity representation
```

Its resolved prerequisites are:

```text
P4 — Publication PUBLIC Exposure contracts/evaluation
G0 — IMP-06 Projection / Exposure / Surface / API spine
```

P5 may consume the positive Exposure result and the exact published revision material needed for the public representation. P5 MUST NOT silently absorb P6 actionability, Opportunity → Enquiry participation, Enquiry persistence or concrete transport.

---

## 5. Next governed action

```text
cycle-closing synchronisation head containing:
    verified P4 implementation + executable proofs
    + P4 conformance evidence
    + this graph refresh
    + synchronised implementation-status.md
    + synchronised programme-gate assertion
        ↓
repository full verification gate
        ↓ SUCCESS
P4 = CONFORMING_COMPLETE
        ↓
P5 = READY
        ↓
derive P5 from accepted authority
        ↓
execute P5 through IMPLEMENTATION-RULES.md
```

Do not begin P5 before this synchronisation gate succeeds. Do not begin P6, Participation, Enquiry or concrete transport before their own graph dependencies become READY.
