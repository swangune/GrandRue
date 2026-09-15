# IMP-06 Fine-Grained Graph Refresh — T1b4 Closure

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Programme target:** `IMP-06 — Read, Exposure & Transport Spine`  
> **Macro state:** **PARTIALLY_CONFORMING**  
> **Verified code-bearing baseline:** `development@cd5434eba2d81598c8320c95d5d31d06ae07dfe0`  
> **Current smallest executable node:** `T4b — continuation affinity/validation` — **READY**

This graph refresh records implementation state after T1b4 conformance closure. It is implementation navigation/evidence only and does not create design or programme authority.

---

## 1. Current fine-grained graph

```text
Projection
    P1 Projection Contract registration                     CONFORMING_COMPLETE
    P2 Projection Serviceability evaluation                 CONFORMING_COMPLETE

v1.13/v1.14 bounded representation prerequisite chain
    BR1 bounded-read/binding/fragment integrity primitives   CONFORMING_COMPLETE
    BR2 P2 exact bounded-read binding                        CONFORMING_COMPLETE
    BR3 owner/query material + source affinity               CONFORMING_COMPLETE
    BR4 material-affinity observation contribution           CONFORMING_COMPLETE
    BR5 owner evaluator current-progress comparison          CONFORMING_COMPLETE
    BR6 same-read E4-positive fragment selection             CONFORMING_COMPLETE

Surface
    S1 Surface Contribution infrastructure                   CONFORMING_COMPLETE
    S2 final PUBLIC/CUSTOMER assembly                        CONFORMING_COMPLETE
    S3 Public Interaction Binding projection                 BLOCKED_DEPENDENCY on capability-owned participation sources

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
    T2d concrete adapters                                    ON_DEMAND
    T3a outcome/problem vocabulary                           CONFORMING_COMPLETE
    T3b owner/evidence safe mapping spine                    CONFORMING_COMPLETE
    T3c concrete mappings                                    ON_DEMAND
    T4a bounded collection contract                          CONFORMING_COMPLETE
    T4b continuation affinity/validation                     READY — CURRENT SMALLEST EXECUTABLE NODE
    T4c opaque continuation encoding/adapter                 BLOCKED_DEPENDENCY on T4b + concrete query adapter
```

---

## 2. Critical path

```text
BR1 ✓
  ↓
BR2 ✓
  ↓
BR3 ✓
  ↓
BR4 ✓
  ↓
BR5 ✓
  ↓
BR6 ✓
  ↓
S2 ✓
  ↓
T1b4 ✓
  ↓
T4b READY
  ↓
remaining applicable IMP-06 closure
  ↓
IMP-06 COMPLETE
  ↓
IMP-07 READY
```

T1b4 evidence:

`docs/development/imp-06-t1b4-bounded-query-conformance-2026-09-03.md`

Final T1b4 code-bearing baseline:

`development@cd5434eba2d81598c8320c95d5d31d06ae07dfe0`

Final T1b4 verification:

- GitHub Actions run `33799702740`;
- job/check `100795878911`;
- `mvn --batch-mode clean verify -Ppostgres-it` — **SUCCESS**.

---

## 3. Promotion decision

T1b4 satisfies the first production bounded query definition/path without reopening owner data acquisition, P2, E4, S2 material selection or transport/continuation responsibilities.

Therefore:

```text
T1b4
    CONFORMING_COMPLETE
        ↓
T4b
    READY
```

This promotion is narrow. It means only that T4b's declared T1b4 prerequisite is satisfied.

No broader promotion follows automatically:

- `IMP-06` remains **PARTIALLY_CONFORMING**;
- `IMP-07` remains blocked on complete IMP-06;
- `S3` remains independently blocked on authoritative capability-owned subject-interaction participation sources;
- `T4c` remains blocked on T4b plus a concrete query adapter;
- `T2d` remains **ON_DEMAND**; and
- `T3c` remains **ON_DEMAND**.

`MS-WATCH-002 — Public Interaction Participation-Source / Programme Dependency Deadlock` remains WATCHING. T1b4 closure does not authorise Surface or API code to infer Public Interaction participation truth.

---

## 4. Preserved boundaries

```text
P2 evidence
    ≠ business value

E4 positive membership
    ≠ data-acquisition authority

BR6 selected material
    ≠ transport DTO

S2 immutable assembly
    ≠ query contract
    ≠ continuation identity

T1b4 query definition/path
    ≠ owner repository read
    ≠ HTTP adapter
    ≠ continuation token encoder
    ≠ concrete owner-value mapper

BoundedProjectionReadBinding
    ≠ continuation identity
    ≠ business/domain identity

PUBLIC/CUSTOMER bounded query
    ≠ Public Interaction participation truth
```

The exact S2 assembly remains the safe bounded representation handoff for the first production Merchant Presence query. T1b4 does not grant generic Surface authority to inspect or serialize arbitrary owner-private business state.

---

## 5. Current independent blocker

S3 remains:

```text
S3 — Public Interaction Binding projection
    BLOCKED_DEPENDENCY
        on authoritative capability-owned
        subject-interaction participation sources
```

This is monitored under `MS-WATCH-002`. Nothing in T1b4 creates the missing participation source, and neither Surface nor transport may synthesize it from type, category, route, labels, provider names or AI inference.

---

## 6. Next executable node

Proceed under `designs/IMPLEMENTATION-RULES.md` v1.6 with T4b only after the T1b4 evidence, this graph refresh and `implementation-status.md` synchronisation are committed consistently and the cycle-closing head verifies successfully:

```text
read accepted T4a/T4b continuation authority
    ↓
identify continuation affinity separate from bounded-read/domain identity
    ↓
write T4b tests-only RED
    ↓
observe intended failure
    ↓
implement minimum continuation affinity/validation GREEN
    ↓
architecture / tamper / replay / cross-query adversarial review
    ↓
full mvn --batch-mode clean verify -Ppostgres-it
    ↓
record T4b evidence
    ↓
refresh graph and macro state
    ↓
synchronise implementation-status.md
    ↓
commit cycle-closing state
```

Do not begin T4c merely because T4b is READY. T4c remains blocked until T4b conforms and the declared concrete-query-adapter prerequisite is satisfied.