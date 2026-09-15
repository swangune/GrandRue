# IMP-07 Graph Refresh — Publication P5 Closure

**Date:** 5 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-07 — Publication → Enquiry vertical slice  
**Authority basis:** accepted Publication representation authority in `MS-PROT-046 v1.2 + v1.3`, bounded-read/currentness mechanics in composite `MS-PROT-027 v1.13 + v1.14`, and the negative participation boundary in `MS-PROT-049 v1.4`  
**Verified P5 GREEN:** `development@3d2775ec2add3aadbdba4b2736cf716f581bad46`, run `33959091152` — **SUCCESS**  
**Verified P5 adversarial head:** `development@2fcc452f0b8df452a2eba42cc10824d24ff0039e`, run `33959274014` — **SUCCESS**  
**Status:** **CURRENT IMP-07 FINE-GRAINED GRAPH — P5 closure / I1 readiness effective when the cycle-closing synchronisation head containing this graph, implementation-status.md and the programme-gate assertion passes full verification**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Current result

```text
G0  IMP-06 Projection / Exposure / Surface / API spine        CONFORMING_COMPLETE
P1  Publication lifecycle/currentness domain foundation       CONFORMING_COMPLETE
P2A durable revision/currentness persistence foundation       CONFORMING_COMPLETE
P2B reconstructible material revisions + Publication History  CONFORMING_COMPLETE
P3  application mutation + transaction + retry idempotency    CONFORMING_COMPLETE
P4  Publication PUBLIC Exposure contracts/evaluation          CONFORMING_COMPLETE
P5  request-scoped public Opportunity representation          CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONISATION CI
```

P5 conformance evidence:

`docs/development/imp-07-publication-p5-conformance-2026-09-05.md`

---

## 2. P5 closure basis

The request-scoped public Opportunity representation has executable proof for:

```text
exact published revision supplies bounded public material                         PASS
unpublished current edit does not replace the published representation            PASS
stable Opportunity instance remains Exposure identity                             PASS
revision/publication internals are excluded from the public value                  PASS
request-scoped and Merchant-Scope material affinity                               PASS
capability-private material-affinity contribution                                 PASS
binding change during acquisition yields no fragment                              PASS
republish between acquisition and E4 fails closed / WITHHOLD                      PASS
request rebinding rejected                                                        PASS
Merchant-Scope rebinding rejected                                                 PASS
same bounded material flows through generic Projection/Exposure assembly          PASS
P6 actionability not absorbed                                                     PASS
Participation/Enquiry/transport not absorbed                                      PASS
cache/index/read-model trigger not introduced                                     PASS
```

Verification trace:

```text
historical RED                 3f7958fca49874ffd606f6f360d049e053d31420 — run 33958778665 FAILURE as intended
initial GREEN                  bbfe026e535cfd468816c79b7f2ee1b8b7cf6ef4 — run 33958986905 FAILURE
repaired GREEN                 3d2775ec2add3aadbdba4b2736cf716f581bad46 — run 33959091152 SUCCESS
post-GREEN adversarial head    2fcc452f0b8df452a2eba42cc10824d24ff0039e — run 33959274014 SUCCESS
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
    P4 Publication PUBLIC Exposure contracts/evaluation           CONFORMING_COMPLETE
    P5 request-scoped public Opportunity representation           CONFORMING_COMPLETE ON CYCLE-CLOSING SYNCHRONISATION CI
    P6 Opportunity actionability derivation                       BLOCKED_DEPENDENCY — authoritative Opportunity facts

Public interaction participation
    I1 owner-qualified Opportunity → enquiry/send-enquiry source  READY ON THE SAME CI
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

P6 Opportunity actionability remains separate. P5 produces request-scoped public representation; it does not decide whether an exposed Opportunity is currently actionable.

---

## 4. Next READY boundary — I1

I1 becomes READY only after the cycle-closing synchronization head passes the repository full verification gate.

I1 responsibility is exactly:

```text
owner-qualified Opportunity → enquiry/send-enquiry participation source
```

Resolved prerequisites on successful P5 closure are:

```text
P5 — request-scoped public Opportunity representation
G0 — IMP-06 generic Public Interaction participation-source/binding infrastructure
```

I1 is Publication-owned. It must establish positive Opportunity participation from accepted Publication/semantic authority and the registered `enquiry/send-enquiry` operation. It MUST NOT infer participation from Exposure, route, business category, labels, provider identity, presentation metadata or AI inference.

I1 participation remains distinct from availability, actionability, authorisation and execution permission. I2 remains the separate concrete Public Interaction Binding proof.

---

## 5. Next governed action

```text
cycle-closing synchronisation head containing:
    verified P5 implementation + executable proofs
    + P5 conformance evidence
    + this graph refresh
    + synchronised implementation-status.md
    + synchronised programme-gate assertion
        ↓
repository full verification gate
        ↓ SUCCESS
P5 = CONFORMING_COMPLETE
        ↓
I1 = READY
        ↓
derive the Publication-owned Opportunity → enquiry/send-enquiry participation source
        ↓
execute I1 through IMPLEMENTATION-RULES.md
```

Do not begin I1 before this synchronisation gate succeeds. Do not begin P6, I2, Enquiry persistence, merchant observation or concrete transport before their own graph dependencies become READY.