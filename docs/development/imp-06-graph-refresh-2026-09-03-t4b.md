# IMP-06 Fine-Grained Graph Refresh — T4b Closure

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Programme target:** `IMP-06 — Read, Exposure & Transport Spine`  
> **Macro state:** **PARTIALLY_CONFORMING**  
> **Verified code-bearing baseline:** `development@fdd859a2a68e62407bff8cc6fe50f52ff4c54b45`  
> **Current smallest executable production-code node:** **NONE**  
> **Next governed action:** `IMP-06 macro applicability/completion review` — **READY after cycle-closing head verifies**

This graph refresh records implementation state after T4b conformance closure. It is implementation navigation/evidence only and does not create design or programme authority.

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
    T4b continuation affinity/validation                     CONFORMING_COMPLETE
    T4c opaque continuation encoding/adapter                 BLOCKED_DEPENDENCY on concrete query adapter
```

---

## 2. Critical path reached

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
T4b ✓
  ↓
IMP-06 macro applicability/completion review
  ↓
if complete under accepted macro scope
    → IMP-06 COMPLETE
    → IMP-07 READY

if an applicable unresolved foundation remains
    → classify the exact node/dependency
    → do not promote IMP-07
```

T4b evidence:

`docs/development/imp-06-t4b-continuation-affinity-conformance-2026-09-03.md`

Final T4b code-bearing baseline:

`development@fdd859a2a68e62407bff8cc6fe50f52ff4c54b45`

Final T4b verification:

- GitHub Actions run `33805294267`;
- job/check `100814217917`;
- `mvn --batch-mode clean verify -Ppostgres-it` — **SUCCESS**.

---

## 3. T4b promotion decision

T4b satisfies the accepted semantic continuation-affinity/validation responsibility without absorbing opaque encoding, cursor representation, concrete applied-filter state, HTTP transport or query execution.

Therefore:

```text
T4b
    CONFORMING_COMPLETE
        ↓
T4c prerequisite: T4b
    SATISFIED

T4c prerequisite: concrete query adapter
    UNSATISFIED
        ↓
T4c
    BLOCKED_DEPENDENCY
```

No accepted dependency rule promotes a blanket T2d adapter merely because T4b completed. Existing graph authority states that `T2d` concrete transport adapters and `T3c` concrete mappings activate ON_DEMAND when a concrete production vertical slice requires them under accepted API authority.

MS-IMP-001 also states that IMP-06 establishes the read/exposure/transport infrastructure and that no broad catalogue of endpoints is required at this stage.

Accordingly, this refresh does not invent a concrete query adapter solely to make T4c executable.

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

ObservationRequestBinding
    ≠ continuation identity
    ≠ cross-request authority

BoundedProjectionReadBinding
    ≠ continuation identity
    ≠ business/domain identity

ApiContinuationAffinityCandidate
    ≠ authentication credential
    ≠ Merchant Scope authority
    ≠ resource authority
    ≠ opaque token representation
    ≠ cursor/keyset position

T4b semantic affinity validation
    ≠ T4c token encoding/decoding
    ≠ concrete query adapter
    ≠ transport syntax validation

static permitted-filter vocabulary affinity
    ≠ concrete applied filter-value/cursor-state affinity

PUBLIC/CUSTOMER bounded query
    ≠ Public Interaction participation truth
```

---

## 5. Independent deferred / blocked nodes

### S3

```text
S3 — Public Interaction Binding projection
    BLOCKED_DEPENDENCY
        on authoritative capability-owned
        subject-interaction participation sources
```

This remains monitored under `MS-WATCH-002`. Neither T4b nor API transport may infer Public Interaction participation truth from type, category, route, label, provider name or AI inference.

### T2d / T3c

```text
T2d concrete adapters    ON_DEMAND
T3c concrete mappings    ON_DEMAND
```

Accepted graph authority activates these when a concrete production vertical slice requires them. Their blanket implementation is not a prerequisite merely for infrastructure completeness.

### T4c

```text
T4c opaque continuation encoding/adapter
    BLOCKED_DEPENDENCY on concrete query adapter
```

T4b completion removes only the T4b side of T4c's dependency conjunction.

---

## 6. No immediate production-code READY node

After T4b, the current graph contains no dependency-complete production-code node that can be selected without either:

- activating a concrete adapter/mapping for a real production vertical slice; or
- manufacturing a missing capability-owned source or concrete query solely to unblock infrastructure.

Neither is authorised by the current graph.

The correct next governed action is therefore a macro applicability/completion review of IMP-06, not speculative production code.

That review must determine, against accepted MS-IMP-001 scope and the fine-grained graph, whether the remaining S3/T2d/T3c/T4c states are legitimately deferred/on-demand for later vertical slices or whether any one is a required unresolved IMP-06 completion dependency.

This graph refresh does **not** pre-judge that macro decision.

---

## 7. Macro state

Until the applicability/completion review is performed and evidenced:

- `IMP-06` remains **PARTIALLY_CONFORMING**;
- `IMP-07` remains **BLOCKED_DEPENDENCY** on complete IMP-06;
- `S3` remains blocked under `MS-WATCH-002`;
- `T2d` and `T3c` remain ON_DEMAND; and
- `T4c` remains blocked on a concrete query adapter.

No programme gate is bypassed by T4b closure.

---

## 8. Next governed action

After T4b evidence, this graph refresh and `implementation-status.md` are committed consistently and the resulting cycle-closing head verifies successfully:

```text
review MS-IMP-001 IMP-06 completion scope
    +
review accepted fine-grained ON_DEMAND / blocked classifications
    +
review S3 and T4c dependency provenance
        ↓
classify each remaining node as
    required-before-IMP-06-completion
    or legitimately deferred/on-demand
        ↓
perform corpus / dependency falsification
        ↓
if existing authority determines the result
    → record IMP-06 macro conformance/completion evidence automatically

if a material semantic / architectural / contract decision is missing
    → stop affected promotion
    → enter DESIGN-RULES lifecycle
```

Do not begin T4c, a blanket T2d/T3c adapter portfolio, S3 synthesis or IMP-07 before that review is complete and its resulting programme state is verified.
