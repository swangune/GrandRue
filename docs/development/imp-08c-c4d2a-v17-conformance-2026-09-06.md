# IMP-08C-C4D2A — v1.7 Committed-baseline Recovery Conformance
Date: 6 September 2026. Scope: exact owner recovery after a previously committed Standing Free consequence. Classification: CONFORMING_COMPLETE under v1.7 on cycle-closing synchronization CI. Parent C4D2 and C4D remain IN_PROGRESS.

## Baseline, verification and freshness
Selection baseline: `0270329fccdb680f5577ef471c86e44dc6aa8215`, run `34010162333` — SUCCESS — 1113 unit/governance + 387 PostgreSQL integration tests.
Implementation proof: `2284b47a32e71018f8d648eba154fd6471b651eb`, run `34010435261` — SUCCESS — 1116 unit/governance + 388 PostgreSQL integration tests, zero failures/errors/skips.
Branch: `development`; `master@b84fd1e8d7078fd9f98ceec9dc94822dca9d1871` unchanged. No migration.
Tests-first RED: `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 -Dtest=StandingFreeFromMerchantAccountEstablishedHandlerTest test` ran five tests and failed the three new adverse cases against the unchanged handler.
GREEN: local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test` passed 1116 tests.
Full proof: `.github/workflows/maven-tests.yml`, Java 25/PostgreSQL 18, `mvn --batch-mode clean verify -Ppostgres-it`, exact CI head/run above.
This is a live dependency review of the handler and its owner-store recovery behavior. It is not a re-audit or blanket recertification of historical Commercial nodes. C4C current evidence remains `docs/development/imp-08c-c4c-v17-impact-conformance-2026-09-06.md`; its old v1.6 evidence remains blob `2de4ac61d9753c18e4079be0453417c214fa9ed6`.

## Exact accepted authority
- MS-IMPLEMENTATION-RULES-001 v1.7, `designs/IMPLEMENTATION-RULES.md`: §30.1 — Exact Authority Pointer Contract; §30.3 — Clean-Code-First Rule; §47 — Implementation Evidence; §48 — Full Verification Gate; §52.14 — Mandatory implementation-status synchronisation; §52.16 — Observation, Deduction and Uncertainty Rule; §52.18 — Counterevidence Before Completion; §52.19 — Evidence Locality Rule.
- MS-PROT-056 v1.6, `designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md`: §3 — Standing Free baseline fact; §5 — Idempotency and duplicate delivery; §7 — Runtime behaviour during propagation gap.
- MS-PROT-026 v1.1, `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §24 — Downstream Idempotency Remains Separate.

## Exact implementation and test anchors at proof head
`src/main/java/mainstreet/application/StandingFreeFromMerchantAccountEstablishedHandler.java:40` — `handle` first resolves the already-committed Commercial baseline. If absent, the original historical FREE revision path remains required. Both recovered and newly established results pass `requireSameEstablishment` at :68, matching merchant, originating establishment and original time. Class-level WHY/pointer at :13 replaces the broader prose; names and control flow explain ordinary mechanics.
Unchanged owner concurrency boundary: `src/main/java/mainstreet/infrastructure/persistence/commercial/JooqStandingFreeBaselineStore.java:59` — `establishIfAbsent`; merchant lock :161, origin guard :168. Owner read boundary `baselineFor` :117. The handler does not query Merchant Account tables.

| Claim / falsification | Exact executable location |
| --- | --- |
| First materialisation uses original time and effective FREE revision | `src/test/java/mainstreet/application/StandingFreeFromMerchantAccountEstablishedHandlerTest.java:26` — binds_standing_free_to_event_time_and_free_revision_effective_at_that_time |
| Non-FREE revision still rejected | Same test file :52 — rejects_non_free_revision_from_catalogue_authority |
| Committed recovery needs neither catalogue nor candidate identity factory | Same test file :68 — committed_replay_does_not_require_catalogue_or_another_candidate_identity |
| Changed origin/time cannot pass the recovery shortcut | Same test file :79 — committed_recovery_rejects_changed_establishment_origin_or_time |
| Faulty owner port returning another merchant is rejected | Same test file :92 — committed_recovery_rejects_a_foreign_merchant_returned_by_the_owner_port |
| PostgreSQL duplicate retains exact identity and generates only one candidate | `src/test/java/mainstreet/infrastructure/persistence/commercial/JooqStandingFreeBaselineStoreIT.java:95` — duplicate_delivery_returns_original_baseline_without_rebinding_identity; revised candidate-count assertion :112 |
| Database-backed store recreation recovers while catalogue/factory are unavailable | Same IT :118 — committed_baseline_recovers_after_store_recreation_with_catalogue_unavailable |
| Two concurrent first deliveries still converge | Same IT :132 — concurrent_duplicate_delivery_converges_to_one_baseline |
| Cross-merchant origin cannot be persisted | Same IT :164 — cross_merchant_originating_establishment_is_rejected_by_postgresql |

## Counterevidence and completion falsification
OBSERVED gap before change: catalogue resolution preceded every duplicate lookup. Three tests failed, including committed replay with catalogue unavailable. Classification: IMPLEMENTATION_GAP in owner recovery availability and missing handler-level provenance validation on a returned baseline. This is not a failure of C4C's static registration scope or C4D1's technical persistence scope.

The shortcut was challenged with different origin, time and merchant; it rejected all three. It returns persisted plan revision and entitlement snapshot without rebinding history. First materialisation remains dependent on a valid historical FREE revision. Existing real-database concurrency and cross-merchant rejection tests continue to pass.

The revised candidate-count assertion is an intentional changed behavior: generating a fresh unused identity on committed replay was an implementation detail, not an accepted invariant. Exact baseline identity, original history and database row-count assertions remain.

Counterevidence against broader claims:
- The handler still accepts an owner fact value; it does not establish event-source authenticity or a scheduled principal. It is an internal owner-operation component, not the protected worker boundary.
- No receipt-to-handler-to-acknowledgement composition or process-kill experiment is exercised here. C4D2B must prove that boundary using C4D1's independent acknowledgement.
- The existing baseline's historical policy is trusted as committed owner evidence; this change does not validate every historical catalogue revision or repair privileged database tampering.
- Catalogue unavailability before first materialisation can still prevent progress. It must not cause fabricated history, a default revision or acknowledgement of an unperformed consequence.
- Existing store concurrency remains necessary when two callers both observe no baseline. The pre-read does not replace its lock or uniqueness/origin invariants.
- No production catalogue provider, worker scheduler, resource/performance or deployment readiness is established by these tests.

OBSERVED: the changed handler, five unit cases and database tests satisfy the narrow recovery contract at the exact proof head.
DEDUCED: C4D2A conforms within that scope; C4D2B can proceed after synchronization.
UNCERTAIN / outside evidence: trusted source/principal establishment, current execution eligibility, complete worker attempts/retry classification, acknowledgement composition and operational recovery liveness. C4D2/C4D/macro completion must not be inferred.
