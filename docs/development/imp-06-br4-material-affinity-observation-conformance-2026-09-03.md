# IMP-06 BR4 Material Affinity Observation Contribution Conformance Evidence

**Date:** 3 September 2026  
**Programme:** IMP-06 — Read, Exposure & Transport Spine  
**Node:** BR4 — Material Affinity Observation Contribution registration/runtime binding  
**Authority:** MS-PROT-027 v1.13 §§25–27 and MS-PROT-027 v1.14, especially §§3–18  
**Record type:** implementation evidence only  
**Status:** **CONFORMING_COMPLETE**  
**Verified code-bearing baseline:** `development@e84918d24849f53fe2edf09bc7293d3f5058a364`  
**Full verification:** GitHub Actions run `33710572713`, job `100508951453` — SUCCESS (`mvn --batch-mode clean verify -Ppostgres-it`; 894 unit tests + 307 PostgreSQL integration tests = 1,201 tests; 0 failures, 0 errors)

## 1. Implemented BR4 boundary

BR4 implements the accepted narrow E3 request-binding construction seam plus the first Profile-owned Material Affinity Observation Contribution.

The concrete Profile boundary includes:

- `ProfileMaterialAffinityObservationContribution`
- `ProfileMaterialAffinityObservationContributionRegistration`
- Profile-owned immutable material-affinity entries derived from already acquired bounded Profile material/source affinity
- exact-release `ObservationContributionDefinitionRegistrySnapshot` registration for `profile / material-affinity`
- exact `ObservationContributionRuntimeBindingSnapshot` binding to the concrete Profile runtime type
- the generic trusted E3 Observation Contribution Construction Boundary authorised by MS-PROT-027 v1.14

The Profile contribution is `PUBLIC`, `SINGLE`, owner-qualified by `profile`, and remains capability-owned. Generic Surface infrastructure supplies only the exact E3-created `ObservationRequestBinding` and trusted `MerchantScope`; it does not manufacture Profile contribution contents.

## 2. Accepted invariants established

The implementation and conformance suite establish the BR4 obligations already accepted by MS-PROT-027 v1.13/v1.14:

1. **Exact request affinity is retained.** A Q1 contribution is bound to the exact E3-created Q1 `ObservationRequestBinding`; cross-request reuse does not become valid merely because merchant, release, candidate identity, values or progress identifiers look equal.
2. **Merchant affinity remains capability-proven.** Profile rejects bounded material belonging to a different Merchant Scope rather than allowing generic E3 to inspect or reinterpret Profile material.
3. **Contribution construction remains narrow.** E3 supplies only exact request binding plus trusted Merchant Scope and invokes the capability constructor once; construction failure/null/malformed output fails closed.
4. **Contribution ownership remains in Profile.** The concrete Profile implementation is final and package-private; the public extension contract remains generic.
5. **Exact semantic/runtime registration remains separated.** `profile / material-affinity` is registered through the existing exact-release definition snapshot and separately through the exact runtime implementation binding snapshot.
6. **PUBLIC/SINGLE semantics are exact.** PUBLIC is the initially permitted audience and duplicate SINGLE contributions fail closed; BR4 does not imply CUSTOMER registration.
7. **Expected progress is bounded-material affinity, not a later owner read.** Profile derives expected material progress from the already acquired bounded owner material/source-affinity evidence and does not re-query the owner during contribution construction.
8. **Material-affinity contents are integrity metadata only.** They identify the candidate/member and expected bounded-material source progress needed by the owner evaluator; they contain no rendered Profile business value, generic business payload, credential, provider secret or precomputed Exposure verdict.
9. **Generic E3 remains value-blind and owner-blind.** It validates only generic request/release/kind/runtime/audience/merchant/cardinality integrity and does not interpret Profile values or owner progress ordering.
10. **No BR5/BR6/S2/T1b4 behaviour was introduced.** BR4 does not perform current owner-progress evaluation, live Contact Point/Location choice comparison, same-read E4-positive fragment selection, final Surface assembly, post-E4 material reacquisition or transport/query delivery.

## 3. Required v1.14 falsification coverage

MS-PROT-027 v1.14 §18 requires the BR4 conformance suite to falsify the principal request, ownership, registration and payload-laundering counterexamples. The executable evidence covers:

- Q1 contribution accepted only for Q1;
- Q1 contribution rejected for Q2;
- Merchant A material cannot bind to Merchant B request;
- unregistered contribution definition rejected;
- wrong exact runtime class rejected;
- wrong audience rejected;
- duplicate SINGLE contribution rejected;
- constructor invoked no more than once;
- constructor failure/null or malformed output fails closed;
- material-affinity contents contain no rendered business value;
- generic `surface` does not depend on Profile; and
- the Profile contribution implementation remains final and package-private.

Owner-specific tests additionally establish that expected material progress comes from the bounded Profile material/acquisition evidence rather than an independent later owner read.

Two explicit post-GREEN conformance tests close gaps that would otherwise have remained dependent on structural inspection alone:

- `ProfileMaterialAffinityPayloadConformanceTest` makes the no-rendered-business-value rule executable; and
- `ProfileMaterialAffinityE3RegistrationConformanceTest` exercises the actual Profile definition, runtime binding and contribution through the real public E3 admission path rather than generic test doubles.

An initial version of the latter test attempted to import Surface's package-private context-details implementation. That test failed at compilation and was corrected rather than weakening encapsulation. The corrected proof asserts admission only through the public E3 result boundary, preserving the intended Surface/Profile architecture boundary.

## 4. Architecture and corpus review

The post-GREEN review found no material semantic, ownership, consistency, API or architectural decision beyond accepted MS-PROT-027 v1.13/v1.14.

The implementation preserves the accepted distinctions:

```text
ObservationRequestBinding affinity
    ≠ authority

material-progress affinity evidence
    ≠ business value
    ≠ Exposure verdict

successful contribution construction
    ≠ accepted E3 contribution
    ≠ Exposure authority

bounded expected progress
    ≠ live current-owner progress comparison
```

Generic Surface has not acquired a dependency on `merchantprofile`, and Profile has not obtained a general request-introspection API. The E3 construction seam is limited to the trusted information explicitly authorised by v1.14.

## 5. Verification evidence

The exact final code-bearing BR4 baseline is:

```text
development@e84918d24849f53fe2edf09bc7293d3f5058a364
```

The exact full verification is:

```text
GitHub Actions run: 33710572713
Job:                100508951453
Command:            mvn --batch-mode clean verify -Ppostgres-it
Unit tests:         894
PostgreSQL ITs:     307
Total:              1,201
Failures:           0
Errors:             0
Result:              SUCCESS
```

Expected PostgreSQL negative-constraint messages remain test evidence and were not globally suppressed. No Mockito self-attach warning was reintroduced.

## 6. Dependency consequence

BR4 is **CONFORMING_COMPLETE**.

The direct successor may therefore be promoted:

```text
BR4 Material Affinity Observation Contribution registration/runtime binding
    CONFORMING_COMPLETE
    ↓
BR5 owner evaluator current-progress comparison
    READY
```

BR6 remains blocked on BR5. S2 remains blocked on BR6, and T1b4 remains blocked on BR6 + S2.
