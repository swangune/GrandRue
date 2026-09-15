# IMP-05 — Merchant Contact Point Authority

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation
**Fine-grained node:** A3a — Merchant Contact Point authority
**Date:** 30 August 2026
**Status:** **CONFORMING_COMPLETE**

## Governing authority

The accepted composite `MS-PROT-051` through v1.2 governs the independent
Merchant Contact Point fact family, explicit merchant/Location scope, exposure,
provenance, immutable revision history, currentness, optimistic concurrency,
logical retry and current-Controller mutation authority. `MS-IMP-001` and
`IMPLEMENTATION-RULES.md` govern node selection, RED → GREEN execution and
implementation evidence.

## Implemented production boundary

Merchant Contact Point mutation now provides:

- one stable merchant-scoped Contact Point identity with explicit `MERCHANT` or
  `MERCHANT_LOCATION` scope;
- the initially accepted `TELEPHONE`, `EMAIL`, `MOBILE` and `WEB_LINK` kinds,
  exact value, explicit `PRIVATE_INTERNAL` or `PUBLIC` exposure and optional
  label;
- immutable `CREATE`, `UPDATE` and `RETIRE` revisions with one exact current
  pointer and terminal retirement;
- expected-revision concurrency and logical-request identity whose exact replay
  returns the originally committed result while changed intent conflicts;
- authenticated current-Controller/open-account mutation authority under the
  existing merchant lifecycle fence;
- active same-merchant Location admission for Location-scoped create/update,
  retaining the exact consumed Location revision; and
- database-enforced same-merchant, same-Location, exact-revision affinity and
  revision/current-pointer integrity.

Retirement preserves the prior material and Location provenance and remains
possible after the referenced Location retires. Contact Point identity remains
merchant-scoped; a revision cannot silently move it to a different scope.

## RED → GREEN evidence

RED failed compilation because no Contact Point domain, mutation authority or
PostgreSQL persistence contract existed.

GREEN passed six focused PostgreSQL contracts proving exact merchant-scope
create/currentness/provenance, immutable update and terminal retirement,
Location admission and exact revision affinity, replay after later mutation,
changed-intent conflict, current-Controller/open-account authorization, and
single-successor concurrent update.

## Canonical PostgreSQL verification

The isolated canonical gate ran only after confirming no competing MainStreet
Maven/JVM owned the shared target directory or PostgreSQL test schema.

```text
MAINSTREET_TEST_POSTGRES_URL=jdbc:postgresql://localhost:32768/mainstreet_test
mvn --batch-mode -Ppostgres-it clean verify

Flyway migrations validated:           48
production Java sources compiled:      740
test Java sources compiled:            247
unit / conformance tests:              674 PASS
PostgreSQL integration tests:          279 PASS
total Maven tests:                     953 PASS
failures / errors / skipped:           0 / 0 / 0
result:                                BUILD SUCCESS
```

Editor Java autobuild was restored afterward.

## Non-claims and next node

A3a does not verify possession of a telephone number or mailbox, normalize
kind-specific values, create Enquiry capability, publish a profile projection,
resolve runtime Exposure, mutate Merchant Configuration, or create an
authenticated provider connection. Stored exposure is the merchant-owned
profile fact, not proof that a runtime surface has exposed it.

A3c External Presence Link authority is the smallest independent READY node.
The accepted composite already defines its stable merchant-scoped identity,
platform kind, public URL, explicit exposure, provenance and the same immutable
create/update/retire mutation semantics. Platform kind can remain an exact
non-empty contextual value; this slice need not invent a standardized taxonomy
or an external-provider connection. A3b remains design-blocked until a concrete
`structuredOrBoundedGeography` representation is accepted.
