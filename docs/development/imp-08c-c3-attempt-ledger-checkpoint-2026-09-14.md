# IMP-08C-C3 Attempt Ledger Checkpoint — 14 September 2026

**Node:** `IMP-08C-C3 — Execution failures/outcome ledger`  
**State:** `IN_PROGRESS`  
**Branch:** `development`  
**Implementation commit:** `eb1e96864a3d022dd0a4c6a2267c597a8565d345`  
**Closure claim:** NONE

## Purpose

Record the bounded persistence tranche completed for C3 without overstating node closure. This checkpoint is implementation navigation/evidence only; accepted semantic and architectural authority remains the corpus resolved through `designs/AUTHORITY-INDEX.md`.

## Implemented tranche

Commit `eb1e96864a3d022dd0a4c6a2267c597a8565d345` changes the durable-work attempt persistence boundary so one physical `WorkAttempt` can be durably represented before an outcome is known and later classified separately.

The tranche includes:

- `WorkAttempt` representation of started/unclassified versus classified attempt evidence;
- `DurableWorkStore.startAttempt(...)`;
- `DurableWorkStore.recordAttemptOutcome(...)`;
- `DurableWorkStore.latestAttempt(...)`;
- PostgreSQL migration `V65__background__separate_attempt_start_from_outcome.sql`;
- exact-start replay/idempotency protection;
- rejection of a blind second physical attempt while the previous attempt remains unresolved;
- admission of another attempt only after the previous attempt is explicitly `RETRY_SAFE`;
- immutable outcome classification under exact replay;
- prevention of generic retry/finalisation from collapsing `RECONCILIATION_REQUIRED` into a different meaning; and
- unit plus PostgreSQL integration-test coverage for the attempt-ledger persistence contract.

## Test-first evidence observed in the implementation cycle

The unit RED gate first demonstrated that the previous `WorkAttempt` shape could not represent a started attempt without a result classification. After correction of one test assumption that exceeded accepted authority, `WorkAttemptTest` passed with `2` tests, zero failures/errors/skips.

The persistence RED gate then demonstrated, at test compilation, that the existing store lacked the separately required `startAttempt(...)`, `recordAttemptOutcome(...)`, and externally readable `latestAttempt(...)` operations. That RED evidence preceded the persistence implementation.

## Verification state

This checkpoint does **not** claim the full C3 verification gate. At the time of this checkpoint:

- the implementation tranche is committed on `development` at `eb1e96864a3d022dd0a4c6a2267c597a8565d345`;
- the unit `WorkAttemptTest` GREEN result is directly observed from the implementation cycle;
- no GitHub Actions workflow run or combined status was available for that commit when inspected; and
- no full `mvn --batch-mode clean verify -Ppostgres-it` result for the C3 tranche is recorded here.

A commit is not conformance evidence by itself.

## Non-claims / remaining C3 work

C3 remains `IN_PROGRESS`. The committed persistence ledger does not yet prove:

- that a real production durable-work executor persists attempt start before consequential owner execution;
- production orchestration from a claimed `DurableWorkInstruction` through the registered owner contract;
- success classification/finalisation after a real owner consequence;
- correct treatment of a worker/process failure after attempt start but before acknowledgement;
- owner-authoritative reconciliation of an unresolved prior attempt after restart/reclaim;
- lost-acknowledgement recovery that resolves an already committed Standing Free baseline without duplicating the authoritative consequence;
- the complete C3 failure/outcome/retry/uncertainty/reconciliation path; or
- the mandatory full backend gate and completion falsification required before `CONFORMING_COMPLETE`.

## Next falsification target

Use the registered Standing Free durable-work path as the bounded real execution slice. Tests must prove, before production orchestration is added, that:

1. durable attempt-start evidence exists before the Commercial owner mutation is invoked;
2. a successful owner consequence is followed by the accepted durable outcome/finalisation progression;
3. an exception or lost acknowledgement after attempt start is not guessed to be a terminal business failure and does not authorise blind retry; and
4. recovery consults owner-authoritative Standing Free evidence and the accepted retry/reconciliation contract before any repeated consequential execution.

Do not introduce a generic callback/executor framework merely to close C3 unless accepted authority requires it.

## Local verification continuation — 14 September 2026

This section supplements the historical checkpoint above. The local review base is
`d2a423c5dd177810fdc108ae47bc8ae6d03046a1` from `origin/development`; the changes
described here belong to the local development checkpoint containing this section.
C3 remains `IN_PROGRESS`.

Exact governing provisions:

- `designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment.md`: §11 — Attempt Evidence Before Consequential Execution; §§24–27 — retry safety, reconciliation, lost acknowledgement and known pre-effect failure; §§51–55 — claims, domain concurrency, multiple workers, principal and scope; §§59–60 — restart and mid-attempt recovery.
- `designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md`: §3 — Standing Free baseline fact; §5 — Idempotency and duplicate delivery; §§6–7 — propagation and fail-closed runtime behaviour.
- `designs/IMPLEMENTATION-RULES.md`: §§47–49 — evidence, verification and readiness; §§52.14–52.19 — status synchronisation, freshness and evidence locality.

The local lease correction in `JooqDurableWorkStore.startAttempt` rejects an
attempt whose supplied start instant is at or after the current claim expiry.
`JooqRegisteredDurableWorkClaimerIT.expired_claim_cannot_start_a_consequential_attempt_before_reclaim`
failed before that correction and passed afterwards. This is a boundary check
against the supplied attempt timestamp; it is not a database-clock fence against
arbitrarily backdated caller timestamps or a guarantee that a started owner
operation finishes within its lease.

Three additional tests in `StandingFreeBackgroundWorkExecutionIT` compose the real
`StandingFreeDurableWorkWorker`, `JooqRegisteredDurableWorkClaimer`,
`JooqDurableWorkStore`, authoritative Merchant Account occurrence lookup,
registered scheduled authority and Commercial handler/baseline store:

1. An independent database reader sees an unclassified attempt before the owner
   resolves its historical FREE revision; success preserves the exact temporal
   anchor, revision and entitlements.
2. A seeded crash boundary leaves the owner baseline committed and the attempt
   unclassified. A replacement worker at claim expiry resolves that same baseline
   without catalogue access or a second attempt.
3. An injected catalogue exception occurs before owner materialisation. Recovery
   classifies the prior attempt `RETRY_SAFE` and reschedules it; only a separate
   claim starts the successful second attempt.

Finalisation assertions read the persisted final classification and ensure that
completed work cannot be reclaimed after the lease would have expired. The new
tests strengthen evidence for existing code; no additional production behaviour
was needed for their initial green result.

Verification: the focused claimer/execution run passed 9 tests before the added
explicit finalisation assertions. The previous full local gate, including the
lease correction, passed 1,251 unit/governance and 430 PostgreSQL integration
tests. The expanded full local gate `mvn --batch-mode clean verify -Ppostgres-it`
then passed with 1,251 unit/governance tests and 433 PostgreSQL integration tests,
zero failures/errors/skips, and `BUILD SUCCESS` in 4 minutes 34 seconds. This run
includes the explicit finalisation assertions and all 65 migrations on PostgreSQL
18.6. Results are local evidence, not a GitHub Actions or deployment claim.

Limits: these tests invoke the worker explicitly, use a deterministic catalogue
fixture and simulate restart through reconstructed adapters and durable state.
They do not prove application scheduler activation, a production catalogue
binding, arbitrary work dispatch, actual process termination, or every overlap
between an expired worker and its replacement. In particular, absent baseline
evidence is not general proof that another worker cannot still commit. The
Standing Free owner's separate idempotency invariant remains essential.

The user explicitly authorised creation of the local `development` branch after
reviewing the branch constraint. It now tracks `origin/development` at the same
review base. This bounded verification cycle is ready for a local checkpoint
commit, including synchronised evidence/status/graph. It does not close C3 or
authorise a push; the next C3 work is overlapping-worker recovery falsification
and production activation/catalogue binding inspection.

## Overlapping worker continuation

The verified local checkpoint above was committed as `7fcad24d` on `development`.
The following continuation is bounded to the same C3 owner contract and remains
local. It does not change accepted authority or the node's `IN_PROGRESS` state.

`StandingFreeBackgroundWorkExecutionIT.expired_worker_resuming_after_replacement_completion_cannot_duplicate_the_owner_effect`
uses two threads and explicit latches, with fixed execution clocks and bounded
failure timeouts rather than sleeps. The original worker has a durable started
attempt and pauses during catalogue resolution, before Commercial mutation.
At lease expiry the replacement reconciles, explicitly reschedules and then
completes on a separate claim. The original worker resumes afterwards with a
different candidate baseline identity. The Commercial store returns the same
baseline, and stale work acknowledgement is rejected. The database retains one
baseline, two attempts, the replacement attempt's `SUCCESS`, and finalised work
that cannot be reclaimed.

This is direct evidence for MS-PROT-056 v1.6 §5 and MS-PROT-065 v1.1 §§24, 52–53
at the exact paths listed above. It demonstrates why absence of a baseline is not
proof of permanent non-completion. The `executeClaimed` Javadoc now states that
the owner's idempotency guarantee provides retry safety even if an expired worker
later commits. No executable production logic changed in this continuation.

The focused PostgreSQL run passed 10 tests with no failures/errors/skips.
The expanded full `mvn --batch-mode clean verify -Ppostgres-it` gate passed
1,251 unit/governance and 434 PostgreSQL integration tests, with zero
failures/errors/skips, in 4 minutes 32 seconds. The overlap test passed in both
the focused and full runs. All 65 migrations were included on PostgreSQL 18.6.

Production activation inspection found no production instantiation of
`StandingFreeDurableWorkWorker`, no `@Scheduled` entry point for it, and no
production implementation of `FreePlanRevisionAuthority`. The existing
`StandardPlanCatalogueRevision` retains catalogue generation and plan snapshots,
but does not supply historical effective-policy-window lookup.

`designs/MS-PROT-056 v1.1 — Standard Plan Hierarchy & Multi-Location Commercial Scale Amendment.md`,
§8 — Plan catalogue revision, requires effective-policy-window provenance;
§27 — Decisions intentionally left open, explicitly leaves the exact FREE
entitlement catalogue as commercial policy. MS-PROT-056 v1.6 §3 requires selection
at the authoritative establishment instant. The current DDR was checked for
relevant catalogue decisions; no production FREE revision/effective-window values
were identified in the inspected source/resources. Test values such as `free-r7`
are fixtures, not approved production policy.

The affected activation path is stopped as `DESIGN_ESCALATION` for an unresolved
Commercial catalogue decision. It must not be treated as routine configuration
collection. `designs/IMPLEMENTATION-RULES.md` §§3–3.2 and §6 require the complete
`designs/DESIGN-RULES.md` lifecycle before resolving this open decision.

The next step is chat-only design work: load the broad applicable authority,
formulate the complete proposal, perform Fundamental Vision Conformance, review,
falsification and mandatory ambiguity review, and produce the required
recommendation. Only an `ACCEPT` recommendation after all applicable gates permits
presentation of the complete final proposed authority and the self-contained
§3.1 recommendation packet for explicit manual approval. A request for catalogue
values, supplied values, or general permission to continue cannot replace this
process or establish approval of the complete authority.

No catalogue resolution or draft authority is recorded here. Before approval,
proposals and review material remain in ChatGPT under DESIGN-RULES §2.8. After
explicit approval attributable to the complete proposal, formalisation,
navigation updates and corpus conformance must precede implementation readiness.
Independent already-governed work may continue. The verified recovery evidence
above remains bounded and does not close C3.
