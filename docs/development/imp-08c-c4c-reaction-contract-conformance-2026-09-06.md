# IMP-08C C4C Conformance — Reaction Registration and Identity
**Date:** 6 September 2026
**Node:** IMP-08C-C4C
**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI
Implementation evidence only.
## Authority and selection
MS-IMP-001 v1.0 + v1.1; IMPLEMENTATION-RULES v1.6; MS-PROT-026 v1.1 §§13–18, 22–24, 26, 29–32, 36; MS-PROT-056 v1.6.
Selection before production changes: `imp-08c-c4c-selection-2026-09-06.md`.
C4B1 closing head `48c991424a48f6793bede6a5336a882d250503d9`, run `33999150773`, passed 1099 unit/governance + 380 PostgreSQL integration tests.
## Implementation
Owner-qualified reaction identity/target and exact reaction affinity are distinct values. EventReactionContractDefinition requires purpose, scope, execution mode, principal, duplicate, ordering, supersession/coalescing, required event data, failure/retry and downstream intent references plus the exact source Event Contract affinity.
The immutable registry rejects duplicate contract identities, preserves same-named different owners and resolves only exact reaction release and source affinity. There is no latest/source-version fallback or listener discovery.
EventReactionIdentity combines source occurrence identity and stable owner-qualified reaction contract identity. Registry release, physical delivery and work attempt are deliberately absent from the logical key; a release update alone cannot manufacture another consequence. Release provenance remains explicit in EventReactionContractAffinity and must be preserved by subsequent durable progression.
StandingFreeEventReactionContract lives beside the existing application handler. It declares the accepted Commercial-owned consequence, original establishment-time requirement, one-baseline-per-merchant origin affinity and lost-acknowledgement recovery reference. It does not choose a new FREE plan policy.
Registration does not execute or acknowledge a reaction. No original actor authority is inherited; trusted scheduled composition remains C4D work. No persistence, service profile, scheduler or provider behavior changes.
## Verification
Tests-first RED: reaction registration/identity types absent.
Seven unit tests cover stable duplicate/independent keys, release distinction, malformed semantics, exact source/release matching, immutable collections/duplicates, empty registries and concrete Standing Free declaration.
Local full suite: **1106 tests, zero failures/errors/skips**, Java 26 targeting Java 25.
Implementation head `cfa5697112847f03c67e3b8b49c36aca03b5f9c6`, CI run `33999465139`: SUCCESS — 1106 unit/governance + 380 PostgreSQL integration tests, zero failures/errors/skips.
Required full gate: `mvn --batch-mode clean verify -Ppostgres-it`, Java 25/PostgreSQL 18.
No new PostgreSQL scenario is required for immutable declarations; all existing database regression scenarios remain mandatory.
## Remaining work
C4D is next for trusted source/worker composition, durable independent acknowledgement and lost-acknowledgement recovery around StandingFreeFromMerchantAccountEstablishedHandler. Catalogue/owner semantics remain with Commercial.
C2/C4 and IMP-08C remain IN_PROGRESS; C3/C5 retain required dependencies. IMP-08A/B eligibility and provider/financial/P6 prerequisites remain.
Conformance evidence, graph, status and executable programme gate close only on successful full synchronization CI.
