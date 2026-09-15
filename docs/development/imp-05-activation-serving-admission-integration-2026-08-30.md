# IMP-05 — Activation-Side Serving Admission Integration

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D5c4 — Activation-side serving admission integration
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements Sections 19 through 23 of accepted composite
`MS-PROT-040` through v1.5. It composes the D5c1 exact RCP-affined
requirement set, D5c2 immutable serving-generation materialisation/support
snapshot, D5c3 ordinary-cohort generation fence and the v1.5 release-purpose
admission authority with the existing atomic Configuration activation.

## 2. Implemented production boundary

The implementation adds:

- append-only exact Semantic Registry Release admission decisions for
  `NEW_CONFIGURATION_VALIDATION` and `NEW_BUSINESS_ACTIVITY`, with a separate
  locked current pointer for each release/purpose pair;
- immutable versioned ordinary new-Configuration release references and a
  singleton current pointer whose advance atomically proves that the selected
  release is currently admitted for both purposes;
- exact request-retry identity and changed-intent conflict for both decision
  recording and ordinary-reference advancement;
- activation admission against the Configuration Revision's pinned release,
  independent of the current ordinary release pointer;
- a shared PostgreSQL lock on the D5c3 ordinary-cohort control row, rejection
  unless it is `STABLE`, and stable-epoch affinity retained until activation
  commit;
- shared locks on both exact current release-purpose decision pointers, with
  fail-closed rejection of missing or withdrawn authority;
- exact package/requirement-set and serving generation/snapshot/materialised
  bundle-digest support proof inside the activation transaction; and
- immutable activation provenance retaining the exact two admission decision
  identities, approved package, requirement set, serving generation, stable
  epoch and materialised bundle digest consumed by the commit.

Committed activation retry reconstructs and returns the original exact
provenance without re-evaluating mutable current pointers. Historical
pre-v1.5 activation rows remain readable without fabricated provenance.

## 3. RED → GREEN evidence

RED failed compilation because release-purpose admission, ordinary release
reference and activation-serving admission contracts did not exist.

GREEN proved append-only decision history, current-pointer replacement,
dual-purpose ordinary-reference admission, exact retry behavior, complete
activation provenance retention, historical retry after withdrawal, distinct
missing-purpose rejection and fail-closed missing/PROMOTING deployment state.
Focused PostgreSQL verification passed all 16 contracts across
`JooqSemanticReleaseAdmissionAuthorityIT` and
`JooqConfigurationReleaseActivationIT`.

The first clean canonical run exposed nonessential cross-slice foreign keys
from activation provenance to D5c1/D5c2 evidence that prevented established
isolated fixture truncation. Those keys were removed; the same transaction
still proves both affinities while the activation row retains all-or-none
identities, and exact release/purpose admission-decision foreign keys remain.
The two affected PostgreSQL fixture classes then passed before the final clean
canonical run.

## 4. Canonical verification

```text
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           44
production Java sources compiled:      708
test Java sources compiled:            242
unit / conformance tests:              670 PASS
PostgreSQL integration tests:          259 PASS
total Maven tests:                     929 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

No competing MainStreet Maven process used the shared target directory or
PostgreSQL schema. Editor Java autobuild was restored afterward.

## 5. Explicit non-claims and next node

D5c4 does not create an operator workflow for release-purpose admission,
automate semantic-release deployment or promotion, switch external serving
state, or treat the ordinary release pointer as activation authority. The
operator workflow remains deferred under `MS-PROT-040-V12-DQ-006`.

D5c4 completes the serving-deployment branch of activation. The subsequent
full authority read for B2 exposed `IMP-05-B2-DG-001`, now resolved by accepted
MS-PROT-050 v1.4. B2a merchant-scope stable weekly-hours revision authority is
the smallest READY node; B2b remains dependent on A4 Merchant Location.
