# IMP-08C-C4E Booking Publication / Reaction Separation Checkpoint

**Date:** 15 September 2026  
**Branch:** `development`  
**Baseline:** `2de298400315353bf1fa92119f565976fe70151a`  
**Implementation head before this evidence:** `22f0e7a47a29001e00640ca0312bd56abbd40cf6`  
**Node:** `IMP-08C-C4E — BookingOutbox global acknowledgement correction`  
**Result:** implementation checkpoint only — **full local verification pending; canonical graph state remains `READY`**

## Scope and governing authority

This checkpoint corrects the bounded Booking owner-outbox defect carried from IMP-00: Booking previously exposed event-level `acknowledgeDelivery(MerchantScope, eventIdentifier)` and persisted `acknowledged_at`, allowing a caller to make one owner event disappear as though one delivery represented global consumer completion.

Governing accepted authority:

- `designs/MS-PROT-026 v1.1 — Production Domain Event Publication, Reaction & Consumption Contract Amendment.md`: §2 — Canonical Separation; §10 — Publication Failure After Commit; §11 — Event Publication Is Not Reaction Completion; §12 — Publication Responsibility; §13 — EventReactionContract; §14 — Registration Is Required; §16 — Event Reaction Identity; §17 — Per-Reaction Acknowledgement; §22 — Exactly-Once Is Not Assumed; §23 — Reaction Deduplication; §32 — Historical Affinity; §45 — Consumer Failure; §51 — No Global Event Transaction.
- `designs/MS-PROT-032 — Backend Module, Bounded Context & Dependency Architecture.md`: §§8–9 — capability communication through published contracts and no arbitrary cross-module mutation; §14 — Internal events.
- `designs/IMPLEMENTATION-RULES.md`: tests-first implementation, exact authority traceability, scoped evidence, counterevidence before completion, full verification before completion promotion.
- `designs/MS-IMP-001.md`: IMP-08C Durable Execution Foundation.

No new Booking Event Reaction Contract is introduced. The accepted generic C4D1 `MerchantEventReactionStore` remains the per-reaction receipt/acknowledgement mechanism when an actual registered reaction exists.

## Test-first history

- `46243267bea984524d561dfc769a2269dd91e750` — adds `BookingOutboxPublicationContractTest`, requiring `recordPublished` and rejecting `acknowledgeDelivery`.
- `0443f7c410883c0d0d2555c7064d159580c7123c` — changes the quarantined legacy Booking notification tests so successful notification delivery must **not** complete Booking publication responsibility.

Both commits use `[skip ci]`. No Maven or GitHub Actions run is claimed.

## Implemented correction

### Booking owner contract

`BookingOutbox` now exposes:

```text
pendingEvents(MerchantScope)
recordPublished(MerchantScope, eventIdentifier)
```

`recordPublished` is explicitly technical publication evidence. It is not Event Reaction acknowledgement and does not state that any or every consumer completed.

### In-memory and PostgreSQL adapters

`InMemoryBookingUnitOfWork` now removes an event from the pending-publication set only through `recordPublished`.

`JooqBookingUnitOfWork` now reads pending owner publication responsibility through `published_at is null` and records only `published_at` for the exact merchant/event key. Merchant Scope remains part of the keying boundary, so one merchant cannot complete another merchant's publication responsibility merely by reusing an event identifier.

`JooqBookingTransaction` no longer writes a consumer-style acknowledgement field when creating the owner event record. The nullable publication marker is left unset until technical publication is recorded.

### Schema migration

`V67__booking__separate_publication_from_reaction_acknowledgement.sql` renames the current Booking outbox column:

```text
acknowledged_at -> published_at
```

and rebuilds the pending index over `published_at is null`.

Historical migrations remain immutable. The correction is applied forward through V67 rather than rewriting V20/V28 history.

### Historical notification prototype

The test-scope `BookingNotificationDelivery` fixture remains quarantined historical evidence only. It no longer mutates `BookingOutbox` after a successful notification attempt. This deliberately falsifies the old assumption:

```text
notification delivery success
    -> Booking event globally complete
```

Production notification semantics remain owned by the generic Notification model and any legitimate event-driven consequence still requires its own registered reaction contract and per-reaction progression.

## Executable evidence prepared

- `BookingOutboxPublicationContractTest` rejects the old global-acknowledgement API shape.
- `BookingNotificationDeliveryTest` requires retryable/unexpected notification failure and notification success alike to leave Booking publication responsibility untouched.
- `JooqBookingUnitOfWorkIT` now proves:
  - technical publication evidence survives adapter recreation;
  - recording publication does not rewrite/recreate the committed Booking result;
  - identical event identifiers remain merchant-scoped;
  - current schema exposes `published_at` and no Booking-level `acknowledged_at` column.

These tests are committed but have **not** been executed under the user's current instruction to defer Maven locally.

## Counterevidence and limits

- Renaming `acknowledged_at` does **not** itself create a legitimate consumer. No Booking listener/reaction is invented.
- `recordPublished` does **not** acknowledge C4D1 receipts. Per-reaction completion remains keyed by Event Reaction identity in the generic event-reaction store.
- Successful notification delivery in the historical prototype does **not** clear owner publication responsibility.
- Source Booking facts, allocation claims, command replay and event identity remain unchanged by publication recording.
- This checkpoint does **not** correct similarly named legacy fields in unrelated Ordering or Appointment outboxes; C4E is specifically the tracked Booking correction. No portfolio-wide completion is claimed by analogy.
- This checkpoint does **not** prove publication transport liveness, a concrete Booking reaction portfolio, C4B2 closure, C3 closure, C5, or IMP-08C macro completion.

## Verification still required

Cycle closure requires the applicable targeted/architecture/static checks and the repository full gate:

```text
mvn --batch-mode clean verify -Ppostgres-it
```

Until that succeeds on the synchronized checkpoint and the evidence/graph/status consistency checks pass, `IMP-08C-C4E` remains `READY` in `docs/development/implementation-programme-state.json`.
