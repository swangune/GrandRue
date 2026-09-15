# IMP-06 Fine-Grained Graph Refresh — S2 Closure

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Programme target:** `IMP-06 — Read, Exposure & Transport Spine`  
> **Macro state:** **PARTIALLY_CONFORMING**  
> **Verified code-bearing baseline:** `development@f92f9118f6dae7344355c5659723d4b04e7c9707`  
> **Current smallest executable node:** `T1b4 — first production bounded query definition/path` — **READY**

This graph refresh records implementation state after S2 conformance closure. It is implementation navigation/evidence only and does not create design or programme authority.

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
    T1b4 query                                               READY — CURRENT SMALLEST EXECUTABLE NODE
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
S2 ✓
  ↓
T1b4 READY
  ↓
remaining applicable IMP-06 closure
  ↓
IMP-06 COMPLETE
  ↓
IMP-07 READY
```

S2 evidence:

`docs/development/imp-06-s2-public-customer-assembly-conformance-2026-09-03.md`

Final S2 code-bearing baseline:

`development@f92f9118f6dae7344355c5659723d4b04e7c9707`

Final S2 verification:

- GitHub Actions run `33776447518`;
- job/check `100719231110`;
- `mvn --batch-mode clean verify -Ppostgres-it` — **SUCCESS**.

---

## 3. Promotion decision

S2 satisfies the accepted final bounded PUBLIC/CUSTOMER representation boundary without reopening S1 Surface membership/composition, P2, E4 or capability ownership.

Therefore:

```text
S2
    CONFORMING_COMPLETE
        ↓
T1b4
    READY
```

No broader promotion follows automatically:

- `IMP-06` remains **PARTIALLY_CONFORMING**;
- `IMP-07` remains blocked on complete IMP-06;
- `S3` remains independently blocked on authoritative capability-owned subject-interaction participation sources;
- `T4b` remains blocked until T1b4 conforms; and
- `T4c` remains blocked on T4b plus a concrete query adapter.

`MS-WATCH-002 — Public Interaction Participation-Source / Programme Dependency Deadlock` remains WATCHING. S2 closure does not authorise Surface or API code to infer participation truth.

---

## 4. Preserved boundaries

```text
Surface Contribution membership
    ≠ Projection material

P2 serviceability
    ≠ E4 Exposure

E4 positive membership
    ≠ data-acquisition authority

BR6 selected material
    ≠ transport DTO

S2 immutable assembly
    ≠ query contract
    ≠ HTTP mapping
    ≠ execution authority

BoundedProjectionReadBinding
    ≠ continuation identity
    ≠ business/domain identity

PUBLIC/CUSTOMER assembly
    ≠ Public Interaction participation truth
```

---

## 5. Next executable node

Proceed under `designs/IMPLEMENTATION-RULES.md` v1.6 with T1b4 only after the S2 cycle-closing evidence/graph/status state is committed:

```text
read T1b4 authority and existing T1a/T1b/T4a seams
    ↓
derive the smallest first production bounded query contract/path
    ↓
write tests-only RED
    ↓
observe intended failure
    ↓
implement minimum production GREEN
    ↓
contract / architecture / adversarial verification
    ↓
full mvn --batch-mode clean verify -Ppostgres-it
    ↓
record T1b4 evidence
    ↓
refresh graph and macro state
    ↓
synchronise implementation-status.md
    ↓
commit the T1b4 cycle-closing state
```

Do not promote S3, T4b, T4c or IMP-07 except through their declared dependency conditions.