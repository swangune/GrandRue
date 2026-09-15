# IMP-06 Graph Refresh — S3 Design Resolution — 3 September 2026

**Status:** POST-DESIGN-RESOLUTION DEPENDENCY REFRESH  
**Programme:** `IMP-06 — Read, Exposure & Transport Spine`  
**Verified code-bearing baseline:** `development@fdd859a2a68e62407bff8cc6fe50f52ff4c54b45`  
**Verified code-bearing run:** `33805294267` / job `100814217917` — SUCCESS  
**Prior cycle-closing governance head:** `development@f8e924efedbd73b5115508007795efee91bc8b98`  
**New accepted authority:** `MS-PROT-049 v1.4 — Capability-Owned Public Interaction Participation Source & Generic Binding Projection Amendment`  
**Resolved gate:** `IMP-06-S3-DG-001`

This graph refresh is implementation navigation/evidence only. Accepted semantic authority remains in the indexed design corpus and implementation sequencing remains governed by `MS-IMP-001.md` plus `IMPLEMENTATION-RULES.md`.

---

## 1. Review result

The post-T4b IMP-06 macro applicability review proved the `MS-WATCH-002` hard dependency cycle:

```text
IMP-06 completion
        ↓
requires generic S3 Public Interaction Binding projection
        ↓
naive completion assumed a positive production participation source
        ↓
MS-PROT-046 v1.2 requires first concrete source:
Opportunity → enquiry / send-enquiry
        ↓
that production source belongs to IMP-07 Publication → Enquiry
        ↓
MS-IMP-001: IMP-06 HARD → IMP-07
        ↓
cycle
```

Falsification showed that changing `MS-IMP-001` or moving Publication/Enquiry semantics into IMP-06 is unnecessary and larger than the actual gap.

MS-PROT-049 v1.4 resolves the gate by establishing a generic owner-routed participation-source contract and making an empty production-source registry a valid fail-closed generic S3 state:

```text
zero production participation sources
        ↓
zero positive subject bindings
```

Concrete positive participation remains owned by the applicable later capability vertical slice.

---

## 2. Refreshed fine-grained graph

```text
IMP-06
  ├── P. Projection
  │     ├── P1 exact Projection Contract registration                 CONFORMING_COMPLETE
  │     └── P2 production Projection Serviceability evaluation        CONFORMING_COMPLETE
  │
  ├── BR. bounded representation prerequisite chain
  │     ├── BR1 bounded-read/binding/fragment integrity               CONFORMING_COMPLETE
  │     ├── BR2 P2 exact bounded-read binding                         CONFORMING_COMPLETE
  │     ├── BR3 owner/query material + source affinity                CONFORMING_COMPLETE
  │     ├── BR4 material-affinity observation contribution            CONFORMING_COMPLETE
  │     ├── BR5 owner evaluator current-progress comparison           CONFORMING_COMPLETE
  │     └── BR6 same-read E4-positive fragment selection              CONFORMING_COMPLETE
  │
  ├── S. Surface
  │     ├── S1 Surface Contribution infrastructure                    CONFORMING_COMPLETE
  │     ├── S2 final PUBLIC/CUSTOMER Surface assembly                 CONFORMING_COMPLETE
  │     └── S3 Public Interaction Binding projection                  READY
  │            authority: MS-PROT-049 v1.4
  │            production sources may be empty and fail closed
  │            positive concrete sources remain later capability work
  │
  ├── E. Exposure
  │     ├── E1 Exposure Element Contract registration                 CONFORMING_COMPLETE
  │     ├── E2 semantic-bundle encoding/materialisation               CONFORMING_COMPLETE
  │     ├── E3 Audience Observation Context/result boundary           CONFORMING_COMPLETE
  │     └── E4-A .. E4-J deterministic production resolver            CONFORMING_COMPLETE
  │
  ├── R. Resource Protection
  │     └── R1 production authority assessment                        CONFORMING_COMPLETE
  │
  └── T. API / transport / bounded query
        ├── T1a registration spine                                    CONFORMING_COMPLETE
        ├── T1b1 command definition                                   CONFORMING_COMPLETE
        ├── T1b2 callback definition                                  CONFORMING_COMPLETE
        ├── T1b3 media-transfer definition                            CONFORMING_COMPLETE
        ├── T1b4 bounded query definition/path                        CONFORMING_COMPLETE
        ├── T2a exact surface/scope establishment                     CONFORMING_COMPLETE
        ├── T2b Controller merchant context                           CONFORMING_COMPLETE
        ├── T2c staff operational context                             CONFORMING_COMPLETE
        ├── T2d concrete adapters                                     ON_DEMAND
        ├── T3a problem/outcome vocabulary                            CONFORMING_COMPLETE
        ├── T3b owner/evidence safe mapping spine                     CONFORMING_COMPLETE
        ├── T3c concrete mappings                                     ON_DEMAND
        ├── T4a bounded collection contract                           CONFORMING_COMPLETE
        ├── T4b continuation affinity/validation                      CONFORMING_COMPLETE
        └── T4c concrete opaque continuation encoding/adapter         BLOCKED_DEPENDENCY
                 requires a real concrete query adapter
```

Macro state:

```text
IMP-06    PARTIALLY_CONFORMING
IMP-07    BLOCKED_DEPENDENCY on complete IMP-06
```

No concrete T2d/T3c/T4c dependency is manufactured merely to finish the generic spine.

---

## 3. Current smallest executable node

```text
S3 — production generic Public Interaction participation-source
     and binding-projection infrastructure
```

S3 is READY only after the accepted-authority formalisation/corpus-conformance head verifies successfully.

S3 must be implemented tests-first.

Minimum conformance includes:

```text
zero production sources → empty bindings
missing source → no inference
valid test-only source → positive path can be exercised
wrong scope/release/context → fail closed
duplicate exact participation ownership → reject
wrong contribution/subject affinity → reject
unexposed/unserviceable subject → no public binding
many-to-many participation → representable
```

The test-only positive source is verification scaffolding, not a production capability source.

---

## 4. Critical path

```text
P1✓ / P2✓
    ↓
E1-E4✓
    ↓
BR1-BR6✓
    ↓
S2✓
    ↓
T1b4✓
    ↓
T4a✓
    ↓
T4b✓
    ↓
MS-PROT-049 v1.4 / IMP-06-S3-DG-001✓
    ↓
S3 READY
    ↓
S3 implementation + adversarial conformance
    ↓
IMP-06 macro completion review/closure
    ↓
IMP-07 READY
```

The first concrete production subject-interaction source remains in IMP-07:

```text
Opportunity
    → enquiry / send-enquiry
```

---

## 5. Required distinctions

The graph preserves:

```text
participation source
    ≠ Surface authority
    ≠ Exposure authority

zero production source registry
    ≠ positive participation portfolio

generic S3 completion
    ≠ concrete interaction-family completion

Public Interaction Binding
    ≠ participation authority
    ≠ availability
    ≠ price authority
    ≠ Actor Authorisation
    ≠ execution permission

E4 positive membership
    ≠ subject-interaction participation

BoundedProjectionReadBinding
    ≠ semantic subject identity
    ≠ participation identity
    ≠ continuation identity

S3
    ≠ Publication implementation
    ≠ Enquiry implementation
    ≠ Booking/Appointment implementation
    ≠ Ordering implementation

T4b
    ≠ T4c token encoding/adapter
```

---

## 6. Watch consequence

`MS-WATCH-002` remains active.

The immediate Criterion-A cycle is resolved by MS-PROT-049 v1.4, but the failure mode is persistent. Future participation-source additions and future programme boundaries must still be checked for ownership transfer, semantic inference and cross-macro cycles.

---

## 7. Next action

After this governance formalisation head is green:

```text
S3 tests-only RED
    ↓
minimum conforming generic implementation
    ↓
GREEN
    ↓
adversarial hardening
    ↓
full Maven/PostgreSQL/architecture verification
    ↓
S3 conformance evidence
    ↓
graph/status closure
```

Do not begin IMP-07, create the concrete Opportunity → Enquiry production source, or declare IMP-06 complete before S3 has completed that cycle.
