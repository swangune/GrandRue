# IMP-06 Fine-Grained Graph Refresh — 3 September 2026

**Macro target:** IMP-06 — Read, Exposure & Transport Spine  
**Sequence authority:** `designs/MS-IMP-001.md`  
**Execution authority:** `designs/IMPLEMENTATION-RULES.md`  
**Record type:** implementation navigation/evidence only  
**Last fully verified code-bearing baseline:** `development@e84918d24849f53fe2edf09bc7293d3f5058a364`  
**Baseline verification:** GitHub Actions run `33710572713` / job `100508951453` — SUCCESS (`mvn --batch-mode clean verify -Ppostgres-it`; 894 unit tests + 307 PostgreSQL integration tests = 1,201 tests; 0 failures, 0 errors)

This refresh incorporates the accepted authorities:

- MS-PROT-051 v1.5 — Merchant Location Public Exposure Choice Revision & Persistence Amendment;
- MS-PROT-027 v1.11 — Instance-Aware Exposure Membership & Owner-Filtered Deterministic Resolution Amendment;
- MS-PROT-027 v1.12 — Opaque Evaluator Submission Affinity Amendment;
- MS-PROT-027 v1.13 — Bounded Projection Read Material & Representation Affinity Amendment;
- MS-PROT-027 v1.14 — Observation Contribution Request-Binding Construction Amendment.

It is implementation navigation/evidence only and does not create substantive semantics.

## 1. Governing selection rule

```text
accepted substantive authority
        ↓
MS-IMP-001 macro eligibility
        ↓
dynamic fine-grained graph
        ↓
smallest dependency-complete READY node
        ↓
IMPLEMENTATION-RULES execution loop
```

A predecessor becoming conforming promotes only those direct successors whose complete dependency set is satisfied.

## 2. Current propagation

```text
IMP-06-E4-DG-001
    → RESOLVED by MS-PROT-027 v1.11

IMP-06-E4-DG-002
    → RESOLVED by MS-PROT-027 v1.12

IMP-06-S2-T1B4-DG-001
    → RESOLVED by MS-PROT-027 v1.13

IMP-06-BR4-DG-001
    → RESOLVED by MS-PROT-027 v1.14

LEC1 Merchant Location Exposure-choice persistence
    → CONFORMING_COMPLETE

E4-A .. E4-J
    → CONFORMING_COMPLETE

BR1 bounded-read / binding / fragment integrity primitives
    → CONFORMING_COMPLETE

BR2 P2 exact bounded-read binding
    → CONFORMING_COMPLETE

BR3 owner/query material acquisition + source-affinity proof
    → CONFORMING_COMPLETE

BR4 Material Affinity Observation Contributions
    → CONFORMING_COMPLETE

BR5 owner evaluator current-progress comparison
    → READY — CURRENT SMALLEST EXECUTABLE NODE

BR6 same-read E4-positive fragment selection
    → BLOCKED on BR5

S2 final PUBLIC/CUSTOMER Surface assembly
    → BLOCKED on BR6

T1b4 first production bounded query path
    → BLOCKED on S2/BR6
```

The accepted architecture preserves:

```text
Projection Serviceability
    ≠ Exposure

stable owner instance identity
    ≠ owner revision identity

stable candidate identity
    ≠ evaluator submission identity

BoundedProjectionReadBinding
    ≠ stable candidate identity
    ≠ continuation identity

Exposure membership
    ≠ evaluator submission identity
    ≠ data-acquisition authority

material-progress affinity evidence
    ≠ business value
    ≠ precomputed Exposure verdict

request-bounded observation
    ≠ global snapshot
    ≠ latest-through-final-byte guarantee

ObservationRequestBinding construction access
    ≠ general request introspection
    ≠ capability authority

bounded expected owner progress
    ≠ current owner choice
    ≠ permission to render
```

## 3. Refreshed IMP-06 graph

```text
IMP-06
  ├── P. Projection
  │     ├── P1 exact Projection Contract Java/registration
  │     │      CONFORMING_COMPLETE
  │     ├── P2 production Projection Serviceability evaluation
  │     │      CONFORMING_COMPLETE
  │     └── BR. v1.13/v1.14 bounded representation prerequisite chain
  │            ├── BR1 bounded-read/binding/fragment integrity      CONFORMING_COMPLETE
  │            ├── BR2 P2 exact bounded-read binding                CONFORMING_COMPLETE
  │            ├── BR3 owner/query material + source affinity       CONFORMING_COMPLETE
  │            ├── BR4 material-affinity observation contribution   CONFORMING_COMPLETE
  │            ├── BR5 owner evaluator current-progress comparison  READY
  │            └── BR6 same-read E4-positive fragment selection     BLOCKED on BR5
  │
  ├── S. Surface
  │     ├── S1 Surface Contribution infrastructure
  │     │      CONFORMING_COMPLETE
  │     ├── S2 final PUBLIC/CUSTOMER Surface assembly
  │     │      BLOCKED_DEPENDENCY on BR6
  │     └── S3 Public Interaction Binding projection
  │            BLOCKED_DEPENDENCY on capability-owned participation sources
  │
  ├── E. Exposure
  │     ├── E1 exact Exposure Element Contract Java/registration
  │     │      CONFORMING_COMPLETE
  │     ├── E2 Exposure semantic-bundle encoding/materialisation
  │     │      CONFORMING_COMPLETE
  │     ├── E3 Audience Observation Context + result boundary
  │     │      CONFORMING_COMPLETE
  │     └── E4 deterministic production Exposure resolution
  │            ├── E4-A member identity specification             CONFORMING_COMPLETE
  │            ├── E4-B element/audience contract indexing        CONFORMING_COMPLETE
  │            ├── E4-C exact-release definition encoding         CONFORMING_COMPLETE
  │            ├── E4-D instance-aware positive result boundary   CONFORMING_COMPLETE
  │            ├── E4-E OwnerExposureEvaluationContext            CONFORMING_COMPLETE
  │            ├── E4-F evaluator binding snapshots               CONFORMING_COMPLETE
  │            ├── E4-G batch evaluator + submission affinity     CONFORMING_COMPLETE
  │            ├── E4-H initial Profile choice evaluators         CONFORMING_COMPLETE
  │            ├── E4-I deterministic generic resolver            CONFORMING_COMPLETE
  │            └── E4-J final verification                        CONFORMING_COMPLETE
  │
  ├── LEC. Profile prerequisite for E4
  │     └── LEC1 Merchant Location Exposure-choice persistence
  │            CONFORMING_COMPLETE
  │
  ├── R. Resource Protection
  │     └── R1 production authority assessment
  │            CONFORMING_COMPLETE
  │
  └── T. API / transport / bounded query
        ├── T1 API Contract infrastructure
        │     ├── T1a common logical registration spine
        │     │      CONFORMING_COMPLETE
        │     └── T1b kind-specific definitions
        │            ├── T1b1 command       CONFORMING_COMPLETE
        │            ├── T1b2 callback      CONFORMING_COMPLETE
        │            ├── T1b3 media         CONFORMING_COMPLETE
        │            └── T1b4 query         BLOCKED_DEPENDENCY on BR6 + S2
        ├── T2 trusted transport-context resolution
        │     ├── T2a exact surface/scope authority spine       CONFORMING_COMPLETE
        │     ├── T2b Controller merchant context               CONFORMING_COMPLETE
        │     ├── T2c staff operational context                 CONFORMING_COMPLETE
        │     └── T2d concrete adapters                         ON_DEMAND
        ├── T3 problem/outcome mapping
        │     ├── T3a semantic vocabulary                       CONFORMING_COMPLETE
        │     ├── T3b safe owner/evidence mapping spine         CONFORMING_COMPLETE
        │     └── T3c concrete mappings                         ON_DEMAND
        └── T4 bounded query infrastructure
              ├── T4a bounded collection contract              CONFORMING_COMPLETE
              ├── T4b continuation affinity/validation         BLOCKED_DEPENDENCY on T1b4
              └── T4c opaque encoding/adapter                   BLOCKED_DEPENDENCY on T4b + concrete query adapter
```

## 4. Approved v1.13/v1.14 implementation chain

```text
E4-A .. E4-J
    CONFORMING_COMPLETE
    ↓
BR1 bounded-read/binding/fragment integrity primitives
    CONFORMING_COMPLETE
    ↓
BR2 P2 exact bounded-read binding
    CONFORMING_COMPLETE
    ↓
BR3 owner/query material acquisition + source-affinity proof
    CONFORMING_COMPLETE
    ↓
BR4 Material Affinity Observation Contribution registration/runtime binding
    CONFORMING_COMPLETE
    ↓
BR5 owner evaluator current-progress comparison
    READY
    ↓
BR6 same-read E4-positive fragment selection
    BLOCKED on BR5
    ↓
S2 final PUBLIC/CUSTOMER Surface assembly
    BLOCKED on BR6
    ↓
T1b4 first production bounded query definition/path
    BLOCKED on S2/BR6
```

The existing E4 conformance evidence remains authoritative for E4. v1.13 does not reopen E4 decision semantics. BR4 adds only the owner-qualified bounded expected-progress contribution required where existing owner decisions are materially revision/progress-affined. BR5 now activates the accepted v1.13 §28–30 owner-evaluator comparison against coherent current owner authority.

### BR1 / BR2 / BR3 / BR4 conformance evidence

BR1 is classified `CONFORMING_COMPLETE` because the implementation establishes the accepted bounded-read integrity primitives and falsification demonstrates fresh opaque read identity, exact request/release/contract/read-use affinity, immutable fragment/source-affinity metadata, duplicate competing fragment rejection and separation from continuation/domain identity.

BR2 is classified `CONFORMING_COMPLETE` because P2 bounded evaluation preserves the exact `BoundedProjectionReadBinding` on both success and fail-closed results, `ProjectionServiceabilityResult.hasExactBoundedReadAffinity(...)` centralises exact identity comparison, and adversarial tests reject both semantically-equal cross-read substitution and generic/unbound P2 laundering.

BR3 is classified `CONFORMING_COMPLETE` by `docs/development/imp-06-br3-owner-query-material-conformance-2026-09-03.md`. The Profile-owned read port obtains one coherent authoritative Merchant Public Descriptor observation, produces typed material and matching P2 evidence from the same exact owner revision, retains exact source dependency/progress affinity, rejects cross-merchant substitution, prevents external construction of the Profile-owned observation, and is guarded by module dependency tests so generic `surface` cannot depend upon `merchantprofile`.

BR4 is classified `CONFORMING_COMPLETE` by `docs/development/imp-06-br4-material-affinity-observation-conformance-2026-09-03.md`. The implementation preserves exact Observation Request and Merchant Scope affinity, exact semantic/runtime registration, PUBLIC/SINGLE cardinality, owner-derived bounded expected progress, capability ownership and a narrow E3 construction boundary. Adversarial tests cover the complete MS-PROT-027 v1.14 §18 checklist, including a real Profile-through-E3 admission proof and an explicit proof that material-affinity contents contain no rendered business value. The full PostgreSQL verification at `development@e84918d24849f53fe2edf09bc7293d3f5058a364` is green: 1,201 tests with 0 failures and 0 errors.

The older structural `ProjectionServiceabilityAuthority` seam remains non-final and does not satisfy or bypass BR6/S2. Final S2 is still blocked on BR6, so no legacy resolver is promoted to production bounded-read authority by this refresh.

## 5. Current READY-node conclusion

The current smallest dependency-complete executable node is:

```text
BR5 — owner evaluator current-progress comparison
```

BR5 is governed by accepted MS-PROT-027 v1.13 §§28–30 together with the already accepted E4 owner-evaluator boundaries. Where an existing Profile Exposure decision is materially revision/progress-affined, the owner evaluator must compare coherent current owner revision/progress with the expected bounded-material progress supplied by the owner-filtered BR4 contribution before applying the current governing choice/requirement.

Canonical accepted behaviour is:

```text
current candidate submission
        +
owner-filtered Material Affinity Observation Contribution
        ↓
Profile evaluator reads coherent current owner authority
        ↓
current owner progress == expected bounded-material progress
        → evaluate the current existing Profile choice/requirement

current owner progress != expected bounded-material progress
or current owner authority unavailable/unresolvable
        → UNRESOLVED
        → candidate WITHHOLD under existing E4 resolution
```

BR5 must preserve the accepted distinction:

```text
current R9 choice
    ✕ cannot authorise bounded R8 material
```

For Merchant Location it must also preserve MS-PROT-051 v1.5's separation between the current active Location revision and the current Location Exposure Choice revision: the current active Location source progress must first match the bounded expected progress; only then may the current Profile-owned Location Exposure Choice govern the candidate.

BR5 must not:

```text
interpret owner progress ordering generically
carry or inspect rendered business values in generic E4
use a newer owner choice to authorise older bounded material
turn material-affinity match into automatic EXPOSE
bypass other current E4 restrictions
perform BR6 same-read E4-positive fragment selection
assemble final S2 surfaces
perform post-E4 material reacquisition
implement T1b4 transport/query delivery
```

## 6. On-demand downstream nodes

`T2d` concrete transport adapters and `T3c` concrete owner/problem mappings remain on-demand. They activate only when a concrete production vertical slice requires them under accepted API authority.

S3 remains blocked by capability-owned Public Interaction participation sources; v1.13/v1.14 do not manufacture those sources.

## 7. Continuation boundary

T4b/T4c remain separately downstream:

```text
T1b4 bounded query definition/path
    ↓
T4b continuation query/scope/order affinity
    ↓
T4c concrete opaque continuation representation
```

`BoundedProjectionReadBinding` is ephemeral same-read affinity and SHALL NOT become continuation authority.

## 8. Next action

Proceed automatically under MS-IMPLEMENTATION-RULES-001 only within accepted BR5 semantics:

```text
inspect the exact existing Profile owner evaluators and accepted authority
    ↓
write BR5 tests-only RED for current-progress comparison
    ↓
observe intended RED
    ↓
implement minimum BR5 GREEN
    ↓
targeted/integration verification
    ↓
architecture/corpus checks
    ↓
BR5 adversarial falsification + structure review
    ↓
full PostgreSQL verification
    ↓
record evidence
    ↓
commit to development
    ↓
refresh graph
```

If BR5 implementation exposes a material semantic, ownership, consistency, API or architectural decision not already determined by accepted authority, affected implementation MUST stop and enter the DESIGN-RULES-governed manual-approval lifecycle required by `designs/IMPLEMENTATION-RULES.md`.
