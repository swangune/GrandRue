# IMP-06 E3 Exposure Result Conformance Evidence

Date: 2026-09-02  
Programme: IMP-06 — Read, Exposure & Transport Spine  
Node: E3 — Audience Observation Context and Exposure Result Boundary  
Record type: implementation evidence  
Status at this record: **CONFORMING_COMPLETE on `development`**

## 1. Governing implementation authorities

Implementation sequence and eligibility are governed by `designs/MS-IMP-001.md`.

Implementation pattern and conformance execution are governed by `designs/IMPLEMENTATION-RULES.md`.

Accepted semantic/design authorities define the meaning to be implemented; they do not replace implementation sequence or execution rules. For this slice, the principal substantive authorities are the accepted MS-PROT-027 Exposure/Projection amendments through v1.10 and ADR-010 Semantic Release Assembly.

This record closes E3 only. It does not authorise E4 or select the next implementation node. After closure, the fine-grained IMP-06 graph must be refreshed and `MS-IMP-001.md` applied to the actual `development` frontier.

## 2. Design-gate closure

Implementation originally discovered two successive E3 design gaps:

- `IMP-06-E3-DG-001` — trusted Audience Observation Context establishment and API-surface-bound result separation, resolved by MS-PROT-027 v1.9;
- `IMP-06-E3-DG-002` — exact `ApiExposedElementSet` / `ApiExposureResolution` result boundary and fail-closed structural invariants, resolved by accepted MS-PROT-027 v1.10.

The second gate did not reopen the first. MS-PROT-027 v1.10 supplied the missing result contract without transferring Projection Serviceability, Actor Authorisation, Provider Readiness, execution, pagination, cache or transport authority into Exposure.

## 3. RED findings that triggered the implementation repair

Post-GREEN falsification of the earlier E3 checkpoint identified two implementation-conformance defects under already-accepted authority:

1. Projection policy semantics for multiple semantic owners had accumulated in `InitialProjectionPolicyEvaluatorPortfolio`, making a shared release-composition class an implementation home for owner-specific policy algorithms.
2. Exposure result construction did not sufficiently enforce exact affinity between an exposed candidate and the Exposure Element Contract whose provenance accompanied that candidate.

These were implementation defects, not reasons to invent new architecture.

## 4. GREEN repair

The repair preserves the generic Projection Serviceability and Exposure boundaries while restoring semantic ownership:

- owner-specific projection policy evaluator implementations are separated into owner-specific evaluator classes;
- `InitialProjectionPolicyEvaluatorPortfolio` is reduced to mechanical composition/registration rather than multi-owner semantic interpretation;
- shared `ProjectionPolicyEvaluationSupport` remains generic support rather than owner-policy authority;
- `ExposureElementContract` enforces owner affinity;
- `ResolvedExposedElement` is constructed from the exact `ExposureElementContract` and rejects candidate/contract reference mismatch;
- API Exposure result-boundary tests enforce immutable positive-only membership, successful-empty distinction, exact request/admission/release/API provenance, duplicate rejection, fail-closed structural resolution and API/internal separation.

No capability/business-type switch was introduced into the generic Projection Serviceability engine, Audience Observation Context, API Exposure result types or shared registries.

## 5. Semantic-gravity and change-amplification falsification

A synthetic third projection owner (`reviews`) is introduced only in test code through `mainstreet.governance.ProjectionOwnerChangeAmplificationTest`.

The test deliberately resides outside the `mainstreet.surface` package, so it cannot depend on package-private implementation seams.

The synthetic owner reaches a Projection Serviceability decision using:

- an owner-defined Projection Contract;
- owner-defined policy evaluators;
- exact-release generic registry snapshots; and
- the unchanged generic `ProjectionServiceabilityEvaluationEngine`.

The falsification required zero production-code changes for the synthetic owner and no changes to:

- `ProjectionServiceabilityEvaluationEngine`;
- `AudienceObservationContext` or its establisher;
- `ApiExposedElementSet`;
- `ApiExposureResolution`; or
- generic Projection/Exposure registry implementations.

This is executable evidence that an ordinary new Projection owner can add semantic rules without causing shared serviceability/context/result machinery to acquire that owner's semantics.

The future semantic-ownership/change-amplification watch remains separately retained in `designs/DEFERRED-DECISION-REGISTER.md`; it is inactive and is not an E3 blocker.

## 6. Branch-level falsification evidence

Before integration, the repaired code-bearing candidate `9b881171a2ccd2f80f0c83101259f5b24c184098` passed Maven Tests #1359 / run `33588503701` with:

- Temurin JDK 25;
- PostgreSQL 18.6;
- 777 unit/conformance tests passed;
- 298 PostgreSQL integration tests passed;
- 1,075 total tests passed;
- 51 Flyway migrations validated/applied;
- zero failures, errors or skipped tests; and
- `BUILD SUCCESS`.

Relevant GREEN tests included:

- `mainstreet.governance.ProjectionOwnerChangeAmplificationTest`;
- `mainstreet.governance.ProjectionPolicyOwnershipConformanceTest`;
- `mainstreet.surface.ExposureContractCandidateAffinityTest`;
- `mainstreet.surface.ApiExposureResolutionBoundaryTest`; and
- `mainstreet.governance.ImplementationProgrammeGateConformanceTest`.

The evidence-only branch commit subsequently passed Maven Tests #1360.

## 7. Integration and authoritative development verification

The E3 repair/evidence branch and the then-current `development` governance state were reconciled in the two-parent merge commit:

`98f5ce2cc009c7c22f627fb25d46675ea78bc8d4`

PR #42 was integrated and GitHub records that merge commit as the PR merge result.

The same commit was then verified as the actual `development` push frontier by:

```text
workflow:                       Maven Tests
run number:                     1364
run id:                         33590849681
branch:                         development
head:                           98f5ce2cc009c7c22f627fb25d46675ea78bc8d4
command:                        mvn --batch-mode clean verify -Ppostgres-it
unit / conformance tests:       777 PASS
PostgreSQL integration tests:   298 PASS
total tests:                    1,075 PASS
Flyway migrations:              51 validated/applied
PostgreSQL server:              18.6
failures / errors / skipped:    0 / 0 / 0
result:                         BUILD SUCCESS
```

This is the authoritative E3 implementation baseline.

## 8. Conformance conclusion

E3 is **CONFORMING_COMPLETE on `development`** because:

- the accepted v1.9 Audience Observation Context boundary is implemented;
- the accepted v1.10 Exposure result boundary is implemented;
- exact candidate/contract affinity is structural and fail-closed;
- API/internal result separation is preserved;
- Projection policy semantics remain owner-owned;
- release composition is mechanical rather than a semantic implementation home;
- the cross-package synthetic-owner change-amplification falsification passes; and
- the full unit + PostgreSQL verification gate is GREEN on the integrated `development` commit.

## 9. Explicit stop condition and graph refresh

This E3 closure does **not** declare E4 READY and does **not** select the next implementation target.

The required next governance action is:

1. refresh the IMP-06 fine-grained dependency graph against `development@98f5ce2cc009c7c22f627fb25d46675ea78bc8d4`;
2. re-evaluate every stale `READY`, `BLOCKED_DEPENDENCY` and `BLOCKED_DESIGN` classification against current accepted authority;
3. apply `designs/MS-IMP-001.md`; and
4. select only the smallest node that is actually READY.

If a genuinely material semantic or architectural decision remains unresolved, the affected node must stop under `designs/IMPLEMENTATION-RULES.md`. An implementation detail already delegated downstream by accepted authority must not be promoted into a fictitious design blocker.