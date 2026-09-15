# IMP-08C C1 Conformance — Registered Background Work Contracts

**Date:** 5 September 2026
**Node:** IMP-08C-C1 — Registered BackgroundWorkContract definitions
**Status:** CONFORMING_COMPLETE effective on successful cycle-closing synchronization CI

Implementation evidence only; no new semantic authority.

## Authority and selection

MS-IMP-001 v1.0 + v1.1 and IMPLEMENTATION-RULES v1.6 govern execution. Composite MS-PROT-065 v1.1 §§2–4 requires explicitly registered, owner-qualified BackgroundWorkContracts and enumerates their semantic responsibilities; §§8–14 preserves current revalidation and downstream logical-intent distinction. Static declaration does not authorize execution. Existing exact-release immutable registry patterns are retained.

IMP-07 closure `f9b5073e86405cdbae08962b7b84f83685f5cc7a`, run `33995487345`, passed 1079 unit/governance + 374 PostgreSQL integration tests, zero failures/errors/skips.

Before production changes, `imp-08-foundation-graph-refresh-2026-09-05.md` assessed existing Media/Privacy, Workforce and Background foundations and selected C1 as a concrete missing prerequisite. IMP-08A/B remain eligible; existing domain/store classes do not establish macro closure. Event occurrence/reaction implementation is a separate branch and is not needed merely to declare static background contracts.

## Implementation

BackgroundWorkContractIdentity qualifies stable responsibility identity by semantic owner. BackgroundWorkTargetReference separately qualifies the target owner operation/process evaluation. Neither is a work, attempt, command, provider request or executable class identity.

BackgroundWorkContractDefinition requires explicit purpose, authority scope, trigger, target, bounded-principal contract, due condition, existing owner-selected overdue mode, current-revalidation requirements, downstream logical-intent rule, retry, uncertainty, supersession/cancellation, data-minimisation and semantic/configuration-affinity references.

All semantic references must be present and nonblank; current-revalidation references are nonempty and immutable. Even a non-consequential target declares an explicit downstream identity rule instead of relying on a permissive missing default. This is static declaration of owner responsibilities, not their resolution or evaluation. It adds no expression language, handler lookup, SQL/script execution, default owner policy or business authorization.

BackgroundWorkContractRegistrySnapshot defensively copies the declarations, rejects duplicate exact identities even if the definitions compare equal, and resolves only the exact requested semantic release and owner-qualified identity. Same-named contracts belonging to different owners remain distinct. Unknown owners/contracts and other releases return empty; an empty registry is valid and fail-closed. No latest-release fallback is available.

## Verification

Tests-first RED was compilation on absent C1 types. Five tests then passed and the local full suite passed **1084 tests, zero failures/errors/skips**, using Java 26 targeting Java 25.

The tests cover retained semantic responsibility references and immutable revalidation sets; missing/blank required references and owner identity; exact release/owner isolation; duplicate identity rejection; defensive registry copying, immutable enumeration and empty-registry behavior.

Implementation commit: `93445b32e17efb9915001934ec753a7866a6212b`.

Full Java 25/PostgreSQL CI run `33995933695`: SUCCESS — 1084 unit/governance + 374 PostgreSQL integration tests, zero failures/errors/skips.

Required command: `mvn --batch-mode clean verify -Ppostgres-it`.

C1 changes no persistence behavior; no new PostgreSQL test is required for immutable static values and a registry. The existing complete PostgreSQL suite remains mandatory regression verification.

## Structure, limits and next work

The four types remain in the existing Background bounded context and reuse BackgroundExecutionScope/OverdueHandling. Existing DurableWorkInstruction, WorkAttempt, scheduling/claims/retries and owner-specific outboxes remain intact. No schema, runtime executor, service profile, provider or deployment change is introduced.

Registration alone is not executable admission. Current principal, owner authority, due conditions, policy references, semantic/configuration affinity and stable downstream intent must be resolved by subsequent concrete owner/runtime composition. C1 does not claim that legacy technical-store methods enforce this new registry.

C2 is next for instruction/contract affinity and execution-binding decomposition, including identification of the first accepted owner responsibility. The current graph keeps executable progression blocked until its concrete dependencies conform. Event/reaction registration/publication/acknowledgement and attempt recovery remain outstanding. IMP-08C remains IN_PROGRESS; provider/financial hard dependencies are preserved.

Evidence, current graph, implementation status and programme-gate assertions close together only when the synchronization head passes the full gate.
