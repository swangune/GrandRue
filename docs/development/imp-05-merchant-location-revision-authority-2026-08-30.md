# IMP-05 — Merchant Location Revision and Lifecycle Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** A4b — Durable Merchant Location revision/lifecycle authority
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## Governing authority

Composite MS-PROT-051 through v1.2 governs this node. A4a supplies the
version-affined `PostalAddressV1` value. MS-PROT-031, MS-PROT-059 and accepted
Merchant Account/Controller/session authority preserve merchant scope, exact
retry and runtime actor admission.

## Implemented production boundary

The implementation adds independently authoritative merchant-scoped Location
identity, immutable exact revisions, one current pointer per identity and the
terminal `ACTIVE → RETIRED` lifecycle. Owner operations create, correct the
same place and retire; a material relocation must use a new identity rather
than the correction operation.

Each revision retains exact original and normalized address evidence,
schema/normalization/country-registry affinity, optional accepted coordinates,
public label, predecessor, actor, current Controller relationship, provenance,
logical request and committed time. Current-pointer advancement and revision
append are atomic. Exact expected-revision concurrency, request replay and
changed-intent rejection fail closed.

Ordinary mutation requires a matching authenticated trusted principal, the
then-current Merchant Controller and an open, unsuspended Merchant Account.
The transaction shares the Merchant lifecycle/Controller-transfer advisory
fence. No delegated-staff authority is fabricated.

## RED → GREEN evidence

RED failed compilation because Location commands, revision/lifecycle/authority
contracts, persistence and migration did not exist.

GREEN passed six focused PostgreSQL contracts proving exact create/current
provenance, immutable correction history, terminal retirement, exact replay
after later change with changed-intent conflict, authenticated current-
Controller/open-account admission and concurrent single-successor correction.

## Canonical PostgreSQL verification

```text
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           46
production Java sources compiled:      729
test Java sources compiled:            245
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          270 PASS
total Maven tests:                     944 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

The run used the isolated `mainstreet_test` PostgreSQL database with no
competing MainStreet Maven process. Editor Java autobuild was restored.

## Non-claims and next node

A4b does not implement Exposure, projections, geocoding/provider selection,
transport/UI workflows, onboarding adoption, delegated staff, service areas,
contact points or executable configuration bindings. It supplies the real
same-merchant active Location authority required by B2b, which is now READY.
