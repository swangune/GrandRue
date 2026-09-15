# IMP-05 — Merchant Standard Business Hours Revision Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** B2a — Merchant-scope stable weekly-hours revision authority
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements the merchant-scope mutation and revision contract in the
accepted composite `MS-PROT-050` through v1.4. It preserves Business Hours as
an independently authoritative merchant-owned fact and does not make Merchant
Profile, onboarding evidence or Configuration lifecycle state its owner.

## 2. Implemented production boundary

The implementation adds:

- immutable merchant-scope stable weekly-hours revisions with exact scope,
  monotonic revision number, predecessor and current-pointer affinity;
- distinct `CONFIGURED` and `WITHDRAWN` revisions, retaining an explicitly
  configured empty week as a value different from withdrawal;
- normalized interval rows and the exact IANA time-zone identifier for every
  configured revision;
- exact expected-current-revision concurrency and single-successor behavior;
- logical-request replay with changed-intent rejection;
- authenticated-current-Controller and open, unsuspended Merchant Account
  admission, fenced with the Merchant lifecycle advisory lock; and
- immutable actor, Controller relationship, logical request and commit-time
  provenance.

Location-scoped mutation fails closed until the governed Merchant Location
authority exists; no placeholder Location ownership is fabricated.

## 3. RED → GREEN evidence

RED failed compilation because the standard Business Hours mutation commands,
revision result, failure algebra and authority contract did not exist.

GREEN passed five focused PostgreSQL contracts proving initial configuration
and current lookup, replacement/withdrawal/closed-week history, exact replay
and stale-write rejection, authenticated current-Controller/open-account
admission with location fail-closed behavior, and concurrent single-successor
commit.

## 4. Canonical PostgreSQL verification

The initially reported 259 integration-test errors were environmental. The
local container did not provide the dedicated database named by the test URL,
so PostgreSQL rejected every connection with SQL state `3D000`. Verification
was moved to the isolated local `mainstreet_test` database on the container's
published port; the shared `mainstreet` database was not used for fixture
truncation.

```text
MAINSTREET_TEST_POSTGRES_URL=jdbc:postgresql://localhost:32768/mainstreet_test
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           45
production Java sources compiled:      716
test Java sources compiled:            243
unit / conformance tests:              670 PASS
PostgreSQL integration tests:          264 PASS
total Maven tests:                     934 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

No competing MainStreet Maven process used the target directory or PostgreSQL
test schema. Editor Java autobuild was restored afterward.

## 5. Explicit non-claims and next gate

B2a does not implement location-scoped Business Hours, dated overrides,
computed operating status, resolution/projection, storefront presentation,
Scheduling or Merchant Configuration changes, or delegated-staff mutation.

B2b remains blocked on A4 Merchant Location authority. A4 cannot begin until
the active `MS-PROT-051-V11-DQ-002` governance decision selects the exact
structured international postal-address representation and evidence contract.
IMP-05 therefore remains **PARTIALLY_CONFORMING**.
