# IMP-08C-C4D2B Trusted Reaction Composition Conformance

**Date:** 12 September 2026  
**Branch:** `development`  
**Implementation baseline:** `8d1054b8396af312ab80c09382324107c3d75119`  
**Node:** `IMP-08C-C4D2B — Trusted reaction composition`  
**Result:** `CONFORMING_COMPLETE`

## Scope and authority

This cycle closes the previously bounded C4D2B obligation only: bind an exact durable `MerchantAccountEstablished` source occurrence and trusted Merchant Scope to Commercial's currently registered Standing Free Event Reaction Contract, establish Commercial's own registered scheduled execution principal, record C4D1 reaction responsibility, invoke C4D2A's exact owner operation, and acknowledge that reaction independently after its business outcome exists.

Current accepted authority was resolved through `designs/AUTHORITY-INDEX.md`:

- `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §13 — EventReactionContract; §14 — Registration Is Required; §16 — Event Reaction Identity; §17 — Per-Reaction Acknowledgement; §21 — Authoritative Downstream Mutation; §23 — Reaction Deduplication; §24 — Downstream Idempotency Remains Separate; §32 — Historical Affinity; §44 — Reaction Result Classification; §45 — Consumer Failure; §47 — Event Receipt Does Not Carry Actor Authority; §48 — Merchant Scope; §51 — No Global Event Transaction.
- `designs/MS-PROT-056 v1.6 — Standing Free Baseline Temporal Anchor Amendment.md`: §1 — Governing decision; §2 — Semantic ownership; §3 — Standing Free baseline fact; §5 — Idempotency and duplicate delivery.
- `designs/MS-PROT-063 — Authentication, Session & Trusted Execution Principal Establishment Model.md`: §12 — System and scheduled principals; §17 — Client input is not trusted context; §18 — Merchant Scope establishment; §20 — Context propagation boundary; §32 — Concurrency / stale context.
- `designs/MS-PROT-065 v1.1 — Production Durable Background Work, Timer, Attempt & Retry Execution Contract Amendment.md`: §38 — Event → Durable Work; §39 — Event Reaction Identity vs Work Identity.
- `designs/MS-PROT-071 v1.2 — Merchant Account Establishment Temporal Evidence Amendment.md`: §3 — Establishment atomicity; §5 — Domain Event propagation.
- `designs/IMPLEMENTATION-RULES.md`: §30.1 — Exact Authority Pointer Contract; §47 — Implementation Evidence; §52.14 — Mandatory implementation-status synchronisation; §52.18 — Counterevidence Before Completion; §52.19 — Evidence Locality Rule.
- `designs/MS-IMP-001.md`: §16 — IMP-08C — Durable Execution Foundation.

No deferred decision supplies or blocks this bounded behaviour. No new semantic or architecture decision was introduced.

## Implemented boundary

- `MerchantAccountEstablishedOccurrenceAuthority` is the Merchant Account owner port for independently resolving exact committed occurrence evidence. `JooqMerchantAccountEstablishmentPublicationOutbox` implements it by reconstructing the registered occurrence from the durable owner outbox and accepting only complete equality. A supplied event identity, establishment identity, payload, or merchant identifier cannot establish source or scope authority by itself.
- `ScheduledEventReactionExecutionAuthority` is the trusted application/runtime boundary for establishing a separately registered scheduled principal for a current Event Reaction Contract and explicit Merchant Scope. `RegisteredScheduledEventReactionExecutionAuthority` keeps registration contract-specific and produces a non-session `TrustedExecutionContext`; no global `SYSTEM` permission exists.
- `StandingFreeMerchantAccountEstablishedReaction` re-resolves the exact source occurrence, current exact Standing Free reaction definition, scheduled principal and Merchant Scope on every delivery, including retry. Only then does it accept the exact C4D1 receipt, call `StandingFreeFromMerchantAccountEstablishedHandler`, and independently acknowledge the returned baseline identity.
- The downstream logical intent is stable for the exact establishment (`standing-free-baseline/<establishment identity>`), while the owner baseline identity remains a distinct Commercial outcome identity.
- C4D1 receipt persistence, C4D2A baseline persistence and acknowledgement remain separate consistency boundaries. No distributed transaction or receipt-as-permission shortcut was added.

## Executable proof

Unit and component coverage:

- `StandingFreeMerchantAccountEstablishedReactionTest` proves the happy path, exact receipt fields, current-contract absence, missing scheduled-principal registration, wrong-scope rejection, owner failure, pending evidence, lost-acknowledgement retry and authority revalidation on retry.
- `RegisteredScheduledEventReactionExecutionAuthorityTest` proves contract-bounded principal registration and scope-bound non-session trusted context.
- Existing `StandingFreeFromMerchantAccountEstablishedHandlerTest` and `MerchantEventReactionReceiptTest` retain owner-recovery and evidence-identity separation.

PostgreSQL integration coverage:

- `StandingFreeMerchantAccountEstablishedReactionIT` composes the real Merchant Account bootstrap/outbox, exact owner source authority, registered scheduled principal, C4D1 jOOQ receipt store, C4D2A jOOQ Standing Free store, owner handler and independent acknowledgement.
- It proves adapter recreation and duplicate convergence, rejects forged event identity and forged Merchant Scope before receipt or business mutation, and kills the path at acknowledgement so a new composition can recover the already committed baseline without catalogue or identity-factory access.
- Adjacent `JooqMerchantAccountEstablishmentPublicationOutboxIT`, `JooqMerchantEventReactionStoreIT` and `JooqStandingFreeBaselineStoreIT` passed unchanged.

## Counterevidence and completion falsification

- A durable reaction receipt alone was presented on retry: source, current contract, principal and Merchant Scope were still revalidated before the owner operation. Receipt-as-permission failed.
- The event identity was changed while the otherwise correct fact was retained: no receipt, baseline or acknowledgement was created.
- The event identity was retained while Merchant Scope was forged: no receipt, baseline or acknowledgement was created.
- The current reaction registry omitted Standing Free: execution stopped before receipt.
- The current scheduled-principal registry omitted the exact reaction contract: execution stopped before receipt.
- A scheduled authority returned another Merchant Scope: execution stopped before receipt.
- The owner operation failed after receipt acceptance: the receipt remained pending and unacknowledged without invalidating the Merchant Account fact.
- Acknowledgement failed after the Commercial baseline committed: the receipt remained pending; restart revalidated authority, C4D2A recovered the exact committed baseline, and C4D1 acknowledged that same outcome.
- Physical redelivery and restart did not create another logical reaction or another baseline.

## Verification

- Focused unit tests: 8 tests, zero failures/errors/skips.
- Focused composition plus adjacent PostgreSQL suites: 16 unit tests and 23 integration tests, zero failures/errors/skips.
- Pre-synchronization full gate: `mvn --batch-mode clean verify -Ppostgres-it` against PostgreSQL 18.6 / Flyway V64 — 1,234 unit/governance tests plus 422 PostgreSQL integration tests, zero failures, zero errors and zero skipped.

The graph/status/evidence tree must pass the same full gate before the cycle-closing commit.

## Graph consequence and non-claims

`IMP-08C-C4D2B`, `IMP-08C-C4D2` and the bounded `IMP-08C-C4D` are `CONFORMING_COMPLETE`. C4D1 and C4D2A retain their prior scoped proofs.

This evidence does **not** prove C4B2 source discovery/publication integration, C4E Booking global-acknowledgement correction, C2B registered durable-work owner binding, C3 WorkAttempt progression, C5 operational evidence/integration, every Event Reaction Contract, or the IMP-08C macro. MS-PROT-065 v1.1 §38 expressly does not require every reaction to create a `DurableWorkInstruction`; no invented universal C2/C3 dependency was introduced.

The smallest newly selected ready node is `IMP-08C-C2B — Registered work owner binding/current revalidation`. It remains independently governed and must refresh its stale owner assessment using the now executable Standing Free boundaries without substituting a generic callback.

DEDUCED: C4D2B conforms within its exact source/principal/owner/effect-acknowledgement scope. No broader macro or portfolio completion is claimed.
