# IMP-05 — External Presence Link Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** A3c — External Presence Link authority
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## Governing authority

The accepted composite `MS-PROT-051` through v1.2 governs the independent
Merchant External Presence Link fact family, stable merchant-scoped identity,
platform kind, public URL, exposure, provenance, immutable revision history,
currentness, optimistic concurrency, logical retry and current-Controller
mutation authority. `MS-IMP-001` and `IMPLEMENTATION-RULES.md` govern node
selection, RED → GREEN execution and implementation evidence.

## Implemented production boundary

External Presence Link mutation now provides:

- one stable merchant-scoped presence identity, independent of its platform or
  URL value;
- exact non-empty platform-kind and public-URL values without inventing a
  standardized platform taxonomy;
- explicit `PRIVATE_INTERNAL` or `PUBLIC` merchant-owned exposure choice;
- immutable `CREATE`, `UPDATE` and `RETIRE` revisions with one exact current
  pointer and terminal retirement;
- expected-revision concurrency and logical-request identity whose exact replay
  returns the originally committed result while changed intent conflicts;
- authenticated current-Controller/open-account mutation authority under the
  existing merchant lifecycle fence; and
- database-enforced same-merchant/same-presence predecessor and current-pointer
  affinity, operation sequencing, provenance and revision integrity.

Changing platform kind or URL revises the same explicit presence identity only
when the caller intentionally addresses that identity. Retirement preserves the
last material values as immutable historical evidence.

## RED → GREEN evidence

RED failed compilation because no External Presence domain, mutation authority
or PostgreSQL persistence contract existed.

GREEN passed five focused PostgreSQL contracts proving exact create/currentness
and provenance, immutable update and terminal retirement, replay after later
mutation and changed-intent conflict, current-Controller/open-account authority,
and single-successor concurrent update.

## Canonical PostgreSQL verification

The isolated canonical gate ran only after confirming no competing MainStreet
Maven/JVM owned the shared target directory or PostgreSQL test schema.

```text
MAINSTREET_TEST_POSTGRES_URL=jdbc:postgresql://localhost:32768/mainstreet_test
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           49
production Java sources compiled:      748
test Java sources compiled:            248
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          284 PASS
total Maven tests:                     958 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

Editor Java autobuild was restored afterward.

## Non-claims and next gate

A3c does not validate ownership of an external profile, normalize or classify
URLs, establish OAuth/provider credentials, create a `ProviderConnection`,
define sync direction/conflict handling, publish a projection, or resolve
runtime Exposure. A stored public link remains merchant-profile information.

No further A3 child is currently implementation-READY. A3b is blocked because
the accepted authority requires `structuredOrBoundedGeography` but deliberately
does not select a concrete versioned representation. Implementing one would
create new semantic authority. The next manual gate is approval of a minimum
versioned `ServiceAreaGeographyV1` contract and its revision-affined persistence
evidence. A3d remains pending behind that graph decision; its exact
classification representation must also avoid silently standardizing the
taxonomy deferred by `MS-PROT-051-V11-DQ-012`.
