# IMP-08C-C4D1 — v1.7 Receipt/acknowledgement Conformance Evidence
Date: 6 September 2026. Classification: CONFORMING_COMPLETE for the bounded C4D1 persistence responsibility, subject to cycle-closing synchronization CI. C4D remains IN_PROGRESS.

## Repository baseline and evidence scope
Selection baseline: `951cfe9e1ec908fab5e26f8af54679bd5fa652a5`, run `34009153710` — 1110 unit/governance + 380 PostgreSQL integration tests.
Implementation proof: `b3dbf2ce2d4ba0c93055dd5658796cc93bbc6d54`, run `34009941352` — SUCCESS — 1113 unit/governance + 387 PostgreSQL integration tests, zero failures/errors/skips.
Branch: `development`; `master@b84fd1e8d7078fd9f98ceec9dc94822dca9d1871` unchanged. Selection: `docs/development/imp-08c-c4d1-selection-2026-09-06.md`.
Immediate prerequisite: `docs/development/imp-08c-c4c-v17-impact-conformance-2026-09-06.md`. No historical-node re-audit or new historical completion claim. Old v1.6 C4C evidence remains Git blob `2de4ac61d9753c18e4079be0453417c214fa9ed6`.

This evidence covers immutable supplied receipt provenance, durable duplicate convergence, exact-affinity pending discovery and independent acknowledgement through the new store. It does not establish trusted event origin, execution principal, permission, downstream business outcome, dispatch liveness, WorkAttempt conformance, all callers or the enclosing C4D/macro target.

## Exact governing authority
- MS-IMPLEMENTATION-RULES-001 v1.7, `designs/IMPLEMENTATION-RULES.md`: §30.1 — Exact Authority Pointer Contract; §30.3 — Clean-Code-First Rule; §47 — Implementation Evidence; §48 — Full Verification Gate; §52.14 — Mandatory implementation-status synchronisation; §52.16 — Observation, Deduction and Uncertainty Rule; §52.18 — Counterevidence Before Completion; §52.19 — Evidence Locality Rule.
- MS-PROT-026 v1.1, `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §2 — Canonical Separation; §16 — Event Reaction Identity; §17 — Per-Reaction Acknowledgement; §23 — Reaction Deduplication; §24 — Downstream Idempotency Remains Separate; §32 — Historical Affinity; §45 — Consumer Failure.
- Boundary only: MS-PROT-065 v1.1, `designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment.md`: §11 — Attempt Evidence Before Consequential Execution; §59 — Restart Recovery. Receipt persistence supplies a prerequisite and is not promoted into proof of these whole execution obligations.

## Exact code and test locations at the proof commit
| Responsibility | Code anchor | Executable evidence |
| --- | --- | --- |
| Logical responsibility ignores physical acceptance time but pins provenance | `src/main/java/mainstreet/semantic/event/MerchantEventReactionReceipt.java:13`, constructor :20, `sameResponsibility` :32 | `src/test/java/mainstreet/semantic/event/MerchantEventReactionReceiptTest.java:18` — physical_acceptance_time_does_not_create_a_new_responsibility; :23 — receipt_rejects_mismatched_contract_key_and_missing_intent |
| Required acknowledgement key/outcome/time | `src/main/java/mainstreet/semantic/event/EventReactionAcknowledgement.java:6` | `src/test/java/mainstreet/semantic/event/MerchantEventReactionReceiptTest.java:30` — acknowledgement_requires_an_exact_reaction_and_outcome_evidence |
| Narrow persistence port and authority boundary | `src/main/java/mainstreet/semantic/event/MerchantEventReactionStore.java:13` | Concrete adapter evidence below; this interface is not a trust provider |
| Durable acceptance, first receipt retained, conflict rejection | `src/main/java/mainstreet/infrastructure/persistence/event/JooqMerchantEventReactionStore.java:25`, :57, lock :112, length-delimited key :123 | `src/test/java/mainstreet/infrastructure/persistence/event/JooqMerchantEventReactionStoreIT.java:40` — receipt_survives_recreation_and_redelivery_preserves_first_acceptance; :48 — conflicting_source_release_scope_or_downstream_intent_cannot_replace_a_receipt; :71 — concurrent_acceptance_converges_on_one_durable_logical_reaction |
| Exact-affinity bounded pending discovery and consumer-local completion | Same adapter :62, :75, :99 | Same IT :62 — acknowledging_one_consumer_neither_completes_another_consumer_nor_another_event; :81 — lost_acknowledgement_replay_returns_original_evidence_and_conflicting_outcomes_fail |
| Composite key, nonblank provenance, paired acknowledgement and pending index | `src/main/resources/db/migration/V60__events__create_merchant_reaction_receipt.sql:4`, index :22 | Same IT :104 — database_constraints_reject_partial_acknowledgement_and_blank_provenance |

Clean-code-first trace placement: receipt class explains why replay cannot rebind history; port explains why acknowledgement is consumer-local and not source authority; adapter lock explains the concurrent first-insert decision. SQL retains the exact constituent pointer. No per-field commentary or invented business rules.

## Verification
Tests-first RED: `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 -Dtest=MerchantEventReactionReceiptTest test` failed compilation because the new production types did not exist. GREEN: the same command passed 3 tests after implementation.
Local `mvn --batch-mode -Dmaven.repo.local=<workspace>/work/m2 test`: 1113 tests, zero failures/errors/skips.
Required CI: `mvn --batch-mode clean verify -Ppostgres-it`, Java 25, PostgreSQL 18, workflow `.github/workflows/maven-tests.yml`, exact run/proof head above. Seven new PostgreSQL tests exercise real Flyway V60 and the adapter. No local PostgreSQL result is claimed.
Initial integration run `34009732035` at `751f44344f453d2cf615933f59b85ec8122e223a` failed all six new ITs because plain SQL bound Instant as varchar. Both timestamp writes were corrected with explicit PostgreSQL timestamptz casts. The passing proof above, not the failed implementation head, supports completion. The rollback test was added before the rerun.

## Counterevidence and completion falsification
| Attempt to falsify | Finding and limit |
| --- | --- |
| Replayed delivery changes release, source, merchant or logical intent | Each conflicts; original receipt remains. Release is not part of the duplicate key. |
| Two first deliveries race | Two database-backed workers converge to one row under transaction-scoped locking. Hash collision can serialize unrelated keys but cannot merge them because the primary key and lookups retain the full tuple. |
| One acknowledgement completes all consumers or all events | Separate consumer and separate event remain pending; the exact release filter excludes another release. |
| Acknowledgement before receipt or with conflicting outcome | Both rejected. Repeated same outcome returns original time/evidence. |
| Adapter recreation loses responsibility | New adapter instances recover the same database receipt/acknowledgement. This is not a process-kill or infrastructure-failover experiment. |
| SQL bypass creates partial completion or blank source release | Database constraints reject both. Privileged direct SQL can still rewrite otherwise valid provenance; immutable history is enforced by the application store, not a database audit/security boundary. |
| Receipt identifiers are mistaken for trusted provenance | Public receipt construction accepts well-formed opaque identifiers. No event-source or registry validation occurs here. This falsifies a trusted execution claim, not the selected storage contract. |
| Stored outcome is mistaken for business truth | The adapter records a supplied outcome reference without querying Commercial. C4D2 must establish outcome truth before calling it. |
| Enclosing transaction rolls back after both store calls | Same IT :93 — enclosing_transaction_rollback_does_not_leak_receipt_or_acknowledgement proves neither row nor acknowledgement escapes rollback. |
| Transaction proof is promoted across downstream mutations | Receipt and acknowledgement each use a transaction. No atomicity across Commercial mutation and acknowledgement is asserted; lost-ack recovery remains C4D2's responsibility. |
| Historical meaning is fabricated | V60 creates an empty technical table; it does not backfill or interpret legacy event occurrences. |
| Passing persistence tests completes C4D/C2/C3/C5 | Rejected. Source, principal, execution policy, delivery/recovery and downstream idempotency remain separate proof obligations. |

OBSERVED: the new store records and recovers one supplied responsibility/acknowledgement per event plus reaction contract; the adverse tests above pass at the exact proof head.
DEDUCED: C4D1's bounded storage responsibility conforms and C4D2 may proceed after committed synchronization.
UNCERTAIN / outside evidence: source authenticity, current execution eligibility, production wiring, restart delivery liveness, cross-operation recovery, provider availability and performance/failover. No downstream or macro completion follows from this evidence.
