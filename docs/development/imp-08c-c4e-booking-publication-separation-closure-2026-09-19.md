# IMP-08C-C4E Booking Publication / Reaction Separation Closure — 19 September 2026

**Node:** `IMP-08C-C4E — BookingOutbox global acknowledgement correction`  
**State:** `CONFORMING_COMPLETE`  
**Branch:** `development`  
**Verified implementation head:** `345bd7b7295a6b387b8af3d5b1c25bf0019c8afd`  
**Verification:** Maven Tests #919 / run `35467458658` — `SUCCESS`

## Closure decision

The retained IMP-00 conflict is closed without inventing a Booking consumer or changing Booking business truth.

The accepted boundary remains:

```text
Booking-owned Domain Event
        ↓
merchant-scoped durable publication responsibility
        ↓
technical published_at evidence

registered Event Reaction, when one exists
        ↓
its own EventReactionIdentity
        ↓
its own receipt / progression / acknowledgement
```

One notification delivery, one technical publication marker and one reaction acknowledgement are deliberately not interchangeable.

## Source correction retained

The 15 September tests-first checkpoint already replaced the conflicting path:

- `BookingOutbox.acknowledgeDelivery(...)` was removed;
- `BookingOutbox.recordPublished(MerchantScope, eventIdentifier)` records technical publication only;
- `InMemoryBookingUnitOfWork` and `JooqBookingUnitOfWork` use the publication distinction;
- Flyway `V67__booking__separate_publication_from_reaction_acknowledgement.sql` moves the live schema from `acknowledged_at` to `published_at` without rewriting historical migrations;
- the quarantined Booking notification prototype can no longer clear Booking publication responsibility after notification success; and
- generic per-reaction acknowledgement remains owned by `MerchantEventReactionStore`.

Merchant Scope remains part of Booking publication identity, so equal event identifiers across merchants do not cross-complete.

## Exact verification

The C4E production, migration and executable-proof blobs at Maven Tests #916 and the current verified #919 head are unchanged:

- `BookingOutbox.java` — `91f77f549e73e300f2d0405b70cde40c7ca62b91`;
- `InMemoryBookingUnitOfWork.java` — `4afc4d7933b6e301235413d2c22ffb02c3a633c2`;
- `JooqBookingUnitOfWork.java` — `2d35f5e9be7ba45f516c38265e2e2cf2e12b04b6`;
- `V67__booking__separate_publication_from_reaction_acknowledgement.sql` — `2ffabe6d4d669fbc536a3d5f52185d69997bb4bd`;
- `BookingOutboxPublicationContractTest.java` — `002f0b4f93c317d803a032cb490a2e8b7aaa83b5`;
- `BookingNotificationDeliveryTest.java` — `0595b3c480b5760e587e69b79cad48564128bd77`; and
- `JooqBookingUnitOfWorkIT.java` — `537b75e23b67f7e604432b0d620c4e36cfa841cd`.

Maven Tests #919 ran the synchronized current source:

```text
head                    345bd7b7295a6b387b8af3d5b1c25bf0019c8afd
command                 mvn --batch-mode clean verify -Ppostgres-it
unit/governance         1,271 PASS — 0 failures / 0 errors / 0 skipped
PostgreSQL integration  446 PASS — 0 failures / 0 errors / 0 skipped
JooqBookingUnitOfWorkIT 9 PASS
result                  BUILD SUCCESS
```

The same gate also validates the source-rooted governance repair required after the earlier C3 frontier advanced; no migration or historical migration receipt was reopened.

## Falsification retained

Executable evidence proves:

- the old `acknowledgeDelivery` contract is absent;
- Booking's current schema has `published_at` and no owner-level `acknowledged_at`;
- successful notification delivery leaves Booking publication responsibility untouched;
- publication evidence survives adapter recreation without rewriting the Booking;
- one merchant cannot publish another merchant's equal event identity; and
- consumer acknowledgement remains independently keyed by Event Reaction identity.

## Non-claims

This closure does not:

- invent a Booking Event Reaction Contract;
- make `published_at` proof that any consumer completed;
- let notification success acknowledge Booking globally;
- complete unrelated Ordering or Appointment legacy outbox corrections by analogy;
- complete the C5A operational-evidence portfolio;
- complete C5 or IMP-08C; or
- reopen GrandRue migration.

## Result

```text
IMP-08C-C4E = CONFORMING_COMPLETE
IMP-08C-C4  = CONFORMING_COMPLETE
IMP-08C-C5A = READY
IMP-08C-C5  = BLOCKED_DEPENDENCY
IMP-08C     = IN_PROGRESS
```
