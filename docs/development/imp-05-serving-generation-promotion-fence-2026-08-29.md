# IMP-05 — Serving-Generation Promotion Fence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** D5c3 — Bidirectional promotion state machine / generation fence
**Date:** 29 August 2026
**Status:** **CONFORMING_COMPLETE**

## 1. Governing authority

This slice implements Sections 12 through 18 of accepted `MS-PROT-040 v1.4`.
It retains one durable ordinary-cohort `STABLE`/`PROMOTING` control row and
append-only promotion-transition identities. Promotion prepare and
reconciliation each use the exclusive PostgreSQL control-row lock; no
transaction spans an external serving switch.

## 2. Implemented production boundary

The implementation adds:

- normalized PostgreSQL control and transition relations with one-open-
  promotion enforcement, generation foreign keys and lifecycle shape checks;
- exact retry identity and changed-intent conflict for initialize, prepare and
  completed reconciliation;
- prepare-time proof that the target materializes the prior generation's exact
  release/bundle digest and supports the complete D5c1 requirement set for
  every currently active Configuration;
- fail-closed missing or ambiguous active requirement evidence;
- atomic `STABLE` to `PROMOTING` prepare with a monotonic epoch;
- target-homogeneous finalize and prior-homogeneous safe abort; and
- continued `PROMOTING` state for mixed, unknown or unexpected observations.

## 3. RED → GREEN evidence

RED failed compilation because exact promotion coverage and durable
coordination types did not exist. GREEN proved exact materialisation and
support coverage, durable prepare state, target finalize, prior abort,
uncertain-state fail-closed behavior, exact retries, changed retry conflict,
and missing current-Configuration requirement evidence rejection.

Focused verification passed two pure admission contracts and four PostgreSQL
state-machine contracts with Flyway at 43 migrations.

## 4. Canonical verification

```text
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           43
production Java sources compiled:      694
test Java sources compiled:            241
unit / conformance tests:              670 PASS
PostgreSQL integration tests:          253 PASS
total Maven tests:                     923 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

No competing MainStreet Maven process used the shared target directory or
PostgreSQL schema. Editor Java autobuild was restored afterward.

## 5. Explicit non-claims and governance gate

D5c3 does not integrate the activation transaction with serving admission and
does not invent current release-purpose authority. D5c4 remains **BLOCKED** by
active decision `MS-PROT-040-V12-DQ-001`.
