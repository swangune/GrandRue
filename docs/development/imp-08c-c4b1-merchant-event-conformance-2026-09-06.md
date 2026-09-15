# IMP-08C C4B1 Conformance — Merchant Account Event Mapping
**Date:** 6 September 2026
**Node:** IMP-08C-C4B1
**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI
Implementation evidence only.

## Authority and selection
MS-IMP-001 v1.0 + v1.1; IMPLEMENTATION-RULES v1.6; MS-PROT-071 §21 and MS-PROT-071 v1.2 §§3–6; MS-PROT-026 v1.1 §§4–10.
Selection before production changes: `imp-08c-c4b-selection-2026-09-06.md`.
C4A final head `b2ffc0b3ee1d7ef4ec84722a4befdbde23548f2d`, run `33998151474`, passed 1095 unit/governance + 377 PostgreSQL integration tests.

## Implementation
MerchantAccountEstablishedEventContract maps existing accepted fact meaning and payload to explicit owner/scope/subject/provenance/evolution references. Initial immutable registry release `merchant-account-event-contracts@1` names this implementation mapping; it is separate from Merchant Configuration release identity.
MerchantAccountEstablishedOccurrence retains exact affinity and the unchanged owner fact. Its event identity uses the injective namespace `merchant-account-established-event/` plus the already durable unique publication identity. This distinguishes event occurrence, publication, establishment fact and logical request identities without extra mutable identity generation on delivery.
V59 adds all-or-none nonblank affinity columns. New bootstrap writes those fields within the existing local transaction. Existing required publication-row failure still rolls back account/controller/request/establishment together.
Outbox reconstruction retains exact affinity. registeredOccurrence requires the supported affinity and exact registered definition; it refuses absent/unknown/reinterpreted meaning. Configuration/provenance or current code is not a fallback for old registration.
Legacy rows remain readable with absent affinity. No historical backfill is performed. event() and markPublished retain their existing technical behavior; no registered consumer or publisher is silently enabled.
Mapping is not reaction execution or acknowledgement. Technical publication completion does not complete any consumer.

## Verification
Tests-first RED was missing mapping APIs/types.
Four unit tests cover accepted mapping, identity distinction, exact contract definition, unknown affinity and invalid occurrence construction.
Three PostgreSQL scenarios cover replay/reconstruction across store recreation and technical publication completion; unknown/legacy affinity preservation; partial/blank SQL rejection.
Existing atomic rollback and occurrence-time tests remain unchanged.
Local full suite: **1099 tests, zero failures/errors/skips** on Java 26 targeting Java 25.
Implementation head `cf6fd38f00690d7fc13019af2b7cbcf03be817fa`, CI run `33998884504`: SUCCESS — 1099 unit/governance + 380 PostgreSQL integration tests, zero failures/errors/skips.
Required gate: `mvn --batch-mode clean verify -Ppostgres-it`, Java 25/PostgreSQL 18.

## Scope and next
C4B1 completes this concrete occurrence/publication mapping; other owner outboxes are not declared conforming by analogy. C4C registered reaction identity and later execution/acknowledgement remain required.
MS-PROT-056 v1.6 explicitly permits a durable Standing Free reaction; this is a concrete owner candidate to inspect after registration.
C2/C4 and IMP-08C remain IN_PROGRESS; C3/C5 retain dependencies, IMP-08A/B remain eligible and provider/financial/P6 gates remain.
Evidence/status/graph/programme gate close only on successful synchronized full CI.
