# IMP-06 E4 Deterministic Exposure Resolution Conformance Evidence

Date: 2026-09-02  
Programme: IMP-06 — Read, Exposure & Transport Spine  
Node: E4 — Deterministic Production Exposure Resolution  
Record type: implementation evidence  
Status at this record: **CONFORMING_COMPLETE on `development`**

## 1. Governing authorities

Implementation sequence and eligibility are governed by `designs/MS-IMP-001.md`.

Implementation execution and conformance are governed by `designs/IMPLEMENTATION-RULES.md`.

The substantive E4 implementation is governed by the accepted composite Exposure authority through:

- `MS-PROT-027 v1.11 — Instance-Aware Exposure Membership & Owner-Filtered Deterministic Resolution Amendment`; and
- `MS-PROT-027 v1.12 — Opaque Evaluator Submission Affinity Amendment`.

The Profile-owned Merchant Location Exposure-choice prerequisite is governed by accepted `MS-PROT-051 v1.5`.

This record is implementation evidence only. It creates no new semantic or architectural authority.

## 2. Design-gate closure

E4 encountered two material design gaps during implementation:

- `IMP-06-E4-DG-001` — instance-aware Exposure membership and owner-filtered deterministic resolution, resolved by MS-PROT-027 v1.11;
- `IMP-06-E4-DG-002` — evaluator result submission affinity across requests/invocations/references, resolved by MS-PROT-027 v1.12.

Both decisions completed the required DESIGN-RULES lifecycle before implementation resumed. The canonical Deferred Decision Register records DG-002 as resolved rather than open/deferred.

The v1.12 amendment is deliberately narrow. It does not resolve S2/T1b4 bounded-read representation affinity and does not authorise a downstream re-query after Exposure.

## 3. E4 implementation decomposition

The E4 production slice was implemented and verified incrementally as:

```text
E4-A member identity specification
E4-B deterministic element/audience indexing
E4-C exact-release Exposure-definition encoding
E4-D instance-aware positive result boundary
E4-E OwnerExposureEvaluationContext
E4-F exact-release evaluator binding snapshots
E4-G batch-capable evaluator contracts and v1.12 submission affinity
E4-H initial Profile merchant-choice evaluators
E4-I deterministic generic resolver
E4-J targeted, PostgreSQL, architecture and full verification
```

The Profile Location Exposure-choice persistence prerequisite LEC1 is independently `CONFORMING_COMPLETE`.

## 4. v1.12 RED evidence

The approved v1.12 contract was first expressed as tests-only RED at:

`d3356de3bb8138237745e2f157a28751e480173f`

Commit: `test(surface): establish E4 evaluator submission affinity RED`

GitHub Actions evidence:

```text
workflow:           Maven Tests
run number:         1437
run id:             33678787799
job id:             100410009183
head:               d3356de3bb8138237745e2f157a28751e480173f
result:             FAILURE
failing step:       Run unit and PostgreSQL integration tests
```

Checkout, JDK and PostgreSQL-container setup all succeeded before the Maven verification step failed. This establishes a genuine RED against the then-absent approved v1.12 production contract.

## 5. Minimum GREEN implementation

The v1.12 implementation became GREEN at:

`4063d9d1139ac9a138d7649958b1d07779df0dba`

Commit: `feat(surface): enforce E4 evaluator submission affinity`

The production change:

- adds opaque `ExposureCandidateEvaluationBinding`;
- keeps concrete binding construction Surface-owned and non-public;
- adds immutable `ExposureCandidateEvaluationSubmission(binding, candidate)`;
- changes requirement and merchant-choice evaluator batch input from raw candidates to submission wrappers;
- changes evaluator result rows from repeated candidate identity to `binding + decision`;
- migrates initial Profile merchant-choice evaluators to echo the exact submitted binding;
- makes `ExposureResolver` issue fresh bindings for each candidate × logical evaluator call;
- privately maps current-call bindings back to exact candidate observations;
- validates exact returned binding coverage and rejects missing, foreign and duplicate bindings structurally; and
- preserves the existing requirement → merchant-choice → baseline decision semantics.

The correction does not add owner/business-specific branches to the generic resolver and does not add persistence, semantic-bundle state, public API identity or payload acquisition authority.

GitHub Actions evidence:

```text
workflow:           Maven Tests
run number:         1438
run id:             33680075048
job id:             100414227746
head:               4063d9d1139ac9a138d7649958b1d07779df0dba
result:             SUCCESS
```

## 6. Adversarial v1.12 falsification

A dedicated post-GREEN falsification corpus was added at:

`8ffc77a6bf763ebb8ba1ba80a7dbe990c03b7a73`

Commit: `test(surface): falsify E4 evaluator submission affinity`

The falsification proves at least:

- a stale binding for the same stable candidate from a prior Observation Request is structural;
- a stale binding from a prior admission invocation of the same request is structural;
- a binding from one exact requirement reference is foreign to another requirement reference;
- a requirement binding is foreign to a merchant-choice invocation;
- reordered complete current-binding results remain valid;
- current `UNRESOLVED` decisions remain candidate-local withholding rather than structural failure;
- one stable candidate receives fresh execution bindings across exact evaluator calls; and
- binding issuance remains private to Surface and does not contaminate owner context, stable candidate identity or positive membership.

GitHub Actions evidence:

```text
workflow:           Maven Tests
run number:         1439
run id:             33680475315
job id:             100415539482
head:               8ffc77a6bf763ebb8ba1ba80a7dbe990c03b7a73
result:             SUCCESS
```

## 7. Architecture-conformance verification

A separate architecture-conformance corpus was added at:

`b83e7719112fe5ac472e89d20cbf03873900c6bf`

Commit: `test(surface): verify E4 submission affinity architecture`

It establishes that:

- the opaque affinity contract and concrete issuer remain Surface-owned;
- concrete binding construction is not public owner extension authority;
- execution affinity does not enter `ExposureCandidateObservation`;
- execution affinity does not enter `ExposedElementMembership`;
- `OwnerExposureEvaluationContext` remains free of evaluation binding and raw request/admission/release affinity;
- evaluator interfaces accept submission wrappers;
- result rows contain exactly `evaluationBinding + decision` rather than duplicated candidate identity;
- Profile evaluators depend only on narrow Profile read ports and cannot own Surface binding issuance; and
- the generic `ExposureResolver` retains no Merchant Profile, Booking, Ordering, Inventory, Payment, Fulfilment, Scheduling or Publication domain dependency as state or public contract.

## 8. Authoritative full verification

The final E4 architecture evidence head was verified by:

```text
workflow:                       Maven Tests
run number:                     1440
run id:                         33680886478
job id:                         100416903089
branch:                         development
head:                           b83e7719112fe5ac472e89d20cbf03873900c6bf
command:                        mvn --batch-mode clean verify -Ppostgres-it
unit / conformance tests:       860 PASS
PostgreSQL integration tests:   307 PASS
total tests:                    1,167 PASS
Flyway migrations:              52 validated/applied
failures / errors / skipped:    0 / 0 / 0
result:                         BUILD SUCCESS
```

The full verification includes the resolver contract, resolver falsification, v1.12 submission-affinity contract, v1.12 affinity falsification, architecture-conformance tests, Profile owner evaluators, exact-release registry tests and the repository-wide PostgreSQL integration corpus.

## 9. Conformance conclusion

E4 is **CONFORMING_COMPLETE on `development`** because:

- E4-A through E4-F remain conforming prerequisites;
- E4-G implements the accepted v1.12 submission/result-affinity contract;
- E4-H initial Profile evaluators are migrated to the v1.12 submission wrapper without acquiring generic Surface authority;
- E4-I deterministically issues and validates exact current-call bindings while preserving v1.11 decision precedence and structural-failure semantics;
- stale cross-request, cross-invocation and cross-reference rows are mechanically rejectable;
- stable candidate identity, positive membership and owner context remain uncontaminated by execution affinity;
- the generic resolver remains owner/business agnostic and value-blind; and
- targeted falsification, architecture conformance, the complete unit suite and PostgreSQL integration suite are all GREEN on the final evidence head.

Accordingly E4-G, E4-H, E4-I and E4-J are `CONFORMING_COMPLETE`, and the E4 deterministic production Exposure resolution node is closed.

## 10. Explicit downstream boundary

E4 closure does **not** make Exposure membership a value-acquisition authority.

The accepted composite MS-PROT-027 through v1.12 still requires downstream S2/T1b4 representation to use the exact bounded read material that was proven legitimate/current/serviceable. It may not re-query a stable member after Exposure and return newer state merely because membership exists.

Therefore:

```text
E4 CONFORMING_COMPLETE
    ≠ S2 READY by itself
    ≠ T1b4 READY by itself
    ≠ bounded-read representation affinity resolved
```

S2/T1b4 remain subject to their independent bounded-read representation-affinity prerequisite.

## 11. Required graph refresh

After this evidence record is integrated, the fine-grained IMP-06 graph must be recomputed from the actual `development` frontier.

E4-dependent blockers must be removed where E4 was the only unresolved predecessor, while independent prerequisites must remain intact. No downstream node may be promoted solely because E4 is now complete if another accepted or unresolved dependency still applies.
