# IMP-06 Graph Refresh — S3 and Macro Closure

**Date:** 3 September 2026  
**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-06 — Read, Exposure & Transport Spine  
**Refresh basis:** `development@97a5fef2a9834d5d9dfe620867b46a4f255a2def`  
**Status:** **CURRENT IMP-06 CLOSURE GRAPH**

This record is implementation/dependency navigation only. It creates no semantic or architectural authority.

---

## 1. Closure result

```text
IMP-06
    CONFORMING_COMPLETE

S3 Public Interaction Binding projection
    CONFORMING_COMPLETE

IMP-07 Publication → Enquiry vertical slice
    READY — NEXT MACRO TARGET
    execution gated on successful CI for the IMP-06 cycle-closing governance head
```

S3 conformance evidence:

`docs/development/imp-06-s3-public-interaction-binding-conformance-2026-09-03.md`

IMP-06 macro closure review:

`docs/development/imp-06-read-exposure-transport-spine-closure-2026-09-03.md`

---

## 2. Fine-grained IMP-06 graph

```text
Projection
    P1 Projection Contract registration                     CONFORMING_COMPLETE
    P2 Projection Serviceability evaluation                 CONFORMING_COMPLETE

Bounded-read / representation affinity
    BR1 bounded-read/binding/fragment integrity primitives   CONFORMING_COMPLETE
    BR2 P2 exact bounded-read binding                        CONFORMING_COMPLETE
    BR3 owner/query material + source affinity               CONFORMING_COMPLETE
    BR4 material-affinity observation contribution           CONFORMING_COMPLETE
    BR5 owner evaluator current-progress comparison          CONFORMING_COMPLETE
    BR6 same-read E4-positive fragment selection             CONFORMING_COMPLETE

Surface
    S1 Surface Contribution infrastructure                   CONFORMING_COMPLETE
    S2 final PUBLIC/CUSTOMER assembly                        CONFORMING_COMPLETE
    S3 Public Interaction Binding projection                 CONFORMING_COMPLETE

Exposure
    E1 Exposure Element Contract registration                CONFORMING_COMPLETE
    E2 Exposure semantic-bundle encoding/materialisation     CONFORMING_COMPLETE
    E3 Audience Observation Context + result boundary        CONFORMING_COMPLETE
    E4-A member identity specification                       CONFORMING_COMPLETE
    E4-B deterministic element/audience indexing             CONFORMING_COMPLETE
    E4-C exact-release Exposure-definition encoding          CONFORMING_COMPLETE
    E4-D instance-aware positive result boundary             CONFORMING_COMPLETE
    E4-E OwnerExposureEvaluationContext                      CONFORMING_COMPLETE
    E4-F exact-release evaluator binding snapshots           CONFORMING_COMPLETE
    E4-G batch evaluator + submission affinity               CONFORMING_COMPLETE
    E4-H Profile merchant-choice evaluators                  CONFORMING_COMPLETE
    E4-I deterministic generic resolver                      CONFORMING_COMPLETE
    E4-J targeted/PostgreSQL/architecture/full verification  CONFORMING_COMPLETE

Profile prerequisite
    LEC1 Merchant Location Exposure-choice persistence       CONFORMING_COMPLETE

Resource Protection
    R1 production authority assessment                       CONFORMING_COMPLETE

API / transport / bounded query
    T1a registration spine                                   CONFORMING_COMPLETE
    T1b1 command                                             CONFORMING_COMPLETE
    T1b2 callback                                            CONFORMING_COMPLETE
    T1b3 media transfer                                      CONFORMING_COMPLETE
    T1b4 query                                               CONFORMING_COMPLETE
    T2a exact surface/scope authority                        CONFORMING_COMPLETE
    T2b Controller merchant context                          CONFORMING_COMPLETE
    T2c staff operational context                            CONFORMING_COMPLETE
    T2d concrete adapters                                    ON_DEMAND — real adapter trigger required
    T3a outcome/problem vocabulary                           CONFORMING_COMPLETE
    T3b owner/evidence safe mapping spine                    CONFORMING_COMPLETE
    T3c concrete mappings                                    ON_DEMAND — real owner/adapter trigger required
    T4a bounded collection contract                          CONFORMING_COMPLETE
    T4b continuation affinity/validation                     CONFORMING_COMPLETE
    T4c opaque continuation encoding/adapter                 BLOCKED_DEPENDENCY — concrete query adapter absent
```

No remaining generic IMP-06 node is READY.

T2d/T3c/T4c are retained because the architecture still needs those responsibilities when concrete adapters appear; they are not promoted now because doing so would manufacture a broad endpoint/adapter portfolio that IMP-06 does not require.

---

## 3. S3 dependency closure

Accepted MS-PROT-049 v1.4 plus the verified production implementation now establish:

```text
applicable PUBLIC_INTERACTION semantics
        +
explicit owner-established participation facts
        +
exact S2-selected P2/E4-approved public material
        ↓
generic S3 projector
        ↓
immutable Public Interaction Bindings
```

The zero-source production state is valid and fail-closed:

```text
registered participation sources = ∅
        ↓
positive participation facts = ∅
        ↓
Public Interaction Bindings = ∅
```

No Publication/Enquiry/Booking/Ordering/Appointment business rule is imported into the generic spine.

---

## 4. Programme graph after closure

```text
IMP-00✓
  ↓
IMP-01✓
  ↓
IMP-02✓
  ↓
IMP-03✓
  ↓
IMP-04✓
  ↓
IMP-05✓
  ↓
IMP-06✓  Read, Exposure & Transport Spine
  ↓ HARD
IMP-07    Publication → Enquiry vertical slice
          READY after IMP-06 cycle-closing governance CI succeeds
```

Critical IMP-06 path:

```text
BR1✓ → BR2✓ → BR3✓ → BR4✓ → BR5✓ → BR6✓
    → S2✓ → T1b4✓ → T4b✓
    → MS-PROT-049 v1.4 design gate✓
    → S3✓
    → IMP-06 macro closure✓
    → cycle-closing governance CI
    → IMP-07 READY/EXECUTABLE
```

---

## 5. Persistent watch state

```text
MS-WATCH-001
    WATCHING — PERSISTENT semantic-gravity pressure

MS-WATCH-002
    WATCHING — PROGRAMME/ARCHITECTURE DEPENDENCY
```

S3 verifies the current MS-WATCH-002 mitigation: generic infrastructure can remain fail-closed without pulling a concrete positive source backward across the IMP-06 → IMP-07 HARD edge.

The watch remains active because the first real owner-qualified Publication → Enquiry source is still unimplemented and must prove that the generic source boundary works under a real backend transition.

---

## 6. Retained distinctions

```text
Projection Serviceability
    ≠ Exposure

Exposure
    ≠ participation

participation
    ≠ availability
    ≠ Actor Authorisation
    ≠ execution permission

BoundedProjectionReadBinding
    ≠ domain identity
    ≠ participation identity
    ≠ continuation identity

T4b continuation validation
    ≠ T4c opaque encoding

IMP-06 generic completion
    ≠ concrete adapter portfolio completion
    ≠ concrete capability participation portfolio completion
```

---

## 7. Next governed action

After the cycle-closing governance commit containing this graph, S3 evidence, macro closure review and synchronised implementation status passes:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

begin IMP-07 by inspecting its accepted Publication → Enquiry authority and decomposing the first smallest executable vertical-slice node.

Do not implement a concrete query adapter merely to unlock T4c. Do not retire `MS-WATCH-002`. Do not infer Publication → Enquiry participation; the first real source must be owned and proven by the IMP-07 vertical slice.
