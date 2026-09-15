# IMP-06 Fine-Grained Graph Refresh — BR6 Closure

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Programme target:** `IMP-06 — Read, Exposure & Transport Spine`  
> **Macro state:** **PARTIALLY_CONFORMING**  
> **Current smallest executable node:** `S2 — final PUBLIC/CUSTOMER Surface assembly` — **READY**

This graph refresh records implementation state after BR6 conformance closure. It is navigation/evidence only and does not create design authority.

---

## 1. Current fine graph

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
    S2 final PUBLIC/CUSTOMER assembly                        READY — CURRENT SMALLEST EXECUTABLE NODE
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
    T1b4 query                                               BLOCKED_DEPENDENCY on S2
    T2a exact surface/scope authority                        CONFORMING_COMPLETE
    T2b Controller merchant context                          CONFORMING_COMPLETE
    T2c staff operational context                            CONFORMING_COMPLETE
    T2d concrete adapters                                    ON_DEMAND
    T3a outcome/problem vocabulary                           CONFORMING_COMPLETE
    T3b owner/evidence safe mapping spine                    CONFORMING_COMPLETE
    T3c concrete mappings                                    ON_DEMAND
    T4a bounded collection contract                          CONFORMING_COMPLETE
    T4b continuation affinity/validation                     BLOCKED_DEPENDENCY on T1b4
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
S2 READY
  ↓
T1b4
  ↓
IMP-06 completion
  ↓
IMP-07
```

BR6 evidence:

`docs/development/imp-06-br6-same-read-fragment-selection-conformance-2026-09-03.md`

Final BR6 code-bearing baseline:

`development@c1d3cb85072a33d185884e467148b4420d6089bb`

Final BR6 full-profile verification:

- GitHub Actions run `33771843413`
- job/check `100703770328`
- `mvn --batch-mode clean verify -Ppostgres-it` — SUCCESS
- 914 unit/conformance + 307 PostgreSQL integration = **1,221 PASS**, 0 failures/errors/skips

---

## 3. Promotion decision

BR6 satisfied its accepted same-read representation-affinity invariant and complete verification requirements. S2 therefore has no remaining BR6 dependency and is promoted to **READY**.

No broader promotion follows automatically:

- T1b4 remains blocked until S2 conforms;
- IMP-06 remains **PARTIALLY_CONFORMING**;
- IMP-07 remains blocked on complete IMP-06; and
- S3 remains independently blocked on authoritative capability-owned subject-interaction participation sources.

`MS-WATCH-002 — Public Interaction Participation-Source / Programme Dependency Deadlock` remains WATCHING. BR6 closure does not authorize Surface to infer participation truth.

---

## 4. Next executable node

Proceed under `designs/IMPLEMENTATION-RULES.md` v1.6 with S2 only:

```text
read S2 authority and current Surface contribution seam
    ↓
derive final PUBLIC/CUSTOMER assembly invariant
    ↓
tests-only RED
    ↓
observe intended failure
    ↓
minimum production GREEN
    ↓
adversarial/conformance verification
    ↓
full mvn --batch-mode clean verify -Ppostgres-it
    ↓
record evidence + refresh graph + synchronise implementation-status.md
```

Do not begin T1b4 before S2 is conforming. Do not infer or synthesize S3 participation truth while progressing S2.