# IMP-05 — Merchant Service Area Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** A3b — Merchant Service Area Descriptor authority
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## Governing authority

The accepted composite `MS-PROT-051` through v1.3 governs the independent
Merchant Service Area Descriptor fact family and the closed
`ServiceAreaGeographyV1` representation. `MS-IMP-001` and
`IMPLEMENTATION-RULES.md` govern node selection, RED → GREEN execution and
implementation evidence.

## Implemented production boundary

Service Area mutation now provides:

- one stable merchant-scoped Service Area identity independent of its current
  geography value;
- a closed versioned geography union containing normalized `NAMED_AREA`,
  `MERCHANT_LOCATION_RADIUS`, `COUNTRY_WIDE` and `REMOTE_COUNTRIES` variants;
- uppercase two-letter country codes, a positive whole-metre radius and
  distinct lexicographically ordered remote-country evidence;
- required merchant-approved public description and explicit
  `PRIVATE_INTERNAL` or `PUBLIC` exposure choice on every revision;
- exact current same-merchant active Location-revision admission for a new or
  updated Location-radius value, while historical replay remains valid after
  later Location correction or retirement;
- immutable `CREATE`, `UPDATE` and `RETIRE` revisions with one exact current
  pointer and terminal retirement;
- expected-revision concurrency and logical-request identity whose exact replay
  returns the original committed result while changed intent conflicts;
- authenticated current-Controller/open-account mutation authority under the
  existing merchant lifecycle fence; and
- database-enforced predecessor, Location-revision, Controller and current
  pointer affinity plus geography-shape, operation and provenance integrity.

Remote-country values are retained as normalized child facts rather than an
opaque array or JSON document. The exact Location revision foreign key prevents
cross-merchant or invented Location anchors.

## RED → GREEN evidence

RED failed compilation because no Service Area geography model, mutation
authority or PostgreSQL persistence contract existed.

GREEN passed seven focused PostgreSQL contracts proving normalized named,
country-wide and remote-country variants; exact current active Location-radius
admission; immutable update and terminal retirement; replay after later
Location retirement and changed-intent conflict; current-Controller/open-account
authority; and single-successor concurrent update.

## Canonical PostgreSQL verification

The canonical gate ran only after confirming no competing MainStreet Maven/JVM
owned the shared target directory or PostgreSQL test schema. The user-owned
working-tree deletion of `lifecyle.md` causes an existing repository-navigation
test to fail before integration tests, so canonical verification used an
isolated temporary copy containing the committed navigation file and the exact
current A3b implementation. The original deletion was not changed, and the
temporary copy was removed afterward.

```text
MAINSTREET_TEST_POSTGRES_URL=jdbc:postgresql://localhost:32768/mainstreet_test
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           50
production Java sources compiled:      762
test Java sources compiled:            249
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          291 PASS
total Maven tests:                     965 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

Editor Java autobuild was restored afterward.

## Non-claims and next gate

A3b does not determine runtime serviceability, route or delivery eligibility,
supported postcodes, GIS polygons, geocoding/provider coverage, Projection,
Exposure resolution, transport contracts or Merchant Configuration. Its
geography is descriptive evidence only.

The next smallest critical A3 child is A3d. Accepted authority establishes
classification as independently owned, revisioned, contextual and strictly
non-executable, but does not yet select its initial value shape, fact identity
or mutation contract. `MS-PROT-051-V11-DQ-012` remains deliberately inactive
until a cross-product standardized taxonomy is required. A3d therefore stops at
a manual design gate for a minimum non-standardizing representation rather than
silently creating taxonomy authority in persistence code.
