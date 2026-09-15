# IMP-05 — Location-Scoped Standard Business Hours Integration

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** B2b — Merchant-location stable weekly-hours revision integration
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## Governing authority

The accepted composite `MS-PROT-050` through v1.4 governs stable Business
Hours scope, mutation and revision authority. The accepted composite
`MS-PROT-051` through v1.2 governs Merchant Location identity, immutable
revisions, currentness and terminal retirement. `MS-IMP-001` and
`IMPLEMENTATION-RULES.md` govern node selection, RED → GREEN execution and
evidence.

## Implemented production boundary

Location-scoped stable Business Hours mutation now:

- acquires the same merchant/location PostgreSQL advisory-lock namespace used
  by Merchant Location mutation;
- proves the referenced Location has a current same-merchant `ACTIVE` revision
  inside the Business Hours mutation transaction;
- distinguishes a missing Location from a retired Location and fails closed for
  either state;
- retains the exact consumed Merchant Location revision identity on every
  configured or withdrawn Business Hours revision;
- enforces same-merchant, same-location, exact-revision affinity with a
  composite database foreign key and scope-shape constraint; and
- preserves exact logical-request replay after later Location retirement by
  returning the originally committed Business Hours revision before evaluating
  current admission.

Merchant-scoped Business Hours retain no Location revision affinity. Business
Hours remain independently authoritative and do not mutate Merchant Location.

## RED → GREEN evidence

RED failed compilation because the Business Hours revision result exposed no
consumed Location revision and the failure algebra could not distinguish a
missing Location from a retired one.

GREEN passed eight focused PostgreSQL contracts: the five existing merchant-
scope contracts plus three new contracts proving exact active-Location
admission/provenance, distinguishable missing/retired failures, and exact replay
after Location retirement.

## Canonical PostgreSQL verification

The isolated canonical gate ran only after confirming no competing MainStreet
Maven/JVM owned the shared target directory or PostgreSQL test schema.

```text
MAINSTREET_TEST_POSTGRES_URL=jdbc:postgresql://localhost:32768/mainstreet_test
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           47
production Java sources compiled:      729
test Java sources compiled:            246
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          273 PASS
total Maven tests:                     947 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

Editor Java autobuild was restored afterward.

## Non-claims and next node

B2b does not implement dated overrides, computed operating status,
projection/Exposure, storefront presentation, Scheduling, Merchant
Configuration mutation, or delegated-staff authority.

B2 is now complete. Graph refresh refines the remaining A3 independent-fact
family into bounded children. A3a Merchant Contact Point authority is the
smallest critical READY node; its accepted authority already defines explicit
scope, stable identity, kind/value/exposure/provenance, immutable revisions,
currentness, concurrency, retry and current-Controller mutation. Exact table
layout remains the implementation detail catalogued by
`MS-PROT-051-V11-DQ-001`, not a new semantic authority.
