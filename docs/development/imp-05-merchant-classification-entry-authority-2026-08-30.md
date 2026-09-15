# IMP-05 A3d — Merchant Classification Entry Authority Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Child node:** A3d — Classification metadata authority
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## Governing authority

The node is governed by composite MS-PROT-051 through v1.4, with the mutation,
scope, trusted-actor, lifecycle, retry and historical-evidence dependencies
listed by that accepted amendment. MS-PROT-051 v1.4 was formalised after the
complete proposal received explicit manual approval and closes
`IMP-05-A3D-DG-001`.

The amendment deliberately does not resolve `MS-PROT-051-V11-DQ-012`.
Classification remains non-standardised, contextual Merchant Profile metadata
and cannot determine Configuration, compiler, capability, runtime, entitlement,
verification, provider or publication behaviour.

## Minimum implementation

The completed node adds:

- the versioned `MerchantClassificationEntryV1` value with the closed
  `CATEGORY`, `DISCOVERY_TAG` and `CONTEXTUAL_DESCRIPTOR` kind set;
- required merchant-approved Unicode NFC labels with surrounding whitespace
  stripped while preserving case and internal wording;
- explicit `PRIVATE_INTERNAL` or `PUBLIC` source exposure choice;
- stable independent merchant-scoped classification identities;
- explicit create, update and terminal-retire commands;
- immutable exact revisions and one durable current pointer per identity;
- an update contract that cannot accept a changed kind;
- a PostgreSQL predecessor foreign key that also enforces kind continuity;
- exact expected-current optimistic concurrency;
- logical request identity with exact replay before current admission and
  changed-intent conflict;
- authenticated current-Controller and open, unsuspended Merchant Account
  admission; and
- exact Controller relationship, actor, provenance and commit-time evidence.

V51 creates only the Classification Entry revision and current-pointer tables.
It adds no taxonomy, provider, projection, Configuration or runtime coupling.

## RED → GREEN evidence

RED introduced the PostgreSQL integration contract before production symbols
existed. Test compilation failed on the missing value, commands, lifecycle,
authority, revision and jOOQ adapter.

GREEN passed six focused PostgreSQL contracts proving:

1. exact schema identity, Unicode normalization, currentness and provenance;
2. all three kinds plus equal-label independent identities and explicit source
   exposure;
3. label/exposure update with immutable kind and history, followed by terminal
   retirement;
4. exact retry after later retirement and changed-intent conflict;
5. authenticated current-Controller and open, unsuspended-account admission;
   and
6. one successful successor under concurrent updates from the same expected
   revision.

## Canonical PostgreSQL verification

The canonical gate ran only after confirming no competing MainStreet Maven/JVM
owned the shared PostgreSQL test schema. It used an isolated committed-tree copy
plus only the scoped A3d files, so the user-owned lifecycle and storefront
working-tree changes were neither modified nor relied upon.

```text
MAINSTREET_TEST_POSTGRES_URL=jdbc:postgresql://localhost:32768/mainstreet_test
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           51
production Java sources compiled:      772
test Java sources compiled:            250
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          297 PASS
total Maven tests:                     971 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

The isolated verification copy was removed after success.

## Non-claims and graph consequence

A3d does not create a taxonomy, primary category, synonym, translation,
ranking, confidence, search, audience Exposure or executable business-type
decision. `PUBLIC` remains a source fact rather than publication authority.

A3 is now **CONFORMING_COMPLETE** across Contact Point, Service Area, External
Presence and Classification Entry authorities. The next graph refresh must
resolve the stale C1 classification: the current conformance map already calls
question/answer semantics a conforming foundation while the child graph still
labels C1 partial. That implementation-evidence audit precedes selecting any
new production node and does not itself create semantic authority.
