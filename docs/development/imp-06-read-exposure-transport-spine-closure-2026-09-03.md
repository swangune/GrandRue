# IMP-06 Read, Exposure & Transport Spine — Macro Closure Review

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro-node:** IMP-06 — Read, Exposure & Transport Spine  
**Date:** 3 September 2026  
**Status:** **CONFORMING_COMPLETE**

---

## 1. Governing authority

This closure review is governed by:

- `designs/MS-IMP-001.md`;
- `designs/IMPLEMENTATION-RULES.md`;
- accepted Projection, Exposure, Surface, Audience Observation, Resource Protection and API/transport authority indexed by `designs/AUTHORITY-INDEX.md`; and
- the fine-grained IMP-06 conformance evidence under `docs/development/`.

This document is implementation evidence and programme navigation only. It creates no new semantic or architectural authority.

---

## 2. MS-IMP-001 scope

MS-IMP-001 defines IMP-06 to provide:

```text
Projection Contract infrastructure
Projection Serviceability
Exposure resolution
Surface Contributions
Audience Observation Context
Resource Protection
API Contract infrastructure
trusted transport-context resolution
problem/outcome mapping foundation
bounded query/pagination infrastructure
```

MS-IMP-001 also states that no broad catalogue of endpoints is required at this stage.

The question for macro closure is therefore whether the reusable generic spine is complete — not whether every future concrete endpoint, owner mapping, query adapter or capability-specific participation source has already been implemented.

---

## 3. Scope-to-evidence closure matrix

| IMP-06 responsibility | Fine-grained closure | Macro conclusion |
|---|---|---|
| Projection Contract infrastructure | P1 `CONFORMING_COMPLETE` | PASS |
| Projection Serviceability | P2 `CONFORMING_COMPLETE` | PASS |
| Exposure resolution | E1/E2/E3 and E4-A..E4-J `CONFORMING_COMPLETE` | PASS |
| Surface Contributions | S1, S2 and S3 `CONFORMING_COMPLETE` | PASS |
| Audience Observation Context | E3 `CONFORMING_COMPLETE` | PASS |
| Resource Protection | R1 `CONFORMING_COMPLETE` | PASS |
| API Contract infrastructure | T1a, T1b1, T1b2, T1b3, T1b4 `CONFORMING_COMPLETE` | PASS |
| trusted transport-context resolution | T2a/T2b/T2c `CONFORMING_COMPLETE`; concrete adapters remain T2d ON_DEMAND | PASS |
| problem/outcome mapping foundation | T3a/T3b `CONFORMING_COMPLETE`; concrete mappings remain T3c ON_DEMAND | PASS |
| bounded query/pagination infrastructure | T4a/T4b `CONFORMING_COMPLETE`; concrete opaque encoding T4c is adapter-triggered | PASS |

The v1.13/v1.14 bounded-read prerequisite chain BR1..BR6 is also `CONFORMING_COMPLETE`, providing the exact P2/E4/S2 material-affinity spine consumed by T1b4, T4b and S3.

Merchant Location Exposure-choice prerequisite LEC1 is `CONFORMING_COMPLETE`.

---

## 4. Why T2d, T3c and T4c do not block macro closure

### T2d — concrete adapters

T2a/T2b/T2c establish the reusable trusted-context authority needed by concrete delivery adapters. T2d is intentionally ON_DEMAND because a concrete public/customer/integration/platform adapter must have a real source and contract to adapt.

Creating a blanket adapter merely to mark IMP-06 complete would manufacture endpoint work and would contradict the programme statement that no broad endpoint catalogue is required yet.

### T3c — concrete owner/adapter mappings

T3a establishes the closed problem/outcome vocabulary. T3b establishes the owner-qualified safe mapping authority spine. T3c necessarily depends on a real owner and real adapter outcome surface.

No missing generic mapping mechanism remains. Concrete mappings are therefore capability/adapter-triggered work, not an unmet macro foundation.

### T4c — opaque continuation encoding/adapter

T4a establishes the bounded collection contract. T4b establishes exact continuation semantic affinity and fail-closed validation without confusing request/bounded-read binding with continuation identity.

T4c is the concrete opaque representation and adapter integration. It remains blocked on a real query adapter. Inventing a token format without a concrete query delivery path would be speculative transport design, not generic-spine completion.

Therefore:

```text
T2d ON_DEMAND
T3c ON_DEMAND
T4c BLOCKED on concrete query adapter
```

are retained latent/conditional nodes. They are not hidden READY work and they do not make IMP-06 incomplete.

---

## 5. S3 final Surface dependency

Before MS-PROT-049 v1.4, S3 exposed a real programme cycle:

```text
IMP-06 completion
    → S3
    → concrete capability-owned positive participation source
    → first such source owned by IMP-07 Publication → Enquiry
    → IMP-07 HARD-blocked on IMP-06
```

MS-PROT-049 v1.4 repaired the cycle at the smallest responsible boundary by defining explicit owner-routed participation sources and allowing a fail-closed zero-source generic S3 state.

S3 is now independently proven `CONFORMING_COMPLETE` by:

`docs/development/imp-06-s3-public-interaction-binding-conformance-2026-09-03.md`.

The first concrete positive Publication → Enquiry source remains correctly owned by IMP-07 and is not pulled backward into the generic spine.

---

## 6. Macro closure falsification

The closure was challenged against the following failure hypotheses.

### Hypothesis A — trusted transport context is still only prototype code

**Falsified.** T2a/T2b/T2c establish production exact-surface/scope, merchant-session and staff-operational context authority. What remains is adapter-specific wiring, intentionally ON_DEMAND.

### Hypothesis B — problem/outcome mapping still lacks a generic production foundation

**Falsified.** T3a/T3b provide the production vocabulary and owner/evidence-affined safe mapping authority. T3c requires a concrete owner/adapter and is therefore downstream-triggered.

### Hypothesis C — bounded query/pagination is incomplete until an opaque cursor token exists

**Falsified.** T4a defines bounded collection semantics and T4b proves semantic continuation affinity/validation. Encoding is representation/adaptation and cannot be meaningfully selected without the concrete query adapter that consumes it.

### Hypothesis D — S3 cannot be complete without a real capability source

**Falsified by accepted authority and implementation evidence.** MS-PROT-049 v1.4 explicitly defines empty registered production sources as a legitimate fail-closed generic state. S3 tests prove zero sources produce zero bindings while a test-only source proves positive mechanics without importing capability semantics.

### Hypothesis E — IMP-07 Publication → Enquiry semantics must be pulled into IMP-06 to prove the spine

**Rejected.** That would violate macro ownership and recreate semantic gravity. IMP-07 is the first mandatory vertical slice and is the proper place for the first real `Opportunity → enquiry / send-enquiry` participation source.

### Hypothesis F — an unclassified remaining READY IMP-06 node exists

**Falsified by the current fine-grained graph.** All generic nodes are `CONFORMING_COMPLETE`; remaining T2d/T3c/T4c work is explicitly ON_DEMAND or dependent on a concrete adapter. No unresolved IMP-06 design gate remains.

---

## 7. Verification frontier

The final code-bearing IMP-06 frontier is the hardened S3 head:

```text
head:               97a5fef2a9834d5d9dfe620867b46a4f255a2def
GitHub Actions run: 33814624759
job:                100843888917
command:            mvn --batch-mode clean verify -Ppostgres-it
result:             SUCCESS
```

S3 lineage:

```text
accepted design formalisation: 44b395d4a8cfbfe79ec74c58923fbc00b8c16b44
RED:                          ef9fa54a58b8f686fabb4b3acde6717e7967094c
minimum GREEN:                3404ce25dbe8c8e452e079a9f37b20afee9d7488
hardened code-bearing head:   97a5fef2a9834d5d9dfe620867b46a4f255a2def
```

A final docs-only cycle-closing commit must still pass the same repository-wide Maven/PostgreSQL gate before IMP-07 execution begins.

---

## 8. Persistent watch state

`MS-WATCH-002` remains **WATCHING — PROGRAMME/ARCHITECTURE DEPENDENCY**.

S3 proves that the current mitigation works:

```text
explicit owner-routed source registry
    +
zero-source fail-closed generic state
    +
no participation inference
```

The watch is not retired because the first real backend participation source has not yet been implemented and a future interaction family could recreate the same dependency pressure.

The next mandatory review point is the first real Publication → Enquiry owner source under IMP-07.

`MS-WATCH-001` semantic-gravity surveillance also remains unaffected.

---

## 9. Deferred/conditional work that does not prevent closure

The following do not reopen IMP-06:

- T2d concrete delivery adapters — ON_DEMAND per real API surface;
- T3c concrete owner/adapter problem mappings — ON_DEMAND per real adapter;
- T4c opaque continuation encoding/adapter — dependent on a concrete query adapter;
- first real Publication → Enquiry participation source — IMP-07;
- capability-specific Booking/Ordering/Appointment participation sources — their owning later vertical slices;
- provider readiness/execution — later provider/runtime macro scope; and
- broad endpoint catalogue — explicitly not required by IMP-06.

When triggered later, these children must consume the IMP-06 generic authority and must not redefine it.

---

## 10. Closure decision

IMP-06 completion criteria are satisfied:

```text
Projection Contract infrastructure             PASS
Projection Serviceability                      PASS
Exposure resolution                            PASS
Surface Contributions                          PASS
Audience Observation Context                   PASS
Resource Protection                            PASS
API Contract infrastructure                    PASS
trusted transport-context resolution           PASS
problem/outcome mapping foundation             PASS
bounded query/pagination infrastructure        PASS
S3 owner-routed participation boundary         PASS
no unresolved generic READY node               PASS
full code-bearing Maven/PostgreSQL gate        PASS
macro-scope falsification                      PASS
```

> **IMP-06 — Read, Exposure & Transport Spine: CONFORMING_COMPLETE.**

Under `MS-IMP-001`, IMP-07 becomes the next macro target after the cycle-closing governance commit containing this review, the S3 conformance record, graph refresh and implementation-status synchronisation passes the full repository verification gate.
