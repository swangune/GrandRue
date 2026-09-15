# IMP-05 — Durable Merchant Public Descriptor Revision Authority — Conformance Evidence

**Programme:** MS-IMP-001 — Main Street Production Implementation Programme  
**Macro target:** IMP-05 — Merchant Definition, Configuration & Activation  
**Node:** A2 — Durable Merchant Public Descriptor revision authority  
**Date:** 28 August 2026  
**Status:** **CONFORMING_COMPLETE**  

```text
Durable Merchant Public Descriptor revision authority = CONFORMING_COMPLETE
```

## 1. Governing authority

This node implements the Merchant Public Descriptor persistence subset of MS-PROT-051 and MS-PROT-051 v1.1 under `designs/MS-IMP-001.md` and `designs/IMPLEMENTATION-RULES.md`.

It preserves independently owned fact families and does not introduce a universal `BusinessProfile` aggregate or global profile version.

## 2. Implemented boundary

The production slice adds:

- `MerchantPublicDescriptorMutationCommand` with exact expected revision and mutation evidence;
- immutable `MerchantPublicDescriptorRevision` history;
- stable failure categories for missing fact, stale revision and request-identity conflict;
- `MerchantPublicDescriptorAuthority` owner contract;
- `JooqMerchantPublicDescriptorAuthority`;
- Flyway V33 `merchant_public_descriptor_revision`; and
- merchant- and request-scoped PostgreSQL advisory-lock serialization.

Current state is the highest immutable revision for one Merchant Scope. Establishment does not create an empty Profile aggregate. Revision does not mutate Merchant Configuration, Exposure, legal identity, trust, Entitlement or capability state.

## 3. Executable conformance

| Requirement | Evidence |
|---|---|
| Zero or one current descriptor per Merchant Scope | Establishment and merchant-scoped immutable revision key |
| Exact immutable history | Historical revision remains readable after current state changes |
| Optimistic concurrency | Stale expected revision conflicts without a new row |
| Retry after lost acknowledgement | Same request returns its exact committed revision even after later revisions |
| Request identity cannot change intent | Reuse with a different merchant/value is rejected |
| Concurrent same-fact edits | Two PostgreSQL threads from one expected revision produce one success and one conflict |
| Unrelated merchants do not globally conflict | Independent merchants each establish revision 1 |
| Durable process-independent state | Fresh jOOQ authority instance reads the same PostgreSQL revision |
| Provenance and attribution | Request identity, provenance, principal, origin and commit time persist |
| No authority collapse | Descriptor schema/model contains no Configuration, Exposure, trust, legal or commercial fields |

## 4. Test-first trace

```text
67b9fd680a5a0e261b41553ca325cff9ec99330e
docs(imp-05): establish conformance graph
Maven Tests #1282 — SUCCESS

b4fec7374249156662a74fc28b068ec7f9bee9f0
test(imp-05): require durable descriptor revisions
Maven Tests #1283 — FAILURE as intended
production compiled; test compilation failed only for missing A2 types

6328897d5a2257a60c3ea58da5b9a99782f64221
feat(imp-05): persist descriptor revisions
Maven Tests #1284 — FAILURE at production compilation
one jOOQ generic row-type mismatch; no tests ran

aaa771807fd29364be7dea8b7ae18034dc60e143
fix(imp-05): generalize descriptor select row
Maven Tests #1285 — SUCCESS
```

## 5. Verified baseline

```text
workflow                                  Maven Tests #1285
run id                                    33211285745
head                                      aaa771807fd29364be7dea8b7ae18034dc60e143
Java                                      Temurin 25 / release 25
PostgreSQL                                18.6
Flyway migrations                        33
production Java sources                  592
test Java sources                        224
unit tests                               640 PASS
PostgreSQL integration tests             197 PASS
total tests                               837 PASS
failures / errors / skipped              0 / 0 / 0
result                                    BUILD SUCCESS
```

## 6. Explicit non-claims and graph refresh

This node does not complete contact points, Location, service areas, external presence, classification, Business Hours, public projection, application authorization, onboarding adoption or any Configuration lifecycle operation.

A2 is complete. B2 (durable Business Hours revisions) remains independently READY. C2 (durable Onboarding Case/evidence store) is selected next because it unlocks the critical C3 → C4 → C5 onboarding path. IMP-05 remains **PARTIALLY_CONFORMING**.
