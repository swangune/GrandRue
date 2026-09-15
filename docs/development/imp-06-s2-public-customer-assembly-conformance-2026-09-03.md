# IMP-06 S2 PUBLIC/CUSTOMER Assembly Conformance Record

> **Date:** 3 September 2026  
> **Branch:** `development`  
> **Node:** `S2 — final PUBLIC/CUSTOMER Surface assembly`  
> **Result:** **CONFORMING_COMPLETE**  
> **Verified code-bearing baseline:** `development@f92f9118f6dae7344355c5659723d4b04e7c9707`

This record closes S2 under accepted MS-IMP-001 sequencing and IMPLEMENTATION-RULES v1.6. It is implementation evidence only and does not create semantic, architectural or programme authority.

---

## 1. Governing authority and scope

S2 is governed principally by:

- composite `MS-PROT-027`, especially v1.13 §§33–37, 53, 75 and 78;
- composite `MS-PROT-049` for Surface Contribution ownership and composition boundaries;
- completed S1 Surface Contribution infrastructure;
- completed BR1–BR6 bounded-read representation chain;
- `designs/MS-IMP-001.md`; and
- `designs/IMPLEMENTATION-RULES.md` v1.6.

The accepted separation is:

```text
S1
    owns established Surface Contribution infrastructure
        ↓
BR1..BR6
    establish exact bounded-read / P2 / E4-positive material selection
        ↓
S2
    assembles the final immutable PUBLIC/CUSTOMER projection material seam
        ↓
T1b4
    maps that safe bounded representation through a registered query contract
```

S2 therefore does not recreate Surface Contribution membership, capability participation, Projection Serviceability or Exposure authority. Existing S1/MS-PROT-049 responsibilities remain independently authoritative.

---

## 2. Governing invariant

For a successful S2 assembly:

```text
BoundedProjectionRead B1
        +
P2 result affined to B1
        +
E4 resolution affined to the same request / merchant / release
        ↓
BR6 same-read positive selection
        ↓
immutable PublicCustomerProjectionAssembly
    - exact B1 binding
    - only selected B1 fragments
    - bounded-read order preserved
```

S2 must not:

```text
re-run P2
re-run E4
read owner repositories
fetch a newer value after E4
manufacture Surface Contribution membership
infer capability participation
introduce transport DTO identity
perform HTTP/query transport mapping
```

---

## 3. Tests-first evidence

Tests-only RED commit:

- `559f8eaf6aadca28fca11f821ec58444de897ff5` — `test(imp-06): add S2 public customer assembly RED`

GitHub Actions:

- run `33776252895` / check `100718578192` — **FAILED as intended**.

The RED was specific to absent S2 assembly production types rather than a weakened or pre-existing expectation.

---

## 4. Minimum production implementation

Production commits:

- `82dd6fb3d218b13e6f657f41fc24abceb0845c61` — `feat(imp-06): add S2 public customer projection assembly`;
- `f92f9118f6dae7344355c5659723d4b04e7c9707` — `feat(imp-06): complete S2 public customer assembly service`.

The implementation adds only:

- `PublicCustomerProjectionAssembly` — immutable exact bounded-read binding plus immutable selected fragments; and
- `PublicCustomerProjectionAssemblyService` — null-checked generic composition boundary delegating exact material selection to the already-conforming BR6 selector.

The service does not hold repository, provider, capability-execution or transport dependencies. It does not inspect owner-specific business values.

---

## 5. Adversarial and architecture review

`PublicCustomerProjectionAssemblyServiceTest` proves the S2-specific boundary through four focused tests:

1. only BR6-selected material enters the assembly;
2. selected material preserves bounded-read order and the exact read binding;
3. the returned selected-fragment collection is immutable;
4. cross-request/structural affinity failure invalidates the whole attempt;
5. P2-omitted material cannot be laundered into an otherwise positive assembly; and
6. the S2 service carries no repository/provider/capability-execution/interaction dependency.

The broader BR6 adversarial suite remains applicable to the delegated same-read selection responsibility and already covers cross-read substitution, forged provenance, NOT_SERVICEABLE input, omitted/non-applicable source material, missing positive correspondence and E4-order versus bounded-read-order attacks.

Comparison of the BR6 cycle-closing head to the S2 code-bearing head:

```text
905ae6ec8789dfff9404c5e7cc22cba746799404
    ..
f92f9118f6dae7344355c5659723d4b04e7c9707
```

contains exactly three S2 files:

- `src/main/java/mainstreet/surface/PublicCustomerProjectionAssembly.java`;
- `src/main/java/mainstreet/surface/PublicCustomerProjectionAssemblyService.java`;
- `src/test/java/mainstreet/surface/PublicCustomerProjectionAssemblyServiceTest.java`.

No P2 engine, E4 resolver/evaluator, capability owner, repository, API transport adapter, semantic authority file or persistence migration changed.

### Falsification conclusion

The plausible failure mode that S2 might need to reimplement S1 contribution composition was rejected by repository/authority review: the IMP-06 baseline already classifies S1 contribution registration/composition and structural PUBLIC/CUSTOMER candidate resolution as conforming, while S2 was specifically blocked on exact P2 evidence plus final E4/BR6 bounded representation. Reintroducing contribution ownership inside S2 would duplicate an accepted responsibility rather than close a missing one.

No implementation-discovered material design ambiguity remains for S2.

---

## 6. Final verification

Final code-bearing baseline:

`development@f92f9118f6dae7344355c5659723d4b04e7c9707`

GitHub Actions:

- workflow: `Maven Tests`;
- run: `33776447518`;
- job/check: `100719231110`;
- command configured by workflow: `mvn --batch-mode clean verify -Ppostgres-it`;
- PostgreSQL service: production-representative PostgreSQL integration profile;
- result: **SUCCESS**.

The available workflow metadata proves successful completion of the unit and PostgreSQL integration test step. This closure does not invent an aggregate test-count figure that is not exposed by the available final-run metadata.

---

## 7. Composite-architecture conformance

S2 uses the smallest fitting constituent pattern:

```text
immutable typed runtime representation
    +
ordinary deterministic orchestration
    +
delegation to already-conforming BR6 exact-selection authority
```

It does not introduce CQRS, an event workflow, a generic payload bag, a provider abstraction, a new capability boundary or distributed-system machinery. Generic Surface remains isolated from Profile/Booking/Inventory business interpretation.

Result: **PASS**.

---

## 8. Dependency consequence

S2 is **CONFORMING_COMPLETE**.

Its completion satisfies the remaining S2 dependency for:

```text
T1b4 — first production bounded query definition/path
    → READY — CURRENT SMALLEST EXECUTABLE NODE
```

No broader promotion follows:

- `IMP-06` remains **PARTIALLY_CONFORMING** until its remaining required query/transport closure is complete;
- `S3` remains **BLOCKED_DEPENDENCY** on authoritative capability-owned Public Interaction participation sources;
- `T4b` remains blocked on T1b4;
- `T4c` remains blocked on T4b plus a concrete query adapter; and
- `IMP-07` remains **BLOCKED_DEPENDENCY** on complete IMP-06.

The next implementation cycle may select T1b4 only after this evidence, graph refresh and implementation-status synchronisation are committed consistently.