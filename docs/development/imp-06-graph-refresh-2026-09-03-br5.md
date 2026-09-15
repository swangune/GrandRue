# IMP-06 Fine-Grained Graph Refresh — BR5 Closure

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Programme target:** `IMP-06 — Read, Exposure & Transport Spine`  
> **Macro state:** **PARTIALLY_CONFORMING**  
> **Current smallest executable node:** `BR6 — same-read E4-positive fragment selection` — **READY**

This graph refresh records implementation state after BR5 conformance closure. It is navigation/evidence only and does not create design authority.

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
    BR6 same-read E4-positive fragment selection             READY — CURRENT SMALLEST EXECUTABLE NODE

Surface
    S1 Surface Contribution infrastructure                   CONFORMING_COMPLETE
    S2 final PUBLIC/CUSTOMER assembly                        BLOCKED_DEPENDENCY on BR6
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
    T1b4 query                                               BLOCKED_DEPENDENCY on BR6 + S2
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
BR6 READY
  ↓
S2
  ↓
T1b4
  ↓
IMP-06 completion
  ↓
IMP-07
```

BR5 evidence:

`docs/development/imp-06-br5-owner-progress-affinity-conformance-2026-09-03.md`

Final BR5 code-bearing baseline:

`development@b4ddc4e250ea98d7012555e15e0f4a25d9a8f6a6`

Final BR5 full-profile verification:

- GitHub Actions run `33722015716`
- job/check `100542935385`
- `mvn --batch-mode clean verify -Ppostgres-it` — SUCCESS

---

## 3. Promotion decision

BR5 has satisfied its accepted owner-progress-affinity invariant and complete verification requirements. BR6 therefore has no remaining BR5 dependency and is promoted to **READY**.

No broader promotion follows automatically:

- S2 remains blocked until BR6 conforms;
- T1b4 remains blocked until BR6 and S2 conform;
- IMP-06 remains **PARTIALLY_CONFORMING**; and
- S3 remains independently blocked on authoritative capability-owned subject-interaction participation sources.

`MS-WATCH-002 — Public Interaction Participation-Source / Programme Dependency Deadlock` remains WATCHING. BR5 closure neither resolves nor worsens that dependency by itself.

---

## 4. Next executable node

Proceed under `designs/IMPLEMENTATION-RULES.md` v1.6 with BR6 only:

```text
read BR6 authority
    ↓
derive same-read E4-positive fragment-selection invariant
    ↓
tests-only RED
    ↓
observe intended failure
    ↓
minimum production GREEN
    ↓
full conformance / PostgreSQL verification
    ↓
record evidence + refresh graph + synchronise implementation-status.md
```

Do not begin S2 or T1b4 before BR6 is conforming. Do not infer or synthesize S3 participation truth while progressing the bounded-representation chain.
