# Main Street Implementation Status

> **Updated:** 15 September 2026
> **Status:** ACTIVE implementation navigation/evidence record
> **Repository identity:** GrandRue / `swangune/GrandRue`; stable `MS-*` authority identities and `mainstreet.*` implementation identifiers remain unchanged unless separately governed
> **Active implementation branch:** `development`
> **Stable promotion branch:** `master`
> **Current programme target:** `IMP-08C — Durable Execution Foundation` — **IN_PROGRESS**
> **Full backend gate:** `mvn --batch-mode clean verify -Ppostgres-it`
> **Canonical fine-grained graph:** `docs/development/implementation-programme-state.json`
> **Historical verbose status through the pre-C2B-closure state:** `docs/development/implementation-status-history-2026-09-14-pre-c2b-closure.md`
> **Completed node:** `IMP-08C-C2A — Durable registered-contract affinity` — **CONFORMING_COMPLETE**
> **Completed node:** `IMP-08C-C2B — Registered work owner binding/current revalidation` — **CONFORMING_COMPLETE**
> **Completed composite:** `IMP-08C-C2 — Registered durable-work owner execution path` — **CONFORMING_COMPLETE**
> **Active node:** `IMP-08C-C3 — Execution failures/outcome ledger` — **IN_PROGRESS**
> **Independent checkpoint:** `IMP-08C-C4B2 — Event source/discovery/publication integration` — implementation present, **local full verification pending; graph state remains READY**
> **Independent checkpoint:** `IMP-08C-C4E — BookingOutbox global acknowledgement correction` — implementation present, **local full verification pending; graph state remains READY**

This file is navigation/evidence only. Accepted substantive authority is indexed by `designs/AUTHORITY-INDEX.md`; sequencing follows MS-IMP-001 v1.0 + v1.1 and execution follows `designs/IMPLEMENTATION-RULES.md`. No branch creation is authorised without an explicit active-chat instruction.

## Current result

### C4E Booking publication / reaction separation checkpoint — 15 September 2026

Test-first commits `46243267bea984524d561dfc769a2269dd91e750` and `0443f7c410883c0d0d2555c7064d159580c7123c` reject Booking-level global consumer acknowledgement and require the quarantined Booking notification prototype to leave owner publication responsibility untouched even after successful notification delivery.

The production correction replaces `BookingOutbox.acknowledgeDelivery(...)` with technical `recordPublished(...)`, changes the PostgreSQL owner marker from `acknowledged_at` to `published_at` through forward-only Flyway V67, and leaves per-reaction completion exclusively to the accepted C4D1 Event Reaction store. `JooqBookingTransaction` no longer writes consumer-style completion state when the Booking event is created; publication recording remains merchant/event scoped and does not rewrite Booking truth.

No Booking Event Reaction Contract has been invented and no generic event-reaction implementation was modified. The correction is deliberately Booking-specific; similarly named legacy fields in unrelated owner outboxes are not promoted into this node by analogy. Exact implementation, falsification and non-claims are recorded in `docs/development/imp-08c-c4e-booking-publication-separation-checkpoint-2026-09-15.md`.

No Maven or GitHub Actions result is claimed under the current instruction to defer Maven locally. `IMP-08C-C4E` therefore remains **READY** in the canonical graph pending `mvn --batch-mode clean verify -Ppostgres-it` and the applicable cycle-closing consistency checks.

### C4B2 publication-integration checkpoint — 15 September 2026

Test-first specification commit `9f94b00b41a2856c92519d281e64f22db93ca2a6` and implementation commit `058fb2ea6c055fac7f7ddfe2ac17157e3f4f1f12` implement the bounded Merchant Account event source/discovery/publication path without absorbing reaction execution. `MerchantAccountEstablishedPublicationSource` exposes pending durable owner publication responsibility; `JooqMerchantAccountEstablishmentPublicationOutbox` preserves exact C4B1 historical-affinity reconstruction; `MerchantAccountEstablishedPublicationWorker` accepts the exact C4D1 Standing Free reaction responsibility before recording technical `published_at`.

Publication remains distinct from consumer completion: this worker never establishes the scheduled execution principal, invokes the Commercial owner operation or acknowledges the reaction. Unknown legacy affinity stays pending; missing current registration fails closed; and an expanded source-reaction set fails closed rather than silently omitting a reaction whose downstream intent mapping is unsupported. Receipt-first/publication-second ordering makes restart after a lost technical publication update converge on the already durable C4D1 responsibility.

No Maven or GitHub Actions result is claimed for this checkpoint under the current instruction to run Maven locally later. Exact scope, tests, recovery ordering and non-claims are recorded in `docs/development/imp-08c-c4b2-publication-integration-checkpoint-2026-09-15.md`. `IMP-08C-C4B2` therefore remains **READY** in the canonical graph pending `mvn --batch-mode clean verify -Ppostgres-it` and the applicable cycle-closing checks; no parent or macro completion is promoted.

### Development-branch coherence repair — 15 September 2026

Pushed `development` head `12698f22ee1784e0b72f2a5957062220394034e0` failed GitHub Maven run `34914386334` during test compilation because `StandingFreeBackgroundWorkProgressionTest` retained the earlier constructor-held C3 durable-store / attempt-identity API while current production `StandingFreeBackgroundWorkExecution` had already converged on method-scoped `executeClaimed(ClaimedWork, DurableWorkStore, String)` execution.

Correction commit `0c8a121caa6b836bb8f1a7df129570edf94faaa2` migrates that progression test to the current claimed-execution API while preserving the tested invariant that durable attempt-start evidence exists before the Commercial owner consequence and that outcome classification/finalisation follow that consequence. GitHub Maven/PostgreSQL run `34915780896` passed for that exact repair SHA using the repository full gate `mvn --batch-mode clean verify -Ppostgres-it`.

This is an implementation/integration coherence correction only. It creates no new design authority, changes no programme-node state and does not establish C3 completion. `IMP-08C-C3` remains **IN_PROGRESS**.

Local governance integration: merge `6968a84d` preserves the three local commits
and incorporates remote `880aab5f`. A stale v1.8 version assertion in the C4C
evidence gate is corrected to accepted IMPLEMENTATION-RULES v1.9, retaining all
exact-heading and historical-proof checks. The full local gate passed: 1251
unit/governance + 434 PostgreSQL integration tests, zero failures/errors/skips;
no node state changes. Scoped evidence: `docs/development/imp-08c-c4c-v17-impact-conformance-2026-09-06.md`,
"Local v1.9 governance integration — 14 September 2026".

IMP-08C remains **IN_PROGRESS**. C2 is complete: C2A proves exact durable registered-contract affinity, while C2B proves that one registered work kind — Standing Free reconciliation — invokes its real Commercial owner operation only after historical contract resolution, current contract registration, authoritative Merchant Account source reconstruction, Merchant Scope validation and bounded scheduled-principal/current eligibility revalidation.

C2B implementation was introduced at `3fd21066319c7f1bd8720eb775e669348f5c4165`. Closure evidence is `docs/development/imp-08c-c2b-standing-free-owner-binding-conformance-2026-09-14.md`.

C3 is **IN_PROGRESS**. The first persistence tranche at `eb1e96864a3d022dd0a4c6a2267c597a8565d345` separates durable physical-attempt start from later outcome classification, adds explicit attempt-start/outcome/latest-attempt store operations, prevents blind second attempts while a prior attempt is unresolved, permits another physical attempt only after `RETRY_SAFE`, and adds migration/test coverage for the durable attempt ledger. The bounded checkpoint is `docs/development/imp-08c-c3-attempt-ledger-checkpoint-2026-09-14.md`.

The owner-bound tranche exists at the inspected `origin/development` base `d2a423c5dd177810fdc108ae47bc8ae6d03046a1`: `StandingFreeBackgroundWorkExecution.executeClaimed(...)` re-establishes current registered contract/source/scope/principal authority, persists attempt start before the Commercial owner mutation, and records/finalises success after the owner consequence. The bounded worker uses the contract-scoped PostgreSQL claimer. The local checkpoint rejects attempt start timestamps at or after claim expiry and adds three real PostgreSQL worker/owner composition tests for success, lost acknowledgement and explicit retry after a pre-effect failure.

The test-first RED sequence is preserved: the durable-execution recovery specification failed before production support existed, and the worker/contract-claim specification was introduced before the bounded worker/claim adapter. A previous full run at implementation commit `cca34ef41614ca0bbfc1d7f011549d7a44cc6775` reached `1249` tests with the new Standing Free execution tests green but failed three stale governance assertions that still expected C3 to be READY. Those guards have been corrected to preserve prior C4 evidence while asserting the current C3 `IN_PROGRESS` state and checkpoint.

The expanded local full gate passed 1,251 unit/governance and 433 PostgreSQL integration tests, with zero failures/errors/skips, including the lease correction, three worker-composition cases and explicit finalisation assertions. Maven reported `BUILD SUCCESS` in 4 minutes 34 seconds. Details and limits are recorded in the local continuation section of `docs/development/imp-08c-c3-attempt-ledger-checkpoint-2026-09-14.md`.

The verified checkpoint is committed locally as `7fcad24d` on the authorised `development` branch. A further PostgreSQL test covers an expired worker resuming after its replacement completes: both converge on one baseline, the replacement retains successful finalisation, and stale acknowledgement is rejected. The focused run passed 10 tests; the expanded full gate passed 1,251 unit/governance and 434 PostgreSQL integration tests with zero failures/errors/skips in 4 minutes 32 seconds. Recovery documentation now attributes retry safety to the owner's idempotency invariant rather than treating an absent baseline as proof that the old worker cannot still commit.

C3 remains open. MS-PROT-056 v1.9 governs catalogue publication and historical selection. Historical selection at `dcd0ba27` and manifest structure at `da955a99` remain preserved evidence. The new bounded durable-catalogue checkpoint implements atomic PostgreSQL publication, retained manifest/receipt recovery, exact/historical reads and publication/read fencing. Its evidence is `docs/development/imp-08c-c3-durable-catalogue-checkpoint-2026-09-14.md`. The store requires an explicit trusted admission component; test fixtures do not prove production authorisation, exact approval, contract resolution or allocation conformance. No production admission implementation or worker activation is supplied. The exact complete production manifest remains open under `MS-PROT-056-V17-DQ-001`; its approval is a manual design gate under IMPLEMENTATION-RULES §§3–3.2. Trusted production admission/composition, initial-catalogue rollout ordering and final C3 verification remain outstanding. The earlier checkpoint-only push restriction remains the default for ordinary C3 work. The user's explicit 15 September 2026 instruction to carry out this coherence repair authorises only this repair/synchronisation sequence; it does not mark C3 complete or authorise unrelated checkpoint-only pushes.

## Current IMP-08C frontier

The durable-catalogue checkpoint full gate passed 1,292 unit/governance and 455 PostgreSQL integration tests with zero failures/errors/skips in 4 minutes 45 seconds, including 21 catalogue persistence tests and 66 migrations. This verifies the storage/admission-invocation boundary and historical-read coordination, not a production admission implementation, the unresolved complete production manifest or worker activation. C3 remains IN_PROGRESS.

| Node | State | Current meaning |
|---|---|---|
| `IMP-08C-C1` | `CONFORMING_COMPLETE` | Registered Background Work Contracts |
| `IMP-08C-C2A` | `CONFORMING_COMPLETE` | Exact durable registered-contract affinity |
| `IMP-08C-C2B` | `CONFORMING_COMPLETE` | Real Standing Free owner binding + current revalidation |
| `IMP-08C-C2` | `CONFORMING_COMPLETE` | C2A + C2B composite owner-execution path complete |
| `IMP-08C-C3` | `IN_PROGRESS` | Attempt ledger + owner-bound claimed execution + contract-scoped worker path implemented; cycle-closing verification/falsification and final composition evidence remain open |
| `IMP-08C-C4B2` | `READY` | Source/discovery/publication implementation checkpoint at `058fb2ea`; local full gate and closure evidence pending; publication remains distinct from reaction acknowledgement |
| `IMP-08C-C4D2B` | `CONFORMING_COMPLETE` | Trusted Standing Free reaction composition |
| `IMP-08C-C4E` | `READY` | Booking publication/reaction separation implemented through V67; local full gate and closure consistency checks pending |
| `IMP-08C-C5A` | `READY` | Operational evidence/reconciliation portfolio |
| `IMP-08C-C5` | `BLOCKED_DEPENDENCY` | Requires C2B, C3, C4B2, C4D2B, C4E and C5A |

Other programme-ready roots remain `IMP-08A-A0` and `IMP-08B-B0`. Under the current IMP-08C execution sequence, C3 remains the selected active node; C4B2 and C4E are independently implemented READY checkpoints awaiting their local closing gates.

## C3 state transition record

Before implementation of C3 began, the synchronized navigation state was:

`IMP-08C-C3 — Execution failures/outcome ledger` — **READY**

That historical READY classification is retained only as transition evidence. The current classification is **IN_PROGRESS**.

## C3 current non-claims

The current C3 code does not by itself prove macro completion, arbitrary durable-work dispatch, every registered work owner, a universal callback framework, scheduler cadence/triggering, C4B2 discovery/publication, C4E acknowledgement correction, C5 reconciliation evidence, or broad recurring-work semantics. The contract-scoped Standing Free worker deliberately does not claim unrelated work and does not translate owner exceptions into guessed terminal classifications.

## Next implementation envelope

`IMP-08C-C3 — Execution failures/outcome ledger`

The earlier worker/overlap continuation at `b9a4fc0d` remains verified evidence, not production activation. The historical-selection, manifest-structure and durable-catalogue checkpoints now provide bounded v1.9 storage/read prerequisites. Production trusted admission, approved catalogue content and worker composition remain outstanding; the injected admission interface is not a completed production security/approval implementation. The complete production manifest remains a separate chat-only DESIGN-RULES manual gate with the IMPLEMENTATION-RULES §3.1 packet. No proposed production catalogue is to be formalised before approval, and no test catalogue may be activated. See `docs/development/imp-08c-c3-durable-catalogue-checkpoint-2026-09-14.md` for current proof and limits. Independent already-governed work may continue. Outside the explicitly authorised 15 September 2026 coherence repair/synchronisation sequence, push only after verified C3 completion.

C3 may move to `CONFORMING_COMPLETE` only after the complete production path, applicable recovery/falsification cases, architecture/conformance/static checks, full PostgreSQL gate, evidence record and graph/status consistency checks pass.

## Preserved history

The previous verbose `implementation-status.md` content is preserved byte-for-byte at `docs/development/implementation-status-history-2026-09-14-pre-c2b-closure.md`. Historical per-node CI narratives remain evidence, but the live status file is intentionally compact so current navigation does not require scanning obsolete frontier prose.
